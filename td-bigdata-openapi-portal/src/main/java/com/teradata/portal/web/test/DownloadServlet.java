package com.teradata.portal.web.test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by John on 2016/8/9.
 */
public class DownloadServlet extends HttpServlet {


    private static final int BUFFERSIZE = 65536;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
    {
        try
        {
            String downFilename=request.getParameter("curfile");
            String filepath=request.getParameter("path");
            response.setContentType("text/plain");
            response.setHeader("Location",downFilename);
            response.setHeader("Content-Disposition", "attachment; filename=" + downFilename);
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(filepath+downFilename);
            byte[] buffer = new byte[BUFFERSIZE];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
        }catch(FileNotFoundException e1)
        {
            System.out.println("没有找到您要的文件");
        }
        catch(Exception e)
        {
            System.out.println("系统错误，请及时与管理员联系");
        }
    }


    public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request,response);
    }
}