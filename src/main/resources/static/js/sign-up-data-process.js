// 기본 유틸리티 함수
function passwordsMatch(password, passwordCheck) {
    return password === passwordCheck;
}

function getFormData(form) {
    const formData = new FormData(form);
    const dataObj = {};
    formData.forEach((value, key) => dataObj[key] = value);
    return dataObj;
}

function displayError(errorElement, message) {
    errorElement.textContent = message;
}

function hideError(errorElement) {
    if (errorElement) {
        errorElement.textContent = '';
    }
}

// 유효성 검사 관련
function checkPasswordMatch(passwordField, passwordCheckField, passwordCheckError) {
    if (!passwordsMatch(passwordField.value, passwordCheckField.value)) {
        displayError(passwordCheckError, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return false;
    } else {
        hideError(passwordCheckError);
        return true;
    }
}

function checkUsernameValid(usernameInput, usernameError) {
    const usernamePattern = /^[a-z][a-z0-9]{4,14}$/;

    if (!usernameInput.value) {
        displayError(usernameError, "아이디는 필수입니다.");
        return false;
    } else if (!usernamePattern.test(usernameInput.value)) {
        displayError(usernameError, "아이디는 5~15자의 영문 소문자, 숫자만 사용 가능합니다.");
        return false;
    } else {
        hideError(usernameError);
        return true;
    }
}

function checkPasswordValid(passwordInput, passwordError) {
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[`~₩!@#$%^&*]).{7,19}$/;

    if (!passwordInput.value) {
        displayError(passwordError, "비밀번호는 필수입니다.");
        return false;
    } else if (!passwordPattern.test(passwordInput.value)) {
        displayError(passwordError, "비밀번호는 8~20자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.");
        return false;
    } else {
        hideError(passwordError);
        return true;
    }
}

function checkRealNameValid(realNameInput, realNameError) {
    if (!realNameInput.value.trim()) {
        displayError(realNameError, "실명은 필수입니다.");
        return false;
    } else {
        hideError(realNameError);
        return true;
    }
}

function checkEmailValid(emailInput, emailError) {
    const emailPattern = /^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

    if (!emailInput.value.trim()) {
        displayError(emailError, "이메일은 필수입니다.");
        return false;
    } else if (!emailPattern.test(emailInput.value)) {
        displayError(emailError, "올바른 이메일 형식으로 입력해 주세요. (예: example@example.com)");
        return false;
    } else {
        hideError(emailError);
        return true;
    }
}

function checkPhoneValid(phoneInput, phoneError) {
    const phonePattern = /^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$/;

    if (!phoneInput.value.trim()) {
        displayError(phoneError, "휴대전화는 필수입니다.");
        return false;
    } else if (!phonePattern.test(phoneInput.value)) {
        displayError(phoneError, "올바른 전화번호 형식으로 입력해 주세요. (예: 01012345678)");
        return false;
    } else {
        hideError(phoneError);
        return true;
    }
}

function checkQuestionValid(questionInput, questionError) {
    if (!questionInput.value.trim()) {
        displayError(questionError, "보안 질문은 필수입니다.");
        return false;
    } else {
        hideError(questionError);
        return true;
    }
}

function checkAnswerValid(answerInput, answerError) {
    if (!answerInput.value.trim()) {
        displayError(answerError, "보안 답변은 필수입니다.");
        return false;
    } else {
        hideError(answerError);
        return true;
    }
}

// 회원가입 처리와 서버 통신 관련
async function sendDataToServer(data) {
    return await fetch("/api/v1/users/signup", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}

async function registerUser(data) {
    try {
        const response = await sendDataToServer(data);

        if (response.ok) {
            alert("회원가입이 완료되었습니다.");
            window.location.href = "/views/login";

        } else {
            const errorData = await response.json();
            alert(errorData.message || "회원가입이 실패되었습니다.");
        }
    } catch (error) {
        console.error("#console# 회원가입 에러", error);
    }
}