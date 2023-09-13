document.addEventListener("DOMContentLoaded", function () {
    var submitButton = document.getElementById("article-search-button");
    submitButton.addEventListener("click", function () {
        var articleList = document.getElementById('article-list');
        articleList.innerHTML = '';

        searchPosts();
    });
});

let currentKeyword = '';

function searchPosts(page = 0) {
    var keyword = document.getElementById("keyword").value;
    currentKeyword = keyword;
    var savedCategory = localStorage.getItem('currentCategory')
    var url = '/api/v1/articles/search?keyword=' + encodeURIComponent(keyword) + '&category=' + savedCategory + '&page=' + page;
    fetch(url)
        .then(response => response.json())
        .then(result => {
            displayArticles(result.content);
            totalPages = result.totalPages;
            currentPage = result.number;
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}
