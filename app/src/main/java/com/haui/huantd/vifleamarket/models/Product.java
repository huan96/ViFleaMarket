package com.haui.huantd.vifleamarket.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by huand on 02/10/18.
 */

public class Product implements Parcelable {
    private String uid;
    private String name;
    private String idSeller;
    private String price;
    private String idType;
    private String idCity;
    private String idDistrict;
    private String time;
    private String note;
    private List<String> listImage;

    public Product() {
    }

    public Product(String uid, String name, String idSeller, String price, String idType, String idCity, String idDistrict, String time, String note, List<String> listImage) {
        this.uid = uid;
        this.name = name;
        this.idSeller = idSeller;
        this.price = price;
        this.idType = idType;
        this.idCity = idCity;
        this.idDistrict = idDistrict;
        this.time = time;
        this.note = note;
        this.listImage = listImage;
    }

    protected Product(Parcel in) {
        uid = in.readString();
        name = in.readString();
        idSeller = in.readString();
        price = in.readString();
        idType = in.readString();
        idCity = in.readString();
        idDistrict = in.readString();
        time = in.readString();
        note = in.readString();
        listImage = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public String getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(String idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(idSeller);
        dest.writeString(price);
        dest.writeString(idType);
        dest.writeString(idCity);
        dest.writeString(idDistrict);
        dest.writeString(time);
        dest.writeString(note);
        dest.writeStringList(listImage);
    }
}
