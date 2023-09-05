    document.addEventListener("DOMContentLoaded", function() {
        var submitButton = document.getElementById("article-write");
        submitButton.addEventListener("click", createArticle);
    });

    function createArticle() {
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

        fetch("/api/v1/articles", {
            method: "POST",
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                alert("게시글이 작성되었습니다.");
                window.location.href = "articles/list";
            })
            .catch(error => console.error("게시글 작성 오류:", error));
    }





