package com.teradata.openapi.sample;

import java.io.*;

/**
 * Created by John on 2016/8/24.
 */
public class FileStreamDemo {
    public static void main(String[] args) {
        getDemo();
    }

    private static void getDemo() {

        String s = "/data/open_api/hdfs/user/oapi/data/json/bc3d7584655e96a058f8a85d6edcb137.chk";
        System.out.println("dddd:" + s.substring(s.lastIndexOf("/")+1));


        String downloadfFileName = "C:\\Users\\John\\Downloads\\list.chk";
        File file = new File(downloadfFileName);
        String filname = file.getName();
        System.out.println(filname);
        try {
            BufferedReader buisr = new BufferedReader(new InputStreamReader(new FileInputStream(downloadfFileName),"UTF-8"));
            String line = null;
            while((line = buisr.readLine()) != null){
                //System.out.println(line);
                String contextFileFullName = (line.split("\\s"))[0];
                System.out.println((line.split("\\s"))[0]);
                String[] contextFileNames = contextFileFullName.split("\\/");
                String contextFileName = contextFileNames[contextFileNames.length-1];
                System.out.println(contextFileName);
                //String[] lineBuf = line.split("\\s");
                //System.out.println(lineBuf[0]);
            };
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void createAnddelete() throws IOException {
        String downloadfFileName = "C:\\Users\\John\\Downloads\\listnew.chk";
        File file = new File(downloadfFileName);
        Boolean b = file.createNewFile();


    }

}
