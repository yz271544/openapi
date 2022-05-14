/**
 * Created by Evan on 2016/7/2.
 */

var userManaged = function () {

    var oTable;

    var queryUesrInfoTable = function (paraid,paraType) {

        //var userid = $("#userid").val();
        var loginAcct = $("#loginAcct").val();
        var userName = $("#userName").val();

        oTable = $('#sample_1');

        if (typeof oTable == 'undefined' && oTable != null){
            oTable.fnClearTable(0); //清空数据
            oTable.fnDraw(); //重新加载数据　
        }else{

            oTable.dataTable({

                "oLanguage": {
                    "sProcessing": '<i class="fa fa-coffee"></i>&nbsp;加载中，请耐心等待...',
                    "sLengthMenu": "每页显示 _MENU_条",
                    "sZeroRecords": "没有找到符合条件的数据",
                    "sInfo": "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
                    "sInfoEmpty": "没有记录",
                    "oPaginate":
                    {
                        "sFirst": "首页",
                        "sPrevious": "前一页",
                        "sNext": "后一页",
                        "sLast": "尾页"
                    }
                },

                "pagingType": "full_numbers",

                "lengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                //"pageLength": 5,
                "aLengthMenu" : [5, 20, 30],
                "iDisplayStart":0,
                "iDisplayLength" : 5,
                "bProcessing" : true,
                "bServerSide": true,
                "bPaginate": true,//是否显示分页器
                "bInfo": true,//是否显示表格的一些信息
                "bFilter":false,//是否启用客户端过滤器
                "bJQueryUI": true,//是否启用JQueryUI风格
                "bAutoWidth": true,
                "bDestroy": true,
                "bSort":false,
                "sPaginationType" : "full_numbers",
                "aoColumns":[
                     {'sTitle':'用户id',"sWidth": "20%",'sClass':'center',"bVisible":false}
                     ,{'sTitle':'用户账号',"sWidth": "20%",'sClass':'center'}
                    ,{'sTitle':'用户姓名',"sWidth": "20%",'sClass':'center'}
                    ,{'sTitle':'状态',"sWidth": "20%",'sClass':'center'}
                    ,{'sTitle':'操作',"sWidth": "40%",'sClass':'center'}

                ],
                "columnDefs": [{"aTargets":[4],"mRender":function(data,type,full){
                    return "<a href=\"javascript:userManaged.editedUserInfoDetailPage('"+full[0]+"');\" class=\"btn btn-outline btn-circle btn-sm purple\"> <i class=\"fa fa-edit\"></i> 编辑 </a>" +
                        "<a href=\"javascript:userManaged.removeUser('"+full[1]+"');\" class=\"btn btn-outline btn-circle dark btn-sm black\"><i class=\"fa fa-trash-o\"></i> 删除 </a>";
                }}],

                "sAjaxSource" : "/openapi/admin/auth/findUserInfoList",
                "fnServerData" : function(sSource,aoData,fnCallback){

                    aoData.push(
                        //{ "name": "userid", "value": userid },
                        { "name": "loginAcct", "value": loginAcct },
                        { "name": "userName", "value": userName },
                        { "name": "paraid", "value": paraid },
                        { "name": "paraType", "value": paraType }
                    );

                    $.ajax({
                        "type" :"post",
                        "url" : sSource,
                        "dataType" : "json",
                        "data": {
                            aoData: JSON.stringify(aoData)
                        },
                        "success" : function(resp){
                            fnCallback(resp);
                        }
                    });
                },
                "bSort":false
            });

            jQuery('#sample_1_wrapper .dataTables_length select').addClass("form-control input-xsmall");
        }

    }

    /**
     * 初始化用户组树
     */
    var initUserGrpTree = function(){
        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUserGrpTree",
            "dataType" : "json",
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                //alert(showAllTree(obj1));
                $("#tree_1").empty().append("<ul>"+showAllTree(obj1)+"</ul>");

                //使用jstree初始化tree
                $('#tree_1').jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        }
                    },
                    "types" : {
                        "default" : {
                            "icon" : "fa fa-folder icon-state-warning icon-lg"
                        },
                        "file" : {
                            "icon" : "fa fa-file icon-state-warning icon-lg"
                        }
                    },
                    "plugins": ["types"]
                });

                //给tree每个节点绑定点击事件
                $('#tree_1').on('select_node.jstree', function(e,data) {
                    var link = $('#' + data.selected).find('a').parent();
                    var nodeid = link.attr("nodeid");
                    queryUesrInfoTable(nodeid,1);
                })

            }
        });
    }


    /**
     * 通过json数据生成符合要求的树de html
     * @param treeJsonStr json格式的数据
     * @param htmlStr 要组合成的html的容器
     */
    var showAllTree = function (treeJsonStr) {

        var output = '';

        for(var node = 0;node <treeJsonStr.length;node++){
            if(treeJsonStr[node].childrens.length > 0){
                var childrenHtml = showAllTree(treeJsonStr[node].childrens);
                output += '<li data-jstree=\'{"opened\" : true }\' nodeid="'+treeJsonStr[node].id+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            }else{
                if(node == 0){
                    output += '<li nodeid="'+treeJsonStr[node].id+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }else{
                    output += '<li nodeid="'+treeJsonStr[node].id+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }
            }

        }

        return output;

        /*for(var node in treeJsonStr){
            //如果有子节点，则遍历该子节点
            if(treeJsonStr[node].childrens.length > 0){
                var listr = $("<li data-jstree='{\"type\":\"file\"}'></li>");
                //将li的文本设置好，并马上添加一个空白的ul子节点，并且将这个li添加到父亲节点中
                $(listr).append(treeJsonStr[node].name).append("<ul></ul>").appendTo(htmlStr);
                //将空白的ul作为下一个递归遍历的父亲节点传入
                //showAllTree(treeJsonStr[node].childrens,listr.children().eq(0));
                //alert(treeJsonStr[node].childrens.length)
                showAllTree(treeJsonStr[node].childrens.html(),$(listr).children().eq(0));
                //如果该节点没有子节点，则直接将该节点li以及文本创建好直接添加到父亲节点中

                //alert($(listr).children().eq(0).html())
            }else{
                $("<li data-jstree='{ \"type\" : \"file\" }'><a href=\"http://www.jstree.com\"></a></li>").append(treeJsonStr[node].name).appendTo(parent);
            }
        }*/
    }

    var loadUserGrpTree = function(param){
        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUserGrpTree",
            "dataType" : "json",
            "data":{
                userId : param
            },
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                var grpid = resp.gId;//返回的用户组id
                //alert(showAllTree(obj1));
                $('#userGrpTree').jstree("destroy");
                $("#userGrpTree").empty().append("<ul>"+showAllTreeWithGrp(obj1,grpid)+"</ul>");

                //使用jstree初始化tree
                $('#userGrpTree').jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        }
                    },
                    "types" : {
                        "default" : {
                            "icon" : "fa fa-folder icon-state-warning icon-lg"
                        },
                        "file" : {
                            "icon" : "fa fa-file icon-state-warning icon-lg"
                        }
                    },
                    "plugins": ["types"]
                });

                //给tree每个节点绑定点击事件
                $('#userGrpTree').on('select_node.jstree', function(e,data) {
                    var link = $('#' + data.selected).find('a').parent();
                    var nodeid = link.attr("nodeid");
                    //alert(nodeid);
                });



            }
        });
    }


    var showAllTreeWithGrp = function (treeJsonStr,p) {

        var output = '';

        for(var node = 0;node <treeJsonStr.length;node++){
            if(treeJsonStr[node].childrens.length > 0){
                var childrenHtml = showAllTreeWithGrp(treeJsonStr[node].childrens,p);
                if(treeJsonStr[node].id == p){

                    output += '<li data-jstree=\'{\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
                }else{
                    output += '<li nodeid="'+treeJsonStr[node].id+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
                }
            }else{
                if(treeJsonStr[node].id == p){
                    output += '<li data-jstree=\'{\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }else{
                    output += '<li nodeid="'+treeJsonStr[node].id+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }

            }

        }



        return output;
    }

    var showResAuthTree = function (treeJsonStr) {

        var output = '';

        for(var node = 0;node <treeJsonStr.length;node++){

            if(treeJsonStr[node].childrens.length > 0){
                var childrenHtml = showResAuthTree(treeJsonStr[node].childrens);
                output += '<li data-jstree=\'{"opened\" : true }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            }else{
                if(node == 0){
                    if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'group'){
                        output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true,\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'user'){
                        output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\" }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }
                }else{
                    if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'group'){
                        output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true,\"disabled\": true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else if(treeJsonStr[node].rightFieldValue != null && treeJsonStr[node].rightFieldValue == treeJsonStr[node].id && treeJsonStr[node].rightMain == 'user'){
                        output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\"}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'" nodeBelong="'+treeJsonStr[node].rightMain+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }

                }
            }

        }

        return output;

    }

    //加载资源权限树
    var loadResAuthTree = function(param){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUnioResourceAuthTree",
            "dataType" : "json",
            "data" :{
                userId : param
            },
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                //使用jstree初始化tree
                //先销毁当前树对象
                $('#MenuAuthTree').jstree("destroy");
                $('#MenuAuthTree').empty().append("<ul>"+showResAuthTree(obj1)+"</ul>");
                $('#MenuAuthTree').jstree({
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
                    //"plugins": ["wholerow", "checkbox", "types","themes", "html_data", "ui","state"],
                    "plugins": ["checkbox"],
                    "checkbox":{
                        "undetermined":false,
                        "three_state":true
                    }
                });

            }
        });
    }

    //加载API权限树
    var loadApiAuthTree = function(param){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUnioAPIAuthTree",
            "dataType" : "json",
            "data" :{
                userId : param
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
                    //"plugins": ["wholerow", "checkbox", "types","themes", "html_data", "ui","state"],
                    "plugins": ["checkbox"],
                    "checkbox":{
                        "undetermined":false,
                        "three_state":true
                    }
                });

            }
        });
    }

    /**
     * 根据用户id查询用户信息
     * @param p
     */
    var queryUserInfoByUserId = function(p){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/findUserInfoByUserId",
            "dataType" : "json",
            "data" :{
                userId : p
            },
            "success" : function(resp){
                var obj1 = eval(resp.uInfo);
                $('#updateUserId').attr("value",obj1.userId);
                $('#updateLoginAcct').attr("value",obj1.loginAcct);
                $('#updateLoginPwd').attr("value",obj1.loginPwd);
                $('#updateUserName').attr("value",obj1.userName);
                $('#updateOrgCode').val(obj1.orgCode);
                $('input[name=updateUserGender][value='+obj1.gender+']').parent().addClass("checked");
                //$('input[name=updateUserGender][value='+obj1.gender+']').attr("checked","true");
                $('#updateUserPhone').attr("value",obj1.phone);
                $('#updateUserMail').attr("value",obj1.email);
                $('#updateUserStat').val(obj1.userStat);
                $('#updateUserType').val(obj1.userType);
                $('#updateRegistPersn').attr("value",obj1.registPersn);
                $('#updateRegistTime').attr("value",obj1.registTime);
            }
        });

    }


    return{
        init:function(){
            if(!jQuery().dataTable){
                return;
            }

            initUserGrpTree();
            queryUesrInfoTable("",0);
        },
        //用户详细信息弹层页面
        openAddUserInfoPage:function () {
            $('#addUserPage').modal("show");
        },
        //刷新用户信息列表
        freshUserInfoTable:function () {
            queryUesrInfoTable("",0);
        },
        //删除用户
        removeUser:function (loginAcct) {
            $.ajax({
                "type" :"post",
                "url" : "/openapi/admin/auth/removeUserByLoginAcct",
                "data" :{
                    loginAcct : loginAcct
                },
                "dataType" : "text",
                "success" : function(resp){
                    //刷新用户信息表格
                    userManaged.freshUserInfoTable();
                }
            });
        },
        //用户详细信息编辑页面
        editedUserInfoDetailPage:function(p){
            var param = p
            queryUserInfoByUserId(param);
            loadUserGrpTree(param);
            loadResAuthTree(param);
            loadApiAuthTree(param);
            $('#userDetailInfo').modal("show");
        }
    }
    
}();

jQuery(document).ready(function() {
    userManaged.init();
    
    //新增用户
    $('#addUser').click(function () {
        userManaged.openAddUserInfoPage()
    });

    $('#addUserSave').click(function () {

        var loginAcct = $("#addLoginAcct").val();
        var userName =  $("#addUserName").val();
        var loginPwd = $("#addLoginPwd").val();
        var loginPwdSecd = $("#addLoginPwdSecd").val();
        var gender = $("input[name='addGender']:checked").val();
        var phone = $("#addPhone").val();
        var mail = $("#addMail").val();
        var position = $('#addPosition').val();
        var userType = $("#addUserType").find("option:selected").val();
        
        
        if(loginPwd == loginPwdSecd){
            $.ajax({
                "type" :"post",
                "url" : "/openapi/admin/auth/saveUserInfo",
                "data" :{
                    addLoginAcct : loginAcct,
                    addUserName:userName,
                    addLoginPwd:loginPwd,
                    addGender:gender,
                    addPhone:phone,
                    addMail:mail,
                    addPosition:position,
                    addUserType:userType
                },
                "dataType" : "text",
                "success" : function(resp){
                    alert(resp);
                    $("#addLoginAcct").val("");
                    $("#addUserName").val("");
                    $("#addLoginPwd").val("");
                    $("#addPhone").val("");
                    $("#addMail").val("");
                    $('#addPosition').val("");
                    $('#addUserPage').modal("hide");
                    //刷新用户信息表格
                    userManaged.freshUserInfoTable();
                }
            });
        }else{
            alert("两次密码不相同，请重新输入！");
        }

    });

    //根据条件信息查询用户信息
    $('#queryConditionButton').click(function () {
        userManaged.freshUserInfoTable();
    });

    //编辑保存用户详细信息
    $('#editedUserInfo').click(function () {

        var checkedResTree = $('#userGrpTree').jstree().get_selected();
        var grpId = $('#'+checkedResTree).attr("nodeid");
        var userId = $('#updateUserId').val();
        //var loginAcct = $('#updateLoginPwd').val();
        var userName = $('#updateUserName').val();
        var orgCode =  $("#updateOrgCode").find("option:selected").val();
        //var userGender = $("input[name='updateUserGender']:checked").val();
        $("input[name='updateUserGender']").each(function(i){
              if($(this).parent().hasClass("checked")){
                  userGender = $(this).val();
              }
        });


        var userPhone = $('#updateUserPhone').val();
        var userMail = $('#updateUserMail').val();
        var userStat = $("#updateUserStat").find("option:selected").val();
        var userType = $("#updateUserType").find("option:selected").val();
        var registPersn = $("#updateRegistPersn").val();

        //开始处理对应的权限信息
        var checkedResTree = $('#MenuAuthTree').jstree().get_checked();//选中的资源树节点id
        var checkedApiTree = $('#ApiAuthTree').jstree().get_checked();//选中的API树节点id

        var allResId = "";
        var allApiId = "";

        //处理选中的资源树节点对应的资源id
        for(var i=0;i<checkedResTree.length;i++){
            var nodeBelong = $('#'+checkedResTree[i]).attr("nodeBelong");
            if(nodeBelong != 'group'){
                var resId = $('#'+checkedResTree[i]).attr("nodeid");
                if(i == checkedResTree.length -1){
                    allResId += resId;
                }else{
                    allResId += resId + ",";
                }
            }

        }

        //处理资源菜单树checkbox状态为半选情况下的值选取问题
        $('#MenuAuthTree').find("*").find("li").each(function(i){
            if($(this).find("a").children().hasClass("jstree-icon jstree-checkbox jstree-undetermined") == true){
                //alert($(this).attr("nodeid"));
                allResId += "," + $(this).attr("nodeid");
            }

        });

        //处理选中的API树节点对应的API的id
        for(var i=0;i<checkedApiTree.length;i++){
            var nodeBelong = $('#'+checkedResTree[i]).attr("nodeBelong");
            if(nodeBelong != 'group'){
                var apiId = $('#'+checkedApiTree[i]).attr("nodeid");
                if(i == checkedApiTree.length -1){
                    allApiId += apiId;
                }else{
                    allApiId += apiId + ",";
                }
            }

        }

        //处理API树checkbox状态为半选情况下的值选取问题
        $('#ApiAuthTree').find("*").find("li").each(function(i){
            if($(this).find("a").children().hasClass("jstree-icon jstree-checkbox jstree-undetermined") == true){
                allApiId += "," + $(this).attr("nodeid");
            }
        });

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/updateUserByUserId",
            "data" :{
                grpId : grpId,
                userId:userId,
                userName:userName,
                orgCode:orgCode,
                userGender:userGender,
                userPhone:userPhone,
                userMail:userMail,
                userStat:userStat,
                userType:userType,
                registPersn:registPersn,
                allResId:allResId,
                allApiId:allApiId
            },
            "dataType" : "text",
            "success" : function(resp){
                alert(resp);
                $('#userDetailInfo').modal("hide");
            }
        });

    });

    
});