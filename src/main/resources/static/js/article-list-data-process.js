function fetchPosts() {
    fetch('/api/v1/articles')
        .then(response => response.json())
        .then(articles => {
            displayArticles(articles);
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

function displayArticles(articles) {
    var articleList = document.getElementById('post-list');
    articles.forEach(function (article) {
        console.log(articles)
        var row = document.createElement('tr');
        var titleElement = document.createElement('td');
        var usernameElement = document.createElement('td');
        var typeElement = document.createElement('td');
        var createdAtElement = document.createElement('td');
        var viewCountElement = document.createElement('td');


        var titleLink = document.createElement('a');
        titleLink.href = article.id;
        titleLink.textContent = article.title;
        titleElement.appendChild(titleLink);

        usernameElement.textContent = article.username;
        typeElement.textContent = article.type;
        createdAtElement.textContent = formatCreatedAt(article.createdAt);
        viewCountElement.textContent = article.viewCount;

        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(usernameElement);
        row.appendChild(createdAtElement);
        row.appendChild(viewCountElement);

        articleList.appendChild(row);
    });
}

fetchPosts();