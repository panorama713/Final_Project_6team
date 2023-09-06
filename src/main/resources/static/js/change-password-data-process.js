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

// 비밀번호 에러 관련
function displayPasswordMismatchError(passwordCheckError) {
    passwordCheckError.textContent = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
}

function hidePasswordError(passwordCheckError) {
    passwordCheckError.textContent = "";
}

function checkPasswordMatch(fields, passwordCheckError) {
    if (!passwordsMatch(fields.password.value, fields.passwordCheck.value)) {
        displayPasswordMismatchError(passwordCheckError);
    } else {
        hidePasswordError(passwordCheckError);
    }
}

// 회원가입 처리와 서버 통신 관련
async function sendDataToServer(data) {
    const userId = localStorage.getItem('userId')

    if (userId == null) {
        document.getElementById('result').textContent = '비밀번호 찾기를 다시 실행해주세요.'
    }

    return await fetch(`/api/v1/users/${userId}/change-password`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}

async function changePw(data) {
    try {
        const response = await sendDataToServer(data);

        if (response.ok) {
            document.getElementById('result').textContent = '비밀번호 변경이 완료되었습니다. 로그인 페이지로 이동합니다.'
            localStorage.removeItem('userId')
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.href = '/views/login';
            });
        } else {
            const errorData = await response.json();
            document.getElementById('result').textContent = errorData.message
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.reload();
            });
        }
    } catch (error) {
        console.error("#console# 에러", error);
    }
}

// 이벤트 핸들러
function handleChangePw(event, passwordCheckError) {
    event.preventDefault();

    const formData = getFormData(event.target);
    const mappedData = {
        newPassword: formData.newPw
    };

    if (!passwordsMatch(formData.password, formData.passwordCheck)) {
        displayPasswordMismatchError(passwordCheckError);
        return;
    }

    changePw(mappedData);
}