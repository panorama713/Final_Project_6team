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
    passwordCheckError.textContent = "입력하신 비밀번호가 일치하지 않습니다.";
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

// 이벤트 핸들러
function handleDeleteUser(event, passwordCheckError) {
    event.preventDefault();

    const formData = getFormData(event.target);
    const mappedData = {
        password: formData.password,
        passwordCheck: formData.passwordConfirm
    };

    if (!passwordsMatch(formData.password, formData.passwordConfirm)) {
        displayPasswordMismatchError(passwordCheckError);
        return;
    }

    document.getElementById('result1').innerHTML = `
                                <h6>정말 탈퇴하시겠습니까?</h6>
                                <p>
                                    탈퇴를 원하시는 경우, 아래 입력창에 <span class="highlight-text">"삭제합니다."</span> 를 입력해주세요.
                                </p>
                                <label for="confirmationInput">
                                    <input type="text" id="confirmationInput" class="form-control">
                                </label>`;

    const confirmButton = document.getElementById('confirmButton')
    confirmButton.disabled = true

    document.getElementById('confirmationInput').addEventListener('input', function () {
        const inputValue = this.value.trim();
        confirmButton.disabled = inputValue !== "삭제합니다.";
    })

    confirmButton.addEventListener('click', async function () {
        process(mappedData)
    });
}