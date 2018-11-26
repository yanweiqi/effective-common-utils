/**
 * Created by songdz on 2015/11/22.
 */
function onClickInsert(event, treeId, treeNode, clickFlag) {
    if (treeNode.level == 2) {
        var have = 0;
        $('#components tbody tr').each(function (index) {
            var temp1 = $(this).children('td').eq(1).find('input');
            var temp = $(this).children('td').eq(1).find('input').attr('name');
            if ($(this).children('td').eq(1).find('input').attr('name') == treeNode.id) {
                have = 1;
            }
        });
        if (have == 0) {
            var appendStr = "<tr>" +
                "<td ></td>" +
                "<td ><input type='text' name='" + treeNode.id + "' id='" + treeNode.realId + "' value='" + treeNode.name + "'  style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;' readonly='readonly'  parentName='" + treeNode.getParentNode().id + "' parentId='" + treeNode.getParentNode().realId + "' parentValue='" + treeNode.getParentNode().name
                + "' / > " +
                "<td><a class='btn btn-danger btn-sm' onclick='deleteComponent(this);'>删除</a></td>" +
                "</tr>";
            $("#components tbody").append(appendStr);
            addComponentOrder();
        }
    }
}

function onClickUpdate(event, treeId, treeNode, clickFlag) {
    if (treeNode.level == 1) {
        var have = 0;
        $('#components tbody tr').each(function (index) {
            var temp1 = $(this).children('td').eq(1).find('input');
            var temp = $(this).children('td').eq(1).find('input').attr('name');
            if ($(this).children('td').eq(1).find('input').attr('name') == treeNode.id) {
                have = 1;
            }
        });
        if (have == 0) {
            var appendStr = "<tr>" +
                "<td ></td>" +
                "<td ><input type='text' name='" + treeNode.id + "' id='" + treeNode.realId + "' value='" + treeNode.name + "'  style='border:1px;border-bottom-style:none;border-top-style:none;border-left-style:none;border-right-style:none;' readonly='readonly'  parentName='" + treeNode.getParentNode().id + "' parentId='" + treeNode.getParentNode().realId + "' parentValue='" + treeNode.getParentNode().name
                + "' / > " +
                "<td><a class='btn btn-danger btn-sm' onclick='deleteComponent(this);'>删除</a></td>" +
                "</tr>";
            $("#components tbody").append(appendStr);
            addComponentOrder();
        }
    }
}

function closeModel(){
    $('#componentSet').modal('hide');
    $('#components tbody').empty();
}

function showComponentInfo(code, operate) {
    $('#componentSet').modal('hide');
    var secondComponentIds = "";
    var secondComponentNames = "";
    var secondComponentCodes = "";
    var ThirdComponentIds = "";
    var ThirdComponentNames = "";
    var ThirdComponentCodes = "";
    $('#components tbody tr').each(function (index) {
        if (index != 0) {
            secondComponentIds = secondComponentIds + $(this).children('td').eq(1).find('input').attr('parentId') + ",";
            secondComponentNames = secondComponentNames + $(this).children('td').eq(1).find('input').attr('parentValue') + ",";
            secondComponentCodes = secondComponentCodes + $(this).children('td').eq(1).find('input').attr('parentName') + ",";
            ThirdComponentIds = ThirdComponentIds + $(this).children('td').eq(1).find('input').attr('id') + ",";
            ThirdComponentCodes = ThirdComponentCodes + $(this).children('td').eq(1).find('input').attr('name') + ",";
            ThirdComponentNames = ThirdComponentNames + $(this).children('td').eq(1).find('input').attr('value') + ",";
        }
    });
    $.ajax({
        type: "GET",
        url: "/jdScfProduct/showComponentInfo.action",
        data: {
            "secondComponentIds": secondComponentIds,
            "secondComponentNames": secondComponentNames,
            "secondComponentCodes": secondComponentCodes,
            "ThirdComponentIds": ThirdComponentIds,
            "ThirdComponentCodes": ThirdComponentCodes,
            "ThirdComponentNames": ThirdComponentNames,
            "pageJsonBefore": JSON.stringify(pageJsonBefore),
            "operate": operate,
            "code": code
        },
        dataType: "json",
        async: false,
        success: function (result) {
            if(result.jsonInfos!="")
            {
                showComponentDetail(result)
            }
            else{
                pageJsonBefore = result.jsonInfos;
                $('#componentInfo').empty();
            }

        }
    });
}


function showComponentDetail(result) {
    var appendStr = "";
    var pageJson = $.parseJSON(result.jsonInfos);
    $('#componentInfo').empty();
    for (var i = 0; i < pageJson.length; i++) {
        appendStr = appendStr +
        '<div id="' + pageJson[i].secondScfComponent.code + '" class="panel panel-default" >'
        + '<div class="panel-heading" >' +
        '<ul class="breadcrumb"> ' +
        '<li class="text-info"> <h3>' + pageJson[i].secondScfComponent.componentName +
        '</h3></li>' +
        '<li>' + '<button type="button" class="btn btn-info btn-sm" onclick="showTree(' + pageJson[i].secondScfComponent.code + ');">修改</button>' +
        '</li>' +
        '</ul>' +
        '</div>';
        for (var j = 0; j < pageJson[i].thirdComponentParams.length; j++) {
            appendStr = appendStr +
            '<div id="' + pageJson[i].thirdComponentParams[j].thirdScfComponent.code + '" class="panel-body" >'+
            '<div class="panel panel-default" >'
            + '<div class="panel-heading" >' +
            '<ul class="breadcrumb"> ' +
            '<li class="text-info">' + pageJson[i].thirdComponentParams[j].thirdScfComponent.componentName +
            '</li>' +
            '</ul>' +
            '</div>' +
            '<div class="panel-body" > ' +
            '<table class="table table-bordered" id="baseParams"> ' +
            '<tbody> ' +
            '<tr> ' +
            '<th>序号</th>' +
            '<th>参数名称</th> ' +
            '<th>参数值</th>' +
            '<th>产品权限</th> ' +
            '</tr>';
            var sort = 0;
            for (var k = 0; k < pageJson[i].thirdComponentParams[j].jdScfBaseParams.length; k++) {
                appendStr = appendStr +
                '<tr>' +
                '<td>' + (++sort) + '</td>' +
                '<td>' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].paramName + '</td>' +
                '<td>' +
                '<input type="text" name=' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].code + ' id='
                + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id + ' value="' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].paramValue +
                '" />' +
                '</td>' +
                '<td>' +
                '<select id="' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id + 'productAuth" >' +
                '<option value="1" ';
                if(pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].productAuth == 1 ){
                    appendStr = appendStr +'selected="selected" ';
                }
                appendStr = appendStr +'>只读</option>' +
                '<option value="2"';
                if(pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].productAuth == 2 ){
                    appendStr = appendStr +'selected="selected" ';
                }
                appendStr = appendStr +' >必填</option>' +
                '<option value="3"';
                if(pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].productAuth == 3 ){
                    appendStr = appendStr +'selected="selected" ';
                }
                appendStr = appendStr +'>可写</option>' +
                '<option value="4"';
                if(pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].productAuth == 4 ){
                    appendStr = appendStr +'selected="selected" ';
                }
                appendStr = appendStr +'>不可写</option>' +
                '</select>' +
                '</td>' +
                '</tr>'
            }
            appendStr = appendStr +
            '</tbody>' +
            '</table>' +
            '</div>' +
            '</div>'+
            '</div>'
        }
        appendStr = appendStr +
        '</div>'
    }
    $('#componentInfo').append(appendStr);

    pageJsonBefore = result.jsonInfos;
}

function addComponentOrder() {
    var number = 0;
    $('#components tbody tr').each(function (index) {
        if ($(this).css("display") != "none") {
            if (index != 0) {
                $(this).children().first().text(++number);
            }
        }
        ;
    });
}
function deleteComponent(tr) {
    $(tr).parent().parent().remove();
    addComponentOrder();
}
function onSubmit(operate) {
    if(isBlank($('#productName').val())){
        alert("产品名称不能为空");
        $('#productName').css('border','1px solid red');
        $('#productName').focus();
        return false;
    }
    $('#productName').css('border','1px solid black');
    if(isBlank($('#productCode').val())){
        alert("产品编码不能为空");
        $('#productCode').css('border','1px solid red');
        $('#productCode').focus();
        return false;
    }
    $('#productCode').css('border','1px solid black');
    var pageJson = $.parseJSON(pageJsonBefore);
    for (var i = 0; i < pageJson.length; i++) {
        for (var j = 0; j < pageJson[i].thirdComponentParams.length; j++) {
            for (var k = 0; k < pageJson[i].thirdComponentParams[j].jdScfBaseParams.length; k++) {
                if(isBlank($('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id).val())){
                    alert("参数值不能为空");
                    $('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id).css('border','1px solid red');
                    $('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id).focus();
                    return false;
                }
                $('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id).css('border','1px solid black');
                pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].paramValue = $('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id).val();
                pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].productAuth = $('#' + pageJson[i].thirdComponentParams[j].jdScfBaseParams[k].id + 'productAuth').val();
            }
        }
    }
    $.ajax({
        type: "GET",
        url: "/jdScfProduct/add.action",
        data: {
            "productName": $('#productName').val(),
            "productCode": $('#productCode').val(),
            "pageJson": JSON.stringify(pageJson),
            "operate": operate
        },
        dataType: "json",
        async: false,
        success: function (result) {
            if (result.status) {
                var msg = result.message;

                msg = "新增产品成功! ";

                alert(msg);
                window.location.href="/jdScfProduct/jdScfProductList.action";

            } else {
                alert("新增产品失败:" + result.message);
                window.location.href="/jdScfProduct/jdScfProductList.action";
            }
        }
    });
}


function toAudit(taskId,id,taskKey) {
    if (taskKey == "1") {
        taskKey = "audit_1";
    }
    else {
        taskKey = "audit_2";
    }
    $("#id").val(id);
    $("#taskKey").val(taskKey);
    $("#taskId").val(taskId);
    $("#pageForm").attr("action", $("#action").val());
    $("#pageForm").submit();
}


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

function queryByComponentName(treeId,searchConditonId){
    searchByFlag_ztree(treeId, searchConditonId);
}
function searchByFlag_ztree(treeId, searchConditionId){
    //<1>.搜索条件
    var searchCondition = $('#' + searchConditionId).val();
    //<2>.得到模糊匹配搜索条件的节点数组集合
    var highlightNodes = new Array();
    if (searchCondition != "") {
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        highlightNodes = treeObj.getNodesByParamFuzzy("name", searchCondition, null);
    }
    //<3>.高亮显示并展示【指定节点s】
    highlightAndExpand_ztree(treeId, highlightNodes);
}
function highlightAndExpand_ztree(treeId, highlightNodes){
    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    //<1>. 先把全部节点更新为普通样式
    var treeNodes = treeObj.transformToArray(treeObj.getNodes());
    for (var i = 0; i < treeNodes.length; i++) {
        treeNodes[i].highlight = false;
        treeObj.updateNode(treeNodes[i]);
    }
    //<2>.收起树, 只展开根节点下的一级节点
    close_ztree(treeId);
    //<3>.把指定节点的样式更新为高亮显示，并展开
    if (highlightNodes != null) {
        for (var i = 0; i < highlightNodes.length; i++) {
            //高亮显示节点，并展开
            highlightNodes[i].highlight = true;
            treeObj.updateNode(highlightNodes[i]);
            //高亮显示节点的父节点的父节点....直到根节点，并展示
            var parentNode = highlightNodes[i].getParentNode();
            var parentNodes = getParentNodes_ztree(treeId, parentNode);
            treeObj.expandNode(parentNodes, true, false, true);
            treeObj.expandNode(parentNode, true, false, true);
        }
    }
}
function getParentNodes_ztree(treeId, node){
    if (node != null) {
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        var parentNode = node.getParentNode();
        return getParentNodes_ztree(treeId, parentNode);
    } else {
        return node;
    }
}

//  function expand_ztree(treeId){
//      var treeObj = $.fn.zTree.getZTreeObj(treeId);
//      treeObj.expandAll(true);
//  }

/**
 * 收起树：只展开根节点下的一级节点
 * @param treeId
 */
function close_ztree(treeId){
    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    var nodes = treeObj.transformToArray(treeObj.getNodes());
    var nodeLength = nodes.length;
    for (var i = 0; i < nodeLength; i++) {
        if (nodes[i].parentTId  == null) {
            //根节点：展开
            treeObj.expandNode(nodes[i], true, true, false);
        } else {
            //非根节点：收起
            treeObj.expandNode(nodes[i], false, true, false);
        }
    }
}

function isBlank(str) {
    if (!str) {
        return true;
    }
    if (jQuery.trim(str) == '') {
        return true;
    }
    return false;
}