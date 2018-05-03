package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.HinhAnhAdapter;
import com.haui.huantd.vifleamarket.utils.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class AddImagesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddImagesActivity";
    private ImageView btnBack, btnShow, btnAddImage, btnAddImage2;
    private LinearLayout layoutAddImage, layoutShowImage;
    private RecyclerView rvImage;
    private int PICK_IMAGE_MULTIPLE = 1;
    private List<String> imagesEncodedList;
    private HinhAnhAdapter hinhAnhAdapter;
    private Button btnTiepTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        initViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnShow = findViewById(R.id.btn_show);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnAddImage2 = findViewById(R.id.btn_add_img2);
        layoutAddImage = findViewById(R.id.layout_add_image);
        layoutShowImage = findViewById(R.id.layout_show_image);
        rvImage = findViewById(R.id.rv_images);
        btnTiepTuc = findViewById(R.id.btn_tiep_tuc);

        btnBack.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        btnTiepTuc.setOnClickListener(this);
        btnAddImage2.setOnClickListener(this);

        imagesEncodedList = new ArrayList<>();
        ImageManager imageManager = new ImageManager(this);
        imagesEncodedList = imageManager.getAllImage();
        hinhAnhAdapter = new HinhAnhAdapter(imagesEncodedList, this, new HinhAnhAdapter.OnImageClick() {

            @Override
            public void onDeleteClick(int position) {
                imagesEncodedList.remove(position);
                hinhAnhAdapter.notifyDataSetChanged();
                if (imagesEncodedList.size() == 0) {
                    layoutShowImage.setVisibility(View.GONE);
                    layoutAddImage.setVisibility(View.VISIBLE);
                }
            }
        });

        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImage.setAdapter(hinhAnhAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + imagesEncodedList.size());
        showListImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                Intent intent = new Intent(AddImagesActivity.this, HuyenActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_show: {
                Intent intent = new Intent(AddImagesActivity.this,
                        AddProductActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_tiep_tuc: {
                tiepTuc();
                break;
            }
            case R.id.btn_add_image: {
                addImage();
                break;
            }
            case R.id.btn_add_img2: {
                addImage();
                break;
            }

        }
    }

    private void tiepTuc() {
        //luu list image sang csdl
        if (imagesEncodedList.size() > 0) {
            ImageManager imageManager = new ImageManager(this);
            imageManager.deleteListImage();
            for (String url : imagesEncodedList) {
                imageManager.addImage(url);
            }
            startActivity(new Intent(AddImagesActivity.this, GiaActivity.class));
            finish();
        } else {
            Toast.makeText(this, getString(R.string.them_anh), Toast.LENGTH_SHORT).show();
        }
    }


    private void addImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            Log.e(TAG, "onActivityResult: getClipData");
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imagesEncodedList.add(imageUri.toString());
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                        }
                    } else if (data.getData() != null) {
                        Log.e(TAG, "onActivityResult: getData");
                        String imagePath = data.getData().getPath();
                        imagesEncodedList.add(imagePath);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                    showListImage();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.khong_the_chon_anh, Toast.LENGTH_SHORT).show();
        }
    }

    private void showListImage() {
        if (imagesEncodedList.size() > 0) {
            layoutShowImage.setVisibility(View.VISIBLE);
            layoutAddImage.setVisibility(View.GONE);
            hinhAnhAdapter.notifyDataSetChanged();
            Log.e(TAG, "showListImage: " + imagesEncodedList.size());
        } else {
            Toast.makeText(this, R.string.chua_chon_anh, Toast.LENGTH_SHORT).show();
            layoutShowImage.setVisibility(View.GONE);
            layoutAddImage.setVisibility(View.VISIBLE);
        }
    }
}
