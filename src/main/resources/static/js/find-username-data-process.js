// 기본 유틸리티 함수
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

// 서버 통신 관련
async function sendDataToServer(data) {
    return await fetch("/api/v1/users/find/username", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}

async function findUsername(data) {
    console.log("Sending Data:", data);
    try {
        const response = await sendDataToServer(data);

        if (response.ok) {
            document.getElementById('usernameResult').textContent = '아이디: ' + await response.text()
            document.getElementById('confirmButton').addEventListener('click', function () {
                // 로그인 페이지로 이동
                window.location.href = '/views/login';
            });
        } else {
            const errorData = await response.json();
            document.getElementById('usernameResult').textContent = errorData.message
            document.getElementById('confirmButton').addEventListener('click', function () {
                // 로그인 페이지로 이동
                window.location.reload();
            });
        }
    } catch (error) {
        console.error("#console# 에러", error);
    }
}