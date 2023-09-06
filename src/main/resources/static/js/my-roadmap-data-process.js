$(document).ready(async function () {
    // 선택된 드롭다운의 값을 가져옵니다.
    var year = document.querySelector(".form-select").value;
    await getRoadmap(year);
    await drawTodayLine();
});

window.addEventListener('resize', adjustFillProgress);

// 현재 년도
// 드롭다운의 default 값을 현재 년도로 설정합니다.
// document.querySelector(".form-select").value = year;
// 드롭다운이 선택될 때마다 실행됩니다.
document.querySelector(".form-select").addEventListener("change", function () {
    year = document.querySelector(".form-select").value;
    // HTML 내용 초기화
    var container = document.querySelector('.roadmap-container');
    container.innerHTML = '';
    getRoadmap(year);
});

/*
* 타임라인 차트 표시
* */
function adjustFillProgress(roadmap) {
    console.log('start adjust...')
    const fillProgresses = document.querySelectorAll('.fill-progress');

    for (const fillProgress of fillProgresses) {
        const start = parseInt(fillProgress.dataset.start, 0) || 0;
        const end = parseInt(fillProgress.dataset.end, 0) || 0;


        const parentWidth = fillProgress.parentElement.offsetWidth;
        const fillWidth = (parentWidth * end / 100) - (parentWidth * start / 100);

        fillProgress.style.width = fillWidth + 'px';
        fillProgress.style.left = start + '%';
    }
}


function displayRoadmaps(roadmaps) {
    var container = document.querySelector('.roadmap-container');

    roadmaps.forEach(function (roadmap) {
        var roadmapList = createRoadmaps(roadmap);
        container.appendChild(roadmapList);
    });
    adjustFillProgress();
}

// 로드맵 타입과 색상을 매핑한 객체
const typeColors = {
    "BACK-END": "rgba(80, 76, 134, 0.85)", // 예시: 투명도 0.85
    "FRONT-END": "rgba(245,105,105,0.85)", // 예시: 투명도 0.85
    "DATA": "rgba(129, 159, 247, 0.85)" // 예시: 투명도 0.85
};

function createRoadmapElement(element, type) {
    var elementDiv = document.createElement('div');
    elementDiv.classList.add('fill-progress');
    elementDiv.dataset.start = calculateDate(element.startDate);
    elementDiv.dataset.end = calculateDate(element.endDate);

    var elementTitle = document.createElement('div');
    elementTitle.classList.add('fill-progress-title');
    elementTitle.textContent = element.title;

    // 로드맵 타입에 따른 색상 설정
    const color = typeColors[type] || 'rgba(80, 76, 134, 0.85)'
    elementDiv.style.backgroundColor = color;

    var done = document.createElement('div');
    done.classList.add('fill-progress');
    done.style.backgroundColor = color;
    // Todo: 데이터의 갯수에 따른 성공 개수에 따라 다른 값이 오도록 해야함
    done.dataset.end = '50';
    done.style.zIndex = -1;

    elementDiv.appendChild(done);
    elementDiv.append(elementTitle);

    return elementDiv;
}

function createRoadmaps(roadmap) {
    var roadmapDiv = document.createElement('div');
    roadmapDiv.classList.add('row', 'category', 'p-0', 'out-b', 'pb-2', 'mb-1');

    var categoryTitle = document.createElement('div');
    categoryTitle.classList.add('category-title', 'rb');
    categoryTitle.textContent = roadmap.type;

    var customFillBox = document.createElement('div');
    customFillBox.classList.add('custom-fill-box', 'mt-2', 'mx-0', 'p-0', 'element');

    roadmapDiv.appendChild(categoryTitle);
    roadmapDiv.appendChild(customFillBox);

    // 데이터 표시를 위한 fill-progress 등의 요소 생성과 설정
    fetch(`/api/v1/roadmaps/${roadmap.id}/elements`, {
        method: 'GET'
    })
        .then((response) => response.json())
        .then(elements => {
            console.log("Fetch elements: ", elements)
            elements.forEach(function (element) {
                var roadmapElement = createRoadmapElement(element, roadmap.type);
                console.log('element: ', element)
                // customFillBox.appendChild(roadmapElement);

                // 검사하려는 날짜 구간
                var newStartDate = calculateDate(element.startDate);
                var newEndDate = calculateDate(element.endDate);
                var overlapping = false;

                // 이미 생성된 요소들과 비교하여 겹치는 부분 확인
                var existingFillProgresses = customFillBox.querySelectorAll('.fill-progress');
                for (const existingFillProgress of existingFillProgresses) {
                    var existingStartDate = parseFloat(existingFillProgress.dataset.start);
                    var existingEndDate = parseFloat(existingFillProgress.dataset.end);

                    if (
                        (newStartDate >= existingStartDate && newStartDate <= existingEndDate) ||
                        (newEndDate >= existingStartDate && newEndDate <= existingEndDate)
                    ) {
                        overlapping = true;
                        break;
                    }
                }

                if (overlapping) {
                    // 겹치는 부분이 있으면 다음 줄에 생성
                    customFillBox = document.createElement('div');
                    customFillBox.classList.add('custom-fill-box', 'mt-2', 'mx-0', 'p-0', 'element');
                    roadmapDiv.appendChild(customFillBox);
                }
                customFillBox.appendChild(roadmapElement);
            })
            adjustFillProgress(roadmap);
        })
        .catch(error => console.error('Error', error))

    return roadmapDiv;
}

/*
* 로드맵 찾기
* */
async function getRoadmap(year) {
    await fetch(`http://localhost:8080/api/v1/roadmaps?year=${year}`, {
        method: 'GET'
    })
        .then((response) => response.json())
        .then(roadmaps => {
            console.log("Fetched Roadmaps", roadmaps);
            displayRoadmaps(roadmaps);
        })
        .catch(error => console.error('Error: ', error))
}

// 오늘 날짜 라인 표시
function drawTodayLine() {
    const today = new Date();
    const todayLine = document.createElement('div');
    todayLine.classList.add('today-line');

    // 오늘의 위치 계산
    const daysPassed = calculateDate(today);
    const todayPosition = (document.querySelector('.days').offsetWidth / 100) * daysPassed;
    todayLine.style.left = todayPosition + 'px';

    // 오늘 날짜 라인의 높이를 조정하여 모든 카테고리 아래에 표시
    const categories = document.querySelectorAll('.custom-fill-box');
    const containerHeight = document.querySelector('.out-b').offsetHeight;
    todayLine.style.top = document.querySelector('.tl').offsetHeight + 'px';
    todayLine.style.height = containerHeight + 'px';

    document.querySelector('.days').appendChild(todayLine);
}

// 날짜 변환 로직
function calculateDate(date) {
    // 시작 날짜를 Date 객체로 변환합니다.
    var dateObj = new Date(date);

    // 해당 년도의 1월 1일을 나타내는 Date 객체를 생성합니다.
    var yearStart = new Date(dateObj.getFullYear(), 0, 0);

    // date와 1월 1일 사이의 시간 차이(ms)를 계산합니다.
    var timeDifference = dateObj - yearStart;

    // 시간 차이를 일(day)로 변환합니다.
    var daysSinceYearStart = Math.ceil(timeDifference / (1000 * 60 * 60 * 24));

    return (daysSinceYearStart / 365) * 100;
}