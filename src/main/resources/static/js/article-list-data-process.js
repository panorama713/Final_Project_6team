// 페이지 정보 저장
let currentPage = 0;
let totalPages = 0;

// createdAt 출력 형식
function formatCreatedAt(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}


// articles 표시
function displayArticles(articles) {
    const articleList = document.getElementById('post-list');
    articleList.innerHTML = ''; // 이전 데이터 초기화

    articles.forEach(function (article) {
        var row = document.createElement('tr');
        var titleElement = document.createElement('td');
        var usernameElement = document.createElement('td');
        var typeElement = document.createElement('td');
        var createdAtElement = document.createElement('td');
        var viewCountElement = document.createElement('td')

        var titleLink = document.createElement('a');
        titleLink.href = article.id;
        titleLink.textContent = article.title;
        titleElement.appendChild(titleLink);

        usernameElement.textContent = article.username;
        typeElement.textContent = article.type;
        createdAtElement.textContent = formatCreatedAt(article.createdAt);
        viewCountElement.textContent = article.viewCount;

        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(usernameElement);
        row.appendChild(createdAtElement);
        row.appendChild(viewCountElement);

        articleList.appendChild(row);
    });
}

// 페이지 번호 표시
function displayPageNumbers() {
    const paginationContainer = document.getElementById('pagination-container');
    paginationContainer.innerHTML = ''; // 기존 버튼 초기화

    for (let i = 0; i < totalPages; i++) {
        const pageNumberButton = document.createElement('button');
        pageNumberButton.textContent = i + 1; // 페이지 번호를 1부터 시작하도록 표시

        // 부트스트랩 버튼 클래스 추가
        pageNumberButton.classList.add('btn');

        // 사용자 정의 클래스 추가 (custom-button)
        pageNumberButton.classList.add('custom-button');

        // 현재 페이지인 경우 강조 스타일 적용
        if (i === currentPage) {
            pageNumberButton.classList.add('active');
        }

        pageNumberButton.addEventListener('click', () => {
            fetchArticles(i); // 페이지 번호 버튼을 클릭하면 해당 페이지의 Articles를 불러옵니다.
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}




// articles 불러오기
function fetchArticles(page) {
    fetch(`/api/v1/articles?page=${page}`)
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;

            displayArticles(result.content);
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}

fetchArticles(currentPage);
