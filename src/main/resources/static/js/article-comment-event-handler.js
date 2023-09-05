let articleId; // 전역 변수로 선언

// 페이지가 로드될 때 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', function() {
    articleId = window.articleId;
    attachCommentEventListeners();
});

// 댓글 관련 이벤트 리스너 연결
function attachCommentEventListeners() {
    document.querySelector(".comment-btn").addEventListener("click", handleCreateComment);
    document.querySelector(".comments").addEventListener('click', handleCommentAction);

    // 댓글 생성 시 엔터 키 이용 가능
    document.querySelector(".form-control").addEventListener("keydown", function(event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            handleCreateComment();
        }
    });
}

// 액션별 설정 맵 (수정, 삭제, 답글 생성 등)
const actionMap = {
    'edit-comment-btn': {
        parentSelector: '.comment',
        action: handleUpdateComment
    },
    'delete-comment-btn': {
        parentSelector: '.comment',
        action: handleDeleteComment
    },
    'reply-comment-btn': {
        parentSelector: '.comment',
        action: handleCreateReply
    },
    'edit-reply-btn': {
        parentSelector: '.comment-reply',
        action: handleUpdateReply
    },
    'delete-reply-btn': {
        parentSelector: '.comment-reply',
        action: handleDeleteReply
    }
};

// 댓글 및 답글 관련 액션 처리
function handleCommentAction(e) {
    const targetClass = Array.from(e.target.classList).find(className => actionMap[className]);

    if (targetClass) {
        const { parentSelector, action } = actionMap[targetClass];
        const parentId = e.target.closest(parentSelector)?.getAttribute('data-id');
        if (parentId) action(parentId);
    }
}

// 댓글 생성 처리
function handleCreateComment() {
    const content = document.querySelector(".form-control").value;

    createComment(articleId, content, function(response) {
        addCommentToPage(response);
        document.querySelector(".form-control").value = '';
        updateCommentCount();
    });
}

// 댓글 수정 처리
function handleUpdateComment(commentId) {
    const currentContent = document.querySelector(`.comment[data-id="${commentId}"] p`).innerText;
    const newContent = prompt("댓글 내용을 수정해주세요:", currentContent);

    if (newContent && newContent !== currentContent) {
        updateComment(articleId, commentId, newContent, function(updatedComment) {
            document.querySelector(`.comment[data-id="${commentId}"] p`).innerText = updatedComment.content;
        });
    }
}

// 댓글 삭제 처리
function handleDeleteComment(commentId) {
    const isConfirmed = confirm("댓글을 삭제하시겠습니까?");

    if (isConfirmed) {
        deleteComment(articleId, commentId, function() {
            document.querySelector(`.comment[data-id="${commentId}"]`).remove();
            updateCommentCount();
        });
    }
}

// 답글 생성 처리
function handleCreateReply(parentCommentId) {
    const replyContent = prompt("답글 내용을 입력해주세요:");

    if (replyContent) {
        createReply(articleId, parentCommentId, replyContent, function(reply) {
            addReplyToPage(reply, parentCommentId);
            updateReplyCount(parentCommentId);
        });
    }
}

// 답글 수정 처리
function handleUpdateReply(replyId) {
    const currentContent = document.querySelector(`.comment-reply[data-id="${replyId}"] p`).innerText;
    const newContent = prompt("답글 내용을 수정해주세요:", currentContent);
    const parentCommentElement = document.querySelector(`.comment-reply[data-id="${replyId}"]`).closest('.comment');
    const parentCommentId = parentCommentElement.getAttribute('data-id');

    if (newContent && newContent !== currentContent) {
        updateComment(articleId, replyId, newContent, function(updatedReply) {
            document.querySelector(`.comment-reply[data-id="${replyId}"] p`).innerText = updatedReply.content;
            updateReplyCount(parentCommentId);
        });
    }
}

// 답글 삭제 처리
function handleDeleteReply(replyId) {
    const isConfirmed = confirm("답글을 삭제하시겠습니까?");

    if (isConfirmed) {
        deleteComment(articleId, replyId, function() {
            const replyElement = document.querySelector(`.comment-reply[data-id="${replyId}"]`);
            const parentCommentId = replyElement.closest('.comment').getAttribute('data-id');
            replyElement && replyElement.remove();

            // DOM 업데이트가 완료 이후 답글 수 업데이트
            setTimeout(() => {
                updateReplyCount(parentCommentId);
            }, 0);
        });
    }
}