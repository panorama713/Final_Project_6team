// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const signUpForm = document.getElementById("sign-up-form");
    const fields = {
        username: signUpForm.querySelector("#username"),
        password: signUpForm.querySelector("#password"),
        passwordCheck: signUpForm.querySelector("#passwordCheck"),
        realName: signUpForm.querySelector("#realName"),
        email: signUpForm.querySelector("#email"),
        phone: signUpForm.querySelector("#phone"),
        personalInfoAgreement: signUpForm.querySelector("#personalInfoAgreement"),
        serviceAgreement: signUpForm.querySelector("#serviceAgreement")
    };

    const errors = {
        usernameError: document.getElementById("usernameError"),
        passwordError: document.getElementById("passwordError"),
        passwordCheckError: document.getElementById("passwordCheckError"),
        realNameError: document.getElementById("realNameError"),
        emailError: document.getElementById("emailError"),
        phoneError: document.getElementById("phoneError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    signUpForm.addEventListener("submit", function(event) {
        handleSignUp(event, fields, errors.passwordCheckError);
    });

    fields.username.addEventListener("input", function() {
        checkUsernameValid(fields.username, errors.usernameError);
    });

    fields.password.addEventListener("input", function() {
        checkPasswordValid(fields.password, errors.passwordError);
    });

    fields.passwordCheck.addEventListener("input", function() {
        checkPasswordMatch(fields, errors.passwordCheckError);
    });

    fields.realName.addEventListener("input", function() {
        checkRealNameValid(fields.realName, errors.realNameError);
    });

    fields.email.addEventListener("input", function() {
        checkEmailValid(fields.email, errors.emailError);
    });

    fields.phone.addEventListener("input", function() {
        checkPhoneValid(fields.phone, errors.phoneError);
    });
}