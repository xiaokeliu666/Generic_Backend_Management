let tableIns;
let tree;
layui.use(['element', 'form', 'table', 'layer', 'laydate','tree', 'util'], function () {
    let table = layui.table;
    let form = layui.form;
    let element = layui.element;
    let laydate = layui.laydate;
    tree = layui.tree;
    let height = document.documentElement.clientHeight - 160;

    tableIns = table.render({
        elem: '#authorityTable'
        , url: ctx + '/sys/sysAuthority/page'
        , method: 'POST'

        , request: {
            pageName: 'page'
            , limitName: 'rows'
        }
        , response: {
            statusName: 'flag'
            , statusCode: true
            , msgName: 'msg'
            , countName: 'records'
            , dataName: 'rows'
        }

        , parseData: function (res) {
            var data = res.data;
            return {
                "flag": res.flag,
                "msg": res.msg,
                "records": data.records,
                "rows": data.rows
            };
        }
        , toolbar: '#authorityTableToolbarDemo'
        , title: 'User List'
        , cols: [[
            {field: 'authorityId', title: 'ID', hide: true}
            , {field: 'authorityName', title: 'Authority Name'}
            , {field: 'authorityContent', title: 'Authority Content'}
            , {field: 'authorityRemark', title: 'Authority Description'}
            , {fixed: 'right', title: 'Action', toolbar: '#authorityTableBarDemo'}
        ]]
        , defaultToolbar: ['', '', '']
        , page: true
        , height: height
        , cellMinWidth: 80
    });


    table.on('toolbar(test)', function (obj) {
        switch (obj.event) {
            case 'addData':

                $("#authorityForm")[0].reset();
                form.render();
                layer.msg("Pleas fill and save the form on the right！");
                break;
            case 'query':
                let queryByAuthorityName = $("#queryByAuthorityName").val();
                let query = {
                    page: {
                        curr: 1
                    }
                    , done: function (res, curr, count) {

                        // reset where, otherwise next request will bring the old data
                        this.where = {};
                    }
                };
                if (queryByAuthorityName) {

                    query.where = {authorityName: queryByAuthorityName};
                }
                tableIns.reload(query);
                $("#queryByAuthorityName").val(queryByAuthorityName);
                break;
        }
    });

    // Listener
    table.on('tool(test)', function (obj) {
        let data = obj.data;
        // Delete
        if (obj.event === 'del') {
            layer.confirm('Are you sure you want to delete?', function (index) {
                // Sedn delete request to server
                $.delete(ctx + "/sys/sysAuthority/delete/" + data.authorityId, {}, function (data) {
                    obj.del();
                    layer.close(index);
                })
            });
        }
        // Edit
        else if (obj.event === 'edit') {
            // show form
            $("#authorityForm").form(data);
            form.render();
        }
    });
});

/**
 * save and submit
 */
function authorityFormSave() {
    let authorityForm = $("#authorityForm").serializeObject();
    $.post(ctx + "/sys/sysAuthority/save", authorityForm, function (data) {
        layer.msg("Save Successfully", {icon: 1,time: 2000}, function () {});
        tableIns.reload();
    });
}