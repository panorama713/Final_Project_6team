// 로그인 체크
function checkLogin() {
    return localStorage.getItem('isLoggedIn');
}

const profileImage = document.getElementById('profileImg')
const username = document.getElementById('username')
const userInfo = document.querySelector('.info-logout')
const loginButton = document.querySelector('.info-login')
const logoutButton = document.getElementById('logout-button')

async function initAuthStatus() {
    const isLoggedIn = checkLogin()

    if (isLoggedIn != null) {
        fetch('/api/v1/users/mini-profile').then((response) => {
            return response.json();
        }).then((data) => {
            profileImage.src = data.profileImg
            username.textContent = data.username
        }).catch((error) => {
            console.error(error.message);
        })

        userInfo.style.display = 'block'
        loginButton.style.display = 'none'
    } else {
        userInfo.style.display = 'none'
        loginButton.style.display = 'block'
    }
}

window.addEventListener('DOMContentLoaded', initAuthStatus)

logoutButton.addEventListener('click', async () => {
    try {
        const response = await fetch('/api/v1/users/logout', {
            method: "POST"
        })

        if (response.ok) {
            window.location.replace("/views/main")

            localStorage.removeItem('isLoggedIn')
            userInfo.style.display = 'none'
            loginButton.style.display = 'block'
        }
    } catch (error) {
        console.error("로그아웃 실패", error)
    }
})

document.querySelector('.search-btn').addEventListener('click', function (event) {
    event.preventDefault();
    const searchInput = document.getElementById('input-keyword').value;

    window.location.href = `/views/search?query=${encodeURIComponent(searchInput)}`
})

// 게시판 카테고리
function handleButtonClick(buttonId) {
    if (buttonId === 'category-front-end') {
        localStorage.setItem('currentCategory', 'FRONTEND');
        localStorage.setItem('currentPage', 0);
    } else if (buttonId === 'category-back-end') {
        localStorage.setItem('currentCategory', 'BACKEND');
        localStorage.setItem('currentPage', 0);
    } else if (buttonId === 'category-mobile') {
        localStorage.setItem('currentCategory', 'MOBILE');
        localStorage.setItem('currentPage', 0);
    } else if (buttonId === 'category-game') {
        localStorage.setItem('currentCategory', 'GAME');
        localStorage.setItem('currentPage', 0);
    } else if (buttonId === 'category-devops') {
        localStorage.setItem('currentCategory', 'DEVOPS');
        localStorage.setItem('currentPage', 0);
    }
    window.location.href = '/views/articles/list';
}

var frontEndBtn = document.getElementById("category-front-end");
frontEndBtn.addEventListener("click", function () {
    handleButtonClick('category-front-end');
});

var backEndBtn = document.getElementById("category-back-end");
backEndBtn.addEventListener("click", function () {
    handleButtonClick('category-back-end');
});

var mobileBtn = document.getElementById("category-mobile");
mobileBtn.addEventListener("click", function () {
    handleButtonClick('category-mobile');
});

var gameBtn = document.getElementById("category-game");
gameBtn.addEventListener("click", function () {
    handleButtonClick('category-game');
});

var devOpsBtn = document.getElementById("category-devops");
devOpsBtn.addEventListener("click", function () {
    handleButtonClick('category-devops');
});