// 웹 페이지에 게시글의 상세 정보 표시
function displayArticleDetails(data, articleId) {
    document.querySelector('#article-title').textContent = data.title;
    document.querySelector('#article-content').innerHTML = data.content.replace(/\n/g, '<br>');
    document.querySelector('#article-username').textContent = "작성자: " + data.username;

    const displayDate = formatDateTime(data.createdAt, data.lastModifiedAt);
    document.querySelector('#article-date').textContent = "작성 일시: " + displayDate;

    let articleLikeCountElement = document.querySelector('#article-likeCount');

    if (articleLikeCountElement) {
        articleLikeCountElement.textContent = data.likeCount;
    } else {
        console.warn('DOM에서 #article-likeCount 요소를 찾을 수 없음');
    }
    if (data.type) {
        // type 값 한글로 바꾸기
        const typeMappings = {
            "NOTI": '공지',
            "QUESTION": '질문',
            "STUDY": '스터디',
            "TIP": '지식',
            "CHAT": '잡담'
        };

        let typeText = typeMappings[data.type] || '';
        document.querySelector('.type').textContent = typeText;
    } else {
        document.querySelector('.type').style.display = 'none';
    }
    displayArticleImages(data.images || [], articleId);

    // 작성자의 일치 여부에 따른 글 설정 액션 버튼 표시
    const articleActionButton = document.querySelector('.btn.btn-secondary.dropdown-toggle.article-dropdown');
    if (articleActionButton) {
        if (data.isWriter) {
            articleActionButton.style.display = 'block'; // 버튼 보이기
        } else {
            articleActionButton.style.display = 'none';  // 버튼 숨기기
        }
    }
}

// 웹 페이지에 게시글의 이미지 표시
function displayArticleImages(images, articleId) {
    const imageContainer = document.querySelector('#article-images');

    // 이미지 컨테이너의 모든 이미지 요소 삭제
    while (imageContainer.firstChild) {
        imageContainer.removeChild(imageContainer.firstChild);
    }

    images.forEach(img => {
        const imgElement = document.createElement('img');
        const imageUrl = convertPathToUrl(img.imageUrl, articleId);

        imgElement.src = imageUrl;
        imgElement.alt = img.imageName;
        imgElement.dataset.imageId = img.id;
        imageContainer.appendChild(imgElement);
        console.log(imgElement.src);
    });


    // 이미지의 유무에 따른 이미지 액션 버튼 표시
    const imageActionButton = document.getElementById('article-images-action');
    if (images.length > 0) {
        imageActionButton.style.display = 'block'; // 버튼 보이기
    } else {
        imageActionButton.style.display = 'none';  // 버튼 숨기기
    }
}

