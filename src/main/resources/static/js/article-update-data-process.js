
let index = {
    init: function () {
        $("#article-update").on("click", () => {
            this.save();
        });

    },

    save: function () {
        var id = $("#articleId").val();
        let data = {
            title: $("#title").val(),
            content: $("#content").val(),
            type: $("#type").val()
        }
        $.ajax({
            type: "PUT",
            url: "/api/v1/articles/"+id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function (res) {
            alert("글 수정이 완료되었습니다.");
            location.href = "/api/v1/articles/list";
        }).fail(function (request,status,error) {
            alert("code:"+request.status+"\n"+id);
        });
    },
}

index.init();