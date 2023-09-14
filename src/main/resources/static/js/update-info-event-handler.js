// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const updateInfoForm = document.getElementById("updateInfoForm");
    const fields = {
        password: updateInfoForm.querySelector("#newPassword"),
        passwordCheck: updateInfoForm.querySelector("#passwordCheck"),
        email: updateInfoForm.querySelector("#newEmail"),
        phone: updateInfoForm.querySelector("#newPhone")
    };

    const errors = {
        passwordError: document.getElementById("newPasswordError"),
        passwordCheckError: document.getElementById("passwordCheckError"),
        emailError: document.getElementById("newEmailError"),
        phoneError: document.getElementById("newPhoneError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    updateInfoForm.addEventListener("submit", function (event) {
        handleUpdateInfo(event, fields);
    });

    // 사용자의 정보 수정 요청을 처리하는 핸들러
    function handleUpdateInfo(event, fields) {
        event.preventDefault();

        // 각 항목의 유효성 검사
        const isValidPassword = checkPasswordValid(fields.password, errors.passwordError);
        const isPasswordMatched = checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);
        const isEmailValid = checkEmailValid(fields.email, errors.emailError);
        const isPhoneValid = checkPhoneValid(fields.phone, errors.phoneError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isValidPassword || !isPasswordMatched || !isEmailValid || !isPhoneValid) {
            return;
        }

        const formData = new FormData();
        formData.append('newPassword', updateInfoForm.querySelector("#newPassword").value);
        formData.append('newEmail', updateInfoForm.querySelector("#newEmail").value);
        formData.append('newPhone', updateInfoForm.querySelector("#newPhone").value);

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

        sendDataToServer(formData)
            .then((response) => {
                if (response.ok) {
                    alert('정보 수정이 완료되었습니다.')
                    window.location.href = '/views/my-page/profile'
                } else {
                    response.json().then(errorData => {
                        alert(errorData.message || '다시 시도해주세요.');
                    });
                }
            })
            .catch((error) => {
                console.error("데이터 전송 중 오류 발생:", error);
            });
    }

    fields.password.addEventListener("input", function () {
        checkPasswordValid(fields.password, errors.passwordError);
    });

    fields.passwordCheck.addEventListener("input", function () {
        checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);
    });

    fields.email.addEventListener("input", function () {
        checkEmailValid(fields.email, errors.emailError);
    });

    fields.phone.addEventListener("input", function () {
        checkPhoneValid(fields.phone, errors.phoneError);
    });
}