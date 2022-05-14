

var authRegist = function (){

    //加载API权限树
    var loadApiAuthTree = function(){

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/right/getUnioAPIAuthTree",
            "dataType" : "json",
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


    return{

        init:function(){
            loadApiAuthTree();
        }

    }

}()

jQuery(document).ready(function() {

    authRegist.init();

    $('#authRegistButton').click(function(){

        var checkedApiTree = $('#ApiAuthTree').jstree().get_checked();//选中的API树节点id
        var allApiId = "";

        for(var i=0;i<checkedApiTree.length;i++){
            var nodeBelong = $('#'+checkedApiTree[i]).attr("nodebelong");
            //alert("------"+checkedApiTree[i] + " : " +  nodeBelong);

            if(nodeBelong != 'group'){
                var apiId = $('#'+checkedApiTree[i]).attr("nodeid");
                if(i == checkedApiTree.length -1){
                    allApiId += apiId;
                }else{
                    allApiId += apiId + ",";
                }
            }

        }

        $.ajax({
            "type" :"post",
            "url" : "/openapi/admin/right/registApiAuth",
            "data" :{
                allApiId:allApiId
            },
            "dataType" : "text",
            "success" : function(resp){
                alert(resp);
            }
        });

    });

});