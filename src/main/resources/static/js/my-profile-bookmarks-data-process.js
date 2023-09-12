window.onload = function() {
    fetchBookmarks(0);
}

let currentPage = 0;
let totalPages = 0;
function fetchBookmarks(page) {

    fetch("/api/v1/bookmarks/articles")
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;
            displayBookmarks(result.content);
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}

// createdAt 출력 형식
function formatCreatedAt(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// articles 표시
function displayBookmarks(bookmarks) {
    const bookmarkList = document.getElementById('bookmark-list');
    bookmarkList.innerHTML = ''; // 이전 데이터 초기화

    bookmarks.forEach(function (bookmark) {
        var row = document.createElement('tr');
        var bookmarkTitleElement = document.createElement('td');
        var articleTitleElement = document.createElement('td');
        var articleWriterElement = document.createElement('td')

        var titleLink = document.createElement('a');
        titleLink.href = "/views/articles/"+ bookmark.articleId;
        titleLink.textContent = bookmark.titleOfArticle;
        articleTitleElement.appendChild(titleLink);


        bookmarkTitleElement.textContent = bookmark.titleOfBookmark;
        articleWriterElement.textContent = bookmark.username;

        row.appendChild(bookmarkTitleElement);
        row.appendChild(articleTitleElement);
        row.appendChild(articleWriterElement);

        bookmarkList.appendChild(row);

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
            fetchArticles(i, username);
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}