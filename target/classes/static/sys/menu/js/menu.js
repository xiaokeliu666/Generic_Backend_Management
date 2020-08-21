let tree = {};
layui.use(['element', 'form', 'table', 'layer', 'tree', 'util'], function () {
    let form = layui.form;
    let element = layui.element;
    tree = layui.tree;

    // Get the data of form
    $.post(ctx + "/sys/sysMenu/listByTier", {}, function (data) {
        // id - id; title - menuName; href - menuPath
        let treeData = commonUtil.updateKeyForLayuiTree(data.data);

        // Turn on the node action
        tree.render({
            elem: '#menuTree'
            , id: 'menuTree'
            , data: [{
                title: 'System Menu Root Node'
                , href: "/"
                , id: "0"
                , spread: true
                , children: treeData
            }]
            , onlyIconControl: true
            , edit: ['add', 'del']
            // Click the node
            , click: function (obj) {
                // id - id; title - menuName; href - menuPath
                $("#menuForm").form({
                    menuId: obj.data.id,
                    menuName: obj.data.title,
                    menuPath: obj.data.href,
                    menuParentName: obj.elem.parent().parent().children(".layui-tree-entry").find(".layui-tree-txt").text(),
                    menuParentId: obj.elem.parent().parent().data("id"),
                    treeId: obj.data.id
                });
            }
            // Click the checkbox
            , oncheck: function (obj) {
                console.log(obj.data); // the data been clicked
                console.log(obj.checked); //current state of node：open、close、normal
                console.log(obj.elem); // current node's element
            }
            // Action
            , operate: function (obj) {
                let type = obj.type; // Action Type：add、edit、del
                let data = obj.data; // Data of node
                let elem = obj.elem; // element of node

                if (type === 'add') { // add node
                    $("#menuForm")[0].reset();
                    // return key
                    return "";
                } else if (type === 'del') { // delete node
                    layer.confirm('Are you sure you want to delete this menu? /n Note: Deleting parent node will also delete the child nodes?', function (index) {
                        $.delete(ctx + "/sys/sysMenu/delete/" + data.id,{}, function () {
                            layer.msg("Delete Successfully");
                            elem.remove();
                        });
                        layer.close(index);
                    });

                }
            }
        });
    });
});


/**
 * submit and save
 */
function menuFormSave() {
    var menuForm = $("#menuForm").serializeObject();
    if(menuForm.menuId === "0"){
        layer.msg("Root node cannot be operated", {icon: 2,time: 2000}, function () {});
        return;
    }
    if(menuForm.menuParentId === "0"){
        menuForm.menuParentId = "";
    }
    $.post(ctx + "/sys/sysMenu/save", menuForm, function (data) {
        layer.msg("Success: Saved!", {icon: 1,time: 2000}, function () {});

        // update tree element
        $("div[data-id='" + menuForm.treeId + "']").children(".layui-tree-entry").find(".layui-tree-txt").text(data.data.menuName);
        $("div[data-id='" + menuForm.treeId + "']").attr("data-id", data.data.menuId);
    });
}