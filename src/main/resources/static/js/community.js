
function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);   //type=1，对提问进行评论
}

function comment2target(targetId, type, content){
    if(!content){
        alert("不能回复空内容~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response){
            if(response.code == 200){
                $("#comment_section").hide();
                window.location.reload();
            }else {
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);

                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=73faf1f14af65694b3cb&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                }else {
                    alert(response.message)
                }
            }
        },
        dataType: "json"
    })
}

function commment(e){
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId, 2, content)
}
//展开二级评论功能
function collapseComments(e){
    var id = e.getAttribute("data-id");
    console.log(id);
    var comments = $("comment-" + id);
    comments.toggleClass("in");
}

function selectTag(e){
    var value = e.getAttribute("data-tag");

    var previous = $("#tag").val()

    if(previous.indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous + ',' + value)
        }else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag(){
    $("#select-tag").show()
}