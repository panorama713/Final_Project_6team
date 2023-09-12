window.addEventListener('resize', adjustFillProgress);
window.addEventListener('resize', roadmapPaddingTop);
window.addEventListener('DOMContentLoaded', function () {
    const roadmapId = getRoadmapIdFromUrl();
    fetchUserRoadmaps(roadmapId);
});

// 로드맵 위치 조정
function roadmapPaddingTop() {
    document.querySelector('.container-md')
        .style.paddingTop = document.querySelector('.container-xxl').offsetHeight + 20 + 'px';
}


function getRoadmapIdFromUrl() {
    const regex = /\/roadmaps\/(\d+)/;
    const match = window.location.pathname.match(regex);
    return match[1];
}

function fetchUserRoadmaps(roadmapId) {
    fetch(`/api/v1/roadmaps/${roadmapId}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // 데이터 콘솔 출력
            displayRoadmaps(data);
        })
        .catch(error => console.error('Error:', error));
}


function displayRoadmaps(roadmap) {
    document.querySelector('#years').textContent = formatDate(roadmap.createdAt);
    const container = document.querySelector('.roadmap-container');
    const roadmapList = createRoadmaps(roadmap);
    container.appendChild(roadmapList);
    adjustFillProgress();
}

function formatDate(createdAt) {
    const createdAtDate = new Date(createdAt);
    const formattedDate = createdAtDate.toLocaleDateString('ko-KR', {year: 'numeric'});
    return formattedDate;
}

function createRoadmaps(roadmap) {
    const roadmapDiv = document.createElement('div');
    roadmapDiv.classList.add('roadmap-div', 'row', 'category', 'p-0', 'out-b', 'pb-2', 'mb-1');

    const typeTitle = document.createElement('div');
    typeTitle.classList.add('category-title', 'rb');
    typeTitle.textContent = roadmap.type + " : " + roadmap.title;
    typeTitle.style.position = 'relative';

    const btnDiv = document.createElement('div');
    btnDiv.classList.add('btn-tool');
    btnDiv.style.position = 'absolute';
    btnDiv.style.textAlign = 'right';

    const bookmarkBtn = document.createElement('button');
    bookmarkBtn.classList.add('bookmark-btn', 'btn', 'btn-primary');
    bookmarkBtn.textContent = '북마크';
    bookmarkBtn.addEventListener('click', async function () {
        if (await existBookmark(roadmap)) {
            const bookmarkId = await getBookmarkId(roadmap);
            await cancelRoadmapBookmark(bookmarkId);
        } else {
            await addRoadmapBookmark(roadmap);
        }
    });

    const userProfileBtn = document.createElement('button');
    userProfileBtn.classList.add('user-profile-btn', 'btn', 'btn-primary');
    userProfileBtn.textContent = '프로필';

    btnDiv.appendChild(bookmarkBtn);
    btnDiv.appendChild(userProfileBtn);

    const customFillBox = document.createElement('div');
    customFillBox.classList.add('custom-fill-box', 'mt-2', 'mx-0', 'p-0', 'element');

    roadmapDiv.appendChild(typeTitle);
    roadmapDiv.appendChild(btnDiv);
    roadmapDiv.appendChild(customFillBox);

    fetchRoadmapElements(roadmap);

    return roadmapDiv;
}

function fetchRoadmapElements(roadmap) {
    // 데이터 표시를 위한 fill-progress 등의 요소 생성과 설정
    fetch(`/api/v1/roadmaps/${roadmap.id}/elements`, {
        method: 'GET'
    })
        .then((response) => response.json())
        .then(elements => {
            console.log(elements);
            displayRoadmapElements(roadmap, elements);
            adjustFillProgress();
        })
        .catch(error => console.error('Error', error));
}

function displayRoadmapElements(roadmap, elements) {
    elements.forEach(function (element) {
        const roadmapElement = createRoadmapElements(roadmap.id, element, roadmap.type);

        // 검사하려는 날짜 구간
        const newStartDate = calculateDate(element.startDate);
        const newEndDate = calculateDate(element.endDate);
        let overlapping = false;
        let customFillBox = document.querySelector('.custom-fill-box');
        let roadmapDiv = document.querySelector('.roadmap-div');

        // 이미 생성된 요소들과 비교하여 겹치는 부분 확인
        const existingFillProgresses = customFillBox.querySelectorAll('.fill-progress');
        for (const existingFillProgress of existingFillProgresses) {
            const existingStartDate = parseFloat(existingFillProgress.dataset.start);
            const existingEndDate = parseFloat(existingFillProgress.dataset.end);

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
    });
}

// 로드맵 타입과 색상을 매핑한 객체
const typeColors = {
    "BACKEND": "rgba(80, 76, 134, 0.85)",
    "FRONTEND": "rgba(245,105,105,0.85)",
    "DATA": "rgba(129, 159, 247, 0.85)",
    "MOBILE": "rgba(190,129,247,0.85)",
    "GAME": "rgba(61,243,152,0.85)"
};

function createRoadmapElements(roadmapId, element, type) {
    const elementDiv = document.createElement('div');
    elementDiv.classList.add('fill-progress');
    elementDiv.dataset.start = calculateDate(element.startDate);
    elementDiv.dataset.end = calculateDate(element.endDate);

    const elementTitle = document.createElement('div');
    elementTitle.classList.add('fill-progress-title', 'element-todo-list');
    elementTitle.dataset.bsToggle = 'modal';
    elementTitle.dataset.bsTarget = '#roadmapElementTodoModal';
    elementTitle.textContent = element.title;
    elementTitle.dataset.elementTitle = element.title;
    elementTitle.dataset.roadmapId = roadmapId;
    elementTitle.dataset.elementId = element.id;
    elementTitle.style.fontWeight = 'normal';

    // 로드맵 타입에 따른 색상 설정
    const color = typeColors[type] || 'rgba(80, 76, 134, 0.85)'
    elementDiv.style.backgroundColor = color;

    const done = document.createElement('div');
    done.classList.add('fill-progress', 'done-progress');
    done.style.backgroundColor = color;
    // Todo: 데이터의 갯수에 따른 성공 개수에 따라 다른 값이 오도록 해야함
    done.dataset.end = '75';
    done.style.zIndex = -1;

    elementDiv.appendChild(done);
    elementDiv.append(elementTitle);

    return elementDiv;
}

// 날짜 변환 로직
function calculateDate(date) {
    // 시작 날짜를 Date 객체로 변환합니다.
    const dateObj = new Date(date);
    // 해당 년도의 1월 1일을 나타내는 Date 객체를 생성합니다.
    const yearStart = new Date(dateObj.getFullYear(), 0, 0);
    // date와 1월 1일 사이의 시간 차이(ms)를 계산합니다.
    const timeDifference = dateObj - yearStart;
    // 시간 차이를 일(day)로 변환합니다.
    const daysSinceYearStart = Math.ceil(timeDifference / (1000 * 60 * 60 * 24));
    return (daysSinceYearStart / 365) * 100;
}

function adjustFillProgress() {
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