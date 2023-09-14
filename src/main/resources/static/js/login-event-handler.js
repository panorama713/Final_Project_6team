// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);
window.addEventListener('DOMContentLoaded', clickToSignUpPage);
window.addEventListener('DOMContentLoaded', preventBack);

function attachEventListeners() {
    const loginForm = document.getElementById("login-form");
    const fields = {
        username: loginForm.querySelector("#username"),
        password: loginForm.querySelector("#password"),
    };

    const errors = {
        usernameError: document.getElementById("usernameError"),
        passwordError: document.getElementById("passwordError"),
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    loginForm.addEventListener("submit", function (event) {
        handleLogin(event, fields);
    });

    // 사용자의 로그인 요청을 처리하는 핸들러
    function handleLogin(event) {
        event.preventDefault()

        // 아이디와 비밀번호의 유효성 검사
        const isValidUsername = checkUsernameValid(fields.username, errors.usernameError);
        const isValidPassword = checkPasswordValid(fields.password, errors.passwordError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isValidUsername || !isValidPassword) {
            return;
        }

        const formData = getFormData(event.target)
        loginUser(formData).catch(error => {
            console.error("로그인 처리 중 오류 발생:", error);
        });
    }

    fields.username.addEventListener("input", function () {
        checkUsernameValid(fields.username, errors.usernameError);
    });

    fields.password.addEventListener("input", function () {
        checkPasswordValid(fields.password, errors.passwordError);
    });
}

function clickToSignUpPage() {
    const signUpButton = document.querySelector(".sign-up-btn")

    signUpButton.addEventListener("click", () => {
        window.location.href = "/views/signup"
    })
}

function preventBack() {
    const socialImages = document.querySelectorAll(".social-image")

    socialImages.forEach(imageLink => {
        imageLink.addEventListener("click", () => {
            localStorage.setItem('isLoggedIn', 'O')
            window.history.pushState({}, null, "/views/main")
        })
    })
}