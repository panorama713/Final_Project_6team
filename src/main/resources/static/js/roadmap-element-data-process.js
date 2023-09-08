// 현재 페이지의 로드맵 ID 추출
function getRoadmapIdFromUrl() {
    const regex = /\/roadmaps\/(\d+)\/elements/;
    const match = window.location.pathname.match(regex);
    return match[1];
}

function getFormData(form) {
    const formData = new FormData(form)
    const dataObj = {}
    formData.forEach((value, key) => dataObj[key] = value)
    console.log(formData);
    return dataObj
}

// 로드맵 요소 생성 API 요청 함수
async function createRoadmapElement(data, roadmapId) {
    await fetch(`/api/v1/roadmaps/${roadmapId}/elements`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    }).then(async response => {
        console.log(response);

        if (response.ok) {
            window.location.href = `/views/roadmaps/${roadmapId}/elements`;
        } else {
            const errorRes = await response.json()
            alert(errorRes.message || "에러")
        }
    })
}