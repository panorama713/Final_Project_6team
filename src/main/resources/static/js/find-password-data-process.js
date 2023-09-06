function getFormData(form) {
    const formData = new FormData(form);
    const dataObj = {};
    formData.forEach((value, key) => dataObj[key] = value);
    return dataObj;
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
            const modal = new bootstrap.Modal(document.getElementById('findPasswordModal'));
            modal.show();
        } else {
            const errorData = await response.json();
            alert(errorData.message || "해당 계정이 존재하지 않습니다.");
        }
    } catch (error) {
        console.error("#console# 에러", error);
    }
}

// 이벤트 핸들러
function handleFindPassword(event) {
    event.preventDefault();

    const formData = getFormData(event.target);

    findPassword(formData);
}