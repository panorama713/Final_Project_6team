
document.addEventListener("DOMContentLoaded", function() {
    var deleteButton = document.getElementById("article-update");
    deleteButton.addEventListener("click", function(event) {
        event.preventDefault();

        var currentUrl = window.location.href;
        var articleId = currentUrl.split('/').pop();
        var editUrl = "/views/articles/edit/" + articleId;
        window.location.href = editUrl;
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const deleteButton = document.getElementById('article-delete');

    deleteButton.addEventListener('click', function(event) {
        event.preventDefault();
        const confirmDelete = confirm('게시글을 삭제하시겠습니까?');

        if (confirmDelete) {
            const url = window.location.href;
            const articleId = url.match(/\/(\d+)(?:\?|$)/)[1];

            fetch('/api/v1/articles/' + articleId, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        alert('게시글이 삭제되었습니다.');
                        window.location.href = '/views/articles/list';
                    } else {
                        console.error('Error Response:', response.status, response.statusText);
                        throw new Error('게시글 삭제 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    alert(error.message);
                    console.error('게시글 삭제 오류:', error);
                });
        }
    });
});

