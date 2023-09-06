// 웹 페이지에 게시글의 상세 정보 표시
function displayArticleDetails(data, articleId) {
    document.querySelector('#article-title').textContent = data.title;
    document.querySelector('#article-content').innerHTML = data.content.replace(/\n/g, '<br>');
    document.querySelector('#article-username').textContent = "작성자: " + data.username;

    const currentDate = new Date().toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });
    document.querySelector('#article-date').textContent = "작성 날짜: " + currentDate;

    let articleLikeCountElement = document.querySelector('#article-likeCount');

    if (articleLikeCountElement) {
        articleLikeCountElement.textContent = data.likeCount;
    } else {
        console.warn('DOM에서 #article-likeCount 요소를 찾을 수 없음');
    }
    if (data.type) {
        document.querySelector('.type').textContent = data.type;
    } else {
        document.querySelector('.type').style.display = 'none';
    }
    displayArticleImages(data.images || [], articleId);
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