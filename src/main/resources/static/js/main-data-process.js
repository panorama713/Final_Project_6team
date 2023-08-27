// 마이프로필 조회
async function getMyData() {
    return await fetch('/api/v1/users/my-profile', {
        method: "GET"
    })
}

// 로그인 체크
async function checkLogin() {
    try {
        const response = await fetch('/api/v1/users/check-login', {
            method: "GET"
        });

        if (response.ok) {
            const data = await response.json()
            return data.isLoggedIn
        } else {
            console.error("로그인 상태 알 수 없음")
            return false;
        }
    } catch (error) {
        console.error("로그인 에러", error)
        return false
    }
}

