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

// 이벤트 핸들러
function handleSignUp(event, fields, passwordCheckError) {
    event.preventDefault();

    if (!fields.personalInfoAgreement.checked || !fields.serviceAgreement.checked) {
        alert("모든 필수 항목에 동의해주세요.");
        return;
    }

    const formData = getFormData(event.target);

    if (!passwordsMatch(formData.password, formData.passwordCheck)) {
        displayPasswordMismatchError(passwordCheckError);
        return;
    }

    registerUser(formData);
}