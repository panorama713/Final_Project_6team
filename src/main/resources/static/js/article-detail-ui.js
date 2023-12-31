// 웹 페이지에 게시글의 상세 정보 표시
function displayArticleDetails(data, articleId) {
    document.querySelector('#article-title').textContent = data.title;
    document.querySelector('#article-content').innerHTML = data.content.replace(/\n/g, '<br>');
    document.querySelector('#article-username').textContent = data.username;
    document.getElementById('bookmark-name').value = data.title;
    const displayDate = formatArticleDateTime(data.createdAt, data.lastModifiedAt);
    document.querySelector('#article-date').textContent = "작성 일시: " + displayDate;

    const imgElement = document.getElementById("article-images");
    imgElement.src = data.imagePath;

    if (data.imagePath == null ) {
        imgElement.style.display = "none";
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
    // displayArticleImages(data.images || [], articleId);

    // 작성자의 일치 여부에 따른 글 설정 액션 버튼 표시
    const articleActionButton = document.querySelector('.btn.btn-secondary.dropdown-toggle.article-dropdown');
    if (articleActionButton) {
        articleActionButton.style.display = data.isWriter ? 'block' : 'none';
    }

    // 좋아요 여부에 따른 버튼 변경
    const isLike = data.isLike;
    const likeButton = document.getElementById('like-btn');

    if (isLike) {
        likeButton.classList.add('unlike');
    } else {
        likeButton.classList.remove('unlike');
    }

    const likeCount = data.likeCount
    likeButton.textContent = '❤️ ' + likeCount;

    // 북마크 여부에 따른 버튼 변경
    const isBookmark = data.isBookmark;
    const bookmarkButton = document.getElementById('bookmark-btn');

    if (isBookmark) {
        bookmarkButton.classList.add('cancel-bookmark');
        bookmarkButton.textContent = '✅ 북마크';
    } else {
        bookmarkButton.classList.remove('cancel-bookmark');
        bookmarkButton.textContent = '북마크';
    }

    // 팔로우 여부에 따른 텍스트 변경
    const isFollow = data.isFollow;
    const followButton = document.getElementById('follow-btn');
    if (isFollow) {
        followButton.textContent = '언팔로우';
    } else {
        followButton.textContent = '팔로우';
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
    imageActionButton.style.display = images.length > 0 ? 'block' : 'none';
}

// 작성 일시 포맷팅
function formatArticleDateTime(createdAt, lastModifiedAt) {
    const createdAtDate = new Date(createdAt);
    const updatedAtDate = lastModifiedAt ? new Date(lastModifiedAt) : null;

    const koreanCreatedAtDate = new Date(createdAtDate.getTime() + (9 * 60 * 60 * 1000))

    const formattedDate = koreanCreatedAtDate.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
    const formattedTime = koreanCreatedAtDate.toLocaleTimeString('ko-KR', {hour: '2-digit', minute: '2-digit'});

    let displayString = `${formattedDate} ${formattedTime}`;

    if (updatedAtDate && (createdAtDate.getTime() !== updatedAtDate.getTime())) {
        displayString += ' (수정됨)';
    }

    return displayString;
}
