/**
 * Created by Evan on 2016/7/14.
 */

var grpManaged = function () {

    var oTable;

    var queryUesrInfoOfGrpTable = function (grpid) {

        oTable = $('#sample_1');

        if (typeof oTable == 'undefined' && oTable != null){
            oTable.fnClearTable(0); //清空数据
            oTable.fnDraw(); //重新加载数据　
        }else{

            oTable.dataTable({

                "language": {
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
                    ,{'sTitle':'用户账号',"sWidth": "15%",'sClass':'center'}
                    ,{'sTitle':'用户姓名',"sWidth": "15%",'sClass':'center'}
                    ,{'sTitle':'状态',"sWidth": "10%",'sClass':'center'}
                    /*,{'sTitle':'所属用户组id',"sWidth": "20%",'sClass':'center',"bVisible":false}*/
                    ,{'sTitle':'所属用户组',"sWidth": "20%",'sClass':'center'}
                    ,{'sTitle':'操作',"sWidth": "40%",'sClass':'center'}

                ],
                "columnDefs": [{"aTargets":[5],"mRender":function(data,type,full){
                    return "<a href=\"javascript:grpManaged.updateUserGrpRelat('"+full[0]+"');\" class=\"btn blue\"><i class=\"fa fa-edit\"></i> 更改用户组 </a>" +
                        "<a href=\"javascript:grpManaged.deleteUserGrpRelat('"+full[0]+"');\" class=\"btn red\"><i class=\"fa fa-edit\"></i> 移出用户组 </a>";
                }}],

                "sAjaxSource" : "/openapi/admin/auth/findUserListByGrpId",
                "fnServerData" : function(sSource,aoData,fnCallback){

                    aoData.push(
                        { "name": "grpid", "value": grpid }
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
                $('#tree_1').jstree("destroy");
                $("#tree_1").empty().append("<ul>"+showAllTree(obj1)+"</ul>");

                //使用jstree初始化tree
                $('#tree_1').jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "multiple": false
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
                    findGrpInfoByGrpId(nodeid);
                    queryUesrInfoOfGrpTable(nodeid);
                });

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
                output += '<li data-jstree=\'{"opened\" : true }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            }else{
                if(node == 0){
                    output += '<li data-jstree=\'{"type\" : \"file\" }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }else{
                    output += '<li data-jstree=\'{"type\" : \"file\" }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';
                }
            }

        }

        return output;

    }

    /**
     * 根据组id查找单个组信息
     * @param grpid 组id
     */
    var findGrpInfoByGrpId = function(grpid){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUserGrpByGrpId",
            "data" :{
                grpId : grpid
            },
            "dataType" : "json",
            "success" : function(resp){
                var userGrp = eval(resp.grpInfo);
                $("#grpId").val(userGrp.id);
                $("#grpName").val(userGrp.name);
                $("#fathrGrpName").attr("idValue",userGrp.parentId);
                $("#fathrGrpName").empty().append(userGrp.parentName);
                $("#grpDesc").val(userGrp.desc);
            }
        });

    }

    /**
     * 加载要选择的用户组树结构
     */
    var loadSelectUserGrpTree = function(){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUserGrpTree",
            "dataType" : "json",
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                //使用jstree初始化tree
                //先销毁当前树对象
                $('#grpTree').jstree("destroy");
                $('#grpTree').empty().append("<ul>"+showAllTree(obj1)+"</ul>");
                $('#grpTree').jstree({
                        "core" : {
                            "themes" : {
                                "responsive": false
                            },
                            "multiple": false
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
                $('#grpTree').on('select_node.jstree', function(e,data) {
                    var link = $('#' + data.selected).find('a').parent();
                    var nodeid = link.attr("nodeid");
                    var nodeName = link.attr("nodeName");
                    $("#selectedGrpNodeId").val(nodeid);
                    $("#selectedGrpNodeName").val(nodeName);
                });

            }
        });
    }

    //加载资源权限树
    var loadResAuthTree = function(param){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getResourceAuthTree",
            "dataType" : "json",
            "data" :{
                grpId : param
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

    /**
     * 通过json数据生成符合要求的【资源权限】树de html
     * @param treeJsonStr json格式的数据
     * @param htmlStr 要组合成的html的容器
     */
    var showResAuthTree = function (treeJsonStr) {

        var output = '';

        for(var node = 0;node <treeJsonStr.length;node++){

            if(treeJsonStr[node].childrens.length > 0){
                var childrenHtml = showResAuthTree(treeJsonStr[node].childrens);
                output += '<li data-jstree=\'{"opened\" : true }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'">' + treeJsonStr[node].name + '<ul>' + childrenHtml + '</ul></li>\n';
            }else{
                if(node == 0){
                    if( treeJsonStr[node].rightFieldValue == treeJsonStr[node].id){
                        output += '<li data-jstree=\'{"type\" : \"file\" ,\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\" }\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }
                }else{
                    if(treeJsonStr[node].rightFieldValue == treeJsonStr[node].id){
                        output += '<li data-jstree=\'{"type\" : \"file\",\"selected\" : true}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }else{
                        output += '<li data-jstree=\'{"type\" : \"file\"}\' nodeid="'+treeJsonStr[node].id+'" nodeName="'+treeJsonStr[node].name+'"><a href="javascript:;">' + treeJsonStr[node].name +  '</a></li>\n';

                    }

                }
            }

        }

        return output;

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
     * 加载要更新的用户组树结构
     */
    var loadUpdateSelectUserGrpTree = function(){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/getUserGrpTree",
            "dataType" : "json",
            "success" : function(resp){
                var obj1 = eval(resp.ugtree);
                //使用jstree初始化tree
                //先销毁当前树对象
                $('#updateUserGrpRelatTree').jstree("destroy");
                $('#updateUserGrpRelatTree').empty().append("<ul>"+showAllTree(obj1)+"</ul>");
                $('#updateUserGrpRelatTree').jstree({
                    "core" : {
                        "themes" : {
                            "responsive": false
                        },
                        "multiple": false
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
                $('#updateUserGrpRelatTree').on('select_node.jstree', function(e,data) {
                    var link = $('#' + data.selected).find('a').parent();
                    var nodeid = link.attr("nodeid");
                    $("#updateSelectedGrpNodeId").val(nodeid);
                });

            }
        });
    }


    return{
        init:function(){
            if(!jQuery().dataTable){
                return;
            }

            initUserGrpTree();
            //queryUesrInfoTable("",0);
        },
        //用户详细信息弹层页面
        openSelectUserGrpTreeDialog:function () {
            loadSelectUserGrpTree();
            $('#userGrpSelect').modal("show");
        },
        //新增用户组弹层页面
        openAddUserGrpPage:function(){
            $('#addUserGrpPage').modal("show");
        },
        //用户组权限弹层页面
        openUserGrpAuthPage:function () {
            var grpId = $("#grpId").val();
            loadResAuthTree(grpId);
            loadApiAuthTree(grpId);
            $('#userGrpAuth').modal("show");
        },
        //更新用户对应的用户组页面
        updateUserGrpRelat:function(p1){
            loadUpdateSelectUserGrpTree();
            $("#updateSelectedLoginAcct").attr("value",p1);
            $('#updateUserGrpSelect').modal("show");
        },
        //删除用户对应的用户组关系
        deleteUserGrpRelat:function(p1){
            $.ajax({
                "type" :"post",
                "url" : "/openapi/admin/auth/deleteUserGrpRelat",
                "data" :{
                    userId:p1
                },
                "dataType" : "text",
                "success" : function(resp){
                    alert(resp);
                    $('#userGrpAuth').modal("hide");
                }
            });
        }
    }
    
}();

jQuery(document).ready(function() {
    grpManaged.init();
    
    //打开选择用户组树
    $('#fathrGrpName').click(function () {
        grpManaged.openSelectUserGrpTreeDialog();
    });

    //选择父用户组节点
    $('#selectedGrp').click(function () {
        var selectedGrpNodeId = $("#selectedGrpNodeId").val();
        var selectedGrpNodeName = $("#selectedGrpNodeName").val();
        $("#fathrGrpName").attr("idValue",selectedGrpNodeId);
        $("#fathrGrpName").empty().append(selectedGrpNodeName);
        $('#userGrpSelect').modal("hide");
    });

    //更新用户组信息
    $('#updateGrp').click(function () {
        var selectedGrpNodeId = $("#selectedGrpNodeId").val();
        var selectedGrpNodeName = $("#selectedGrpNodeName").val();

        var grpId = $("#grpId").val();
        var grpName = $("#grpName").val();
        var parentId = $("#fathrGrpName").attr("idValue");
        var grpDesc = $("#grpDesc").val();

        if(parentId != null && parentId != ""){
            $.ajax({
                "type" :"post",
                "url" : "/openapi/admin/auth/updateUserGrpInfo",
                "data" :{
                    grpId : grpId,
                    grpName : grpName,
                    parentId : parentId,
                    grpDesc : grpDesc
                },
                "dataType" : "text",
                "success" : function(resp){
                    alert(resp);
                    grpManaged.init();
                    $("#grpId").attr("value","");
                    $("#grpName").val("")
                    $("#fathrGrpName").attr("idValue","");
                    $("#fathrGrpName").empty();
                    $("#grpDesc").val("");
                }
            });
        }else{
            alert("请选择父用户组！");
        }


    });

    //打开新增用户组页面
    $('#openAddGrpDialog').click(function () {
        var grpId = $("#grpId").val();
        var grpName = $("#grpName").val();
        if(grpId != null && grpId != ""){
            $('#addFathrGrpName').attr("idValue",grpId);
            $('#addFathrGrpName').html(grpName);
            grpManaged.openAddUserGrpPage();
        }else{
            alert("请选择当前一个用户组节点！");
        }

    });

    //保存用户组信息
    $('#saveUserGrp').click(function () {

        var grpName = $("#addGrpName").val();
        var parentId = $("#addFathrGrpName").attr("idValue");
        var grpDesc = $("#addGrpDesc").val();

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/saveUserGrpInfo",
            "data" :{
                grpName : grpName,
                parentId : parentId,
                grpDesc : grpDesc
            },
            "dataType" : "text",
            "success" : function(resp){
                alert(resp);
                grpManaged.init();
                $("#addGrpName").val("")
                $("#addFathrGrpName").attr("idValue","");
                $("#addFathrGrpName").empty();
                $("#addGrpDesc").val("");
                $('#addUserGrpPage').modal("hide");
            }
        });

    });

    //删除用户组
    $('#removeGrp').click(function(){

        //删除前先获得要删除用户组的id，如果没有没有获得id，直接返回
        var grpId = $("#grpId").val();
        if(grpId != null && grpId != ""){
            $.ajax({
                "type" :"post",
                "url" : "/openapi/admin/auth/removeUserGrpInfo",
                "data" :{
                    grpId : grpId
                },
                "dataType" : "text",
                "success" : function(resp){
                    alert(resp);
                    grpManaged.init();
                    $("#grpId").attr("value","");
                    $("#grpName").val("")
                    $("#fathrGrpName").attr("idValue","");
                    $("#fathrGrpName").empty();
                    $("#grpDesc").val("");
                }
            });
        }else{
            alert("请选择用户组！");
        }
    });

    //打开用户组权限页面
    $('#grpAuth').click(function () {
        grpManaged.openUserGrpAuthPage();
    });
    //保存用户组对应的权限信息
    $('#saveUserGrpAuth').click(function () {

        var grpId = $("#grpId").val();//用户组id
        var checkedResTree = $('#MenuAuthTree').jstree().get_checked();//选中的资源树节点id
        var checkedApiTree = $('#ApiAuthTree').jstree().get_checked();//选中的API树节点id

        var allResId = "";
        var allApiId = "";


        //处理选中的资源树节点对应的资源id
        for(var i=0;i<checkedResTree.length;i++){
            var resId = $('#'+checkedResTree[i]).attr("nodeid");
            if(i == checkedResTree.length -1){
                allResId += resId;
            }else{
                allResId += resId + ",";
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
            var apiId = $('#'+checkedApiTree[i]).attr("nodeid");
            if(i == checkedApiTree.length -1){
                allApiId += apiId;
            }else{
                allApiId += apiId + ",";
            }
        }

        //处理API树checkbox状态为半选情况下的值选取问题
        $('#ApiAuthTree').find("*").find("li").each(function(i){
            if($(this).find("a").children().hasClass("jstree-icon jstree-checkbox jstree-undetermined") == true){
                allApiId += "," + $(this).attr("nodeid");
            }
        });




        //alert(allResId);



        //alert($('#MenuAuthTree').find("*").find("li").length);
        //alert($('#j3_21').html());

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/saveAuthInfo",
            "data" :{
                grpId : grpId,
                allResId:allResId,
                allApiId:allApiId
            },
            "dataType" : "text",
            "success" : function(resp){
                alert(resp);
                $('#userGrpAuth').modal("hide");
            }
        });

    });

    //更新用户对应的用户组
    $('#updateSelectedGrp').click(function () {

        var loginAcct = $('#updateSelectedLoginAcct').val();
        var grpId = $('#updateSelectedGrpNodeId').val();
        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/auth/updateUserGrpRelat",
            "data" :{
                grpId : grpId,
                loginAcct:loginAcct
            },
            "dataType" : "text",
            "success" : function(resp){
                alert(resp);
                $('#updateUserGrpSelect').modal("hide");
            }
        });
    });


});