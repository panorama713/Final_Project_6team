

// URL에서 마지막 숫자 추출
var articleId = parseInt(window.location.pathname.split('/').pop());

// 서버에서 게시물 데이터 가져오기
fetch('/api/v1/articles/' + articleId)
    .then(response => response.json())
    .then(article => {
        console.log("Fetched Article:", article);
        displayArticle(article);
    })
    .catch(error => console.error('Error:', error));

function displayArticle(article) {
    var titleElement = document.getElementById('title');
    var usernameElement = document.getElementById('username');
    var typeElement = document.getElementById('type');
    var contentElement = document.getElementById('content');

    titleElement.textContent = article.title;
    usernameElement.textContent = article.username;
    typeElement.textContent = article.type;
    contentElement.innerHTML = article.content;
}
