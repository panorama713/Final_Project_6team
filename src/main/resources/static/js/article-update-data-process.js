
document.addEventListener("DOMContentLoaded", function() {
    fetchArticleInfo();

    var submitButton = document.getElementById("article-update");
    submitButton.addEventListener("click", updateArticle);
});

function fetchArticleInfo() {
    var articleId = window.location.pathname.split('/').pop();
    fetch("/api/v1/articles/" + articleId)
        .then(response => response.json())
        .then(data => {
            document.getElementById("title").value = data.title;
            document.getElementById("content").value = data.content;
            document.getElementById("type").value = data.type;
        })
        .catch(error => console.error("게시글 정보 가져오기 오류:", error));
}

function updateArticle() {
    var title = document.getElementById("title").value;
    var content = document.getElementById("content").value;
    var type = document.getElementById("type").value;
    var image = document.getElementById("image").files[0];

    var formData = new FormData();

    var jsonParams = {
        title: title,
        content: content,
        type: type
    };

    formData.append("params", new Blob([JSON.stringify(jsonParams)], { type: "application/json" }));
    formData.append("images", image);

    var articleId = window.location.pathname.split('/').pop();


    fetch("/api/v1/articles/" + articleId, {
        method: "PUT",
        body: formData
    })
        .then(data => {
            alert("게시글이 수정되었습니다.");
            window.location.href = "/views/articles/"+articleId;
        })
        .catch(error => console.error("게시글 수정 오류:", error));
}
