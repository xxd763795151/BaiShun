$(function () {
    $('#vip-user-list').DataTable({
        'paging': true,
        'lengthChange': false,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false,
        "ajax": {
            "url": 'user/info/log/list'
        },
        "columns": [
            {
                "title": "卡号",
                "data": "userId",
                "bSearchable": false
            },
            {
                "title": "姓名",
                "data": "name",
                "bSearchable": false
            },
            {
                "title": "手机号",
                "data": "tel",
                "bSearchable": true
            },
            {
                "title": "变更类型",
                "data": "type",
                "bSearchable": false,
                "render": function (data, type, full) {
                    var val = data;
                    switch (data) {
                        case 'create':
                            val = '新建用户';
                            break;
                        case 'recharge':
                            val = '充值';
                            break;
                        case 'deduction':
                            val = '扣费';
                            break;
                        case 'update':
                            val = '信息变更';
                            break;
                        default:
                            break;
                    }
                    return val;
                }
            },
            {
                "title": "更新日期",
                "data": "updateTime",
                "bSearchable": false
            },
            {
                "title": "操作信息",
                "data": "log",
                "bSearchable": false
            }
        ],
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
            "search": "搜索(只支持手机号)："
        }
    })
})

function dataTableRefresh() {
    $('#vip-user-list').dataTable()._fnAjaxUpdate();
    $('#vip-user-list_filter input[type="search"]').val('');
}


