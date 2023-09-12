document.addEventListener('click', function (event) {
    if (event.target.classList.contains('fill-progress-title')) {
        const container = document.querySelector('.element-todo-body');
        container.innerHTML = '';
        getElementTodo(event);
    }

    if (event.target.classList.contains('.form-check-input')) {
        // 체크박스 상태가 변경될 때마다 업데이트 함수 호출
        const checkboxes = document.querySelectorAll('.form-check-input');
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', updateCheckedCount);
        });
    }

    if (event.target.classList.contains('create-todo-submit')) {
        var title = document.getElementById('todo-title').value;
        var content = document.getElementById('todo-content').value;
        var url = document.getElementById('todo-url').value;

        var jsonData = {
            title: title,
            content: content,
            url: url
        };
        createTodo(event.target.dataset.roadmapId, event.target.dataset.elementId, jsonData);
    }

    // updateTodo 관련 로직
    if (event.target.classList.contains('update-todo-button')) {
        var title = event.target.dataset.title;
        var content = event.target.dataset.content;
        var url = event.target.dataset.url;
        var todoId = parseInt(event.target.dataset.todoId);

        document.getElementById('update-todo-title').value = title;
        document.getElementById('update-todo-content').value = content;
        document.getElementById('update-todo-url').value = url;

        // update-submit data-todoId 추가
        // console.log('todo id : ', todoId);
        document.getElementById('update-todo-submit').dataset.todoId = todoId;
    }

    if (event.target.classList.contains('update-todo-submit')) {
        var title = document.getElementById('update-todo-title').value;
        var content = document.getElementById('update-todo-content').value;
        var url = document.getElementById('update-todo-url').value;
        var todoId = parseInt(event.target.dataset.todoId);

        var jsonData = {
            title: title,
            content: content,
            url: url
        };
        updateTodo(event.target.dataset.roadmapId, event.target.dataset.elementId, todoId, jsonData);
    }

    // deleteTodo 관련 로직
    if (event.target.classList.contains('delete-todo-button')) {
        var roadmapId = event.target.dataset.roadmapId;
        var elementId = event.target.dataset.elementId;
        var todoId = parseInt(event.target.dataset.todoId);

        console.log(event.target.dataset)

        deleteTodo(roadmapId, elementId, todoId);
    }
})

async function getElementTodo(event) {
    var roadmapId = event.target.dataset.roadmapId;
    var elementId = event.target.dataset.elementId;
    var elementTitle = event.target.dataset.elementTitle;

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
    // createTodoSubmit
    document.getElementById('create-todo-submit').dataset.roadmapId = roadmapId;
    document.getElementById('create-todo-submit').dataset.elementId = elementId;

    // updateTodoSubmit
    document.getElementById('update-todo-submit').dataset.roadmapId = roadmapId;
    document.getElementById('update-todo-submit').dataset.elementId = elementId;

    var modal = document.getElementById('roadmapElementTodoModalLabel');
    modal.textContent = '일정: ' + elementTitle;

    var donePercent = document.createElement('span');
    donePercent.classList.add('col', 'align-self-end')
    donePercent.style.float = 'right';
    donePercent.textContent = doneProgress(todoDataList) + '%';

    modal.append(donePercent);

    var container = document.querySelector('.element-todo-body');
    todoDataList.forEach(data => {
        var todoDiv = document.createElement('div');
        todoDiv.classList.add('todo-item', 'row');

        var todoDone = document.createElement('div');
        todoDone.classList.add('todo-done');
        todoDone.style.textAlign = 'left';
        todoDone.innerHTML =
            `
            <div class="form-check" style="text-align: left">
              <input class="form-check-input" type="checkbox" value="" id="todo-done-check" ${data.done ? 'checked' : ''}>
              <a class="form-check-label" for="todo-done-check" href="#" >
                ${data.title}
              </a>
              <span class="delete-todo-button" id="delete-todo" style="float: right;"
              data-roadmap-id="${roadmapId}" data-element-id="${elementId}" data-todo-id="${data.id}"
              >삭제</span>
              <span style="float: right;">&nbsp|&nbsp</span>
              <span class="update-todo-button" id="update-todo-button" data-bs-toggle="modal" 
              data-bs-target="#updateTodo" style="float: right;"
              data-title='${data.title}' data-content='${data.content}' 
              data-url='${data.url}' data-todo-id="${data.id}"
                >수정</span>
            </div>
            `
        todoDiv.appendChild(todoDone);
        container.appendChild(todoDiv);
    })

    // createTodo
    var createTodoButton = document.createElement('div');
    createTodoButton.innerHTML =
        `
        <div>
            <button class="btn btn-sm create-todo"  data-bs-toggle="modal" data-bs-target="#createTodo">+ Todo 추가하기</button>
        </div>
        `
    container.append(createTodoButton);
}

// done-progress data-end 변경 기능
function doneProgress(todoDataList) {
    var todoCount = todoDataList.length;
    var trueCount = 0;

    // 원래 값이 없다면 0%
    if (todoCount === 0) {
        return 0;
    } else {
        todoDataList.forEach(data => {
            if (data.done === true) trueCount = trueCount + 1;
        })
        return Math.round(trueCount / todoCount * 100);
    }
}

//Bootstrap multiple modal
$('#createTodo').on('hidden.bs.modal', function (e) {
    e.stopPropagation();
});

$('#roadmapElementTodoModal').on('hide.bs.modal', function (e) {
    if (e.target === this) {
        e.stopPropagation();
    }
});

$('#closeTodoModalButton').click(function () {
    $('#parentModal').modal('hide');
});

// createTodo
function createTodo(roadmapId, elementId, jsonData) {
    fetch(`/api/v1/roadmaps/${roadmapId}/elements/${elementId}/todo`, {
        method: 'POST',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(jsonData)
    })
        .then(data => {
            alert("todo가 생성되었습니다");
            location.reload();
        })
        .catch(error => {
            console.error("todo 생성 오류:", error);
            alert(error.message);
        });
}

// updateTodo
function updateTodo(roadmapId, elementId, todoId, jsonData) {
    fetch(`/api/v1/roadmaps/${roadmapId}/elements/${elementId}/todo/${todoId}`, {
        method: 'PUT',
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(jsonData)
    })
        .then(data => {
            alert("todo가 수정되었습니다");
            location.reload();
        })
        .catch(error => {
            console.error("todo 수정 오류:", error);
            alert(error.message);
        });
}

// deleteTodo
function deleteTodo(roadmapId, elementId, todoId) {
    var answer = confirm("정말로 삭제 하시겠습니까?")
    if (answer) {
        fetch(`/api/v1/roadmaps/${roadmapId}/elements/${elementId}/todo/${todoId}`, {
            method: 'DELETE'
        })
            .then(data => {
                alert("todo가 삭제 되었습니다");
            })
            .catch(error => {
                console.error("todo 삭제 오류:", error);
                alert(error.message);
            });
    }
}