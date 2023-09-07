// 로그인 체크
async function checkLogin() {
    try {
        const response = await fetch('/api/v1/users/check-login');

        if (response.ok) {
            return await response.json()
        } else {
            console.error("로그인 상태 알 수 없음")
            return null;
        }
    } catch (error) {
        console.error("로그인 에러", error)
        return null
    }
}

const profileImage = document.getElementById('profileImg')
const username = document.getElementById('username')
const userInfo = document.querySelector('.info-logout')
const loginButton = document.querySelector('.info-login')
const logoutButton = document.getElementById('logout-button')

async function initAuthStatus() {
    const isLoggedIn = await checkLogin()

    if (isLoggedIn != null) {
        profileImage.src = isLoggedIn.profileImg
        username.textContent = isLoggedIn.username

        userInfo.style.display = 'block'
        loginButton.style.display = 'none'
    } else {
        userInfo.style.display = 'none'
        loginButton.style.display = 'block'
    }
}

window.addEventListener('DOMContentLoaded', initAuthStatus)

logoutButton.addEventListener('click', async() => {
    try {
        const response = await fetch('/api/v1/users/logout', {
            method: "POST"
        })

        if (response.ok) {
            window.location.replace("/views/main")

            userInfo.style.display = 'none'
            loginButton.style.display = 'block'
        }
    } catch (error) {
        console.error("로그아웃 실패", error)
    }
})

document.querySelector('.search-btn').addEventListener('click', function(event) {
    event.preventDefault();
    const searchInput = document.getElementById('input-keyword').value;

    window.location.href = `/views/search?query=${encodeURIComponent(searchInput)}`
})