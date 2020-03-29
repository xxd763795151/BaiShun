$(function () {
    //toastr

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "custom-toast-top-center",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }

    $('#vip-user-list').DataTable({
        'paging': true,
        'lengthChange': false,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false,
        "ajax": {
            "url": 'users/vip/all',
            "dataSrc": function (json) {
                for (var i = 0, ien = json.data.length; i < ien; i++) {
                    var data = json.data[i];
                    var rechargeBtn = "recharge" + data.id;
                    var deductionBtn = "deduction" + data.id;
                    var modifyBtn = "modify" + data.id;
                    data.op = "<button type=\"button\" class=\"btn  btn-sm   btn-primary\"" + " id=" + deductionBtn + " >扣费</button>  " +
                        " <button type=\"button\" class=\"btn  btn-sm  btn-success\"" + " id=" + rechargeBtn + " >充值</button>" +
                        " <button type=\"button\" class=\"btn  btn-sm  btn-warning\"" + " id=" + modifyBtn + " >修改信息</button>";
                    var recharge = function () {
                        $('#vip_user_recharge_modal').modal({
                            keyboard: false
                        })
                        $('#vip_user_recharge_modal form span[name="recharge_name"]').text(this.name);
                        $('#vip_user_recharge_modal form span[name="recharge_id"]').text(this.id);
                        $('#vip_user_recharge_modal form span[name="recharge_current_remain_money"]').text(this.money);
                        $('#vip_user_recharge_modal form #recharge_money').val(0.00);
                    }
                    $('#vip-user-list').on('click', 'tbody #' + rechargeBtn, recharge.bind(data))
                    var deduction = function () {
                        $('#vip_user_deduction_modal').modal({
                            keyboard: false
                        })
                        $('#vip_user_deduction_modal form span[name="deduction_name"]').text(this.name);
                        $('#vip_user_deduction_modal form span[name="deduction_id"]').text(this.id);
                        $('#vip_user_deduction_modal form span[name="deduction_current_remain_money"]').text(this.money);
                        $('#vip_user_deduction_modal form #deduction_money').val(0.00);
                    }
                    $('#vip-user-list').on('click', 'tbody #' + deductionBtn, deduction.bind(data))

                    var modify = function () {
                        $('#modify_vip_user_modal').modal({
                            keyboard: false
                        })
                        $('#modify_vip_user_modal form span[name="id"]').text(this.id);
                        $('#modify_vip_user_modal form input[name="name"]').val(this.name);
                        $('#modify_vip_user_modal form input[name="tel"]').val(this.tel);

                    }
                    $('#vip-user-list').on('click', 'tbody #' + modifyBtn, modify.bind(data))
                }
                return json.data;
            }
        },
        "columns": [
            {
                "title": "卡号",
                "data": "id",
                "bSearchable": true
            },
            {
                "title": "姓名",
                "data": "name",
                "bSearchable": true
            },
            {
                "title": "手机号",
                "data": "tel",
                "bSearchable": true
            },
            {
                "title": "余额",
                "data": "money",
                "bSearchable": false
            },
            {
                "title": "更新日期",
                "data": "updateTime",
                "bSearchable": false
            },
            {
                "title": "操作",
                "data": "op",
                "bSearchable": false
            }
        ],
        "stripeClasses": ['name', 'id', 'tel'],
        "language": {
            "paginate": {
                "previous": "上一页",
                "next": "下一页",
                "infoEmpty": "没有数据",
                "first": "第一页",
                "last": "最后一页"
            },
            "zeroRecords": "没有匹配信息",
            "loadingRecords": "请等待 - 加载中...",
            "sInfo": "显示第 _START_ 到 _END_ 条， 共 _TOTAL_ 条",
            "search": "搜索："
        }
    })
})

function dataTableRefresh() {
    $('#vip-user-list').dataTable()._fnAjaxUpdate();
    $('#vip-user-list_filter input[type="search"]').val('');
}


$('#add_vip_user_modal').on('hide.bs.modal',
    function () {
        $('#add_vip_user_modal .form-group input').val('');
        $('#add_vip_user_modal .form-group .input_validator').css('display', 'none');
    })


function validateMoney(id) {
    var pass = nullValidate(id);
    if (!pass) {
        return false;
    }
    var isNum = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
    var val = $(id).val();
    pass = isNum.test(val);
    if (!pass) {
        $(id).parent('div').next('label').css('display', 'block');
    }
    return pass;
}

function addVip() {
    var pass = nullValidate('#name');
    pass &= nullValidate('#id');
    pass &= nullValidate('#tel');
    pass &= validateMoney('#money');
    if (pass) {
        var data = {
            'name': $('#name').val(),
            'id': $('#id').val(),
            'tel': $('#tel').val(),
            'money': $('#money').val()
        };
        $.ajax({
            'url': '/users/vip/save',
            'type': 'POST',
            'async': true,
            'data': JSON.stringify(data),
            'contentType': 'application/json',
            'dataType': 'json',
            'success': function (json) {
                if (json.rtnCode != 0) {
                    toastr.error(json.rtnMessage);
                    return false;
                }
                $("#add_vip_user_modal").modal('hide');
                toastr.success('新增会员信息成功');
                dataTableRefresh();
            },
            'error': function (ignore, errorMessage) {
                toastr.error('保存失败，错误消息：' + errorMessage);
            }
        });
    }
}

function updateVip() {
    var pass = nullValidate('#modify_vip_user_modal form input[name="name"]');
    pass &= nullValidate('#modify_vip_user_modal form input[name="tel"]');

    if (pass) {
        var id = $('#modify_vip_user_modal form span[name="id"]').text();
        var name = $('#modify_vip_user_modal form input[name="name"]').val();
        var tel = $('#modify_vip_user_modal form input[name="tel"]').val();

        var data = {
            'name': name,
            'id': id,
            'tel': tel
        };
        $.ajax({
            'url': '/users/vip/name/tel/update',
            'type': 'POST',
            'async': true,
            'data': JSON.stringify(data),
            'contentType': 'application/json',
            'dataType': 'json',
            'success': function (json) {
                if (json.rtnCode != 0) {
                    toastr.error(json.rtnMessage);
                    return false;
                }
                $("#modify_vip_user_modal").modal('hide');
                toastr.success('更新会员信息成功');
                dataTableRefresh();
            },
            'error': function (ignore, errorMessage) {
                toastr.error('保存失败，错误消息：' + errorMessage);
            }
        });
    }
}

function deductionMoney() {
    if (validateMoney('#vip_user_deduction_modal form #deduction_money')) {
        var id = $('#vip_user_deduction_modal form span[name="deduction_id"]').text();
        var money = $('#vip_user_deduction_modal form #deduction_money').val();
        $.ajax({
            'url': '/users/vip/money/update',
            'type': 'POST',
            'data': {"id": id, "money": money, "isRecharge": false},
            'dataType': 'json',
            'success': function (json) {
                if (json.rtnCode != 0) {
                    toastr.error(json.rtnMessage);
                } else {
                    $("#vip_user_deduction_modal").modal('hide');
                    toastr.success('扣费成功');
                    dataTableRefresh();
                }
            },
            'error': function (ignore, errorMessage) {
                toastr.error('扣费失败，错误消息：' + errorMessage);
            }
        });
    }
}

function rechargeMoney() {
    if (validateMoney('#vip_user_recharge_modal form #recharge_money')) {
        var id = $('#vip_user_recharge_modal form span[name="recharge_id"]').text();
        var money = $('#vip_user_recharge_modal form #recharge_money').val();
        $.ajax({
            'url': '/users/vip/money/update',
            'type': 'POST',
            'data': {"id": id, "money": money, "isRecharge": true},
            'dataType': 'json',
            'success': function (json) {
                if (json.rtnCode != 0) {
                    toastr.error(json.rtnMessage);
                    return false;
                }
                $("#vip_user_recharge_modal").modal('hide');
                toastr.success('充值成功');
                dataTableRefresh();
            },
            'error': function (ignore, errorMessage) {
                toastr.error('充值失败，错误消息：' + errorMessage);
            }
        });
    }
}

function hide_hint(id) {
    $(id).parent('div').next('label').css('display', 'none');

}

function nullValidate(id) {
    var name = $(id).val();
    if (name == undefined || name == '') {
        $(id).parent('div').next('label').css('display', 'block');
        return false;
    }
    return true;
}
