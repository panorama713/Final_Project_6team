// 통합 검색 결과 리스트
const searchList = document.getElementById('item-list');
const paginationContainer = document.getElementById('pagination-nums');
const searchButton = document.querySelector('.search-button');
const searchText = document.getElementById('search-bar-text');

let currentPage = 1;
let totalPages = 0;
const itemsPerPage = 8;

let query = getQueryFromURL();

function getQueryFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('query') || '';
}

document.addEventListener('DOMContentLoaded', () => {
    reloadKeyword();
    fetchDataAndRender(currentPage)
})

searchButton.addEventListener('click', (event) => {
    event.preventDefault();
    query = searchText.value;
    currentPage = 1;
    reloadKeyword();
    fetchDataAndRender(currentPage)
    window.location.href = `/views/search?query=${query}`
})

function reloadKeyword() {
    searchText.value = query
}

function fetchDataAndRender(page) {
    // 초기 페이지 로드 시 첫 번째 페이지의 데이터를 가져와 렌더링
    fetchData(page)
        .then(([roadmapData, articleData]) => {
            const roadmapItems = roadmapData.content;
            const articleItems = articleData.content;

            const allItems = [...roadmapItems, ...articleItems];
            totalPages = Math.ceil((roadmapData.totalElements + articleData.totalElements) / itemsPerPage);

            renderItems(allItems);

            renderPagination(currentPage, totalPages);
        })
        .catch((error) => {
            console.error('에러 발생', error);
        })
}

function fetchData(page) {
    const roadmapURL = `/api/v1/roadmaps/total-search?keyword=${query}&page=${page - 1}`;
    const articleURL = `/api/v1/articles/total-search?keyword=${query}&page=${page - 1}`;

    const roadmapPromise = fetch(roadmapURL)
        .then(async (response) => {
            if (!response.ok) {
                throw new Error('데이터 가져오기 실패');
            }
            return await response.json();
        })
        .catch((error) => {
            console.error('에러 발생', error);
            throw error;
        });

    const articlePromise = fetch(articleURL)
        .then(async (response) => {
            if (!response.ok) {
                throw new Error('게시글 데이터 가져오기 실패');
            }
            return await response.json();
        })
        .catch((error) => {
            console.error('에러 발생', error);
            throw error;
        });

    return Promise.all([roadmapPromise, articlePromise]);
}

function renderItems(data) {
    searchList.innerHTML = '';

    if (data.length > 0) {
        data.forEach((item) => {
            const resultItemDiv = document.createElement('div');
            resultItemDiv.classList.add('result-item');

            const titleDiv = document.createElement('div');
            titleDiv.classList.add('result-title');

            const typeDiv = document.createElement('div');
            typeDiv.classList.add('result-type');

            if (item.hasOwnProperty('roadmapId')) {
                titleDiv.innerHTML = `<h4><a href="/views/roadmaps/${item.roadmapId}" class="item-link">${item.title}</a></h4>`
                typeDiv.textContent = '위치: 로드맵';
            } else {
                titleDiv.innerHTML = `<h4><a href="/views/articles/${item.articleId}" class="item-link">${item.title}</a></h4>`
                typeDiv.textContent = '위치: 게시글';
            }

            const descDiv = document.createElement('div');
            descDiv.classList.add('result-desc');
            descDiv.textContent = `설명: ${item.description}`;

            const writerDiv = document.createElement('div');
            writerDiv.classList.add('result-writer');
            writerDiv.textContent = `작성자: ${item.writer}`;

            const divideLine = document.createElement('hr')

            resultItemDiv.appendChild(titleDiv);
            resultItemDiv.appendChild(typeDiv);
            resultItemDiv.appendChild(descDiv);
            resultItemDiv.appendChild(writerDiv);

            searchList.appendChild(resultItemDiv);
            searchList.appendChild(divideLine);
        });
    } else {
        const noDataDiv = document.createElement('div');
        noDataDiv.innerHTML = `<h3 class="noResult">찾으시는 결과가 없습니다.</h3>`
        searchList.appendChild(noDataDiv);
    }
}

function renderPagination(currentPage, totalPages) {
    paginationContainer.innerHTML = '';

    if (totalPages <= 1) {
        return;
    }

    const paginationList = document.createElement('nav');
    paginationList.setAttribute('aria-label', 'Page navigation');

    const ul = document.createElement('ul');
    ul.classList.add('pagination');

    // Previous 버튼 추가
    const previousLi = document.createElement('li');
    previousLi.classList.add('page-item');
    if (currentPage === 1) {
        previousLi.classList.add('disabled');
    }
    const previousLink = document.createElement('a');
    previousLink.classList.add('page-link');
    previousLink.href = '#';
    previousLink.setAttribute('aria-label', 'Previous');
    previousLink.innerHTML = '<span aria-hidden="true">&laquo;</span>';
    previousLink.addEventListener('click', () => {
        if (currentPage > 1) {
            fetchData(currentPage - 1)
                .then(([roadmapData, articleData]) => {
                    const roadmapItems = roadmapData.content;
                    const articleItems = articleData.content;

                    const allItems = [...roadmapItems, ...articleItems];

                    renderItems(allItems);

                    currentPage = currentPage - 1; // 현재 페이지 업데이트
                    updatePagination(currentPage);
                })
                .catch((error) => {
                    console.error('에러 발생', error);
                });
        }
    });

    previousLi.appendChild(previousLink);
    ul.appendChild(previousLi);

    // 페이지 번호 추가
    for (let page = 1; page <= totalPages; page++) {
        const li = document.createElement('li');
        li.classList.add('page-item');
        if (page === currentPage) {
            li.classList.add('active');
        }
        const pageLink = document.createElement('a');
        pageLink.classList.add('page-link');
        pageLink.href = '#';
        pageLink.textContent = page;
        pageLink.addEventListener('click', () => {
            if (page !== currentPage) {
                fetchData(page)
                    .then(([roadmapData, articleData]) => {
                        const roadmapItems = roadmapData.content;
                        const articleItems = articleData.content;

                        const allItems = [...roadmapItems, ...articleItems];

                        renderItems(allItems);

                        currentPage = page;
                        updatePagination(currentPage);
                    })
                    .catch((error) => {
                        console.error('에러 발생', error);
                    });
            }
        });
        li.appendChild(pageLink);
        ul.appendChild(li);
    }

    // Next 버튼 추가
    const nextLi = document.createElement('li');
    nextLi.classList.add('page-item');
    if (currentPage === totalPages) {
        nextLi.classList.add('disabled');
    }
    const nextLink = document.createElement('a');
    nextLink.classList.add('page-link');
    nextLink.href = '#';
    nextLink.setAttribute('aria-label', 'Next');
    nextLink.innerHTML = '<span aria-hidden="true">&raquo;</span>';
    nextLink.addEventListener('click', () => {
        if (currentPage < totalPages) {
            fetchData(currentPage + 1)
                .then(([roadmapData, articleData]) => {
                    const roadmapItems = roadmapData.content;
                    const articleItems = articleData.content;

                    const allItems = [...roadmapItems, ...articleItems];

                    renderItems(allItems);

                    currentPage = currentPage + 1;
                    updatePagination(currentPage);
                })
                .catch((error) => {
                    console.error('에러 발생', error);
                });
        }
    });
    nextLi.appendChild(nextLink);
    ul.appendChild(nextLi);

    paginationList.appendChild(ul);
    paginationContainer.appendChild(paginationList);
}

// 페이지네이션 업데이트 함수
function updatePagination(newPage) {
    const paginationItems = paginationContainer.querySelectorAll('.page-item');
    paginationItems.forEach((item) => {
        const pageLink = item.querySelector('.page-link');
        if (pageLink.textContent === String(newPage)) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    // Previous 버튼 활성화/비활성화
    const previousItem = paginationContainer.querySelector('.page-item:first-child');
    if (newPage === 1) {
        previousItem.classList.add('disabled');
    } else {
        previousItem.classList.remove('disabled');
    }

    // Next 버튼 활성화/비활성화
    const nextPage = paginationContainer.querySelector('.page-item:last-child');
    if (newPage === totalPages) {
        nextPage.classList.add('disabled');
    } else {
        nextPage.classList.remove('disabled');
    }
}