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

// 서버 통신 관련
async function sendDataToServer(data) {
    return await fetch("/api/v1/users/find/password", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}

async function findPassword(data) {
    try {
        const response = await sendDataToServer(data);

        if (response.ok) {
            localStorage.setItem('userId', await response.text())
            document.getElementById('passwordResult').textContent = '계정이 확인되었습니다. 비밀번호 변경 페이지로 이동합니다.'
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.href = '/views/change-password';
            });
        } else {
            const errorData = await response.json();
            document.getElementById('passwordResult').textContent = errorData.message
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.reload();
            });
        }
    } catch (error) {
        console.error("#console# 에러", error);
    }
}