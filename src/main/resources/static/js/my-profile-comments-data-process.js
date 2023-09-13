window.onload = function () {
    fetchComments(0);
}

let currentPage = 0;
let totalPages = 0;

function fetchComments(page) {
    fetch("/api/v1/articles/{articleId}/comments/getComments?page=" + page)
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;
            displayComments(result.content);
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

// Comments 표시
function displayComments(comments) {
    const commentList = document.getElementById('comment-list');
    commentList.innerHTML = ''; // 이전 데이터 초기화

    comments.forEach(function (comment) {
        var row = document.createElement('tr');
        var commentElement = document.createElement('td');
        var createdAtElement = document.createElement('td');

        var CommentDiv = document.createElement('div');
        var articleTitleDiv = document.createElement('div');

        var titleLink = document.createElement('a');
        titleLink.href = "/views/articles/" + comment.articleId;
        titleLink.textContent = comment.articleTitle;
        articleTitleDiv.appendChild(titleLink);

        CommentDiv.textContent = comment.content;
        createdAtElement.textContent = formatCreatedAt(comment.createdAt);

        commentElement.appendChild(CommentDiv);
        commentElement.appendChild(articleTitleDiv);
        row.appendChild(commentElement);
        row.appendChild(createdAtElement);

        commentList.appendChild(row);

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
            fetchComments(i);
        });
        paginationContainer.appendChild(pageNumberButton);
    }
}