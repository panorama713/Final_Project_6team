// 페이지가 로드될 때 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', function () {
    // 현재 페이지의 게시글 ID 추출
    const articleId = getArticleIdFromUrl();

    // 게시글 상세 정보 가져오기
    fetchArticleDetails(articleId);

    // 수정 버튼 클릭 시 게시글 수정 페이지 이동
    document.getElementById("article-update").addEventListener("click", function(event) {
        event.preventDefault();
        window.location.href = "/views/articles/edit/" + articleId;
    });

    // 삭제 버튼 클릭 시 게시글 삭제 로직 실행
    document.getElementById('article-delete').addEventListener('click', function(event) {
        event.preventDefault();
        handleDeleteArticle(articleId);
    });
});


// 목록으로 돌아가기 버튼
const listButton = document.getElementById('list-btn');
listButton.addEventListener('click', () => {
    window.location.href = '/views/articles/list';
});


