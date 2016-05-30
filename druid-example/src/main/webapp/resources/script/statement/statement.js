/**
 * Description
 *
 * Created by Silence<me@chenzhiguo.cn> on 1/12/16.
 */
var statement = (function(){

    /**
     * 按分页号码获取数据
     *
     * @param pageNum
     */
    //var goPageFunc = function (page){
    //    var searcheDateFrom = null;
    //    var searchDateTo = null;
    //    if ($('#searchDateFrom').val()) {
    //        searcheDateFrom = new Date($('#searchDateFrom').val().replace(new RegExp("/", "gm"), "-") + ' 00:00:00');
    //    }
    //    if ($('#searchDateTo').val()) {
    //        searchDateTo = new Date($('#searchDateTo').val().replace(new RegExp("/", "gm"), "-") + ' 23:59:59');
    //    }
    //    $.getJSON('getLoanInfoByPage', {
    //        page: page,
    //        financingApplyTimeStart: searcheDateFrom,
    //        financingApplyTimeEnd: searchDateTo,
    //        status: $('#gylrzjlzt').val(),
    //        verifyStatus: $('#gylrzjlhkzt').val()
    //    }).done(function (data) {
    //        if (data.success) {
    //            vm.loanInfoList = data.loanInfoPage.dataList;
    //            vm2.loanInfoPage = data.loanInfoPage;
    //        } else {
    //            alert(data.message);
    //        }
    //    });
    //};

    /**
     * 重新生成账单响应事件
     */
    var createStatementAgainFunc = function(){
        var id = $('input:radio:checked').val();
        if (id != undefined) {
            BootstrapDialog.confirm('你确定要重新生成该账单吗?', function (result) {
                if (result) {
                    $.ajax({
                        type: "POST",
                        url: "/statement/createStatementAgain",
                        data: {id: id},
                        error: function () {
                            BootstrapDialog.warning('操作失败!');
                        },
                        success: function (response) {
                            if(response.success) {
                                BootstrapDialog.show({
                                    type: BootstrapDialog.TYPE_SUCCESS,
                                    title: '信息提示',
                                    message: '账单重新生成成功!',
                                    buttons: [{
                                        label: '确定',
                                        action: function (dialog) {
                                            dialog.close();
                                            window.location.reload();
                                        }
                                    }]
                                });
                            }else {
                                BootstrapDialog.show({
                                    type: BootstrapDialog.TYPE_DANGER,
                                    title: '信息提示',
                                    message: response.message,
                                    buttons: [{
                                        label: '确定',
                                        action: function (dialog) {
                                            dialog.close();
                                            window.location.reload();
                                        }
                                    }]
                                });
                            }
                        }
                    });
                }
            });
        } else {
            //$.notify({title: "警告", message: "未选中要任何账单！"}, {type: 'warning'});
            BootstrapDialog.notify("未选中要任何账单！", BootstrapDialog.TYPE_WARNING);
        }
    };

    return {
        //goPage : goPageFunc,
        createStatementAgain : createStatementAgainFunc
    }
})();
