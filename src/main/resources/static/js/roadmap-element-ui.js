// 이벤트 리스너 연결, 동적 UI 구성
window.addEventListener('DOMContentLoaded', createInputFields);

// 로드맵 엘리먼트 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const modalBody = document.querySelector('.add-element-body');

    modalBody.querySelectorAll('.input-row').forEach(row => {
        const label = row.getAttribute('data-label');
        const type = row.getAttribute('data-type');
        const id = row.getAttribute('data-id');

        // HTML 구조를 동적으로 생성
        row.innerHTML = `
            <div class="row mb-3">
                <label for="${id}" class="form-label col-sm-3">${label}</label>
                <div class="col-sm-9">
                    <input type="${type}" class="form-control" id="${id}" name="${id}" required>
                </div>
            </div>
        `;
    });
}