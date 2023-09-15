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

function checkPasswordValid(passwordInput, passwordError) {
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[`~₩!@#$%^&*]).{7,19}$/;

    if (!passwordInput.value) {
        displayError(passwordError, "새로운 비밀번호는 필수입니다.");
        return false;
    } else if (!passwordPattern.test(passwordInput.value)) {
        displayError(passwordError, "비밀번호는 8~20자의 영문, 숫자, 특수문자를 모두 포함해야 합니다.");
        return false;
    } else {
        hideError(passwordError);
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

// 정보 수정 처리와 서버 통신 관련
async function sendDataToServer(formData) {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        console.error("사용자 ID를 가져올 수 없습니다.");
        throw new Error('Invalid userId');
    }

    return await fetch(`/api/v1/users/${userId}/update`, {
        method: "PUT",
        body: formData
    });
}