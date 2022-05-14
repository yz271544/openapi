package com.teradata.portal.web.dashboard.sevlet;

import com.teradata.portal.web.dashboard.pojo.ShoppingCart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by John on 2016/6/21.
 */
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数 id,price
        String bookName = request.getParameter("id");
        int price = Integer.parseInt(request.getParameter("price"));
        //2.获取购物车对象，
        HttpSession session = request.getSession();
        ShoppingCart sc = (ShoppingCart) session.getAttribute("sc");

        if (sc == null){
            sc = new ShoppingCart();
            session.setAttribute("sc",sc);
        }
        //3.把点击的选项加入到购物车中
        sc.addToCart(bookName,price);
        //4.准备响应的JSON对象 {"bookName":"","totalBookNumber":1,"totalMoney":100}
        StringBuilder result = new StringBuilder();
        result.append("{")
                .append("\"bookName\":\"" + bookName + "\"")
                .append(",")
                .append("\"totalBookNumber\":" + sc.getTotalBookNumber())
                .append(",")
                .append("\"totalMoney\":" + sc.getTotalMoney())
                .append("}");
        //5.响应
        response.setContentType("text/javascripts");
        response.getWriter().print(result.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
