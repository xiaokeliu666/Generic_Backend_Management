let tableIns;
let tableInsOnLine;
let tree;
layui.use(['element', 'form', 'table', 'layer', 'laydate', 'tree', 'util'], function () {
    let table = layui.table;
    let form = layui.form;
    let element = layui.element;
    let laydate = layui.laydate;
    tree = layui.tree;
    let height = document.documentElement.clientHeight - 160;

    // User List
    tableIns = table.render({
        elem: '#userTable'
        , url: ctx + '/sys/sysUser/page'
        , method: 'POST'
        , request: {
            pageName: 'page' // filed name of page, default：page
            , limitName: 'rows' // filed name of the data amount of each page, default: limit
        }
        , response: {
            statusName: 'flag' //data status, by default: code
            , statusCode: true // Success status by default:0
            , msgName: 'msg'
            , countName: 'records'
            , dataName: 'rows'
        }
        // Data processing after response
        , parseData: function (res) { //res
            var data = res.data;
            return {
                "flag": res.flag, // interface status
                "msg": res.msg, // parse text
                "records": data.records, // data length
                "rows": data.rows // datalist
            };
        }
        , toolbar: '#userTableToolbarDemo'
        , title: 'User List'
        , cols: [[
            {field: 'userId', title: 'ID', hide: true}
            , {field: 'userName', title: 'User Name'}
            , {field: 'loginName', title: 'Login Name'}
            , {field: 'valid', title: 'Valid', hide: true}
            , {field: 'limitMultiLogin', title: 'Multi Login', hide: true}
            , {field: 'limitedIp', title: 'Banned IP', hide: true}
            , {field: 'expiredTime', title: 'Expired Time', hide: true}
            , {field: 'lastChangePwdTime', title: 'Last Change Password Time', hide: true}
            , {field: 'createTime', title: 'Create Time', hide: true}
            , {field: 'updateTime', title: 'Update Time', hide: true}
            , {fixed: 'right', title: 'Action', toolbar: '#userTableBarDemo'}
        ]]
        , defaultToolbar: ['', '', '']
        , page: true
        , height: height
        , cellMinWidth: 80
    });

    // Current online
    tableInsOnLine = table.render({
        elem: '#userOnLineTable'
        , url: ctx + '/sys/sysUser/pageOnLine'
        , method: 'POST'
        // Request parameter process
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

        , parseData: function (res) { //res
            var data = res.data;
            return {
                "flag": res.flag,
                "msg": res.msg,
                "records": data.records,
                "rows": data.rows
            };
        }
        , toolbar: '#userOnLineTableToolbarDemo'
        , title: 'Current Online User'
        , cols: [[
            {field: 'loginName', title: '', hide: true}
            , {field: 'loginName', title: 'Login Name'}
            , {fixed: 'right', title: 'Action', toolbar: '#userOnLineTableBarDemo'}
        ]]
        , defaultToolbar: ['', '', '']
        , height: height
        , cellMinWidth: 80
    });

    //toolbar event
    table.on('toolbar(test)', function (obj) {
        switch (obj.event) {
            case 'addData':
                //reset action form
                $("#userForm")[0].reset();
                let nowTime = commonUtil.getNowTime();
                $("input[name='createTime']").val(nowTime);
                $("input[name='updateTime']").val(nowTime);
                $("input[name='lastChangePwdTime']").val(nowTime);
                form.render();
                loadMenuTree();
                loadAuthorityTree();
                layer.msg("please fill out the form on the right and save！");
                break;
            case 'query':
                let queryByLoginName = $("#queryByLoginName").val();
                let query = {
                    page: {
                        curr: 1 // from page 1
                    }
                    , done: function (res, curr, count) {
                        // this.where = {};
                    }
                };
                if (!queryByLoginName) {
                    queryByLoginName = "";
                }

                query.where = {loginName: queryByLoginName};
                tableIns.reload(query);
                $("#queryByLoginName").val(queryByLoginName);
                break;
            case 'reload':
                tableInsOnLine.reload();
                break;
        }
    });

    // listener
    table.on('tool(test)', function (obj) {
        let data = obj.data;
        // delete
        if (obj.event === 'del') {
            layer.confirm('Delete?', function (index) {
                // send delete request to the server
                $.delete(ctx + "/sys/sysUser/delete/" + data.userId, {}, function (data) {
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }
        // edit
        else if (obj.event === 'edit') {
            // showform
            $("#userForm").form(data);
            form.render();
            loadMenuTree();
            loadAuthorityTree();
        }
        // offline
        else if (obj.event === 'forced') {
            layer.confirm('Force this user to offline?', function (index) {
                // send delete request to server
                $.delete(ctx + "/sys/sysUser/forced/" + data.loginName, {}, function (data) {
                    tableInsOnLine.reload();
                    layer.close(index);
                })
            });
        }
    });

    // date select
    laydate.render({
        elem: '#expiredTimeDate',
        format: "yyyy-MM-dd HH:mm:ss"
    });
});

/**
 * Submit and save
 */
function userFormSave() {
    let userForm = $("#userForm").serializeObject();
    userForm.updateTime = commonUtil.getNowTime();
    $.post(ctx + "/sys/sysUser/save", userForm, function (data) {
        let menuIdList = [];
        for (let check of tree.getChecked('userMenuTree')[0].children) {
            menuIdList.push(check.id);
            if (check.children && check.children.length > 0) {
                for (let check1 of check.children) {
                    menuIdList.push(check1.id);
                }
            }
        }
        let postData = {
            userId: data.data.userId,
            menuIdList: menuIdList.join(",")
        };
        $.post(ctx + "/sys/sysUserMenu/saveAllByUserId", postData, function (data) {});

        let authorityIdList = [];
        for (let check of tree.getChecked('userAuthorityTree')[0].children) {
            authorityIdList.push(check.id);
        }
        let postData2 = {
            userId: data.data.userId,
            authorityIdList: authorityIdList.join(",")
        };
        $.post(ctx + "/sys/sysUserAuthority/saveAllByUserId", postData2, function (data) {});


        layer.msg("Success: Save", {icon: 1, time: 2000}, function () {});

        //update table、updateTime
        $("input[name='updateTime']").val(userForm.updateTime);
        tableIns.reload();
    });
}

/**
 * reset password
 */
function resetPassword() {
    let userForm = $("#userForm").serializeObject();
    if (userForm.userId === "") {
        layer.msg("Only existed user can reset password", {icon: 2, time: 2000}, function () {
        });
        return;
    }
    $.post(ctx + "/sys/sysUser/resetPassword", userForm, function (data) {
        if (data.flag) {
            layer.msg("Password Reset!", {icon: 1, time: 2000}, function () {
            });
        }
    });
}

/**
 * load user menu
 */
function loadMenuTree() {
    let userForm = $("#userForm").serializeObject();
    // get menu data
    $.post(ctx + "/sys/sysUserMenu/findUserMenuAndAllSysMenuByUserId", userForm, function (data) {
        // id - id, title - menuName, href - menuPath
        let treeData = commonUtil.updateKeyForLayuiTree(data.data.sysMenuVoList);


        treeData = commonUtil.checkedForLayuiTree(treeData, JSON.stringify(data.data.userSysMenuVoList));

        tree.render({
            elem: '#userMenuTree'
            , id: 'userMenuTree'
            , data: [{
                title: 'System Menu Root Node'
                , href: "/"
                , id: 0
                , spread: true
                , children: treeData
            }]
            , showCheckbox: true
        });
    });
}

/**
 * Load user Authority
 */
function loadAuthorityTree() {
    let userForm = $("#userForm").serializeObject();
    // Get the menu data
    $.post(ctx + "/sys/sysUserAuthority/findUserAuthorityAndAllSysAuthorityByUserId", userForm, function (data) {
        // id - id, title - menuName, href - menuPath
        let treeData = [];
        let userTreeString = JSON.stringify(data.data.sysUserAuthorityVoList);
        for (let authority of data.data.sysAuthorityVoList) {
            let tree = {
                title: authority.authorityName
                , id: authority.authorityId
                , spread: true
            };
            //echo user authority
            if(userTreeString.search(authority.authorityId) !== -1){
                tree.checked = true;
            }
            treeData.push(tree);
        }


        tree.render({
            elem: '#userAuthorityTree'
            , id: 'userAuthorityTree'
            , data: [{
                title: 'System Authority Root Node'
                , href: "/"
                , id: 0
                , spread: true
                , children: treeData
            }]
            , showCheckbox: true
        });
    });
}