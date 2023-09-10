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
    const articleList = document.getElementById('article-list');
    articleList.innerHTML = ''; // 이전 데이터 초기화

    articles.forEach(function (article) {
        var row = document.createElement('tr');
        var titleElement = document.createElement('td');
        var typeElement = document.createElement('td');
        var categoryElement = document.createElement('td');
        var createdAtElement = document.createElement('td');
        var viewCountElement = document.createElement('td')
        var likeCountElement = document.createElement('td')

        var titleLink = document.createElement('a');
        titleLink.href = "/views/articles/"+ article.id;
        titleLink.textContent = article.title;
        titleElement.appendChild(titleLink);

        // 카테고리 값 한글로 바꾸기
        const CategoryMappings = {
            "FRONTEND": '프론트엔드',
            "BACKEND": '백엔드',
            "MOBILE": '모바일',
            "GAME": '게임',
            "DEVOPS": '데브옵스'
        };

        let categoryText = CategoryMappings[article.category] || '';
        categoryElement.textContent = categoryText;


        // type 값 한글로 바꾸기
        const typeMappings = {
            "NOTI": '공지',
            "QUESTION": '질문',
            "STUDY": '스터디',
            "TIP": '지식',
            "CHAT": '잡담'
        };

        let typeText = typeMappings[article.type] || '';
        typeElement.textContent = typeText;

        createdAtElement.textContent = formatCreatedAt(article.createdAt);
        viewCountElement.textContent = article.viewCount;
        likeCountElement.textContent = article.likeCount;

        row.appendChild(categoryElement);
        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(createdAtElement);
        row.appendChild(viewCountElement);
        row.appendChild(likeCountElement);

        articleList.appendChild(row);

    });
}

// 작성글 페이지 번호 표시
function displayPageNumbers() {
    const paginationContainer = document.getElementById('pagination-container');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageNumberButton = document.createElement('button');
        pageNumberButton.textContent = i + 1;
        pageNumberButton.classList.add('btn');
        pageNumberButton.classList.add('custom-button');

        if (i === currentPage) {
            pageNumberButton.classList.add('active');
        }

        pageNumberButton.addEventListener('click', () => {
            fetchArticles(i, username);
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}


let currentPage = 0;
let totalPages = 0;
function fetchArticles(page, username) {
    fetch(`/api/v1/articles/userArticles?page=${page}&username=${username}`)
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;
            displayArticles(result.content);
            displayPageNumbers();
        })
        .catch(error => console.error('Error:', error));
}

let countOfFollower = 0;
// 팔로워 수 받아오기
function getCountOfFollower(username) {
    fetch('/api/v1/users/follow?username='+username)
        .then(response => {
            if (!response.ok) {
                throw new Error('팔로워 수 불러오기 오류');
            }
            return response.json(); // JSON 형식으로 파싱
        })
        .then(data => {
            var followCount = data;
            document.querySelector('#follower-num').textContent = followCount;
        })
        .catch(error => console.error('Error:', error));

}

// 게시글 수 받아오기
function getCountOfArticles(username) {
    fetch("/api/v1/articles/countOfArticles?username="+username)
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글 수 불러오기 오류');
            }
            return response.json(); // JSON 형식으로 파싱
        })
        .then(data => {
            var articleCount = data;
            document.querySelector('#article-num').textContent = articleCount;
        })
        .catch(error => console.error('Error:', error));

}

const username = localStorage.getItem('currentWriter');
document.querySelector('#profile-name').textContent = username;

fetchArticles(0, username);
getCountOfFollower(username);
getCountOfArticles(username);

