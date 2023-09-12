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
        handleSignUp(event, fields);
    });

    // 사용자의 회원가입 요청을 처리하는 핸들러
    function handleSignUp(event, fields) {
        event.preventDefault();

        // 각 항목의 유효성 검사
        const isValidUsername = checkUsernameValid(fields.username, errors.usernameError);
        const isValidPassword = checkPasswordValid(fields.password, errors.passwordError);
        const isPasswordMatched = checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);
        const isRealNameValid = checkRealNameValid(fields.realName, errors.realNameError);
        const isEmailValid = checkEmailValid(fields.email, errors.emailError);
        const isPhoneValid = checkPhoneValid(fields.phone, errors.phoneError);
        const isQuestionValid = checkQuestionValid(fields.question, errors.questionError);
        const isAnswerValid = checkAnswerValid(fields.answer, errors.answerError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isValidUsername || !isValidPassword || !isPasswordMatched || !isRealNameValid || !isEmailValid || !isPhoneValid || !isQuestionValid || !isAnswerValid) {
            return;
        }

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
        checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);
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