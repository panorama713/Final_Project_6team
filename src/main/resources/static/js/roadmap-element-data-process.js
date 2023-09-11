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
        console.log('add-element:', response);

        if (response.ok) {
            alert('일정이 추가되었습니다!')
            location.reload();
        } else {
            const errorRes = await response.json()
            alert(errorRes.message || "에러")
        }
    })
}