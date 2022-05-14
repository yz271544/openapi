package com.teradata.portal.web.dashboard.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 2016/6/21.
 */
public class ShoppingCart {
    //存放ShoppingCartItem的Map： 键：书名，值：ShoppingCartItem对象
    private Map<String,ShoppingCartItem> items = new HashMap<String,ShoppingCartItem>();

    public void addToCart(String bookName,int price){
        if (items.containsKey(bookName)){
            ShoppingCartItem item = items.get(bookName);
            item.setNumber(item.getNumber()+1);
        }else {
            ShoppingCartItem item = new ShoppingCartItem();
            item.setBookName(bookName);
            item.setPrice(price);
            item.setNumber(1);
            items.put(bookName,item);
        }
    }

    public int getTotalBookNumber(){
        int total = 0 ;
        for(ShoppingCartItem item:items.values()){
            total += item.getNumber();
        }
        return total;
    }
    public int getTotalMoney(){
        int money = 0 ;
        for(ShoppingCartItem item:items.values()){
            money += item.getNumber() * item.getPrice();
        }
        return money;
    }


}
