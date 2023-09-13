$(document).ready(async function () {
    // 선택된 드롭다운의 값을 가져옵니다.
    var year = document.querySelector(".select-year").value;
    await getRoadmap(year);
    await roadmapPaddingTop();
    await adjustDoneProgress();
    // await drawTodayLine();
});

window.addEventListener('resize', adjustFillProgress);
window.addEventListener('resize', roadmapPaddingTop);

document.querySelector("#create-roadmap-submit").addEventListener('click', createNewRoadmap)

// 로드맵 위치 조정
function roadmapPaddingTop() {
    document.querySelector('.container-md')
        .style.paddingTop = document.querySelector('.container-xxl').offsetHeight + 20 + 'px';
}

// 선택된 dropdown-item에 따라 dropdown-toggle 업데이트
document.querySelectorAll('.dropdown-item').forEach(function (item) {
    item.addEventListener('click', function () {
        const selectedValue = item.getAttribute('value');
        const selectedText = item.textContent;
        const dropdownButton = document.getElementById('dropdown-btn');

        // dropdown-toggle의 값을 업데이트
        dropdownButton.value = selectedValue;
        dropdownButton.textContent = selectedText;

        // 값이 변경되고 조건에 맞는 로드맵 불러오기
        let year = selectedValue;
        // HTML 내용 초기화
        const container = document.querySelector('.roadmap-container');
        container.innerHTML = '';
        getRoadmap(year).then(response => {
            console.log('조건에 맞는 로드맵을 불러왔습니다.', response)
        })
    });
});

/*
* 타임라인 차트 표시
* */
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

function adjustDoneProgress() {
    const doneProgresses = document.querySelectorAll('.done-progress');

    for (const doneProgress of doneProgresses) {
        const start = parseInt(doneProgress.dataset.start);
        const end = parseInt(doneProgress.dataset.end);

        const parentWidth = doneProgress.parentElement.offsetWidth;
        const doneWidth = (parentWidth * end / 100) - (parentWidth * start / 100);

        doneProgress.style.width = doneWidth + 'px';
        doneProgress.style.left = start + "%";
    }
}

function displayRoadmaps(roadmaps) {
    var container = document.querySelector('.roadmap-container');

    roadmaps.forEach(function (roadmap) {
        var roadmapList = createRoadmaps(roadmap);
        container.appendChild(roadmapList);
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

// element_todo 달성비율 확인 기능
async function roadmapElementTodoDoneProgress(roadmapId, element) {
    await fetch(`/api/v1/roadmaps/${roadmapId}/elements/${element.id}/todo/done-progress`, {
        method: 'GET'
    })
        .then(response => {
            if (response.ok) {
                // console.log(response)
                return response.json()
            } else {
                throw new Error('서버로부터 데이터를 가져오는 중 오류 발생');
            }
        })
        .then(data => {
            console.log('progress data:', data);
            document.getElementById(`done-progress-${element.id}`).dataset.end = data.toString();
        })
        .catch(error => {
            console.log('done progress 불러오기 오류', error.message);
        })
}

function createRoadmapElements(roadmapId, element, type) {
    var elementDiv = document.createElement('div');
    elementDiv.classList.add('fill-progress');
    elementDiv.dataset.start = calculateDate(element.startDate);
    elementDiv.dataset.end = calculateDate(element.endDate);

    var elementTitle = document.createElement('div');
    elementTitle.classList.add('fill-progress-title', 'element-todo-list');
    elementTitle.dataset.bsToggle = 'modal';
    elementTitle.dataset.bsTarget = '#roadmapElementTodoModal'
    elementTitle.textContent = element.title;
    elementTitle.dataset.elementTitle = element.title;
    elementTitle.dataset.roadmapId = roadmapId;
    elementTitle.dataset.elementId = element.id;
    elementTitle.style.fontWeight = 'normal';

    // 로드맵 타입에 따른 색상 설정
    const color = typeColors[type] || 'rgba(80, 76, 134, 0.85)'
    elementDiv.style.backgroundColor = color;

    var done = document.createElement('div');
    done.classList.add('fill-progress', 'done-progress');
    done.id = `done-progress-${element.id}`;
    done.dataset.start = '0';
    done.style.backgroundColor = color;
    done.style.zIndex = '-1';

    elementDiv.appendChild(done);
    elementDiv.append(elementTitle);

    return elementDiv;
}

function createRoadmaps(roadmap) {
    var roadmapDiv = document.createElement('div');
    roadmapDiv.classList.add('row', 'category', 'p-0', 'out-b', 'pb-2', 'mb-1');

    var categoryTitle = document.createElement('div');
    categoryTitle.classList.add('category-title', 'rb');
    categoryTitle.textContent = roadmap.type + " : " + roadmap.title;
    categoryTitle.style.position = 'relative';


    var categoryToolButton = document.createElement('div');
    categoryToolButton.classList.add('category-tool');
    categoryToolButton.style.position = 'absolute';
    categoryToolButton.style.textAlign = 'right';

    var categoryAddElement = document.createElement('span');
    categoryAddElement.classList.add('add-element')
    categoryAddElement.dataset.roadmapId = roadmap.id;
    categoryAddElement.dataset.roadmapType = roadmap.type;
    categoryAddElement.dataset.bsToggle = 'modal';
    categoryAddElement.dataset.bsTarget = '#createRoadmapElementModal';
    categoryAddElement.textContent = '일정추가';

    var line1 = document.createElement('span');
    line1.textContent = ' | ';
    var line2 = document.createElement('span');
    line2.textContent = ' | ';

    var categoryUpdate = document.createElement('span');
    categoryUpdate.classList.add('roadmap-update');
    categoryUpdate.dataset.bsToggle = 'modal';
    categoryUpdate.dataset.bsTarget = '#updateRoadmap';
    categoryUpdate.dataset.roadmapId = roadmap.id;
    categoryUpdate.dataset.roadmapTitle = roadmap.title;
    categoryUpdate.dataset.roadmapType = roadmap.type;
    categoryUpdate.dataset.roadmapDescription = roadmap.description;
    categoryUpdate.textContent = '수정';

    var categoryDelete = document.createElement('span');
    categoryDelete.classList.add('roadmap-delete')
    categoryDelete.dataset.roadmapId = roadmap.id;
    categoryDelete.dataset.roadmapTitle = roadmap.title;
    categoryDelete.textContent = '삭제';

    categoryToolButton.appendChild(categoryAddElement);
    categoryToolButton.appendChild(line1);
    categoryToolButton.appendChild(categoryUpdate);
    categoryToolButton.appendChild(line2);
    categoryToolButton.appendChild(categoryDelete);

    var customFillBox = document.createElement('div');
    customFillBox.classList.add('custom-fill-box', 'mt-2', 'mx-0', 'p-0', 'element');

    roadmapDiv.appendChild(categoryTitle);
    roadmapDiv.appendChild(categoryToolButton);
    roadmapDiv.appendChild(customFillBox);

    // 데이터 표시를 위한 fill-progress 등의 요소 생성과 설정
    fetch(`/api/v1/roadmaps/${roadmap.id}/elements`, {
        method: 'GET'
    })
        .then((response) => response.json())
        .then(elements => {
            // console.log("Fetch elements: ", elements)
            if (elements.length === 0) {
                customFillBox.textContent = '일정을 추가해 주세요'
            } else {
                elements.forEach(function (element) {
                    var roadmapElement = createRoadmapElements(roadmap.id, element, roadmap.type);
                    roadmapElementTodoDoneProgress(roadmap.id, element).then(response => {
                            adjustDoneProgress();
                        }
                        // console.log('test:',document.getElementById(`done-progress-${element.id}`).dataset.end)
                    );

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
            }
            adjustFillProgress();
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
            // console.log("Fetched Roadmaps", roadmaps);
            displayRoadmaps(roadmaps);
        })
        .catch(error => {
            console.error('Error: ', error);
        })
}

// 로드맵 생성 로직
async function createNewRoadmap() {
    var title = document.getElementById('title').value;
    var type = document.getElementById('type').value;
    var description = document.getElementById('description').value;

    var jsonData = {
        title: title,
        type: type,
        description: description
    }

    await fetch(`/api/v1/roadmaps`, {
        method: 'POST',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.message || "로드맵 생성중 오류가 발생하였습니다.");
                });
            }
            return response.json();
        })
        .then(data => {
            alert("로드맵이 생성되었습니다");
            window.location.href = "my-roadmap";
        })
        .catch(error => {
            console.error("로드앱 생성 오류:", error);
            alert(error.message);
        });
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


// 로드맵 수정 기능
document.addEventListener('click', function (event) {
    if (event.target.classList.contains('roadmap-update')) {
        var roadmapTitle = event.target.dataset.roadmapTitle;
        var roadmapType = event.target.dataset.roadmapType;
        var roadmapDescription = event.target.dataset.roadmapDescription;

        var updateTypeSelect = document.getElementById('update-type');

        updateTypeSelect.value = roadmapType;
        document.getElementById('update-title').placeholder = roadmapTitle;
        document.getElementById('update-description').placeholder = roadmapDescription;
        document.getElementById('update-roadmap-submit').dataset.roadmapId = event.target.dataset.roadmapId;
    }
})

document.getElementById('update-roadmap-submit').addEventListener('click', updateRoadmap);

async function updateRoadmap(event) {
    var roadmapId = document.getElementById('update-roadmap-submit').dataset.roadmapId;
    var roadmapTitle = document.getElementById('update-title').value;
    var roadmapType = document.getElementById('update-type').value;
    var roadmapDescription = document.getElementById('update-description').value;

    var jsonData = {
        title: roadmapTitle,
        type: roadmapType,
        description: roadmapDescription
    }
    await fetch(`/api/v1/roadmaps/${roadmapId}`, {
        method: 'PUT',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.message || "로드맵 수정중 오류가 발생하였습니다.");
                });
            }
            return response.json();
        })
        .then(data => {
            alert("로드맵이 수정되었습니다");
            location.reload();
        })
        .catch(error => {
            console.error("로드앱 수정 오류:", error);
            alert(error.message);
        });
}


// 로드맵 삭제 기능
document.addEventListener('click', function (event) {
    if (event.target.classList.contains('roadmap-delete')) {
        deleteRoadmap(event);
    }
})

async function deleteRoadmap(event) {
    // 이벤트 객체에서 roadmap 가져오기
    var roadmapTitle = event.target.dataset.roadmapTitle;
    var roadmapId = event.target.dataset.roadmapId;

    var answer = confirm('정말 ' + `${roadmapTitle}` + '(을)를 삭제하시겠습니까?')

    if (answer === true) {
        await fetch(`/api/v1/roadmaps/${roadmapId}`, {
            method: 'DELETE'
        })
            .then(response => {
                alert('로드맵이 삭제되었습니다.');
                location.reload();
            })
            .catch(error => {
                console.log(error.message);
            })
    }
}