package com.example.grocery.ui.completedorders;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String customername;
    private String phone;
    private String orderid;

    public Item() {
    }

    public Item(String customername, String phone, String orderid) {
        this.customername = customername;
        this.orderid = orderid;
        this.phone = phone;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

}
