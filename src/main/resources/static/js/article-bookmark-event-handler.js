const openModalBtn = document.getElementById("bookmark-btn");
const modal = document.getElementById("bookmarkModal");
const closeModalBtn = document.getElementById("closeModalBtn");
const sendBookmark = document.getElementById("sendBookmarkBtn");

// 모달 열기
openModalBtn.addEventListener("click", function () {
    modal.style.display = "block";
});

// 모달 닫기
closeModalBtn.addEventListener("click", function () {
    modal.style.display = "none";
});


sendBookmark.addEventListener("click", function () {

    const bookmarkName = document.getElementById("bookmark-name")
    const param = {
        title: bookmarkName.value
    }

    fetch("/api/v1/bookmarks/articles/" + articleId, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(param)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 409) {
                    if (confirm('이미 북마크된 게시물입니다. 북마크를 취소하시겠습니까?')) {
                        fetch("/api/v1/bookmarks/articles/" + articleId, {
                            method: "DELETE"
                        })
                            .then(data => {
                                alert("북마크가 취소되었습니다.");
                                window.location.reload();
                            })
                            .catch(error => {
                                console.error("북마크 취소 오류:", error);
                            });
                    }
                }
                if (response.status === 403) {
                    alert("자신의 글은 북마크할 수 없습니다.")
                }
            } else {
                alert("북마크 되었습니다.")
                window.location.reload();
            }
        })
});

