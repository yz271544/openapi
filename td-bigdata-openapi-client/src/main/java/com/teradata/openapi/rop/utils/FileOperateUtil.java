package com.teradata.openapi.rop.utils;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by John on 2016/8/23.
 */
public class FileOperateUtil {
    private final static Log logger = StaticLog.get();
    //public static String FILEDIR = null;
    /**
     * 上传
     * @param
     * @throws IOException
     */
    private static String initFilePath(String name) {
        String dir = getFileDir(name) + "";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        Long num = new Date().getTime();
        Double d = Math.random()*num;
        return (file.getPath() + "/" + num + d.longValue() + "_" + name).replaceAll(" ", "-");
    }
    private static int getFileDir(String name) {
        return name.hashCode() & 0xf;
    }
    public static void download(String downloadfFileName, long start, long size, ServletOutputStream sos) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(sos);
            RandomAccessFile raf = new RandomAccessFile(downloadfFileName, "r");
            write(start, size, raf, out);
        } catch (FileNotFoundException e) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(sos);
                RandomAccessFile raf = new RandomAccessFile(new String(downloadfFileName.getBytes("iso-8859-1")), "r");
                write(start, size, raf, out);
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.error("error:",e1);
            }
            logger.error("error:File not found!",e);
        }
    }

    /**
     * 写入数据
     * @param start
     * @param size
     * @param raf
     * @param out
     */
    private static void write(long start, long size, RandomAccessFile raf, BufferedOutputStream out) {
        try{
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            long readLength = 0;
            raf.seek(start);
            while (readLength <= size - 1024) {
                bytesRead = raf.read(buffer, 0, 1024);
                readLength += 1024;
                out.write(buffer, 0, bytesRead);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (readLength <= size) {
                bytesRead = raf.read(buffer, 0, (int)(size - readLength));
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } catch (IOException e) {
        } finally {
            try {
                if (raf != null) raf.close();
            }
            catch (IOException ex) {
            }
            try {
                if (out != null) out.close();
            }
            catch (IOException ex) {
            }
        }
    }

}
