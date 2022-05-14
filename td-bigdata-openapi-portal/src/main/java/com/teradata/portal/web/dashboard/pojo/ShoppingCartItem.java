package com.teradata.portal.web.dashboard.pojo;

/**
 * Created by John on 2016/6/21.
 */
public class ShoppingCartItem {
    private int number;
    private String bookName;
    private int price;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
