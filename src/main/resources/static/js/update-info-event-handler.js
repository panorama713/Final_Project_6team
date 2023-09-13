// 폼 제출 이벤트 핸들러
document.getElementById("updateInfoForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData();

    formData.append('newPassword', this.querySelector("#newPassword").value);
    formData.append('newEmail', this.querySelector("#newEmail").value);
    formData.append('newPhone', this.querySelector("#newPhone").value);

    const dtoData = {
        password: formData.get('newPassword'),
        email: formData.get('newEmail'),
        phone: formData.get('newPhone')
    }
    const json = JSON.stringify(dtoData);
    const blob = new Blob([json], {type: 'application/json'})

    formData.append('dto', blob);

    const imageInput = document.getElementById('profileImg')
    if (imageInput.files.length > 0) {
        const imageFile = imageInput.files[0];
        formData.append('profileImg', imageFile);
    }

    // 서버로 데이터를 전송하고 처리하는 함수 호출
    sendDataToServer(formData);
});

// 서버로 데이터를 전송하고 처리하는 함수
function sendDataToServer(formData) {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        console.error("사용자 ID를 가져올 수 없습니다.");
        return;
    }

    fetch(`/api/v1/users/${userId}/update`, {
        method: "PUT",
        body: formData
    })
        .then((response) => {
            if (response.ok) {
                alert('정보 수정이 완료되었습니다.')
                window.location.href = '/views/my-page/profile'
            } else {
                alert('다시 시도해주세요.')
            }
        })
        .catch((error) => {
            // 오류 처리
            console.error("데이터 전송 중 오류 발생:", error);
        });
}