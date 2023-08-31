// 게시글의 상세 정보 가져오기
function fetchArticleDetails(id) {
    fetch(`/api/v1/articles/${id}`)
        .then(response => response.json())
        .then(data => {
            console.log('게시글 상세 정보:', data);
            displayArticleDetails(data);
        })
        .catch(error => console.error('Error:', error));
}

// 게시글 삭제 처리
function handleDeleteArticle(articleId) {
    if (confirm('게시글을 삭제하시겠습니까?')) {
        fetch('/api/v1/articles/' + articleId, { method: 'DELETE' })
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
}

// 현재 페이지의 게시글 ID 추출
function getArticleIdFromUrl() {
    const pathSegments = window.location.pathname.split('/');
    const articleId = pathSegments[pathSegments.length - 1];
    console.log('URL:', window.location.href);
    console.log('Pathname:', window.location.pathname);
    console.log('articleId:', articleId);
    return articleId;
}