document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("category").value = localStorage.getItem('currentCategory');
    var submitButton = document.getElementById("article-write");
    submitButton.addEventListener("click", createArticle);
});

function createArticle() {
    var title = document.getElementById("title").value;
    var content = document.getElementById("content").value;
    var category = document.getElementById("category").value;
    var type = document.getElementById("type").value;
    var image = document.getElementById("image");


    // 이미지 파일이 선택 되었을 경우
    if (image.files.length != 0) {
        const imageData = new FormData();
        imageData.append("file", image.files[0]);

        fetch("/s3/resource", {
            method: "POST",
            body: imageData
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(error.message || "이미지 업로드 중 오류가 발생했습니다.");
                    });
                }
                return response.json();
            })
            .then(data => {
                var imagePath = data.path;
                var formData = new FormData();

                var jsonParams = {
                    title: title,
                    content: content,
                    category: category,
                    type: type,
                    imagePath: imagePath
                };

                formData.append("params", new Blob([JSON.stringify(jsonParams)], {type: "application/json"}));


                fetch("/api/v1/articles", {
                    method: "POST",
                    body: formData
                })
                    .then(response => {
                        if (!response.ok) {
                            return response.json().then(error => {
                                throw new Error(error.message || "게시글 작성 중 오류가 발생했습니다.");
                            });
                        }
                        return response.json();
                    })
                    .then(data => {
                        alert("게시글이 작성되었습니다.");
                        window.location.href = "articles/list";
                    })
                    .catch(error => {
                        console.error("게시글 작성 오류:", error);
                        alert(error.message);
                    });
            })
            .catch(error => {
                console.error("게시글 작성 오류:", error);
                alert(error.message);
            });
    }

    else {
        var imagePath = null;
        var formData = new FormData();

        var jsonParams = {
            title: title,
            content: content,
            category: category,
            type: type,
            imagePath: imagePath
        };

        formData.append("params", new Blob([JSON.stringify(jsonParams)], {type: "application/json"}));


        fetch("/api/v1/articles", {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(error.message || "게시글 작성 중 오류가 발생했습니다.");
                    });
                }
                return response.json();
            })
            .then(data => {
                alert("게시글이 작성되었습니다.");
                window.location.href = "articles/list";
            })
            .catch(error => {
                console.error("게시글 작성 오류:", error);
                alert(error.message);
            });
    }
}