// JWT 재발급 함수
async function refreshJWT() {
    try {
        const response = await fetch('/api/v1/users/reissue', {
            method: 'POST'
        });

        if (!response.ok) {
            const error = await response.json();
            console.error(error)
            alert('다시 로그인해주시기 바랍니다.')
            window.location.href = '/views/login'
            localStorage.removeItem('isLoggedIn')
        }
    } catch (error) {
        throw new Error('토큰 재발급 중 오류 발생: ' + error.message);
    }
}

// 원래의 Fetch 함수를 저장
const originalFetch = window.fetch;

// Fetch 함수를 래핑하여 토큰 재발급을 수행하는 인터셉터를 추가
window.fetch = async (url, options = {}) => {
    try {
        // 원래의 Fetch 함수를 호출
        const response = await originalFetch(url, options);

        // 만약 응답이 401 이면 JWT를 재발급하고 다시 요청
        if (response.status === 401) {
            await refreshJWT();

            return originalFetch(url, options);
        }

        return response;
    } catch (error) {
        console.error('Fetch 오류:', error);
        throw error;
    }
};
