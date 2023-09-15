// 기본 유틸리티 함수
function getFormData(form) {
    const formData = new FormData(form)
    const dataObj = {}
    formData.forEach((value, key) => dataObj[key] = value)
    return dataObj
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

// 로그인 처리와 서버 통신 관련
async function sendDataToServer(data) {
    return await fetch("/api/v1/users/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
}

async function loginUser(data) {
    try {
        const response = await sendDataToServer(data);

        if (response.ok) {
            localStorage.setItem('isLoggedIn', 'O')
            window.location.replace("/views/main")
        } else {
            const errorRes = await response.json()
            alert(errorRes.message || "에러")
            window.location.replace("/views/login")
        }
    } catch (error) {
        console.error("로그인 에러", error)
    }
}