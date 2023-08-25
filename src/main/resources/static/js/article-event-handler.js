
function ArticleDeleteConfirm(){
    if(!confirm("삭제된 글은 복구할 수 없습니다.\n삭제하시겠습니까?")){
        return false;
    }else{
        // location.href="${pageContext.request.contextPath }/board/delete.do?board_idx=${boardContents.board_idx}";
        }
}

function CommentDeleteConfirm(){
    if(!confirm("댓글을 삭제하시겠습니까?")){
        return false;
    }else{

    }
}

function ReplyDeleteConfirm(){
    if(!confirm("댓글을 삭제하시겠습니까?")){
        return false;
    }else{

    }
}