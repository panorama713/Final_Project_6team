$(document).ready(function () {
    adjustFillProgress();
    const data = getRoadmap();
    getRoadmapElement(data);
});
window.addEventListener('resize', adjustFillProgress);

/*
* 로드맵 찾기
* */
async function getRoadmap() {
    const url = new URL('http://localhost:8080/api/v1/roadmaps');
    // 선택된 드롭다운의 값을 가져옵니다.
    var value = document.querySelector(".form-select").value;
    const params = { year: value }
    url.search = new URLSearchParams(params).toString();

    return await fetch(url, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    })
        .then((res) => res.json())
        .then((res) => console.log(res))
        .catch((err) => console.log(err))
}

// 현재 년도
var now = new Date();
var year = now.getFullYear();

// 드롭다운의 default 값을 현재 년도로 설정합니다.
document.querySelector(".form-select").value = year;
// 드롭다운이 선택될 때마다 실행됩니다.
document.querySelector(".form-select").addEventListener("change", function () {
    getRoadmap();
});

/*
* 로드맵 element read
* */
async function getRoadmapElement(data) {
    const url = new URL(
        'http://localhost:8080/api/v1/roadmaps/' + 1 + '/elements/' + 1
    )
    return await fetch(url, {
        method: "GET",
        headers: {"Content-Type": "application/json"}
    })
        .then((res) => res.json())
        .then((res) => console.log(res))
        .catch((err) => console.log(err))
}

/*
* 타임라인 차트 표시
* */
function adjustFillProgress() {
    const fillProgresses = document.querySelectorAll('.fill-progress');

    for (const fillProgress of fillProgresses) {
        const start = parseInt(fillProgress.dataset.start, 0) || 0;
        const end = parseInt(fillProgress.dataset.end, 0) || 0;
        const color = fillProgress.dataset.color || 'rgba(80,76,134,0.85)' +
            '\n';

        const parentWidth = fillProgress.parentElement.offsetWidth;
        const fillWidth = (parentWidth * end / 100) - (parentWidth * start / 100);

        fillProgress.style.width = fillWidth + 'px';
        fillProgress.style.left = start + '%';
        fillProgress.style.backgroundColor = color;
    }
}