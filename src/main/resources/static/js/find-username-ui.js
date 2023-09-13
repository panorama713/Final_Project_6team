const QUESTIONS = {
    NICKNAME: "어렸을 적 기억나는 별명은?",
    TEACHER_NAME: "학창시절 기억나는 선생님 혹은 짝꿍의 이름은?",
    FIRST_PET: "첫 애완동물의 이름은?",
    FIRST_FLIGHT_DESTINATION: "처음으로 비행기를 타고 방문한 곳은?"
};

// 동적 UI 구성
window.addEventListener('DOMContentLoaded', init);

function init() {
    createInputFields();
}

// 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const container = document.querySelector('.account-info-inputs');

    container.querySelectorAll('.input-row').forEach(row => {
        const label = row.getAttribute('data-label');
        const type = row.getAttribute('data-type');
        const id = row.getAttribute('data-id');

        let inputElement = '';
        if (row.classList.contains('select-row')) {
            inputElement = `
                <select class="form-select" id="${id}" name="${id}" required>
                    <option selected disabled>보안 질문을 선택하세요.</option>
                    ${Object.entries(QUESTIONS).map(([key, value]) =>
                `<option value="${key}">${value}</option>`).join('')
            }
                </select>
            `;
        } else {
            inputElement = `<input type="${type}" class="form-control" id="${id}" name="${id}" required>`;
        }

        let errorElement = '';
        if (id === 'realName' || id === 'email' || id === 'question' || id === 'answer') {
            errorElement = `<div id="${id}Error" class="text-danger"></div>`;
        }

        // HTML 구조를 동적으로 생성
        row.innerHTML = `
            <div class="row mb-3">
                <label for="${id}" class="form-label col-sm-3">${label}</label>
                <div class="col-sm-9">
                    ${inputElement}
                    ${errorElement}
                </div>
            </div>
        `;
    });
}