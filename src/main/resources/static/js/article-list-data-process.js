function fetchPosts() {
    fetch('/api/v1/articles')
        .then(response => response.json())
        .then(articles => {
            displayArticles(articles);
        })
        .catch(error => console.error('Error:', error));
}

function displayArticles(articles) {
    var articleList = document.getElementById('post-list');
    articles.forEach(function (article) {
        var row = document.createElement('tr');
        var titleElement = document.createElement('td');
        var usernameElement = document.createElement('td');
        var typeElement = document.createElement('td');


        var titleLink = document.createElement('a');
        titleLink.href = article.id;
        titleLink.textContent = article.title;
        titleElement.appendChild(titleLink);

        usernameElement.textContent = article.username;
        typeElement.textContent = article.type;

        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(usernameElement);

        articleList.appendChild(row);
    });
}

fetchPosts();