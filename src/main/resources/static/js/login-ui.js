window.addEventListener('DOMContentLoaded', init);

function init() {
    createInputFields();
    setupPasswordVisibility();
}

// 사용자 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const idRow = document.querySelector('.input-id');

    if (idRow) {
        const label = idRow.getAttribute('data-label');
        const type = idRow.getAttribute('data-type');
        const id = idRow.getAttribute('data-id');

        idRow.innerHTML = `
            <div class="row mb-1">
                <div class="col">
                    <input type="${type}" placeholder="${label}" class="form-control" id="${id}" name="${id}" required>
                </div>
            </div>
        `;
    }

    const pwRow = document.querySelector('.input-pw');

    if (pwRow) {
        const label = pwRow.getAttribute('data-label');
        const type = pwRow.getAttribute('data-type');
        const id = pwRow.getAttribute('data-id');

        pwRow.innerHTML = `
            <div class="row mb-1">
                <div class="col">
                    <div class="pw-eye">
                        <input type="${type}" placeholder="${label} 8~20자" class="form-control" id="${id}" name="${id}" required>
                        <i class="fa-solid fa-eye fa-lg" style="color: #000000;"></i>
                    </div>
                </div>
            </div>
        `;
    }
}

// 비밀번호 보이기 기능 설정
function setupPasswordVisibility() {
    const pwEye = document.querySelector('.pw-eye');
    if (pwEye) {
        const pwInput = pwEye.querySelector('input');
        const pwIcon = pwEye.querySelector('i');

        pwIcon.addEventListener('click', () => {
            if (pwInput.type === 'password') {
                pwInput.type = 'text';
                pwIcon.style.color = '#a7a5cd';
            } else {
                pwInput.type = 'password';
                pwIcon.style.color = '#000000';
            }
        });
    }
}