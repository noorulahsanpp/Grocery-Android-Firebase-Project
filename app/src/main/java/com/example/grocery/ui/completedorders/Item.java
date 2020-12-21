package com.example.grocery.ui.completedorders;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String customername;
    private String customerphone;
    private String orderid;
    private ArrayList<String> itemname;
    private ArrayList<String> itemno;
    private ArrayList<String> itemimage;
    private ArrayList<String> itemprice;
    public Item() {
    }

    public Item(String customername, String customerphone, String orderid,ArrayList<String> itemname,ArrayList<String> itemno,ArrayList<String> itemimage,ArrayList<String> itemprice ) {
        this.customername = customername;
        this.orderid = orderid;
        this.customerphone = customerphone;
        this.itemname = itemname;
        this.itemno = itemno;
        this.itemimage = itemimage;
        this.itemprice = itemprice;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public ArrayList<String> getItemname() {
        return itemname;
    }

    public void setItemname(ArrayList<String> itemname) {
        this.itemname = itemname;
    }

    public ArrayList<String> getItemno() {
        return itemno;
    }

    public void setItemno(ArrayList<String> itemno) {
        this.itemno = itemno;
    }

    public ArrayList<String> getItemimage() {
        return itemimage;
    }

    public void setItemimage(ArrayList<String> itemimage) {
        this.itemimage = itemimage;
    }

    public ArrayList<String> getItemprice() {
        return itemprice;
    }

    public void setItemprice(ArrayList<String> itemprice) {
        this.itemprice = itemprice;
    }
}
