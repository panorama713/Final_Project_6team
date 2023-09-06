// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', wrap);

function wrap() {
    attachEventListeners()
    clickBackButton()
}

function attachEventListeners() {
    const findUsernameForm = document.getElementById("findUsernameForm");

    // 폼 제출 이벤트에 대한 핸들러 연결
    findUsernameForm.addEventListener("submit", function(event) {
        handleFindUsername(event);
    });
}

function clickBackButton() {
    const backButton = document.querySelector('.back-btn')

    if (backButton) {
        backButton.addEventListener('click', () => {
            window.location.href = "/views/login"
        })
    }
}