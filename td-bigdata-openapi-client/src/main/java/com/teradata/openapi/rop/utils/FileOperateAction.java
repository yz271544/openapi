package com.teradata.openapi.rop.utils;

import com.teradata.openapi.access.biz.bean.DownLoadAttr;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by John on 2016/8/23.
 */

public class FileOperateAction {
    private final static Log logger = StaticLog.get();

    public void download(HttpServletRequest request, HttpServletResponse response, DownLoadAttr downLoadAttr) {
        logger.info("Start DownLoad FormCode:" + downLoadAttr.getFormCode() + " FormatFileFinger:" + downLoadAttr.getFormatFileFinger());
        //init(request);
        try {
            String downloadfFileName = String.format("/data/open_api/hdfs/user/oapi/data/%s/%s.chk",downLoadAttr.getFormCode(),downLoadAttr.getFormatFileFinger());  //request.getParameter("reqID");
            logger.debug("1:downloadfFileName:" + downloadfFileName);

            downloadfFileName = new String(downloadfFileName.getBytes("iso-8859-1"), "utf-8");
            String fileName = downloadfFileName.substring(downloadfFileName.indexOf("_") + 1);

            logger.debug("2:downloadfFileName:" + downloadfFileName + " fileName:" + fileName);

            String userAgent = request.getHeader("User-Agent");
            byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes("UTF-8");
            fileName = new String(bytes, "ISO-8859-1");
            logger.debug("3:downloadfFileName:" + downloadfFileName + " fileName:" + fileName);

            response.reset();
            response.setHeader("Accept-Ranges", "bytes");

            String[] sizes = getLength(request, response);
            //File file = new File(FileOperateUtil.FILEDIR + "/" + downloadfFileName);
            File file = new File(downloadfFileName);
            long start = 0L;
            long size = file.length();
            logger.info("download sizes:" + size);
            if (sizes == null) {

            } else if (sizes.length == 1) {
                start = Long.parseLong(sizes[0]) - 1L;
                size = size - Long.parseLong(sizes[0]) + 1L;
                String contentRange = new StringBuffer(request.getHeader("Range")).append("/").append(size).toString();
                contentRange = contentRange.replaceAll("=", " ");
                response.setHeader("Content-Range", contentRange);
            } else if (sizes.length == 2) {
                start = Long.parseLong(sizes[0]) - 1L;
                String contentRange = new StringBuffer(request.getHeader("Range")).append("/").append(size).toString();
                contentRange = contentRange.replaceAll("=", " ");
                response.setHeader("Content-Range", contentRange);
                size = Long.parseLong(sizes[1]) - start + 1L;
            } else {
                return;
            }
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.addHeader("Content-Length", String.valueOf(size));
            System.out.println(start + "-" + size + ":   " + request.getHeader("Range"));
            logger.debug(start + "-" + size + ":   " + request.getHeader("Range") + " response:" + response.getHeader("Content-disposition"));
            //logger.debug("FileOperateUtil.download:" + downloadfFileName  + " start:" + start + " size:" + size);
            FileOperateUtil.download(downloadfFileName, start, size, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getLength(HttpServletRequest request, HttpServletResponse response) {
        String[] sizes = null;
        String range = request.getHeader("Range");
        logger.debug("RANGE VALUE IS:" + range);
        if (range != null && !"".equals(range.trim())) {
            response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
            range = range.replaceAll("bytes=", "").trim();
            sizes = range.split("-");
        }
        return sizes;
    }
}
