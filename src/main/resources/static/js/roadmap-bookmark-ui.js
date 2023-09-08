window.addEventListener('DOMContentLoaded', fetchMyRoadmapBookmarks);

// 나의 로드맵 북마크 목록 정보 가져오기
function fetchMyRoadmapBookmarks() {
    fetch(`/api/v1/bookmarks/roadmaps`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log('로드맵 북마크 상세 정보:', data);
            displayMyRoadmapBookmarks(data.content);
        })
        .catch(error => console.error('Error:', error));
}

// 나의 로드맵 북마크 목록 표시
function displayMyRoadmapBookmarks(bookmarks) {
    bookmarks.forEach(function (bookmark) {
        document.querySelector('#roadmap-link').href = "/views/roadmaps/" + bookmark.roadmapId;
        document.querySelector('#roadmap-bookmark-title').textContent = bookmark.titleOfBookmark;
        document.querySelector('#roadmap-username').textContent = bookmark.username;

        const displayDate = formatDate(bookmark.createdAt);
        document.querySelector('#roadmap-bookmark-date').textContent = displayDate;

        const typeMappings = {
            "BACKEND": 'BE',
            "FRONTEND": 'FE',
            "DATA": 'DB',
            "CS": 'CS',
        };

        let typeText = typeMappings[bookmark.typeOfRoadmap];
        document.querySelector('#roadmap-type').textContent = typeText;
    });
}

function formatDate(createdAt) {
    const createdAtDate = new Date(createdAt);
    const formattedDate = createdAtDate.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });

    return `${formattedDate}`;
}