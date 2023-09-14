// 동적 UI 구성
window.addEventListener('DOMContentLoaded', init);

function init() {
    createInputFields();
}

// 사용자 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const container = document.querySelector('.user-info-inputs');

    container.querySelectorAll('.input-row').forEach(row => {
        const label = row.getAttribute('data-label');
        const type = row.getAttribute('data-type');
        const id = row.getAttribute('data-id');

        let inputElement = '';
        if (type === 'file' && id === 'profileImg') {
            inputElement = `
        <label for="profileImg" class="form-label">${label}</label>
        <input type="file" class="form-control" id="profileImg" name="profileImg" accept="image/*">
    `;
        } else {
            inputElement = `<input type="${type}" class="form-control" id="${id}" name="${id}" required>`;
        }

        let errorElement = '';
        if (id === 'newPassword' || id === 'passwordCheck' || id === 'newEmail' || id === 'newPhone') {
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