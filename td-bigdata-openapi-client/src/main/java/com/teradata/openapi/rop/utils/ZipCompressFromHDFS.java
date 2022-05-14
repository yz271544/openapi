package com.teradata.openapi.rop.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.teradata.openapi.access.biz.bean.DownLoadAttr;
import com.teradata.openapi.framework.message.request.FormDetail;
import com.teradata.openapi.framework.message.request.Format;
import com.teradata.openapi.framework.util.pluginUtil.HDFSPathHelper;
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

/**
 * Created by John on 2016/8/25.
 */
public class ZipCompressFromHDFS implements ZipCompress {
    private static final long serialVersionUID = 1L;
    private final static Log logger = StaticLog.get();
    FileSystem fileSystem = PluginEnv.cacheFileSystem();

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response, DownLoadAttr downLoadAttr) {
        System.out.println("download:" + JSON.toJSONString(downLoadAttr));
        List<String> filepaths = getFileList(downLoadAttr);
        logger.debug("All the file List is:" + filepaths);
        ServletOutputStream os = null;
        try {
            String outFileName = downLoadAttr.getFormatFileFinger() + ".zip";
            response.addHeader("Content-Disposition", "attachment; filename=\"" + IoUtil.toUtf8String(outFileName) + "\"");
            //FileOutputStream f = new FileOutputStream(outFileName);
            os = response.getOutputStream();
            // 输出校验流,采用Adler32更快
            //CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            CheckedOutputStream csum = new CheckedOutputStream(os, new Adler32());
            //创建压缩输出流
            ZipOutputStream zos = new ZipOutputStream(csum);
            //BufferedOutputStream out = new BufferedOutputStream(zos);
            //设置Zip文件注释
            zos.setComment("The file Format is:" + downLoadAttr.getFormCode() + " file finger is:" + downLoadAttr.getFormatFileFinger());
            for (String sourceFileFullName : filepaths) {
                System.out.println("Writing file " + sourceFileFullName);

                String pattern = "(.*)(\\.chk)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(sourceFileFullName);
                String targetFileName = "";
                if (!(m.find())) {
                    targetFileName = sourceFileFullName.substring(sourceFileFullName.lastIndexOf("/") + 1) + "." + downLoadAttr.getFormCode();
                } else {
                    targetFileName = sourceFileFullName.substring(sourceFileFullName.lastIndexOf("/") + 1);
                }
                logger.debug("Writing file " + targetFileName);

                //针对单个文件建立读取流
                //BufferedReader bin = new BufferedReader(new FileReader(s));

                //FileInputStream fis = new FileInputStream(sourceFileFullName);
                FSDataInputStream fis = HDFSPathHelper.loadInputStreamFromHDFS(sourceFileFullName, fileSystem);

                //ZipEntry ZIP 文件条目
                //putNextEntry 写入新条目，并定位到新条目开始处
                zos.putNextEntry(new ZipEntry(targetFileName));

                byte buffer[] = new byte[64000];
                int len = 0;
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }

                fis.close();
                zos.flush();
            }
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("file not found:", e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException:", e);
        }
    }

    @Override
    public void uncompress(String zipAbsFileName) {
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(zipAbsFileName);
            //查看压缩包里的文件列表
            CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());
            ZipInputStream in2 = new ZipInputStream(csumi);
            BufferedInputStream bis = new BufferedInputStream(in2);
            ZipEntry ze;
            while ((ze = in2.getNextEntry()) != null) {
                System.out.println("Reader File " + ze);
                logger.debug("Reader File " + ze);
                int x;
                while ((x = bis.read()) != -1)
                    System.out.println(x);
                logger.debug("zip context file:" + x);
            }
            //利用ZipFile解压压缩文件
            ZipFile zf = new ZipFile("D:\\test.zip");
            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                System.out.println("File name : " + ze2);
                logger.debug("File name : " + ze2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getFileList(DownLoadAttr downLoadAttr) {

        System.out.println("getFileList formCode:" + downLoadAttr.getFormCode());
        Format format = new Format();
        try {
            try {
                format = JSON.parseObject(downLoadAttr.getFormCode(), Format.class);
            } catch (JSONException e) {
                format.setFormType(downLoadAttr.getFormCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            format.setFormType(downLoadAttr.getFormCode()); //目前修改为context.getFormat(),将来可修改为json等默认值
            format.setFormDetail(new FormDetail());
        }

        String downloadfFileName = String.format("%s/%s/%s.CHK", plugFrmtTargetPath, format.getFormType(), downLoadAttr.getFormatFileFinger());
        logger.debug("downloadfFileName:" + downloadfFileName);

        List<String> contextAllFileName = new LinkedList<String>();

        Path hdfsFilePath = new Path(downloadfFileName);
        if (HDFSPathHelper.exists(hdfsFilePath, fileSystem)) {
            contextAllFileName.add(downloadfFileName);
            System.out.println("chkFileList:" + contextAllFileName);
            try {
                FSDataInputStream fsDataInputStream = HDFSPathHelper.loadInputStreamFromHDFS(downloadfFileName, fileSystem);
                BufferedReader buisr = new BufferedReader(new InputStreamReader(fsDataInputStream));
                String line = null;
                while ((line = buisr.readLine()) != null) {
                    if (line.trim().length() != 0) {
                        String contextFileFullName = (line.split("\\s+"))[0];
                        System.out.println("contextFileFullName:" + contextFileFullName);
                        contextAllFileName.add(contextFileFullName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("not exists file!" + downloadfFileName);
        }

        return contextAllFileName;

    }
}