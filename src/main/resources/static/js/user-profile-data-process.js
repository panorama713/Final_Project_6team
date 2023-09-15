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
        titleLink.href = "/views/articles/" + article.id;
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

        // 0을 제외한 (답글이 아닌) 댓글 수 표시
        if (article.commentCount > 0) {
            titleLink.innerHTML = `${article.title}<span class='inline-block'>&nbsp;[${article.commentCount}]</span>`;
        } else {
            titleLink.textContent = article.title;
        }

        // 이미지의 유무에 따른 아이콘 표시
        if (article.hasImage) {
            titleLink.innerHTML += `<span class='inline-block'>&nbsp;📷</span>`;
        }

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

function displayRoadmaps(roadmaps) {
    const roadmapList = document.getElementById('roadmap-list');
    roadmapList.innerHTML = ''; // 이전 데이터 초기화

    roadmaps.forEach(function (roadmap) {
        var row = document.createElement('tr');
        var typeElement = document.createElement('td');
        var titleElement = document.createElement('td');
        var createdAtElement = document.createElement('td');

        var titleLink = document.createElement('a');
        titleLink.href = "/views/roadmaps/" + roadmap.id;
        titleLink.textContent = roadmap.title;
        titleElement.appendChild(titleLink);

        createdAtElement.textContent = formatCreatedAt(roadmap.createdAt);
        typeElement.textContent = roadmap.type;

        row.appendChild(typeElement);
        row.appendChild(titleElement);
        row.appendChild(createdAtElement);

        roadmapList.appendChild(row);
    });
}

// 작성글 페이지 번호 표시
function displayArticlePageNumbers() {
    const articlePaginationContainer = document.getElementById('article-pagination-container');
    articlePaginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const articleNumberButton = document.createElement('button');
        articleNumberButton.textContent = i + 1;
        articleNumberButton.classList.add('btn');
        articleNumberButton.classList.add('custom-button');

        if (i === currentPage) {
            articleNumberButton.classList.add('active');
        }

        articleNumberButton.addEventListener('click', () => {
            fetchArticles(i, username);
        });

        articlePaginationContainer.appendChild(articleNumberButton);
    }
}

// 작성글 로드맵 번호 표시
function displayRoadmapPageNumbers(userId) {
    const roadmapPaginationContainer = document.getElementById('roadmap-pagination-container');
    roadmapPaginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageNumberButton = document.createElement('button');
        pageNumberButton.textContent = i + 1;
        pageNumberButton.classList.add('btn');
        pageNumberButton.classList.add('custom-button');

        if (i === currentPage) {
            pageNumberButton.classList.add('active');
        }

        pageNumberButton.addEventListener('click', () => {
            fetchRoadmaps(i, userId);
        });

        roadmapPaginationContainer.appendChild(pageNumberButton);
    }
}



let currentPage = 0;
let totalPages = 0;

function fetchArticles(page, username) {
    fetch(`/api/v1/articles/userArticles?page=${page}&username=${username}`)
        .then(response => response.json())
        .then(result => {
            var userId = localStorage.getItem('userId');

            // if (result.content) {
            //     followElement.setAttribute("user-id-value", result.content[0].userId);
            //     userId = followElement.getAttribute("user-id-value")
            // } else {
            //     userId = localStorage.getItem('userId')
            // }

            totalPages = result.totalPages;
            currentPage = result.number;
            displayArticles(result.content);
            displayArticlePageNumbers();
            getUserImage(userId)
            getCountOfFollower(userId);
            isFollow(userId);
            getCountOfRoadmaps(userId)
            fetchRoadmaps(0, userId)
            displayRoadmapPageNumbers(userId)

        })
        .catch(error => console.error('Error:', error));
}


function fetchRoadmaps(page, userId) {
    fetch('/api/v1/roadmaps/userProfile/'+userId+'?page='+page)
        .then(response => response.json())
        .then(result => {
            displayRoadmaps(result.content);
            displayRoadmapPageNumbers();
        })
        .catch(error => console.error('Error:', error))
}

let countOfFollower = 0;

// 팔로워 수 받아오기
function getCountOfFollower(userId) {
    fetch(`/api/v1/users/${userId}/follow`)
        .then(response => {
            if (!response.ok) {
                throw new Error('팔로워 수 불러오기 오류');
            }
            return response.json();
        })
        .then(data => {
            var followCount = data;
            document.querySelector('#follower-num').textContent = followCount;
        })
        .catch(error => console.error('Error:', error));

}

// 유저 프로필 사진 받아오기
function getUserImage(userId) {
    fetch("/api/v1/users/" + userId)
        .then(response => response.json())
        .then(result => {
            const profileImage = document.getElementById('profile-pic');
            profileImage.src = result.profileImg;
        })
        .catch(error => console.error('Error:', error));
}


// 로드맵 수 받아오기
function getCountOfRoadmaps(userId) {
    fetch("/api/v1/roadmaps/count/" + userId)
        .then(response => {
            if (!response.ok) {
                throw new Error('로드맵 수 불러오기 오류');
            }
            return response.json();
        })
        .then(data => {
            var roadmapCount = data;
            document.querySelector('#roadmap-num').textContent = roadmapCount;
        })
        .catch(error => console.error('Error:', error));
}

// 게시글 수 받아오기
function getCountOfArticles(username) {
    fetch("/api/v1/articles/countOfArticles?username=" + username)
        .then(response => {
            if (!response.ok) {
                throw new Error('게시글 수 불러오기 오류');
            }
            return response.json();
        })
        .then(data => {
            var articleCount = data;
            document.querySelector('#article-num').textContent = articleCount;
        })
        .catch(error => console.error('Error:', error));
}


// 팔로우 여부 받아오기
function isFollow(userId) {
    fetch(`/api/v1/users/${userId}/follow/isFollow`)
        .then(response => {
            if (!response.ok) {
                throw new Error('팔로우 여부 받아오기 오류');
            }
            return response.json();
        })
        .then(data => {
            var isFollow = data;
            const followButton = document.getElementById('follow-btn');

            if (isFollow) {
                followButton.classList.add('unfollow');
                followButton.textContent = '팔로우 중';
            } else {
                followButton.classList.remove('unfollow');
                followButton.textContent = '팔로우';
            }
        })
        .catch(error => console.error('Error:', error));
}

const username = localStorage.getItem('currentWriter');
document.querySelector('#profile-name').textContent = username;


var followElement = document.getElementById("follow-btn");
fetchArticles(0, username);
getCountOfArticles(username);

