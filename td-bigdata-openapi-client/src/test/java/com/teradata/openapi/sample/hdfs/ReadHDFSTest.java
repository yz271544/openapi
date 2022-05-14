package com.teradata.openapi.sample.hdfs;

import com.teradata.openapi.framework.util.pluginUtil.HDFSPathHelper;
import com.teradata.openapi.framework.util.pluginUtil.PluginEnv;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ReadHDFSTest {
    public static void main(String[] args) {
        //FileSystem fileSystem = PluginEnv.cacheFileSystem();
        String plugHDFSUri = "hdfs://master01:8020/";
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(new URI(plugHDFSUri), new Configuration());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String sourceFileFullName = "/user/oapi/huzy.txt";
        FSDataInputStream fis = HDFSPathHelper.loadInputStreamFromHDFS(sourceFileFullName,fileSystem);
        String str = null;
        try {
            while ((str = fis.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
