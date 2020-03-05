$(function () {
    $('#vip-user-list').DataTable({
        'paging': true,
        'lengthChange': false,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false,
        "ajax": {
            "url": './data/vip_user_list.json',
            "dataSrc": function (json) {
                for (var i = 0, ien = json.data.length; i < ien; i++) {
                    var data = json.data[i];
                    var rechargeBtn = "recharge" + data.id;
                    var deductionBtn = "deduction" + data.id;
                    data.op = "<button type=\"button\" class=\"btn  btn-sm   btn-primary\"" + " id=" + rechargeBtn + " >扣费</button>   <button type=\"button\" class=\"btn  btn-sm  btn-yellow\"" + " id=" + deductionBtn + " >充值</button>";
                    var recharge = function () {
                        //console.log(this);
                    }
                    $('#vip-user-list').on('click', 'tbody #' + rechargeBtn, recharge.bind(data))
                    var deduction = function () {
                        //console.log(this);
                    }
                    $('#vip-user-list').on('click', 'tbody #' + deductionBtn, deduction.bind(data))
                }
                return json.data;
            }
        },
        "columns": [
            {
                "title": "姓名",
                "data": "name"
            },
            {
                "title": "卡号",
                "data": "id"
            },
            {
                "title": "手机号",
                "data": "tel"
            },
            {
                "title": "余额",
                "data": "money"
            },
            {
                "title": "更新日期",
                "data": "updateTime"
            },
            {
                "title": "操作",
                "data": "op"/*,
                    "render": function (data, type, full) {
                        var rechargeBtn = "recharge" + full.id;
                        var deductionBtn = "deduction" + full.id;
                        $('#vip-user-list ').undelegate('tbody #'+rechargeBtn, 'click');

                        $('#vip-user-list').on('click', 'tbody #'+rechargeBtn, function () {
                            console.log(full);
                        })
                        $('#vip-user-list ').undelegate('tbody #'+deductionBtn, 'click');
                        $('#vip-user-list').on('click', 'tbody #'+deductionBtn, function () {
                            console.log(full);

                        })

                        return "<button type=\"button\" class=\"btn  btn-sm   btn-primary\"" + " id=" + rechargeBtn + " >扣费</button>   <button type=\"button\" class=\"btn  btn-sm  btn-yellow\"" + " id=" + deductionBtn + " >充值</button>";

                    }*/
            }
        ]
    })
})

/*
function addVip() {
    //alert('ok');
}*/
