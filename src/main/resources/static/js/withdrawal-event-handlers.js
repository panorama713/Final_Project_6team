// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const withdrawalForm = document.getElementById("withdrawalForm");
    const fields = {
        password: withdrawalForm.querySelector("#password"),
        passwordCheck: withdrawalForm.querySelector("#passwordCheck"),
    };

    const errors = {
        passwordError: document.getElementById("passwordError"),
        passwordCheckError: document.getElementById("passwordCheckError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    withdrawalForm.addEventListener("submit", function (event) {
        handleDeleteUser(event);
    });

    // 사용자의 회원 탈퇴 요청을 처리하는 핸들러
    function handleDeleteUser(event) {
        event.preventDefault();

        // 비밀번호와 비밀번호 확인의 유효성 검사
        const isValidPassword = checkPasswordValid(fields.password, errors.passwordError);
        const isPasswordMatched = checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isValidPassword || !isPasswordMatched) {
            return;
        }

        const formData = getFormData(event.target);
        sendDataToServer(formData)
            .then(response => {
                if (response.ok) {
                    const modal = new bootstrap.Modal(document.getElementById('withdrawalModal'));
                    modal.show();
                    setupWithdrawalModal();
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
}