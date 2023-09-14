// 동적 UI 구성
window.addEventListener('DOMContentLoaded', init);

function init() {
    createInputFields();
    setupPasswordVisibility();
    setupSaveUsername();
    setupIdSearchLink();
    setupPwSearchLink();
}

// 사용자 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const container = document.querySelector('.account-info-input');

    container.querySelectorAll('.input-row').forEach(row => {
        const label = row.getAttribute('data-label');
        const type = row.getAttribute('data-type');
        const id = row.getAttribute('data-id');

        let errorElement = '';
        if (id === 'username' || id === 'password') {
            errorElement = `<div id="${id}Error" class="text-danger"></div>`;
        }

        // HTML 구조를 동적으로 생성
        row.innerHTML = `
            <div class="row mb-3">
                <label for="${id}" class="form-label col-sm-3">${label}</label>
                <div class="col-sm-9">
                    <input type="${type}" class="form-control" id="${id}" name="${id}" required>
                    ${errorElement}
                </div>
            </div>
        `;
    });
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

function setupSaveUsername() {
    const saveUsernameCheckbox = document.getElementById('save-username');
    const usernameInput = document.getElementById('username');

    // 페이지가 로드될 때 저장된 아이디를 불러와 입력 필드에 적용
    const savedUsername = getCookie('savedUsername');
    if (savedUsername) {
        usernameInput.value = savedUsername;
        saveUsernameCheckbox.checked = true;
    }

    // 체크박스 상태가 변경될 때 쿠키 설정/삭제
    saveUsernameCheckbox.addEventListener('change', () => {
        if (saveUsernameCheckbox.checked) {
            setCookie('savedUsername', usernameInput.value, 365); // 쿠키를 1년 동안 저장
        } else {
            deleteCookie('savedUsername');
        }
    });
}

// 쿠키 설정 함수
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`;
}

// 쿠키 가져오는 함수
function getCookie(name) {
    const cookieValue = document.cookie.match(`(^|;) ?${name}=([^;]*)(;|$)`);
    return cookieValue ? cookieValue[2] : null;
}

// 쿠키 삭제 함수
function deleteCookie(name) {
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/`;
}

// 아이디 찾기 페이지로 이동하는 함수
function setupIdSearchLink() {
    const idSearchLink = document.getElementById('idSearch');
    idSearchLink.addEventListener('click', () => {
        window.location.href = '/views/find/username';
    });
}

// 비밀번호 찾기 페이지로 이동하는 함수
function setupPwSearchLink() {
    const pwSearchLink = document.getElementById('pwSearch');
    pwSearchLink.addEventListener('click', () => {
        window.location.href = '/views/find/password';
    });
}