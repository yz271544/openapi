package com.teradata.openapi.sample.response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by John on 2016/10/18.
 */
public class RegexJavaDemo {
    public static void main(String[] args) {
        String fileNameLine = "abcd134.chk";
        String pattern = "(.*)(\\.chk)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileNameLine);
        if (m.find()){
            System.out.println(fileNameLine);
        }else {
            System.out.println("Not match");
        }


    }
}
