// 북마크 ID 가져오는 함수
async function getBookmarkId(roadmap) {
    try {
        const response = await fetch(`/api/v1/bookmarks/roadmaps/${roadmap.id}`, {
            method: 'GET'
        });

        if (response.ok) {
            const data = await response.json();
            return data.id;
        } else {
            throw new Error('북마크 ID를 가져오는데 실패했습니다.');
        }
    } catch (error) {
        console.error('북마크 ID를 가져오는데 실패했습니다:', error);
        return null; // 에러가 발생하면 null을 반환
    }
}

async function existBookmark(roadmap) {
    try {
        const response = await fetch(`/api/v1/bookmarks/roadmaps/${roadmap.id}/exist`, {
            method: 'GET'
        });
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('북마크 존재 여부를 확인하는 도중 오류 발생:', error);
    }
}

async function cancelRoadmapBookmark(bookmarkId) {
    const confirmMessage = '북마크를 취소하시겠습니까?';
    if (confirm(confirmMessage)) {
        await fetchCancelRoadmapBookmarks(bookmarkId);
    }
}

// 북마크 취소를 수행하는 함수
async function fetchCancelRoadmapBookmarks(bookmarkId) {
    try {
        await deleteRoadmapBookmark(bookmarkId);
    } catch (error) {
        console.error('북마크 취소 또는 ID 가져오기 중 에러 발생:', error);
    }
}

// 북마크 삭제 함수
async function deleteRoadmapBookmark(bookmarkId) {
    try {
        const response = await fetch(`/api/v1/bookmarks/${bookmarkId}/roadmaps`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('북마크를 취소했습니다!');
            location.reload();
        } else {
            const errorRes = await response.json();
            alert(errorRes.message || "에러");
        }
    } catch (error) {
        console.error('북마크 취소 요청 실패:', error);
    }
}

async function addRoadmapBookmark(roadmap) {
    const bookmarkTitle = prompt('저장할 로드맵 북마크 제목을 입력하세요: ');
    if (bookmarkTitle) {
        await fetchAddRoadmapBookmarks(bookmarkTitle, roadmap);
    }
}

function fetchAddRoadmapBookmarks(data, roadmap) {
    fetch(`/api/v1/bookmarks/roadmaps/${roadmap.id}`, {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    }).then(async response => {
        if (response.ok) {
            alert('북마크가 추가되었습니다!');
            location.reload();
        } else {
            const errorRes = await response.json()
            alert(errorRes.message || "에러")
        }
    });
}

/*function fetchCancelRoadmapBookmarks(roadmap) {
    fetch(`/api/v1/bookmarks/roadmaps/${roadmap.id}`, {
        method: 'GET'
    })
        .then(async response => {
            if (response.ok) {
                const data = await response.json();
                return data.id;
            } else {
                throw new Error('북마크 ID를 가져오는데 실패했습니다.');
            }
        })
        .then((bookmarkId) => {
            // 이제 bookmarkId를 사용하여 북마크 취소 요청을 보낼 수 있습니다.
            fetch(`/api/v1/bookmarks/${bookmarkId}/roadmaps`, {
                method: 'DELETE'
            })
                .then(async (response) => {
                    if (response.ok) {
                        alert('북마크를 취소했습니다!');
                        location.reload();
                    } else {
                        const errorRes = await response.json();
                        alert(errorRes.message || "에러");
                    }
                })
                .catch((error) => {
                    console.error('북마크 취소 요청 실패:', error);
                });
        })
        .catch((error) => {
            console.error('북마크 ID를 가져오는데 실패했습니다:', error);
        });
}*/