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
        question: signUpForm.querySelector("#question"),
        answer: signUpForm.querySelector("#answer"),
        personalInfoAgreement: signUpForm.querySelector("#personalInfoAgreement"),
        serviceAgreement: signUpForm.querySelector("#serviceAgreement")
    };

    const errors = {
        usernameError: document.getElementById("usernameError"),
        passwordError: document.getElementById("passwordError"),
        passwordCheckError: document.getElementById("passwordCheckError"),
        realNameError: document.getElementById("realNameError"),
        emailError: document.getElementById("emailError"),
        phoneError: document.getElementById("phoneError"),
        questionError: document.getElementById("questionError"),
        answerError: document.getElementById("answerError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    signUpForm.addEventListener("submit", function(event) {
        handleSignUp(event, fields, errors.passwordCheckError);
    });

    // 사용자의 회원가입 요청을 처리하는 핸들러
    function handleSignUp(event, fields) {
        event.preventDefault();

        if (!fields.personalInfoAgreement.checked || !fields.serviceAgreement.checked) {
            alert("모든 필수 항목에 동의해주세요.");
            return;
        }

        const formData = getFormData(event.target);
        registerUser(formData).catch(error => {
            console.error("회원가입 처리 중 오류 발생:", error);
        });
    }

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

    fields.question.addEventListener("input", function() {
        checkQuestionValid(fields.question, errors.questionError);
    });

    fields.answer.addEventListener("input", function() {
        checkAnswerValid(fields.answer, errors.answerError);
    });
}