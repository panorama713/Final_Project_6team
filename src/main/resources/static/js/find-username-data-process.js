function getFormData(form) {
    const formData = new FormData(form);
    const dataObj = {};
    formData.forEach((value, key) => dataObj[key] = value);
    return dataObj;
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
        console.error("#console# 회원가입 에러", error);
    }
}

// 이벤트 핸들러
function handleFindUsername(event) {
    event.preventDefault();

    const formData = getFormData(event.target);

    findUsername(formData);
}