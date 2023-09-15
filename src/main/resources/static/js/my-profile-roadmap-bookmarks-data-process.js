window.onload = function () {
    fetchRoadmapBookmarks(0);
}

let currentPage = 0;
let totalPages = 0;

function fetchRoadmapBookmarks(page) {

    fetch("/api/v1/bookmarks/roadmaps?page=" + page, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            totalPages = data.totalPages;
            currentPage = data.number;
            displayBookmarks(data.content);
            displayRoadmapBookmarkPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}


// bookmarks 표시
function displayBookmarks(bookmarks) {
    const bookmarkList = document.getElementById('roadmap-bookmark-list');
    bookmarkList.innerHTML = ''; // 이전 데이터 초기화

    bookmarks.forEach(function (bookmark) {
        const row = document.createElement('tr');
        const roadmapTypeElement = document.createElement('td');
        const bookmarkTitleElement = document.createElement('td');
        const roadmapTitleElement = document.createElement('td');
        const roadmapWriterElement = document.createElement('td')
        const bookmarkCreatedAtElement = document.createElement('td');

        const titleLink = document.createElement('a');
        titleLink.href = "/views/roadmaps/" + bookmark.roadmapId;
        titleLink.textContent = bookmark.titleOfRoadmap;
        roadmapTitleElement.appendChild(titleLink);

        const typeMappings = {
            "BACKEND": 'BE',
            "FRONTEND": 'FE',
            "DATA": 'DB',
            "MOBILE": 'MB',
            "GAME": 'GM'
        };
        roadmapTypeElement.textContent = typeMappings[bookmark.typeOfRoadmap] || '';
        bookmarkTitleElement.textContent = bookmark.titleOfBookmark;
        roadmapWriterElement.textContent = bookmark.username;
        bookmarkCreatedAtElement.textContent = formatCreatedAt(bookmark.createdAt);

        row.appendChild(roadmapTypeElement);
        row.appendChild(bookmarkTitleElement);
        row.appendChild(roadmapTitleElement);
        row.appendChild(roadmapWriterElement);
        row.append(bookmarkCreatedAtElement);

        bookmarkList.appendChild(row);
    });
}

function formatCreatedAt(createdAt) {
    const date = new Date(createdAt);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 작성글 페이지 번호 표시
function displayRoadmapBookmarkPageNumbers() {
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
            fetchRoadmapBookmarks(i);
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}