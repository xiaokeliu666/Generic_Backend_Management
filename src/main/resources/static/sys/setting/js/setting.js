let sysNoticeTextEdit;
layui.use(['colorpicker','form'], function () {
    let form = layui.form;
    let colorpicker = layui.colorpicker;

    // start all the functions
    colorpicker.render({
        elem: '#test-form-sysColor'
        ,color: $('#sysColor').val()
        ,format: 'rgb'
        ,predefine: true
        ,alpha: true
        ,done: function(color){
            $('#sysColor').val(color);
        }
        ,change: function(color){
            // set theme color
            $('.header-demo,.layui-side .layui-nav').css('background-color', color);
        }
    });

    // Build editor
    sysNoticeTextEdit = UE.getEditor('sysNoticeTextEdit');
    // show the content
    sysNoticeTextEdit.ready(function() {
        sysNoticeTextEdit.setContent($("#sysNoticeText").val());
    });

    //radio checkbox
    $("#sysForm").find("[name='sysApiEncrypt'][value='" + $("#sysApiEncrypt").val() + "']").attr("checked", true);
    form.render();
});

/**
 * Save and Submit
 */
function sysFormSave() {
    let serializeObject = $("#sysForm").serializeObject();
    // get the edited content
    serializeObject.sysNoticeText = sysNoticeTextEdit.getContent();
    $.post(ctx + "/sys/sysSetting/save", serializeObject, function (data) {
        layer.msg("Update Successfully", {icon: 1, time: 2000}, function () {});
        $("#sysForm").form(data.data);
        $("#sysApiEncrypt").val(data.data.sysApiEncrypt)
    });
}