/**
 * 判断字符串是否为空
 * @param str 要判断的字符串
 * @return {boolean}
 */
function isBlank(str) {
    if (!str) {
        return true;
    }
    if (jQuery.trim(str) == '') {
        return true;
    }
    return false;
}


jQuery(document).ready(function () {
    $('.datetimepicker').datetimepicker({
        format: 'yyyy-MM-dd hh:mm:ss',
        language: 'en'
    });
    $('.datetimepicker').dblclick(function () {
        $(this).children("input").val("");
    });

    $(".audit").click(function () {
        $("#id").val($(this).attr("val"));
        $("#taskId").val($(this).attr("taskId"));
        $("#pageForm").attr("action", $("#action").val());
        $("#pageForm").submit();
    });

    $.alert = {
        dialogFirst: true,
        dialog: function (title, content, width, height, cssName, method) {
            var _this = this;
            if (_this.dialogFirst == true) {

                var temp_float = new String;
                temp_float = "<div class=\"modal-backdrop fade in\"></div>";
                temp_float += "<div id=\"floatBox\" class=\"modal fade in\" style=' overflow-y: auto;' >";
                temp_float += "<div class=\"modal-header\">";
                temp_float += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>";
                temp_float += "<h3>对话框标题</h3>";
                temp_float += "</div>";
                temp_float += "<div class=\"modal-body\" >";
                temp_float += "<p>loading...</p>";
                temp_float += "</div>";
                temp_float += "<div class=\"modal-footer\">";
                temp_float += "<button type='button' class=\"btn btn-primary  \" id='close'>关闭</button>";
                temp_float += "</div>";
                temp_float += "</div>";
                $("body").append(temp_float);
                _this.dialogFirst = false;
            }

            $(".close,#close").click(function () {
                _this.dialogFirst = true;
                if (method) {
                    method();
                } else {
                    $('.modal-backdrop').remove();
                    $('#floatBox').remove();
                }
            });

            $("#floatBox .modal-header h3").html(title);
            contentType = content.substring(0, content.indexOf(":"));
            content = content.substring(content.indexOf(":") + 1, content.length);
            switch (contentType) {
                case "url":
                    var content_array = content.split("?");
                    $.ajax({
                        type: content_array[0],
                        url: content_array[1],
                        data: content_array[2],
                        error: function () {
                            $("#floatBox .modal-body p").html("error...");
                        },
                        success: function (html) {
                            $("#floatBox .modal-body p").html(html);
                        }
                    });
                    break;
                case "text":
                    $("#floatBox .modal-body p").html(content);

                    break;
                case "id":
                    $("#floatBox .modal-body p").html($("#" + content + "").html());
                    break;
                case "iframe":
                    $("#floatBox .modal-body p").html("<iframe src=\"" + content + "\" width=\"100%\" height=\"" + (parseInt(height) - 30) + "px" + "\" scrolling=\"auto\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\"></iframe>");
                    break;
                case "img":
                    $("#floatBox .modal-body p").html("<img src=\"" + content + "\" width=\"100%\" height=\"" + (parseInt(height) - 30) + "px" + "\" scrolling=\"auto\" border=\"0\" marginheight=\"0\" marginwidth=\"0\" />");
            }

            $("#floatBoxBg").show();
            $("#floatBoxBg").animate({opacity: "0.5"}, "normal");
            $("#floatBox").attr("class", "modal " + cssName);
            $("#floatBox").css({
                display: "block",
                left: "50%",
                top: "0px",
                marginLeft: -(parseInt(width) / 2) + "px"
            });
            $("#floatBox").css({display: "block", width: width, height: height});
            $("#floatBox").animate({top: "200px"}, "normal");
        }
    }
});

function audit(val, url) {
    var remark = $("#remark").val();
    if (remark == null || remark == 'undefined' || remark == '') {
        $("#err-message").show();
        $("#err-message").addClass("alert-error");
        $("#err-span").html("请输入审核意见");
        return false;
    }
    if (confirm("是否确认提交申请信息?")) {
        $("#err-message").hide();
        $(".audit_btn").attr('disabled', true);
        $.ajax({
            type: "GET",
            url: url,
            data: {
                "taskId": $("#taskId").val(),
                "id": $("#id").val(),
                "processId": $("#processId").val(),
                "opinion": val,
                "approvalOpinion": remark,
                "taskKey": $("#taskKey").val()
            },
            dataType: "json",
            async: false,
            success: function (opBackResult) {
                if (opBackResult.status) {
                    $(".audit_btn").hide();
                    $("#remark").attr("readonly", "readonly");
                    $.alert.dialog("提示信息", "text:审核完成，请返回列表", "550px", "auto", "");
                } else {
                    $(".audit_btn").hide();
                    $("#remark").attr("readonly", "readonly");
                    $.alert.dialog("提示信息", "text:" + opBackResult.message, "550px", "auto", "");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $(".audit_btn").attr('disabled', false);
                $(".audit_btn").show();
                $.alert.dialog("提示信息", "text:服务器出现异常，请稍后再试", "550px", "auto", "");
            }
        });
    }
}

function toModify(id, url) {
    $('#pageForm').attr('action', url);
    $('#limitId').val(id);
    $('#pageForm').submit();
}

function cancelPaf(id, url) {
    jQuery.ajax({
        type: "GET",
        url: url,
        data: {"id": id},
        cache: false,
        success: function (opBackResult) {
            alert(opBackResult.message);
            $("#search").click();
        },
        error: function () {
            alert("系统暂时不能提供服务,请稍候再试");
        }
    });
}




