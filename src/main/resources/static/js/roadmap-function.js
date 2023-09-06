window.addEventListener('DOMContentLoaded', createInputFields);
// 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', attachEventListeners);
// 로드맵 엘리먼트 정보 입력 필드를 동적으로 생성
function createInputFields() {
    const modalBody = document.querySelector('.modal-body');

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

function attachEventListeners() {
    const modalForm = document.getElementById('addRoadmapElement');

    // 폼 제출 이벤트에 대한 핸들러 연결
    modalForm.addEventListener("submit", function(event) {
        handleAddRoadmapElement(event);
    });
}

function handleAddRoadmapElement(event) {
    event.preventDefault()

    const formData = getFormData(event.target)

    createRoadmapElement(formData).then(() => {
        window.history.pushState({}, null, '/views/roadmap-function')
    }).catch(error => {
        console.error("로드맵 엘리멘트 생성 에러", error)
    })
}

function getFormData(form) {
    const formData = new FormData(form)
    const dataObj = {}
    formData.forEach((value, key) => dataObj[key] = value)
    console.log(formData);
    return dataObj
}

async function createRoadmapElement(data) {
    await fetch("/api/v1/roadmaps/1/elements", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    }).then(async response => {
        console.log(response);

        if (response.ok) {
            window.location.href = "/views/roadmap-function";
        } else {
            const errorRes = await response.json()
            alert(errorRes.message || "에러")
        }
    })
    // try {
    //     const response = await sendDataToServer(data);
    //     console.log(response);
    //
    //     if (response.ok) {
    //         window.location.href = "/views/roadmap-function"
    //     } else {
    //         const errorRes = await response.json()
    //         alert(errorRes.message || "에러")
    //     }
    //     // window.location.href = "/views/roadmap-function"
    //
    // } catch (error) {
    //     console.error("로드맵 엘리멘트 생성 에러", error)
    // }
}

async function sendDataToServer(data) {
    return await fetch("/api/v1/roadmaps/1/elements", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
}