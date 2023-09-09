// 게시글의 상세 정보 가져오기
function fetchArticleDetails(id) {
    fetch(`/api/v1/articles/${id}`)
        .then(response => response.json())
        .then(data => {
            console.log('게시글 상세 정보:', data);
            displayArticleDetails(data, id);
            localStorage.setItem('currentWriter', data.username);
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
    console.trace("getArticleIdFromUrl 호출 추적");
    console.log("getArticleIdFromUrl 호출 시작");
    const pathSegments = window.location.pathname.split('/');
    const articleId = pathSegments[pathSegments.length - 1];
    console.log('URL:', window.location.href);
    console.log('Pathname:', window.location.pathname);
    console.log('articleId:', articleId);
    console.log("getArticleIdFromUrl 호출 종료");
    return articleId;
}

// 전체 URL 변환 (상대 경로, 절대 경로 모두 처리)
function convertPathToUrl(path, articleId) {
    if (path.startsWith('http://') || path.startsWith('https://')) {
        return path;
    }
    const basePath = "http://localhost:8080";
    if (path.startsWith('/uploads/article_images/')) {
        const imageName = path.split('/').pop();
        return `${basePath}/api/v1/articles/${articleId}/images/${imageName}`;
    }
    if (path.startsWith('/')) {
        return basePath + path;
    }
    return basePath + '/' + path;
}

