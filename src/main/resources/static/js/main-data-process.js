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
