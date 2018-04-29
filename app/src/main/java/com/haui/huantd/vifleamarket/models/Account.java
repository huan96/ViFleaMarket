package com.haui.huantd.vifleamarket.models;

import java.util.List;

/**
 * Created by huand on 02/08/18.
 */

public class Account {
    private String uid;
    private String name;
    private String phone;
    private String address;
    private String urlAvatar;
    private List<String> listProducts;

    public Account() {
    }

    public Account(String uid, String name, String phone, String address, String urlAvatar) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.urlAvatar = urlAvatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public List<String> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<String> listProducts) {
        this.listProducts = listProducts;
    }
}
