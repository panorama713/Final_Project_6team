// 페이지가 로드될 때 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', function () {
    // 현재 페이지의 게시글 ID 추출
    const articleId = getArticleIdFromUrl();
    window.articleId = articleId; // 전역 변수로 선언

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

    // 이미지 선택 모드 활성화 여부
    let updateSelectingMode = false;
    let deleteSelectingMode = false;
    window.selectedImageIds = window.selectedImageIds || [];

    // 선택된 이미지들의 상태 초기화
    function clearImageSelections() {
        const images = document.querySelectorAll(
            '.article img.selected-for-update, .article img.selected-for-delete'
        );

        images.forEach(img => {
            const isActionButton = img.nextSibling &&
                (img.nextSibling.className === "update-image-btn" || img.nextSibling.className === "delete-image-btn");

            if (isActionButton) {
                img.parentNode.removeChild(img.nextSibling);
            }
            img.classList.remove('selected-for-update', 'selected-for-delete');
        });
        window.selectedImageIds = [];
    }


    // 이미지 선택 모드 활성화/비활성화
    function toggleSelectionMode(mode, className, callback, preventMode, preventCallback) {
        const article = document.querySelector('.article');
        const images = document.querySelectorAll('.article img');

        // 다른 모드가 활성화된 경우 초기화
        if (preventMode) {
            clearImageSelections();
            images.forEach(img => img.removeEventListener('click', preventCallback));
            preventMode = false;
        }
        // 이미지 선택 모드 활성화
        if (mode) {
            article.classList.add(className);
            images.forEach(img => img.addEventListener('click', callback));
        } else { // 이미지 선택 모드 비활성화
            article.classList.remove(className);
            images.forEach(img => img.removeEventListener('click', callback));
        }
    }

    // 이미지를 클릭하면 선택된 이미지의 상태를 토글 및 해당 액션(수정 또는 삭제)을 수행
    function handleImageSelection(event, action, className, actionButton) {
        const img = event.target;
        const imageId = img.dataset.imageId;
        const btn = document.createElement('button');

        btn.innerText = action;
        btn.className = actionButton;
        btn.onclick = function() {
            if (actionButton === 'update-image-btn') {
                handleImageUpdate();
            } else if (actionButton === 'delete-image-btn') {
                handleImageDelete();
            }
            img.classList.remove(className);
            if (img.nextSibling && img.nextSibling.className === actionButton) {
                img.parentNode.removeChild(img.nextSibling);
            }
            if (actionButton === 'update-image-btn') {
                updateSelectingMode = false;
                toggleSelectionMode(
                    false, 'update-selecting', handleImageUpdateSelection
                );
            } else {
                deleteSelectingMode = false;
                toggleSelectionMode(
                    false, 'delete-selecting', handleImageDeleteSelection
                );
            }
        };

        // 이미 선택된 이미지를 다시 클릭하면 선택 해제
        if (img.classList.contains(className)) {
            img.classList.remove(className);
            const index = window.selectedImageIds.indexOf(imageId);
            if (index > -1) {
                window.selectedImageIds.splice(index, 1);
            }
            if (img.nextSibling && img.nextSibling.className === actionButton) {
                img.parentNode.removeChild(img.nextSibling);
            }
        } else { // 이미지를 처음 클릭하면 선택 상태로 변경
            img.classList.add(className);
            window.selectedImageIds.push(imageId);
            img.parentNode.insertBefore(btn, img.nextSibling);
        }
    }

    function handleImageUpdateSelection(event) {
        handleImageSelection(
            event, "수정", 'selected-for-update', 'update-image-btn'
        );
    }

    function handleImageDeleteSelection(event) {
        handleImageSelection(
            event, "삭제", 'selected-for-delete', 'delete-image-btn'
        );
    }

    document.getElementById("article-update-image").addEventListener("click", function(event) {
        event.preventDefault();
        updateSelectingMode = !updateSelectingMode;
        toggleSelectionMode(
            updateSelectingMode, 'update-selecting', handleImageUpdateSelection,
            deleteSelectingMode, handleImageDeleteSelection
        );
    });

    document.getElementById("article-delete-image").addEventListener("click", function(event) {
        event.preventDefault();
        deleteSelectingMode = !deleteSelectingMode;
        toggleSelectionMode(
            deleteSelectingMode, 'delete-selecting', handleImageDeleteSelection,
            updateSelectingMode, handleImageUpdateSelection
        );
    });
});