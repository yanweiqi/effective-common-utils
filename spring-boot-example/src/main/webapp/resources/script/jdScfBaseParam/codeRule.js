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
//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt)  { //author: meizz
	var o = {
	 "M+" : this.getMonth()+1,                 //月份
	 "d+" : this.getDate(),                    //日
	 "h+" : this.getHours(),                   //小时
	 "m+" : this.getMinutes(),                 //分
	 "s+" : this.getSeconds(),                 //秒
	 "q+" : Math.floor((this.getMonth()+3)/3), //季度
	 "S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt))
	 fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
	 if(new RegExp("("+ k +")").test(fmt))
	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	return fmt;
}
function validateSaveCode() {
    if (isBlank($('#paramName').val())) {
        alert('参数名称不能为空');
        return false;
    }
    if($("#paramValue").val() != ''){
        if($('#valueType').val() == 1){
            if(isNaN($('#paramValue').val())){
                alert('参数值应为数字类型');
                return false;
            }
            //return true;
        }
        if($('#valueType').val() == 2){
            if(!isNaN($('#paramValue').val())){
                alert('参数值应为字符类型');
                return false;
            }
            //return true;
        }
    }

    return true;
}

function validateSaveNewCode() {
    if (isBlank($('#paramName2').val())) {
        alert('参数名称不能为空');
        return false;
    }

    if($('#valueType2').val() == 1){
        if(isNaN($('#paramValue2').val())){
            alert('参数值应为数字类型');
            return false;
        }
        //return true;
    }
    if($('#valueType2').val() == 2){
        if(!isNaN($('#paramValue2').val())){
            alert('参数值应为字符类型');
            return false;
        }
        //return true;
    }
    return true;
}


/*
function queryByParamName(){
    var paramName = $("#paramName1").val();
    $.ajax({
        type: "POST",
        url: "/baseParam/queryByParamName.action",
        data: {"paramName":paramName},
        success:function(result){
            if(result.status){
                $("#code").val(result.dataValue.code);
                $("#paramName").val(result.dataValue.paramName);
                $("#paramValue").val(result.dataValue.paramValue);
                $("#paramType").val(result.dataValue.paramType);
                $("#valueType").val(result.dataValue.valueType);
                $("#isDisplay").val(result.dataValue.isDisplay);
                $("#isDefault").val(result.dataValue.isDefault);
                $("#paramSort").val(result.dataValue.paramSort);
                $("#createTime").val(result.dataValue.createTime);
                $("#updateTime").val(result.dataValue.updateTime);
                $("#remark").val(result.dataValue.remark);
            }else{
                alert(result.message);
            }

        }
    });
}

function saveCode() {
    if (validateSaveCode()) {
        $.ajax({
            type: "POST",
            url: "/baseParam/saveCode.action",
            data: $("#baseParamForm").serialize(),
            success: function(result){
                if(typeof result.message == "undefined"){
                    $('#detailInfo').html(result);
                }else{
                    alert(result.message);
                    if(result.status==1)window.location.reload();
                }
            }
        });
    }
}
function deleteCode() {
    var children = $.fn.zTree.getZTreeObj("tree").getSelectedNodes()[0].children;
    //alert($("#id").val());
    if(children !=null && children.length!=0){
        alert("存在节点，不能删除！");
    } else {
        var statu = confirm("确定删除？");
        if(statu){
            $.ajax({
                type: "POST",
                url: "#springUrl('')/baseParam/deleteCode.action",
                data: "id="+$('#id').val(),
                error: function () {
                    alert("操作失败");
                },
                success: function(msg){
                    if(typeof msg.message == "undefined"){
                        $('#detailInfo').html(msg);
                    }else{
                        alert(msg.message);
                        if(msg.status==1)window.location.reload();
                    }
                }
            });
        }

    }
}

function saveNewCode() {
    if (validateSaveNewCode()) {
        $.ajax({
            type: "POST",
            url: "/baseParam/saveCode.action",
            data: $("#newParamForm").serialize(),
            error: function () {
                alert("操作失败");
            },
            success: function(msg){
                if(typeof msg.message == "undefined"){
                    $('#detailInfo').html(msg);
                }else{
                    alert(msg.message);
                    if(msg.status==1)window.location.reload();
                }
            }
        });
    }
}

function getbaseParam(id) {
    $.ajax({
        url : "#springUrl('')/baseParam/getCode.action",
        data: "id="+id,
        cache: false,
        success : function(baseParam) {
            $("#id").val(baseParam.id);
            $("#paramName").val(baseParam.paramName);
            $("#code").val(baseParam.code);
            $("#paramValue").val(baseParam.paramValue);
            $("#paramType").val(baseParam.paramType);
            $("#paramSort").val(baseParam.paramSort);
            $("#valueType").val(baseParam.valueType);
            $("#isDisplay").val(baseParam.isDisplay);
            $("#parentCode").val(baseParam.parentCode);
            $("#remark").val(baseParam.remark);
            $("#parentCode2").val(baseParam.code);
            $("#isDisplay").val(baseParam.isDisplay);
            $("#isDefualt").val(baseParam.isDefualt);
            $("#createTime").val(baseParam.createTime);
            $("#updateTime").val(baseParam.updateTime);
        }
    });
}
*/
