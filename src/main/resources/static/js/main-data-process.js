// 로드맵 총 개수 카운트
const roadmapCountElement = document.getElementById('roadmapCount');

fetch('/api/v1/roadmaps/count')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        roadmapCountElement.textContent = await response.json();
    })
    .catch((error) => {
        console.error('로드맵 데이터 가져오기 에러:', error);
        roadmapCountElement.textContent = '?';
    });

// 전체 이용자 수 카운트
const userCountElement = document.getElementById('userCount');

fetch('/api/v1/users/count')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        userCountElement.textContent = await response.json();
    })
    .catch((error) => {
        console.error('유저 데이터 가져오기 에러:', error);
        userCountElement.textContent = '?';
    });

// 오늘자 로드맵 개수 카운트
const todayRoadmapCountElement = document.getElementById('todayRoadmapCount');

fetch('/api/v1/roadmaps/count?date=today')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        todayRoadmapCountElement.textContent = await response.json();
    })
    .catch((error) => {
        console.error('로드맵 데이터 가져오기 에러:', error);
        todayRoadmapCountElement.textContent = '?';
    });

// 추천 로드맵 리스트
const recommendRoadmapList = document.getElementById('recommend-roadmap-list');

fetch('/api/v1/roadmaps/top5?keyword=recommend')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        const data = await response.json();
        console.log(data)

        if (data.length > 0) {
            data.forEach((roadmap, index) => {
                const roadmapMainDiv = document.createElement('div');
                roadmapMainDiv.classList.add('roadmap-main');

                const titleDiv = document.createElement('div');
                titleDiv.classList.add('roadmap-title');
                titleDiv.innerHTML = `<h5>${index + 1}.<a href="/views/roadmaps/${roadmap.roadmapId}"> ${roadmap.title}</a></h5>`;

                const writerDiv = document.createElement('div');
                writerDiv.classList.add('roadmap-writer');
                writerDiv.textContent = `작성자: ${roadmap.username}`;

                roadmapMainDiv.appendChild(titleDiv);
                roadmapMainDiv.appendChild(writerDiv);

                recommendRoadmapList.appendChild(roadmapMainDiv);
            })
        } else {
            const noDataDiv = document.createElement('div')
            noDataDiv.textContent = '없음'
            recommendRoadmapList.appendChild(noDataDiv);
        }
    })
    .catch((error) => {
        console.error('에러 발생', error)
        const errorDiv = document.createElement('div')
        errorDiv.textContent = '에러'
        recommendRoadmapList.appendChild(errorDiv);
    })

// 인기 로드맵 리스트
const popularRoadmapList = document.getElementById('popular-roadmap-list');

fetch('/api/v1/roadmaps/top5?keyword=popularity')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        const data = await response.json();
        console.log(data)

        if (data.length > 0) {
            data.forEach((roadmap, index) => {
                const roadmapMainDiv = document.createElement('div');
                roadmapMainDiv.classList.add('roadmap-main');

                const titleDiv = document.createElement('div');
                titleDiv.classList.add('roadmap-title');
                titleDiv.innerHTML = `<h5>${index + 1}.<a href="/views/roadmaps/${roadmap.roadmapId}"> ${roadmap.title}</a></h5>`;

                const writerDiv = document.createElement('div');
                writerDiv.classList.add('roadmap-writer');
                writerDiv.textContent = `작성자: ${roadmap.username}`;

                roadmapMainDiv.appendChild(titleDiv);
                roadmapMainDiv.appendChild(writerDiv);

                popularRoadmapList.appendChild(roadmapMainDiv);
            })
        } else {
            const noDataDiv = document.createElement('div')
            noDataDiv.textContent = '없음'
            popularRoadmapList.appendChild(noDataDiv);
        }
    })
    .catch((error) => {
        console.error('에러 발생', error)
        const errorDiv = document.createElement('div')
        errorDiv.textContent = '에러'
        popularRoadmapList.appendChild(errorDiv);
    })

// 신규 로드맵 리스트
const newRoadmapList = document.getElementById('new-roadmap-list');

fetch('/api/v1/roadmaps/top5')
    .then(async (response) => {
        if (!response.ok) {
            throw new Error('데이터 가져오기 실패');
        }
        const data = await response.json();
        console.log(data)

        if (data.length > 0) {
            data.forEach((roadmap, index) => {
                const roadmapMainDiv = document.createElement('div');
                roadmapMainDiv.classList.add('roadmap-main');

                const titleDiv = document.createElement('div');
                titleDiv.classList.add('roadmap-title');
                titleDiv.innerHTML = `<h5>${index + 1}.<a href="/views/roadmaps/${roadmap.roadmapId}"> ${roadmap.title}</a></h5>`;

                const writerDiv = document.createElement('div');
                writerDiv.classList.add('roadmap-writer');
                writerDiv.textContent = `작성자: ${roadmap.username}`;

                roadmapMainDiv.appendChild(titleDiv);
                roadmapMainDiv.appendChild(writerDiv);

                newRoadmapList.appendChild(roadmapMainDiv);
            })
        } else {
            const noDataDiv = document.createElement('div')
            noDataDiv.textContent = '없음'
            newRoadmapList.appendChild(noDataDiv);
        }
    })
    .catch((error) => {
        console.error('에러 발생', error)
        const errorDiv = document.createElement('div')
        errorDiv.textContent = '에러'
        newRoadmapList.appendChild(errorDiv);
    })

// 검색 -> 데이터 쿼리 파라미터로 넘겨서 검색 페이지에서 처리
document.querySelector('.search-button').addEventListener('click', function (event) {
    event.preventDefault();
    const searchInput = document.getElementById('lg-input-text').value;

    window.location.href = `/views/search?query=${encodeURIComponent(searchInput)}`
})