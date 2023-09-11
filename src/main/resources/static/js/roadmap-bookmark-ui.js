window.addEventListener('DOMContentLoaded', fetchMyRoadmapBookmarks);

// 나의 로드맵 북마크 목록 정보 가져오기
function fetchMyRoadmapBookmarks() {
    fetch(`/api/v1/bookmarks/roadmaps`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log('로드맵 북마크 상세 정보:', data.content);
            displayMyRoadmapBookmarks(data.content);
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}

// 나의 로드맵 북마크 목록 표시
function displayMyRoadmapBookmarks(bookmarks) {
    // 북마크를 표시할 컨테이너 요소
    const bookmarkList = document.querySelector('#bookmark-list');
    // 이전 데이터 초기화
    bookmarkList.innerHTML = '';

    if (bookmarks.length === 0) {
        const noBookmarkMessage = document.createElement('p');
        noBookmarkMessage.textContent = "북마크한 로드맵이 없습니다.";
        bookmarkList.appendChild(noBookmarkMessage);
    } else {
        bookmarks.forEach(function (bookmark) {
            const bookmarkItem = document.createElement('a'); // 각 북마크를 담을 링크 요소 생성
            bookmarkItem.classList.add('list-group-item', 'list-group-item-action'); // 클래스 추가

            const div1 = document.createElement('div'); // 첫 번째 내부 컨테이너
            div1.classList.add('d-flex', 'w-100', 'justify-content-between');

            const title = document.createElement('h5'); // 제목
            title.classList.add('mb-1', 'bookmark-title');

            const date = document.createElement('small'); // 날짜
            date.classList.add('text-muted', 'bookmark-date');

            const div2 = document.createElement('div'); // 두 번째 내부 컨테이너
            div2.classList.add('d-flex', 'w-100', 'justify-content-between');

            const type = document.createElement('p'); // 타입
            type.classList.add('mb-1', 'badge', 'rounded-pill', 'type');

            const username = document.createElement('small'); // 사용자 이름
            username.classList.add('text-muted', 'username');

            bookmarkItem.href = "/views/roadmaps/" + bookmark.roadmapId;
            title.textContent = bookmark.titleOfBookmark;
            const displayDate = formatDate(bookmark.createdAt);
            date.textContent = displayDate;

            div1.appendChild(title);
            div1.appendChild(date);

            const typeMappings = {
                "BACKEND": 'BE',
                "FRONTEND": 'FE',
                "DATA": 'DB',
                "CS": 'CS',
            };
            type.textContent = typeMappings[bookmark.typeOfRoadmap] || '';
            username.textContent = bookmark.username;

            div2.appendChild(type);
            div2.appendChild(username);

            bookmarkItem.appendChild(div1);
            bookmarkItem.appendChild(div2);

            bookmarkList.appendChild(bookmarkItem);
        });
    }
}

function formatDate(createdAt) {
    const createdAtDate = new Date(createdAt);
    const formattedDate = createdAtDate.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });

    return `${formattedDate}`;
}