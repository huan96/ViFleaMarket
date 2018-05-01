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


}
