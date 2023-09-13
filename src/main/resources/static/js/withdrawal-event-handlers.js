// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const withdrawalForm = document.getElementById("withdrawalForm");
    const fields = {
        password: withdrawalForm.querySelector("#password"),
        passwordCheck: withdrawalForm.querySelector("#passwordConfirm"),
    };
    const passwordCheckError = document.getElementById("passwordCheckError");

    // 폼 제출 이벤트에 대한 핸들러 연결
    withdrawalForm.addEventListener("submit", function (event) {
        handleDeleteUser(event, passwordCheckError);
    });

    // 비밀번호 확인 입력 이벤트에 대한 핸들러 연결
    fields.passwordCheck.addEventListener("input", function () {
        checkPasswordMatch(fields, passwordCheckError);
    });
}