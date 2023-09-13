// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', wrap);

function wrap() {
    attachEventListeners()
    clickBackButton()
}

function attachEventListeners() {
    const findUsernameForm = document.getElementById("findUsernameForm");
    const fields = {
        realName: findUsernameForm.querySelector("#realName"),
        email: findUsernameForm.querySelector("#email"),
        question: findUsernameForm.querySelector("#question"),
        answer: findUsernameForm.querySelector("#answer")
    };

    const errors = {
        realNameError: document.getElementById("realNameError"),
        emailError: document.getElementById("emailError"),
        questionError: document.getElementById("questionError"),
        answerError: document.getElementById("answerError")
    };

    // 폼 제출 이벤트에 대한 핸들러 연결
    findUsernameForm.addEventListener("submit", function (event) {
        handleFindUsername(event, fields);
    });

    // 사용자의 아이디 찾기 요청을 처리하는 핸들러
    function handleFindUsername(event) {
        event.preventDefault();

        // 실명, 이메일, 질문, 답변의 유효성 검사
        const isRealNameValid = checkRealNameValid(fields.realName, errors.realNameError);
        const isEmailValid = checkEmailValid(fields.email, errors.emailError);
        const isQuestionValid = checkQuestionValid(fields.question, errors.questionError);
        const isAnswerValid = checkAnswerValid(fields.answer, errors.answerError);

        // 하나라도 유효하지 않다면 서버로 데이터를 전송하지 않음
        if (!isRealNameValid || !isEmailValid || !isQuestionValid || !isAnswerValid) {
            return;
        }

        const formData = getFormData(event.target);
        findUsername(formData).catch(error => {
            console.error("아이디 찾기 처리 중 오류 발생:", error);
        });

        // 유효성 검사가 모두 통과된 후 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('findUsernameModal'));
        modal.show();
    }

    fields.realName.addEventListener("input", function () {
        checkRealNameValid(fields.realName, errors.realNameError);
    });

    fields.email.addEventListener("input", function () {
        checkEmailValid(fields.email, errors.emailError);
    });

    fields.question.addEventListener("input", function () {
        checkQuestionValid(fields.question, errors.questionError);
    });

    fields.answer.addEventListener("input", function () {
        checkAnswerValid(fields.answer, errors.answerError);
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