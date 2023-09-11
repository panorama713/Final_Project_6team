document.addEventListener('click', function (event) {
    if (event.target.classList.contains('fill-progress-title')) {
        getElementTodo(event);
    }
})

async function getElementTodo(event) {
    var roadmapId = event.target.dataset.roadmapId;
    var elementId = event.target.dataset.elementId;

    await fetch(`/api/v1/roadmaps/${roadmapId}/elements/${elementId}/todo`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(elementTodo => {
                console.log(elementTodo);
                displayTodo(elementTodo);
            })
        .catch(error => {
            console.log(error.message);
            alert('Todo 목록을 가져오는 도중 에러가 발생하였습니다.')
        })
}

function displayTodo(todoData) {

}