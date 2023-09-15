document.addEventListener('click', function (event) {
    if (event.target.classList.contains('fill-progress-title')) {
        const container = document.querySelector('.element-todo-body');
        container.innerHTML = '';
        getElementTodo(event);
    }
})

async function getElementTodo(event) {
    const roadmapId = event.target.dataset.roadmapId;
    const elementId = event.target.dataset.elementId;
    const elementTitle = event.target.dataset.elementTitle;

    await fetch(`/api/v1/roadmaps/${roadmapId}/elements/${elementId}/todo`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(elementTodoList => {
            console.log(elementTodoList);
            displayTodo(roadmapId, elementId, elementTodoList, elementTitle);
        })
        .catch(error => {
            console.log(error.message);
            alert('Todo 목록을 가져오는 도중 에러가 발생하였습니다.')
        })
}

// elementTodo 모달창 내용 생성
function displayTodo(roadmapId, elementId, todoDataList, elementTitle) {
    const modal = document.getElementById('roadmapElementTodoModalLabel');
    modal.textContent = elementTitle;

    const container = document.querySelector('.element-todo-body');
    todoDataList.forEach(data => {
        const todoDiv = document.createElement('div');
        todoDiv.classList.add('todo-item', 'row');

        const todoDone = document.createElement('div');
        todoDone.classList.add('todo-done');
        todoDone.style.textAlign = 'left';
        todoDone.innerHTML =
            `
            <div class="form-check" style="text-align: left">
              <a class="form-check-label" for="todo-done-check" href="#" >
                ${data.title}
              </a>
            </div>
            `
        todoDiv.appendChild(todoDone);
        container.appendChild(todoDiv);
    })
}