document.addEventListener("DOMContentLoaded", function() {
    var submitButton = document.getElementById("article-search-button");
    submitButton.addEventListener("click", function() {
        var articleList = document.getElementById('article-list');
        articleList.innerHTML = '';

        searchPosts();
    });
});


function searchPosts() {
    var keyword = document.getElementById("keyword").value;
    var savedCategory = localStorage.getItem('currentCategory')
    var url = '/api/v1/articles/search?keyword=' + encodeURIComponent(keyword)+'&category='+savedCategory;
    fetch(url)
        .then(response => response.json())
        .then(articles => {
            displayArticles(articles);
        })
        .catch(error => console.error('Error:', error));
}
