// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const signUpForm = document.getElementById("sign-up-form");
    const fields = {
        password: signUpForm.querySelector("#password"),
        passwordCheck: signUpForm.querySelector("#passwordCheck"),
        personalInfoAgreement: signUpForm.querySelector("#personalInfoAgreement"),
        serviceAgreement: signUpForm.querySelector("#serviceAgreement")
    };
    const passwordCheckError = document.getElementById("passwordCheckError");

    // 폼 제출 이벤트에 대한 핸들러 연결
    signUpForm.addEventListener("submit", function(event) {
        handleSignUp(event, fields, passwordCheckError);
    });

    // 비밀번호 확인 입력 이벤트에 대한 핸들러 연결
    fields.passwordCheck.addEventListener("input", function() {
        checkPasswordMatch(fields, passwordCheckError);
    });
}