
// 게시판 카테고리
function handleButtonClick(buttonId) {
    if (buttonId === 'category-front-end') {
        localStorage.setItem('currentCategory', 'FRONTEND');
    } else if (buttonId === 'category-back-end') {
        localStorage.setItem('currentCategory', 'BACKEND');
    } else if (buttonId === 'category-mobile') {
        localStorage.setItem('currentCategory', 'MOBILE');
    } else if (buttonId === 'category-game') {
        localStorage.setItem('currentCategory', 'GAME');
    } else if (buttonId === 'category-devops') {
        localStorage.setItem('currentCategory', 'DEVOPS');
    }
    window.location.href = '/views/articles/list';
}

var frontEndBtn = document.getElementById("category-front-end");
frontEndBtn.addEventListener("click", function() {
    handleButtonClick('category-front-end');
});

var backEndBtn = document.getElementById("category-back-end");
backEndBtn.addEventListener("click", function() {
    handleButtonClick('category-back-end');
});

var mobileBtn = document.getElementById("category-mobile");
mobileBtn.addEventListener("click", function() {
    handleButtonClick('category-mobile');
});

var gameBtn = document.getElementById("category-game");
gameBtn.addEventListener("click", function() {
    handleButtonClick('category-game');
});

var devOpsBtn = document.getElementById("category-devops");
devOpsBtn.addEventListener("click", function() {
    handleButtonClick('category-devops');
});


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
        var usernameElement = document.createElement('td');
        var typeElement = document.createElement('td');
        var createdAtElement = document.createElement('td');
        var viewCountElement = document.createElement('td')
        var likeCountElement = document.createElement('td')

        var titleLink = document.createElement('a');
        titleLink.href = article.id;
        titleLink.textContent = article.title;
        titleElement.appendChild(titleLink);

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

        // 이미지의 유무에 따른 아이콘 표시
        if (article.hasImage) {
            // titleLink.textContent += " 📷"; // 아이콘을 제목 뒤에 추가
            titleLink.textContent = "📷 " + article.title; // 아이콘을 제목 앞에 추가
        }

        usernameElement.textContent = article.username;
        createdAtElement.textContent = formatCreatedAt(article.createdAt);
        viewCountElement.textContent = article.viewCount;
        likeCountElement.textContent = article.likeCount;

        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(usernameElement);
        row.appendChild(createdAtElement);
        row.appendChild(viewCountElement);
        row.appendChild(likeCountElement);

        articleList.appendChild(row);

    });
}

// 페이지 번호 표시
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
            fetchArticles(i);
        });

        paginationContainer.appendChild(pageNumberButton);
    }
}


let currentPage = 0;
let totalPages = 0;

function fetchArticles(page, category) {
    console.log(category)
    fetch(`/api/v1/articles?page=${page}&category=${category}`)
        .then(response => response.json())
        .then(result => {
            totalPages = result.totalPages;
            currentPage = result.number;
            displayArticles(result.content);
            displayPageNumbers();
            localStorage.setItem('currentPage', currentPage);
        })
        .catch(error => console.error('Error:', error));
}

const savedPage = localStorage.getItem('currentPage');
const savedCategory = localStorage.getItem('currentCategory')
if (savedPage !== null && savedPage !== null) {
    fetchArticles(savedPage, savedCategory);
    document.querySelector('#category-title').textContent = savedCategory+ ' 게시판';
} else {
    fetchArticles(0, 'FRONTEND');
    document.querySelector('#category-title').textContent = savedCategory+ '게시판';
}
