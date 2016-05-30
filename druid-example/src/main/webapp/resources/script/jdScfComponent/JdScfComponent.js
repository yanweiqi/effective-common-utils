/**
 * 系统组件管理
 *
 * Created by Silence<me@chenzhiguo.cn> on 11/19/15.
 */
function getTreeNode(id) {
    window.location.href = '/component/getComponentList.action?id=' + id;
    //$.ajax({
    //    url: "/component/getComponentById.action",
    //    data: "id=" + id,
    //    cache: false,
    //    success: function (component) {
    //        console.log(component);
    //        $("#id").val(component.id);
    //        $("#componentName").val(component.componentName);
    //        $("#code").val(component.code);
    //        $("#componentValue").val(component.componentValue);
    //        $("#componentType").val(component.componentType);
    //        $("#componentSort").val(component.componentSort);
    //        $("#isDisplay").val(component.isDisplay);
    //        $("#parentCode").val(component.parentCode);
    //        $("#remark").val(component.remark);
    //        $("#isDefualt").val(component.isDefualt);
    //        //if (component.createTime != null) {
    //        //    $("#createTime").val((new Date(component.createTime).Format("yyyy-MM-dd hh:mm:ss")));
    //        //} else {
    //        //    $("#createTime").val("");
    //        //}
    //        //if (component.updateTime != null) {
    //        //    $("#updateTime").val((new Date(component.updateTime).Format("yyyy-MM-dd hh:mm:ss")));
    //        //} else {
    //        //    $("#updateTime").val("");
    //        //}
    //    }
    //});
}

/**
 * 准备增加组件DIV
 */
function toAddComponent() {
    //Select Parent Auto
    if ($("#componentType").val() == 2) {
        $("#parentCodeNew").val($("#parentCode").val());
    } else {
        $("#parentCodeNew").val($("#code").val());
    }
}

/**
 * 更新操作响应事件
 */
function updateComponent() {
    if ($("#id").val() != '') {
        var validator = $("#modifyComponentForm").data("bs.validator");
        validator.validate();
        if (!validator.hasErrors()) {
            $.ajax({
                type: "POST",
                url: "/component/modifyJdScfComponent.action",
                data: $("#modifyComponentForm").serialize(),
                error: function () {
                    //BootstrapDialog.warning('操作失败!');
                    $.notify({title: "警告", message: "操作失败！"}, {type: 'warning'});
                },
                success: function (response) {
                    $.notify({title: "提示", message: "组件修改成功！"}, {type: 'success'});
                    //BootstrapDialog.notify("组件修改成功!");
                }
            });
        }
    } else {
        $.notify({title: "警告", message: "未选择任何组件！"}, {type: 'warning'});
        //BootstrapDialog.notify('未选择任何组件!', BootstrapDialog.TYPE_WARNING);
    }
}

/**
 * 删除操作响应事件
 */
function deleteNode() {
    if ($("#id").val() == '') {
        $.notify({title: "警告", message: "未选择任何组件！"}, {type: 'warning'});
        //BootstrapDialog.warning('未选择任何组件!');
        return false;
    }
    var baseParamCodeInputs = $('input[name="baseParamCode"]:hidden');

    var hasBaseParam = false;
    for (var i in baseParamCodeInputs) {
        if (baseParamCodeInputs[i].tagName != "INPUT") continue;
        hasBaseParam = true;
    }
    if (hasBaseParam) {
        $.notify({title: "警告", message: "该组件存在关联参数！如要删除，请先删除关联参数！"}, {type: 'warning'});
        return false;
    }
    //var children = $.fn.zTree.getZTreeObj("tree").getSelectedNodes()[0].children;
    var children = $.fn.zTree.getZTreeObj("tree").getNodeByParam("id", $("#code").val()).children;
    if (children != null && children.length != 0) {
        $.notify({title: "警告", message: "存在子节点(组件)，不能删除！"}, {type: 'warning'});
        //BootstrapDialog.notify("存在节点，不能删除！", BootstrapDialog.TYPE_WARNING);
    } else {
        BootstrapDialog.confirm('你确定要删除该组件吗?', function (result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: "/component/deleteJdScfComponent.action",
                    data: "id=" + $('#id').val(),
                    error: function () {
                        BootstrapDialog.alert('操作失败!');
                    },
                    success: function (response) {
                        BootstrapDialog.show({
                            type: BootstrapDialog.TYPE_SUCCESS,
                            title: '信息提示',
                            message: '组件删除成功!',
                            buttons: [{
                                label: '确定',
                                action: function (dialog) {
                                    dialog.close();
                                    window.location.reload();
                                }
                            }]
                        });
                    }
                });
            }
        });
    }
}

/**
 * 保存新组件事件
 */
function saveNewNode() {
    var validator = $("#componentForm").data("bs.validator");
    validator.validate();
    if (!validator.hasErrors()){
        $.ajax({
            type: "POST",
            url: "/component/saveJdScfComponent.action",
            data: {
                "code": $('#codeNew').val(),
                "componentName": $('#componentNameNew').val(),
                "componentValue": $('#componentValueNew').val(),
                "componentSort": $('#componentSortNew').val(),
                "parentCode": $('#parentCodeNew').val(),
                "componentType": $('#componentTypeNew').val(),
                "isDisplay": $('#isDisplayNew').val(),
                "isDefault": $('#isDefaultNew').val(),
                "remark": $('#remarkNew').val()
            },
            error: function () {
                BootstrapDialog.warning('操作失败!');
            },
            success: function (response) {
                if (response.success) {
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_SUCCESS,
                        message: '组件添加成功!',
                        onshown: function (dialogRef) {
                            setTimeout(function () {
                                dialogRef.close();
                            }, 1500);
                        },
                        onhidden: function (dialogRef) {
                            //dialogRef.close();
                            window.location.reload();
                        }
                    });
                } else {
                    BootstrapDialog.warning(response.message);
                }
            }
        });
    } else{

    }
}

/**
 * 添加组件响应事件(获取参数树)
 */
function toAddParam() {
    if ($("#componentType").val() != 2 && $("#id").val() == '') {
        //$.notify({title: "警告", message: "组件类型不合法或未选择任意组件,无法添加参数!"}, {type: 'warning'});
        BootstrapDialog.notify('组件类型不合法或未选择任意组件,无法添加参数!', BootstrapDialog.TYPE_WARNING);
        return false;
    } else {
        $.ajax({
            method: "POST",
            cache: false,
            url: "/baseParam/getBaseParamList.action",
            //data: { name: "John", location: "Boston"},
            success: function (data, textStatus) {
                var setting = {
                    check: {
                        enable: true
                    },
                    view: {
                        selectedMulti: true
                    },
                    data: {
                        keep: {
                            parent: true,
                            leaf: true
                        },
                        simpleData: {
                            enable: true
                        }
                    },
                    callback: {
                        onClick: onClick
                    }
                };

                //点击响应事件
                function onClick(event, treeId, treeNode, clickFlag) {
                    //getTreeNode(treeNode.dbID);
                }

                var zNodes = [];
                var baseParamCodeInputs = $('input[name="baseParamCode"]:hidden');

                var baseParamCodeArray = [];
                for (var i in baseParamCodeInputs) {
                    if (baseParamCodeInputs[i].tagName != "INPUT") continue;
                    baseParamCodeArray.push(baseParamCodeInputs[i].value);
                }
                for (var i in data) {
                    if (baseParamCodeArray.join(",").indexOf(data[i].code) >= 0) {
                        var temp = {
                            id: data[i].code,
                            pId: data[i].parentCode,
                            name: data[i].paramName,
                            dbID: data[i].id,
                            open: true,
                            nocheck: true
                        };
                    } else {
                        var temp = {
                            id: data[i].code,
                            pId: data[i].parentCode,
                            name: data[i].paramName,
                            dbID: data[i].id,
                            open: true
                        };
                    }
                    zNodes.push(temp);
                }
                $.fn.zTree.init($("#baseParamTree"), setting, zNodes);
            },
            error: function () {
                BootstrapDialog.notify('访问请求失败!', BootstrapDialog.TYPE_WARNING);
            }
        });
    }
}

/**
 * 添加参数响应事件
 */
function saveParams() {
    var nodes = $.fn.zTree.getZTreeObj("baseParamTree").getCheckedNodes(true);
    var baseParamIds = [];
    nodes.forEach(function (node) {
        if (!node.isParent) {
            baseParamIds.push(node.dbID);
        }
    });
    $.ajax({
        type: "POST",
        url: "/component/saveJdScfComponentParams.action",
        data: {
            ids: baseParamIds.join(","),
            componentId: $("#id").val(),
            componentCode: $("#code").val(),
            componentName: $("#componentName").val()
        },
        error: function () {
            BootstrapDialog.warning('操作失败!');
        },
        success: function (response) {
            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_SUCCESS,
                title: '信息提示',
                message: '参数保存成功,接下来可修改参数默认值!',
                buttons: [{
                    label: '确定',
                    action: function (dialog) {
                        dialog.close();
                        window.location.reload();
                    }
                }]
            });
        }
    });
}

/**
 * CheckBox全选/取消全选
 */
function checkAll(me) {
    //console.log($('input[name="componentParamCheckbox"]'));
    //$('input[name="componentParamCheckbox"]').attr("checked", me.checked);
    $("input[name='componentParamCheckbox']:checkbox").each(function () {
        $(this).attr("checked", me.checked);
    })
}


/**
 * 删除参数响应事件
 */
function deleteParams() {
    var checked = [];
    $('input:checkbox:checked').each(function () {
        checked.push($(this).val());
    });
    if (checked.length != 0) {
        BootstrapDialog.confirm('你确定要删除选中参数吗?', function (result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: "/component/deleteJdScfComponentParams.action",
                    data: {ids: checked.join(",")},
                    error: function () {
                        BootstrapDialog.warning('操作失败!');
                    },
                    success: function (response) {
                        BootstrapDialog.show({
                            type: BootstrapDialog.TYPE_SUCCESS,
                            title: '信息提示',
                            message: '参数删除成功!',
                            buttons: [{
                                label: '确定',
                                action: function (dialog) {
                                    dialog.close();
                                    window.location.reload();
                                }
                            }]
                        });
                    }
                });
            }
        });
    } else {
        $.notify({title: "警告", message: "未选中要删除的任意参数！"}, {type: 'warning'});
        //BootstrapDialog.notify("未选中要删除的任意参数！", BootstrapDialog.TYPE_WARNING);
    }
}

/**
 * 打开修改参数界面
 * @param id
 */
function toUpdateComponentParam(id) {
    $.ajax({
        url: "/component/getComponentParamById.action",
        data: "id=" + id,
        cache: false,
        success: function (componentParam) {
            console.log(componentParam);
            $("#componentParamId").val(componentParam.id);
            $("#paramCode").val(componentParam.paramCode);
            $("#paramName").val(componentParam.paramName);
            $("#paramValue").val(componentParam.value);
        }
    });
}

/**
 * 修改参数值响应事件
 */
function modifyBaseParam() {
    var validator = $("#modifyBaseParamForm").data("bs.validator");
    validator.validate();
    if (!validator.hasErrors()){
        $.ajax({
            type: "POST",
            url: "/component/modifyJdScfComponentParam.action",
            data: {
                "id": $("#componentParamId").val(),
                "value": $("#paramValue").val()
            },
            error: function () {
                BootstrapDialog.warning('操作失败!');
            },
            success: function (response) {
                if (response.success) {
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_SUCCESS,
                        message: '参数值修改成功!',
                        onshown: function (dialogRef) {
                            setTimeout(function () {
                                dialogRef.close();
                            }, 1500);
                        },
                        onhidden: function (dialogRef) {
                            //dialogRef.close();
                            window.location.reload();
                        }
                    });
                } else {
                    BootstrapDialog.warning(response.message);
                }
            }
        });
    }
}

/**
 * 树节点模糊查询
 */
function searchComponent(){
    var componentTree = $.fn.zTree.getZTreeObj("tree");
    //先清理
    var zNodes = componentTree.transformToArray(componentTree.getNodes());
    for (var i = 0, l = zNodes.length; i < l; i++) {
        zNodes[i].highlight = false;
        componentTree.updateNode(zNodes[i]);
    }
    //后高亮
    var nodeList = componentTree.getNodesByParamFuzzy("name", $('#componentSearchText').val());
    for( var i=0, l=nodeList.length; i<l; i++) {
        nodeList[i].highlight = true;
        componentTree.updateNode(nodeList[i]);
    }
}

//function validateSaveNewNode() {
//    if (isBlank($('#componentNameNew').val())) {
//        BootstrapDialog.warning("组件名称不能为空！");
//        return false;
//    }
//    if (isBlank($('#codeNew').val())) {
//        BootstrapDialog.warning("组件编码不能为空！");
//        return false;
//    }
//    if (isBlank($('#componentValueNew').val())) {
//        BootstrapDialog.warning("组件值不能为空！");
//        return false;
//    }
//    if (isBlank($('#componentSortNew').val())) {
//        BootstrapDialog.warning("组件排序不能为空！");
//        return false;
//    }
//    var reg = /^[1-9][0-9]*(\.0)?$/;
//    if (!reg.test($('#componentSortNew').val())) {
//        BootstrapDialog.warning("组件排序只能为正整数！");
//        return false;
//    }
//    return true;
//}

/**
 * 判断字符串是否为空
 * @param str 要判断的字符串
 * @return {boolean}
 */
//function isBlank(str) {
//    if (!str) {
//        return true;
//    }
//    if (jQuery.trim(str) == '') {
//        return true;
//    }
//    return false;
//}