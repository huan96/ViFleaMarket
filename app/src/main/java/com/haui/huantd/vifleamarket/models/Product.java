package com.haui.huantd.vifleamarket.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by huand on 02/10/18.
 */

public class Product implements Parcelable {
    private String id;
    private String tieuDe;
    private String idNguoiBan;
    private String gia;
    private String danhMuc;
    private String loaiSP;
    private String tinh;
    private String huyen;
    private String thoiGian;
    private String chiTiet;
    private List<String> listImage;

    public Product() {
    }

    public Product(String id, String tieuDe, String idNguoiBan, String gia, String danhMuc
            , String loaiSP, String tinh, String huyen, String thoiGian, String chiTiet
            , List<String> listImage) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.idNguoiBan = idNguoiBan;
        this.gia = gia;
        this.danhMuc = danhMuc;
        this.loaiSP = loaiSP;
        this.tinh = tinh;
        this.huyen = huyen;
        this.thoiGian = thoiGian;
        this.chiTiet = chiTiet;
        this.listImage = listImage;
    }

    protected Product(Parcel in) {
        id = in.readString();
        tieuDe = in.readString();
        idNguoiBan = in.readString();
        gia = in.readString();
        danhMuc = in.readString();
        loaiSP = in.readString();
        tinh = in.readString();
        huyen = in.readString();
        thoiGian = in.readString();
        chiTiet = in.readString();
        listImage = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tieuDe);
        dest.writeString(idNguoiBan);
        dest.writeString(gia);
        dest.writeString(danhMuc);
        dest.writeString(loaiSP);
        dest.writeString(tinh);
        dest.writeString(huyen);
        dest.writeString(thoiGian);
        dest.writeString(chiTiet);
        dest.writeStringList(listImage);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getIdNguoiBan() {
        return idNguoiBan;
    }

    public void setIdNguoiBan(String idNguoiBan) {
        this.idNguoiBan = idNguoiBan;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public String getHuyen() {
        return huyen;
    }

    public void setHuyen(String huyen) {
        this.huyen = huyen;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }
}
