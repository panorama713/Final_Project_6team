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
            document.getElementById('usernameResult').textContent = await response.text()
            const modal = new bootstrap.Modal(document.getElementById('findUsernameModal'));
            modal.show();
        } else {
            const errorData = await response.json();
            alert(errorData.message || "해당 계정이 존재하지 않습니다.");
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