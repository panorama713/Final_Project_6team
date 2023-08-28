// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);
window.addEventListener('DOMContentLoaded', clickToSignUpPage);
window.addEventListener('DOMContentLoaded', preventBack);

function attachEventListeners() {
    const loginForm = document.getElementById("login-form");

    // 폼 제출 이벤트에 대한 핸들러 연결
    loginForm.addEventListener("submit", function(event) {
        handleLogin(event);
    });
}

function clickToSignUpPage() {
    const signUpButton = document.querySelector(".sign-up-btn")

    signUpButton.addEventListener("click", () => {
        window.location.href = "/views/signup"
    })
}

function preventBack() {
    const socialImages = document.querySelectorAll(".social-image")

    socialImages.forEach(imageLink => {
        imageLink.addEventListener("click", () => {
            window.history.pushState({}, null, "/views/main")
        })
    })
}