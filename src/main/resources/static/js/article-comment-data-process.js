// API 요청을 처리하는 공통 함수
function makeRequest(url, options, callback) {
    fetch(url, options)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || "에러가 발생했습니다.");
                });
            }
            if (response.status === 204 || response.status === 205) {
                return null;
            }
            return response.json();
        })
        .then(callback)
        .catch(error => {
            alert(error.message);
            console.error("Error:", error);
        });
}

// 댓글 생성 API 요청 함수
function createComment(articleId, content, callback) {
    const url = `/api/v1/articles/${articleId}/comments`;
    const data = { content: content };

    makeRequest(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }, callback);
}

// 댓글 목록 조회 API 요청 함수
function readComments(articleId, callback) {
    const url = `/api/v1/articles/${articleId}/comments`;

    makeRequest(url, {}, callback);
}

// 댓글 수정 API 요청 함수
function updateComment(articleId, commentId, content, callback) {
    const url = `/api/v1/articles/${articleId}/comments/${commentId}`;
    const data = { content: content };

    makeRequest(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }, callback);
}

// 댓글 삭제 API 요청 함수
function deleteComment(articleId, commentId, callback) {
    const url = `/api/v1/articles/${articleId}/comments/${commentId}`;

    makeRequest(url, {
        method: 'DELETE'
    }, callback);
}

// 답글 생성 API 요청 함수
function createReply(articleId, parentCommentId, content, callback) {
    const url = `/api/v1/articles/${articleId}/comments/${parentCommentId}/replies`;
    const data = { content: content };

    makeRequest(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }, callback);
}