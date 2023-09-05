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
    window.selectedImageIds = window.selectedImageIds || [];

    // 이미지 선택 모드 활성화/비활성화
    function toggleImageSelectionMode(isSelecting) {
        const article = document.querySelector('.article');
        const images = document.querySelectorAll('.article img');

        // 이미지 선택 모드 활성화
        if (isSelecting) {
            article.classList.add('update-selecting');
            images.forEach(img => {
                img.addEventListener('click', toggleImageSelection);
            });
        } else { // 이미지 선택 모드 비활성화
            article.classList.remove('update-selecting');
            images.forEach(img => {
                img.removeEventListener('click', toggleImageSelection);
            });
        }
    }

    // 이미지를 클릭하면 선택된 이미지의 상태를 토글
    function toggleImageSelection(event) {
        const img = event.target;
        const imageId = img.dataset.imageId;
        const btn = document.createElement('button');

        btn.innerText = "수정";
        btn.className = "update-image-btn";
        btn.onclick = function() {
            handleImageUpdate();
            img.classList.remove('selected-for-update');
            if (img.nextSibling && img.nextSibling.className === "update-image-btn") {
                img.parentNode.removeChild(img.nextSibling);
            }
            updateSelectingMode = false;
            toggleImageSelectionMode(false);
        };

        // 이미 선택된 이미지를 다시 클릭하면 선택 해제
        if (img.classList.contains('selected-for-update')) {
            img.classList.remove('selected-for-update');
            const index = window.selectedImageIds.indexOf(imageId);
            if (index > -1) {
                window.selectedImageIds.splice(index, 1);
            }
            if (img.nextSibling && img.nextSibling.className === "edit-image-btn") {
                img.parentNode.removeChild(img.nextSibling);
            }
        } else { // 이미지를 처음 클릭하면 선택 상태로 변경
            img.classList.add('selected-for-update');
            window.selectedImageIds.push(imageId);
            img.parentNode.insertBefore(btn, img.nextSibling);
        }
    }

    document.getElementById("article-update-image").addEventListener("click", function(event) {
        event.preventDefault();
        updateSelectingMode = !updateSelectingMode;
        toggleImageSelectionMode(updateSelectingMode);
    });
});