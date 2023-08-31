// 웹 페이지에 게시글의 상세 정보 표시
function displayArticleDetails(data) {
    document.querySelector('#article-title').textContent = data.title;
    document.querySelector('#article-content').textContent = data.content;
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
    if (data.images && data.images.length > 0) {
        displayArticleImages(data.images, getArticleIdFromUrl());
    }
}

// 웹 페이지에 게시글의 이미지 표시
function displayArticleImages(images, articleId) {
    const imageContainer = document.querySelector('#article-images');

    images.forEach(img => {
        const imgElement = document.createElement('img');
        const imageUrl = convertPathToUrl(img.imageUrl, articleId);

        imgElement.src = imageUrl;
        imgElement.alt = img.imageName;
        imageContainer.appendChild(imgElement);
        console.log(imgElement.src);
    });
}