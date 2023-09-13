document.addEventListener("DOMContentLoaded", function () {
    var likeButton = document.getElementById("like-btn");
    likeButton.addEventListener("click", function () {
        fetch("/api/v1/articles/" + articleId + "/like", {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 409) {
                        if (confirm('이미 좋아요를 누른 게시물입니다. 좋아요를 취소하시겠습니까?')) {
                            fetch("/api/v1/articles/" + articleId + "/like", {
                                method: "DELETE"
                            })
                                .then(data => {
                                    console.log("좋아요 취소 완료");
                                    window.location.reload();
                                })
                                .catch(error => {
                                    console.error("좋아요 취소 오류:", error);
                                });
                        }
                    }
                    if (response.status === 403) {
                        alert("자신의 글은 좋아요를 누를 수 없습니다.")
                    }
                } else {
                    alert("좋아요 누르셨습니다.")
                    window.location.reload();
                }
            })
    })
});

