package com.teradata.openapi.rop.utils;

import com.teradata.openapi.access.biz.bean.DownLoadAttr;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Created by John on 2016/8/24.
 */
public class FileIODeal {
    private static final long serialVersionUID = 1L;
    private final static Log logger = StaticLog.get();
    /**
     * 文件下载
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    public void download(HttpServletRequest request, HttpServletResponse response,DownLoadAttr downLoadAttr) throws UnsupportedEncodingException {
        String downloadfFileName = String.format("/data/open_api/hdfs/user/oapi/data/%s/%s.chk",downLoadAttr.getFormCode(),downLoadAttr.getFormatFileFinger());
        File downloadFile = new File(downloadfFileName);
        long pos = FileUtil.headerSetting(downloadFile, request, response);
//      log.info("跳过"+pos);
        ServletOutputStream os = null;
        BufferedOutputStream out = null;
        RandomAccessFile raf = null;
        byte b[] = new byte[1024];//暂存容器
        try {
            os = response.getOutputStream();
            out = new BufferedOutputStream(os);
            raf = new RandomAccessFile(downloadFile, "r");
            raf.seek(pos);
            try {
                int n = 0;
                while ((n = raf.read(b, 0, 1024)) != -1) {
                    out.write(b, 0, n);
                }
                out.flush();
            } catch(IOException ie) {
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private File getFile(HttpServletRequest request) throws UnsupportedEncodingException {
        String uriStr = request.getParameter("uri");
        if (null != uriStr){
            uriStr = URLDecoder.decode(uriStr,"UTF-8");
            if (uriStr.startsWith("file://")){
                uriStr = uriStr.substring(7);
                return new File(uriStr);
            }else if (uriStr.startsWith("hbase://")){
                try {
                    return new File(new URI(uriStr));
                } catch (URISyntaxException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
        throw new RuntimeException("it's not a real uri");
    }
}
