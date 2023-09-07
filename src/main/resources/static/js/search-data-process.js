// 로드맵 검색 결과 리스트
const searchRoadmapList = document.getElementById('item-list');
const paginationContainer = document.getElementById('pagination-nums');
const searchButton = document.querySelector('.search-button');
const searchText = document.getElementById('search-bar-text');

let currentPage = 1;
let totalPages = 0;
const itemsPerPage = 7;

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
        .then((pageData) => {
            const data = pageData.content;

            // 총 페이지 수 계산 (서버에서 총 아이템 수를 받아서 계산)
            totalPages = Math.ceil(pageData.totalElements / itemsPerPage);

            // 데이터를 받아서 결과 항목 및 페이징을 렌더링
            renderItems(data);

            // 페이징을 렌더링
            renderPagination(currentPage, totalPages);
        })
        .catch((error) => {
            console.error('에러 발생', error);
        });
}

function fetchData(page) {
    return fetch(`/api/v1/roadmaps/search?keyword=${query}&page=${page - 1}&limit=${itemsPerPage}`)
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
}

function renderItems(data) {
    searchRoadmapList.innerHTML = '';

    if (data.length > 0) {
        data.forEach((item) => {
            const resultItemDiv = document.createElement('div');
            resultItemDiv.classList.add('result-item');

            const titleDiv = document.createElement('div');
            titleDiv.classList.add('result-title');
            // TODO 구현 되면 수정
            titleDiv.innerHTML = `<h4><a href="/views/roadmaps/${item.roadmapId}" class="item-link">${item.title}</a></h4>`

            const descDiv = document.createElement('div');
            descDiv.classList.add('result-desc');
            descDiv.textContent = `설명: ${item.description}`;

            const writerDiv = document.createElement('div');
            writerDiv.classList.add('result-writer');
            writerDiv.textContent = `작성자: ${item.writer}`;

            const divideLine = document.createElement('hr')

            resultItemDiv.appendChild(titleDiv);
            resultItemDiv.appendChild(descDiv);
            resultItemDiv.appendChild(writerDiv);

            searchRoadmapList.appendChild(resultItemDiv);
            searchRoadmapList.appendChild(divideLine);
        });
    } else {
        const noDataDiv = document.createElement('div');
        // noDataDiv.textContent = '찾으시는 결과가 없습니다.';
        noDataDiv.innerHTML = `<h3 class="noResult">찾으시는 결과가 없습니다.</h3>`
        searchRoadmapList.appendChild(noDataDiv);
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
                .then((pageData) => {
                    const data = pageData.content;
                    renderItems(data);
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
                    .then((pageData) => {
                        const data = pageData.content;
                        renderItems(data);
                        currentPage = page; // 현재 페이지 업데이트
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
                .then((pageData) => {
                    const data = pageData.content;
                    renderItems(data);
                    currentPage = currentPage + 1; // 현재 페이지 업데이트
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