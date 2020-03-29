$(function () {
    $('#vip-user-list').DataTable({
        'paging': true,
        'lengthChange': false,
        'searching': true,
        'ordering': false,
        'info': true,
        'autoWidth': false,
        "ajax": {
            "url": 'users/vip/all'
        },
        "columns": [
            {
                "title": "卡号",
                "data": "id",
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
                "title": "操作信息",
                "data": "money",
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


