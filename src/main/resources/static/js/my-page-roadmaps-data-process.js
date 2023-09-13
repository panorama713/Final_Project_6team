window.onload = function() {
    fetchRoadmaps(0);
}

document.addEventListener('click', function (event) {
    if (event.target.classList.contains('update-roadmap')) {
        var roadmapId = event.target.dataset.roadmapId;
        var type = event.target.dataset.type;
        var title = event.target.dataset.title;
        var description = event.target.dataset.description;

        document.getElementById('update-roadmap-submit').dataset.roadmapId = roadmapId;
        document.getElementById('update-title').value = title;
        document.getElementById('update-type').value = type;
        document.getElementById('update-description').value = description;
    }

    if (event.target.classList.contains('delete-roadmap')) {
        myPageDeleteRoadmap();
    }
})

document.getElementById('update-roadmap-submit').addEventListener('click', myPageUpdateRoadmap)

// createdAt 출력 형식
function formatCreatedAt(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// roamdpas 표시
function displayMyPageRoadmaps(roadmaps) {
    const roadmapList = document.getElementById('roadmap-list');
    roadmapList.innerHTML = ''; // 이전 데이터 초기화

    roadmaps.forEach(function (roadmap) {
        var row = document.createElement('tr');
        var categoryElement = document.createElement('td');
        var titleElement = document.createElement('td');
        var createdAtElement = document.createElement('td');
        var updateElement = document.createElement('td')
        var deleteElement = document.createElement('td')

        categoryElement.textContent = roadmap.type;

        var titleLink = document.createElement('a');
        // titleLink.href = "/views/roadmap/"+ roadmap.id;
        titleLink.textContent = roadmap.title;
        titleLink.href = '/views/my-roadmap'
        titleElement.appendChild(titleLink);

        createdAtElement.textContent = formatCreatedAt(roadmap.createdAt);
        updateElement.classList.add('update-roadmap');
        updateElement.textContent = '수정';
        updateElement.dataset.bsToggle = 'modal';
        updateElement.dataset.bsTarget = '#updateRoadmap';

        // 데이터 저장
        updateElement.dataset.roadmapId = `${roadmap.id}`;
        updateElement.dataset.title = `${roadmap.title}`;
        updateElement.dataset.type = `${roadmap.type}`;
        updateElement.dataset.description = `${roadmap.description}`;

        deleteElement.classList.add('delete-roadmap')
        deleteElement.textContent = '삭제';
        deleteElement.dataset.roamdapId = `${roadmap.id}`;

        row.appendChild(categoryElement);
        row.appendChild(titleElement);
        row.appendChild(createdAtElement);
        row.appendChild(updateElement);
        row.appendChild(deleteElement);

        roadmapList.appendChild(row);

    });
}

// 작성글 페이지 번호 표시
function displayPageNumbers() {
    const paginationContainer = document.getElementById('pagination-container');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageNumberButton = document.createElement('button');
        pageNumberButton.textContent = i + 1;
        pageNumberButton.classList.add('btn');
        pageNumberButton.classList.add('custom-button');

        if (i === currentPage) {
            pageNumberButton.classList.add('active');
        }

        pageNumberButton.addEventListener('click', () => {
            fetchRoadmaps(i);
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}


let currentPage = 0;
let totalPages = 0;
async function fetchRoadmaps(page) {
    await fetch(`/api/v1/roadmaps/my-page?num=${page}`)
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;
            console.log(result.content);
            displayMyPageRoadmaps(result.content);
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}

// roadmap update
async function myPageUpdateRoadmap() {
    var roadmapId = parseInt(document.getElementById('update-roadmap-submit').dataset.roadmapId);
    var type = document.getElementById('update-type').value;
    var title = document.getElementById('update-title').value;
    var description = document.getElementById('update-description').value;

    var jsonData = {
        type: type,
        title: title,
        description: description
    }

    await fetch(`/api/v1/roadmaps/${roadmapId}`,{
        method: 'PUT',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
            alert('로드맵이 수정되었습니다.')
            location.reload();
        })
        .catch(error => console.log(error.message));
}

async function myPageDeleteRoadmap() {
    var roadmapId = document.querySelector('.delete-roadmap').dataset.roadmapId;
    var answer = confirm("정말로 삭제하시겠습니까?")
    if (answer) {
        await fetch(`/api/v1/roadmaps/${roadmapId}`, {
            method: 'DELETE'
        })
            .then(response => {
                alert("로드맵이 삭제되었습니다.")
                location.reload();
            })
            .catch(error => console.log(error.message))
    }
}
// roadmap delete
