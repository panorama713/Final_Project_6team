// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', wrap);

function wrap() {
    attachEventListeners()
    clickBackButton()
}

function attachEventListeners() {
    const findPasswordForm = document.getElementById("findPasswordForm");
    const fields = {
        realName: findPasswordForm.querySelector("#realName"),
        username: findPasswordForm.querySelector("#username"),
    };

    const errors = {
        realNameError: document.getElementById("realNameError"),
        usernameError: document.getElementById("usernameError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    findPasswordForm.addEventListener("submit", function(event) {
        handleFindPassword(event, fields);
    });

    // 사용자의 비밀번호 찾기 요청을 처리하는 핸들러
    function handleFindPassword(event) {
        event.preventDefault();

        // 실명, 아이디의 유효성 검사
        const isRealNameValid = checkRealNameValid(fields.realName, errors.realNameError);
        const isValidUsername = checkUsernameValid(fields.username, errors.usernameError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isRealNameValid || !isValidUsername) {
            return;
        }

        const formData = getFormData(event.target);
        findPassword(formData).catch(error => {
            console.error("비밀번호 찾기 처리 중 오류 발생:", error);
        });

        // 유효성 검사가 모두 통과된 후 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('findPasswordModal'));
        modal.show();
    }

    fields.realName.addEventListener("input", function() {
        checkRealNameValid(fields.realName, errors.realNameError);
    });

    fields.username.addEventListener("input", function() {
        checkUsernameValid(fields.username, errors.usernameError);
    });
}

function clickBackButton() {
    const backButton = document.querySelector('.back-btn')

    if (backButton) {
        backButton.addEventListener('click', () => {
            window.location.href = "/views/login"
        })
    }
}