
document.addEventListener("DOMContentLoaded", function() {
    var likeButton = document.getElementById("like-btn");
    likeButton.addEventListener("click", function() {
        fetch("/api/v1/articles/" + articleId + "/like", {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) {
                    console.log(1)
                    if (response.status === 409) {
                        console.log(2)
                        if (confirm('이미 좋아요를 누른 게시물입니다. 좋아요를 취소하시겠습니까?')) {
                            console.log(3)
                            fetch("/api/v1/articles/" + articleId + "/like", {
                                method: "DELETE"
                            })
                                .then(data => {
                                    console.error("좋아요 취소 완료");
                                })
                                .catch(error => {
                                    console.error("좋아요 취소 오류:", error);
                                });
                        }
                    }
                }
                else {
                    alert("좋아요를 누르셨습니다.")
                }
            })
    })
});

