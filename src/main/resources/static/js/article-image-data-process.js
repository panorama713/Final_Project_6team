// 이미지만 수정 핸들러
function handleImageUpdate() {
    if (window.selectedImageIds && window.selectedImageIds.length > 0) {
        const articleId = window.articleId;

        let input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        input.multiple = true;
        input.onchange = function(event) {
            if (event.target.files.length > 0) {
                let formData = new FormData();

                formData.append('imageIds', new Blob([JSON.stringify(window.selectedImageIds)], { type: "application/json" }));

                for (let i = 0; i < event.target.files.length; i++) {
                    const file = event.target.files[i];
                    formData.append('images', file);
                }

                fetch(`/api/v1/articles/${articleId}/images`, {
                    method: 'PUT',
                    body: formData
                })
                    .then(response => {
                        if (!response.ok) {
                            return response.json().then(err => {
                                throw new Error(err.message || '에러가 발생했습니다.');
                            });
                        }
                        if (response.status === 204) {
                            fetchArticleDetails(articleId);
                            window.selectedImageIds = [];
                            exitImageSelectionMode();
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                        console.error('Error:', error);
                    });
            }
        }
        input.click();
    } else {
        alert('수정할 이미지를 먼저 선택해주세요.');
    }
}

// 이미지만 삭제 핸들러
function handleImageDelete() {
    if (window.selectedImageIds && window.selectedImageIds.length > 0) {
        const articleId = window.articleId;

        if (confirm('선택한 이미지를 삭제하시겠습니까?')) {
            let formData = new FormData();

            formData.append('imageIds', new Blob([JSON.stringify(window.selectedImageIds)], { type: "application/json" }));

            fetch(`/api/v1/articles/${articleId}/images`, {
                method: 'DELETE',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => {
                            throw new Error(err.message || '에러가 발생했습니다.');
                        });
                    }
                    if (response.status === 204) {
                        fetchArticleDetails(articleId);
                        window.selectedImageIds = [];
                        exitImageSelectionMode();
                    }
                })
                .catch(error => {
                    alert(error.message);
                    console.error('Error:', error);
                });
        }
    } else {
        alert('삭제할 이미지를 먼저 선택해주세요.');
    }
}