/**
 * Created by 许晓东 on 2020-04-25.
 */
var _idSort = function(a, b) {
    var an = Number(a), bn = Number(b);
    if (isNaN(an) && !isNaN(bn)) {
        return 1;
    } else if (!isNaN(an) && isNaN(bn)) {
        return -1;
    } else {
        return ((an < bn) ? -1 : ((an > bn) ? 1 : 0));
    }
}

jQuery.extend( jQuery.fn.dataTableExt.oSort, {
    "id_sort-asc": function (a, b) {
        return _idSort(a, b);
    },
    "id_sort-desc": function (a, b) {
        return _idSort(a, b) * -1;
    }
});