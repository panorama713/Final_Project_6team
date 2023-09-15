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

function checkPasswordMatch(passwordField, passwordCheckField, passwordCheckError) {
    if (!passwordsMatch(passwordField.value, passwordCheckField.value)) {
        displayError(passwordCheckError, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return false;
    } else {
        hideError(passwordCheckError);
        return true;
    }
}

// 회원 탈퇴 처리와 서버 통신 관련
async function sendDataToServer(data) {
    const userId = localStorage.getItem('userId')

    if (userId == null) {
        document.getElementById('result').textContent = '다시 실행해주세요.'
    }

    return await fetch(`/api/v1/users/${userId}/withdrawal`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}

async function process(data) {
    try {
        const response = await sendDataToServer(data);
        if (response.ok) {
            document.getElementById('result2').textContent = '회원탈퇴가 완료되었습니다.';
            document.getElementById('confirmButton2').addEventListener('click', function () {
                localStorage.removeItem('userId');
                localStorage.removeItem('isLoggedIn');
                window.location.href = '/views/main';
            })
        } else {
            const errorData = await response.json();
            document.getElementById('result2').textContent = errorData.message
            document.getElementById('confirmButton2').addEventListener('click', function () {
                window.location.reload();
            });
        }
    } catch (error) {
        console.error("#console# 에러", error);
    }
}