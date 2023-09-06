// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', wrap);

function wrap() {
    attachEventListeners()
    clickBackButton()
}

function attachEventListeners() {
    const findPasswordForm = document.getElementById("findPasswordForm");

    // 폼 제출 이벤트에 대한 핸들러 연결
    findPasswordForm.addEventListener("submit", function(event) {
        handleFindPassword(event);
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