// 페이지가 로드될 때 이벤트 리스너 연결
window.addEventListener('DOMContentLoaded', function () {
    // 현재 페이지의 게시글 ID 추출
    const articleId = getArticleIdFromUrl();
    window.articleId = articleId; // 전역 변수로 선언

    // 게시글 상세 정보 가져오기
    fetchArticleDetails(articleId);

    // 수정 버튼 클릭 시 게시글 수정 페이지 이동
    document.getElementById("article-update").addEventListener("click", function (event) {
        event.preventDefault();
        window.location.href = "/views/articles/edit/" + articleId;
    });

    // 삭제 버튼 클릭 시 게시글 삭제 로직 실행
    document.getElementById('article-delete').addEventListener('click', function (event) {
        event.preventDefault();
        handleDeleteArticle(articleId);
    });


// 목록으로 돌아가기 버튼
const listButton = document.getElementById('list-btn');
listButton.addEventListener('click', () => {
    window.location.href = '/views/articles/list';
});

    // 이미지 선택 모드 활성화 여부
    let imageSelectingMode = false;
    window.selectedImageIds = window.selectedImageIds || [];

    // 선택된 이미지들의 상태 초기화
    function clearImageSelections() {
        const selectedImages = document.querySelectorAll('.article img.selected-for-action');

        selectedImages.forEach(img => {
            Array.from(img.parentNode.children).forEach(child => {
                if (['update-image-btn', 'delete-image-btn'].includes(child.className)) {
                    img.parentNode.removeChild(child);
                }
            });

            img.classList.remove('selected-for-action');
        });
        window.selectedImageIds = [];
    }

    // 이미지 선택 모드 활성화/비활성화
    function toggleImageSelectionMode(event) {
        const img = event.target;
        const imageId = img.dataset.imageId;
        const isImageSelected = img.classList.contains('selected-for-action');

        img.classList.toggle('selected-for-action', !isImageSelected);
        if (isImageSelected) {
            window.selectedImageIds = window.selectedImageIds.filter(id => id !== imageId);
        } else {
            window.selectedImageIds.push(imageId);
        }

        const buttonTypes = [
            { className: 'update-image-btn', text: '수정', handler: handleImageUpdate },
            { className: 'delete-image-btn', text: '삭제', handler: handleImageDelete }
        ];

        buttonTypes.forEach(buttonType => {
            const buttonExists = Array.from(img.parentNode.children).some(child => child.className === buttonType.className);
            if (isImageSelected || buttonExists) return;

            const btn = document.createElement('button');
            btn.innerText = buttonType.text;
            btn.className = buttonType.className;
            btn.onclick = buttonType.handler;

            if (btn.className === 'update-image-btn') {
                img.parentNode.insertBefore(btn, img.nextSibling);
            } else {
                const updateBtn = img.parentNode.querySelector('.update-image-btn');
                if (updateBtn) {
                    img.parentNode.insertBefore(btn, updateBtn.nextSibling);
                } else {
                    img.parentNode.insertBefore(btn, img.nextSibling);
                }
            }
        });
    }

    // 이미지 액션 버튼에 클릭 이벤트 리스너 추가 (이미지 선택 모드 활성화/비활성화)
    document.getElementById('article-images-action').addEventListener('click', function(event) {
        event.preventDefault();
        imageSelectingMode = !imageSelectingMode;

        const images = document.querySelectorAll('.article img');
        const article = document.querySelector('.article');
        article.classList.toggle('image-action-mode', imageSelectingMode);

        images.forEach(img => {
            if (imageSelectingMode) {
                img.addEventListener('click', toggleImageSelectionMode);
            } else {
                img.removeEventListener('click', toggleImageSelectionMode);
            }
        });

        if (!imageSelectingMode) clearImageSelections();
    });

    // 이미지 선택 모드 종료
    window.exitImageSelectionMode = function() {
        imageSelectingMode = false;
        const article = document.querySelector('.article');
        article.classList.remove('image-action-mode');
        clearImageSelections();
        const images = document.querySelectorAll('.article img');
        images.forEach(img => img.removeEventListener('click', toggleImageSelectionMode));
    };
});

