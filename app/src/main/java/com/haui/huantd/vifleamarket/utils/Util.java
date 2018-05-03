package com.haui.huantd.vifleamarket.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    // Đọc nội dung text của một file nguồn.
    public static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getThoiGian(String thoiGian) {
        if (thoiGian == null) {
            return "null";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMMAT);
        Calendar cal = Calendar.getInstance();
        String time2 = dateFormat.format(cal.getTime());
        String day1 = thoiGian.substring(0, 9);
        String day2 = time2.substring(0, 9);
        Log.e("Util.getThoiGian", "getThoiGian: " + day1);
        if (day1.equals(day2)) {
            return thoiGian.substring(0, 9);
        } else {
            Log.e("Util.getThoiGian", "getThoiGian: " + thoiGian.substring(11, 15));
            return thoiGian.substring(11, 15);
        }
    }


}
