// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);

function attachEventListeners() {
    const signUpForm = document.getElementById("changePwForm");
    const fields = {
        password: signUpForm.querySelector("#newPassword"),
        passwordCheck: signUpForm.querySelector("#passwordCheck"),
    };

    const errors = {
        passwordError: document.getElementById("newPasswordError"),
        passwordCheckError: document.getElementById("passwordCheckError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    signUpForm.addEventListener("submit", function (event) {
        handleChangePw(event);
    });

    // 사용자의 비밀번호 변경 요청을 처리하는 핸들러
    function handleChangePw(event) {
        event.preventDefault();

        // 새 비밀번호와 비밀번호 확인의 유효성 검사
        const isValidPassword = checkPasswordValid(fields.password, errors.passwordError);
        const isPasswordMatched = checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isValidPassword || !isPasswordMatched) {
            return;
        }

        const formData = getFormData(event.target);
        changePw(formData).catch(error => {
            console.error("비밀번호 변경 처리 중 오류 발생:", error);
        });

        // 유효성 검사가 모두 통과된 후 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('changePasswordModal'));
        modal.show();
    }

    fields.password.addEventListener("input", function () {
        checkPasswordValid(fields.password, errors.passwordError);
    });

    fields.passwordCheck.addEventListener("input", function () {
        checkPasswordMatch(fields.password, fields.passwordCheck, errors.passwordCheckError);
    });
}