document.addEventListener("DOMContentLoaded", function() {
    var userFollowBtn = document.getElementById("user-follow");

    userFollowBtn.addEventListener("click", function() {
        event.preventDefault();
        var usernameToFollow = document.querySelector('#article-username').textContent;
        fetch("/api/v1/users/follow?usernameToFollow="+encodeURIComponent(usernameToFollow), {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 409) {
                        if (confirm('이미 팔로우 중인 유저입니다. 팔로우를 취소하시겠습니까?')) {
                            fetch("/api/v1/users/follow?usernameToFollow="+encodeURIComponent(usernameToFollow), {
                                method: "DELETE"
                            })
                                .then(data => {
                                    alert("팔로우가 취소되었습니다.");
                                })
                                .catch(error => {
                                    console.error("팔로우 취소 오류:", error);
                                });
                        }
                    }
                }
                else {
                    alert("팔로우가 완료되었습니다.")
                }
            })
    })
});
