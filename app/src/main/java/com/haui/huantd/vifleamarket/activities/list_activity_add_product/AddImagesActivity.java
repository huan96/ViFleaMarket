package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddImagesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddImagesActivity";
    private ImageView btnBack, btnShow, btnAddImage, imgShow;
    private LinearLayout layoutAddImage, layoutShowImage;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button btnTiepTuc;
    private Uri imageUri;

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
        imgShow = findViewById(R.id.img_image);
        layoutAddImage = findViewById(R.id.layout_add_image);
        layoutShowImage = findViewById(R.id.layout_show_image);
        btnTiepTuc = findViewById(R.id.btn_tiep_tuc);

        btnBack.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        imgShow.setOnClickListener(this);
        btnTiepTuc.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: ");
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
            case R.id.btn_add_image:
            case R.id.img_image:
                getImageFromAlbum();
                break;

        }
    }

    private void tiepTuc() {
        //luu list image sang csdl
        if (!PreferencesManager.getUrlImage(this).equals("")) {
            startActivity(new Intent(AddImagesActivity.this, GiaActivity.class));
            finish();
        } else {
            Toast.makeText(this, getString(R.string.them_anh), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imgShow);
                PreferencesManager.savePathImage(imageUri.toString(), this);
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMMAT2);
                Calendar cal = Calendar.getInstance();
                String id = FirebaseAuth.getInstance().getUid() + dateFormat.format(cal.getTime());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagePost").child((id + ".jpg"));
                Log.e("xxx", String.valueOf(imageUri));
                UploadTask uploadTask = storageRef.putFile(imageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        PreferencesManager.saveUrlImage(downloadUrl.toString(), AddImagesActivity.this);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, R.string.khong_the_chon_anh, Toast.LENGTH_SHORT).show();
        }
    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }


    private void showListImage() {
        String url = PreferencesManager.getUrlImage(this);
        if (!url.equals("")) {
            layoutShowImage.setVisibility(View.VISIBLE);
            layoutAddImage.setVisibility(View.GONE);
            Log.e(TAG, "showListImage: ");
        } else {
            Toast.makeText(this, R.string.chua_chon_anh, Toast.LENGTH_SHORT).show();
            layoutShowImage.setVisibility(View.GONE);
            layoutAddImage.setVisibility(View.VISIBLE);
        }
    }
}
