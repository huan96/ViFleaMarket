package com.haui.huantd.vifleamarket.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by huand on 8/10/2017.
 */

public class PreferencesManager {
    public static final String FILE_NAME = "ViFleaMarket";

    public static void saveDanhMuc(String danhMuc, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.DANH_MUC, danhMuc);
        editor.commit();
    }

    public static String getDanhMuc(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.DANH_MUC, "");
    }

    public static void saveLoaiSP(String loaiSP, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOAI_SAN_PHAM, loaiSP);
        editor.commit();
    }

    public static String getLoaiSP(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.LOAI_SAN_PHAM, "");
    }

    public static void saveTinh(String tinh, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TINH_THANH_PHO, tinh);
        editor.commit();
    }

    public static String getTinh(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TINH_THANH_PHO, "");
    }

    public static void saveHuyen(String huyen, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.HUYEN, huyen);
        editor.commit();
    }

    public static String getHuyen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.HUYEN, "");
    }

    public static void saveGia(String gia, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.GIA, gia);
        editor.commit();
    }

    public static String getGia(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.GIA, "");
    }

    public static void saveTieuDe(String tieuDe, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TITLE, tieuDe);
        editor.commit();
    }

    public static String getTieuDe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.TITLE, "");
    }

    public static void saveThongTin(String thongtin, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.INFOR, thongtin);
        editor.commit();
    }

    public static String getThongTin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.INFOR, "");
    }

    public static void saveUrlImage(String url, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.URL_IMAGE, url);
        editor.commit();
    }

    public static String getUrlImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.URL_IMAGE, "");
    }

    public static void savePathImage(String path, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PATH_IMAGE, path);
        editor.commit();
    }

    public static String getPathImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PATH_IMAGE, "");
    }
}
