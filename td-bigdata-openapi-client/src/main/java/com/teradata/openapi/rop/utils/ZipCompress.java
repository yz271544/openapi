package com.teradata.openapi.rop.utils;

import com.teradata.openapi.access.biz.bean.DownLoadAttr;
import com.teradata.openapi.framework.message.request.Format;
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ZipCompress {

    String plugFrmtTargetPath = PluginEnv.plugFrmtTargetDir();


    void download(HttpServletRequest request, HttpServletResponse response, DownLoadAttr downLoadAttr);

    void uncompress(String zipAbsFileName);

    List<String> getFileList(DownLoadAttr downLoadAttr);
}
