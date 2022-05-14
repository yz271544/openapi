
var apiUser = function(){

    var getUserGrpCode = function(){
        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/getUserGrpCode",
            "dataType" : "json",
            "success" : function(resp){
                var userGrp = eval(resp.userGrp);
                $('#userGrpName').empty();
                var selectStr = "";
                selectStr += "<option >请选择用户组</option>";
                for(var i=0;i<userGrp.length;i++){
                    selectStr += "<option value=\""+userGrp[i].id+"\">" + userGrp[i].name + "</option>";
                }
                $("#userGrpName").append(selectStr);
            }
        });
    }

    //加载API权限树
    var loadApiAuthTree = function(param){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getApiInfoAuthTree",
            "dataType" : "json",
            "data" :{
                grpId : param
            },
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                //使用jstree初始化tree
                //先销毁当前树对象
                $('#ApiAuthTree').jstree("destroy");
                $('#ApiAuthTree').empty().append("<ul>"+showResAuthTree(obj1)+"</ul>");
                $('#ApiAuthTree').jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "multiple": true
                    },
                    "types" : {
                        "default" : {
                            "icon" : "fa fa-folder icon-state-warning icon-lg"
                        },
                        "file" : {
                            "icon" : "fa fa-file icon-state-warning icon-lg"
                        }
                    },
                    "plugins": ["wholerow", "checkbox", "types","themes", "html_data", "ui"]
                });

            }
        });
    }

    var showResAuthTree = function (treeJsonStr) {

        var output = '';

        for(var node = 0;node <treeJsonStr.length;node++){

            if(treeJsonStr[node].childrens.length > 0){
                var childrenHtml = showResAuthTree(treeJsonStr[node].childrens);
                output += '<li data-jstree=\'{"type\" : \"file\" ,\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            }else{
                if(node == 0){
                    if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightFieldValue != -1){
                        output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true,\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\",\"disabled\": true }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }
                }else{
                    if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightFieldValue != -1){
                        output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true,\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\",\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }

                }
            }

        }

        return output;

    }



    return{
        init:function(){
            getUserGrpCode();
        },
        showLoadApiAuthTree:function(p){
            loadApiAuthTree(p);
        },
        //注册保存
        registSave:function () {

            var loginAcct = $('#loginAcct').val();
            var loginPwd = $('#submit_form_password').val();
            var rLoginPwd = $('#r_submit_form_password').val();
            var realUserName = $('#realUserName').val();
            var orgCode =  $("#orgCode").find("option:selected").val();
            var userGender = $("input[name='userGender']:checked").val();
            var userPhone = $('#userPhone').val();
            var  userMail = $('#userMail').val();
            var userGrpId =  $("#userGrpName").find("option:selected").val();

            if(loginPwd == rLoginPwd){
                $.ajax({
                    "type" :"post",
                    "url" : "/openapi/admin/apiDeveloper/registSave",
                    "data" :{
                        loginAcct:loginAcct,
                        loginPwd:loginPwd,
                        realUserName:realUserName,
                        orgCode:orgCode,
                        userGender:userGender,
                        userPhone:userPhone,
                        userMail:userMail,
                        userGrpId:userGrpId
                    },
                    "dataType" : "text",
                    "success" : function(resp){
                        alert(resp);
                        $('#form_wizard_1 .button-submit').hide();
                    }
                });
            }else{
                alert("两次密码输入不一致，请重新输入！");
            }

        }
    }

}();

jQuery(document).ready(function() {

    apiUser.init();

    //选择不同的用户组，查询对应的API权限信息
    $('#userGrpName').change(function(){
        var userGrpId = $(this).children('option:selected').val();
        apiUser.showLoadApiAuthTree(userGrpId);
    });


});