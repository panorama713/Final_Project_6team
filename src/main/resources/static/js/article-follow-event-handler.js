document.addEventListener("DOMContentLoaded", function() {
    var userFollowBtn = document.getElementById("follow-btn");

    userFollowBtn.addEventListener("click", function() {
        event.preventDefault();
        var usernameToFollow = localStorage.getItem('currentWriter');
        var userId = userFollowBtn.getAttribute("user-id-value")
        fetch(`/api/v1/users/${userId}/follow`, {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 409) {
                        if (confirm('이미 팔로우 중인 유저입니다. 팔로우를 취소하시겠습니까?')) {
                            fetch(`/api/v1/users/${userId}/follow`, {
                                method: "DELETE"
                            })
                                .then(data => {
                                    alert("팔로우가 취소되었습니다.");
                                    window.location.reload();
                                })
                                .catch(error => {
                                    console.error("팔로우 취소 오류:", error);
                                });
                        }
                    }
                    if (response.status === 403) {
                        alert("자기 자신은 팔로우 할 수 없습니다.")
                    }
                }
                else {
                    alert("팔로우가 완료되었습니다.")
                    window.location.reload();
                }
            })
    })
});
