package com.haui.huantd.vifleamarket.utils;

import android.content.Context;
import android.util.Log;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.DanhMuc;
import com.haui.huantd.vifleamarket.models.LoaiSanPham;
import com.haui.huantd.vifleamarket.models.Huyen;
import com.haui.huantd.vifleamarket.models.Tinh;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataManager {

    private static final String TAG = DataManager.class.getName();

    public static ArrayList<Tinh> getListTinh(Context context) {
        ArrayList<Tinh> listTinh = new ArrayList<>();
        // Đọc nội dung text của file company.json
        String jsonText = null;
        try {
            jsonText = Util.readText(context, R.raw.tinh);
            // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
            JSONArray jsonRoot = new JSONArray(jsonText);

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject obj = (JSONObject) jsonRoot.get(i);
                String id = obj.getString("MaTinh");
                String name = obj.getString("Ten");
                String type = obj.getString("Loai");

                Tinh tinh = new Tinh();
                tinh.setId(id);
                tinh.setName(name);
                tinh.setType(type);
                listTinh.add(tinh);
            }
        } catch (Exception e) {
            Log.d(TAG, "readJSONTinh: " + e.toString());
        }
        return listTinh;
    }

    public static ArrayList<Huyen> getListHuyen(Context context) {
        ArrayList<Huyen> listHuyen = new ArrayList<>();
        String jsonText = null;
        try {
            jsonText = Util.readText(context, R.raw.huyen);
            JSONArray jsonRoot = new JSONArray(jsonText);

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject obj = (JSONObject) jsonRoot.get(i);
                String id = obj.getString("MaHuyen");
                String name = obj.getString("Ten");
                String type = obj.getString("Loai");
                String idTinh = obj.getString("MaTinh");

                Huyen huyen = new Huyen();
                huyen.setId(id);
                huyen.setName(name);
                huyen.setType(type);
                huyen.setIdTinh(idTinh);
                listHuyen.add(huyen);
            }
        } catch (Exception e) {
            Log.d(TAG, "readJSONTinh: " + e.toString());
        }
        return listHuyen;
    }

    public static ArrayList<DanhMuc> getListDanhMuc(Context context) {
        ArrayList<DanhMuc> listDanhMuc = new ArrayList<>();
        String jsonText = null;
        try {
            jsonText = Util.readText(context, R.raw.danhmuc);
            JSONArray jsonRoot = new JSONArray(jsonText);

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject obj = (JSONObject) jsonRoot.get(i);
                String id = obj.getString("ID");
                String name = obj.getString("Ten");

                DanhMuc danhMuc = new DanhMuc();
                danhMuc.setId(id);
                danhMuc.setName(name);
                listDanhMuc.add(danhMuc);
            }
        } catch (Exception e) {
            Log.d(TAG, "readJSONTinh: " + e.toString());
        }
        return listDanhMuc;
    }

    public static ArrayList<LoaiSanPham> getListDanhMucNho(Context context) {
        ArrayList<LoaiSanPham> listDanhMuc = new ArrayList<>();
        String jsonText = null;
        try {
            jsonText = Util.readText(context, R.raw.danhmucnho);
            JSONArray jsonRoot = new JSONArray(jsonText);

            for (int i = 0; i < jsonRoot.length(); i++) {
                JSONObject obj = (JSONObject) jsonRoot.get(i);
                String id = obj.getString("ID");
                String name = obj.getString("Ten");
                String idDanhMuc = obj.getString("MaDM");

                LoaiSanPham danhMuc = new LoaiSanPham();
                danhMuc.setId(id);
                danhMuc.setName(name);
                danhMuc.setIdDanhMuc(idDanhMuc);
                listDanhMuc.add(danhMuc);
            }
        } catch (Exception e) {
            Log.d(TAG, "readJSONTinh: " + e.toString());
        }
        return listDanhMuc;
    }

}
