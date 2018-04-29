package com.haui.huantd.vifleamarket.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    //can sua lai logic upload anh
    private static int RESULT_LOAD_IMAGE = 1;
    private TextView btnBack, btnPost;
    private ImageView img;
    private EditText edtName, edtPrice, edtNote;
    private Spinner spType, spProvince, spDistrict;
    private Uri imageUri;
    private String[] mListType = {"Đồ điện tử", "Đồ gia dụng", "Vật nuôi"};
    private String[] mListProvince = {"Hà Nội", "Đà Nẵng", "TP Hồ Chí Minh"};
    private String[] mListDistrict = {"Cầu Giấy", "Dan Phượng", "Hoài Đức"};
    private Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initComponents();
    }

    private void initComponents() {
        btnBack = findViewById(R.id.btn_cancel);
        btnPost = findViewById(R.id.btn_post);
        img = findViewById(R.id.img_image);
        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        edtNote = findViewById(R.id.ed_note);
        spType = findViewById(R.id.sp_type);
        spProvince = findViewById(R.id.sp_province);
        spDistrict = findViewById(R.id.sp_district);
        initSpinner();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });
    }

    private void initSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mListType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spType.setAdapter(adapter);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mListProvince);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spProvince.setAdapter(adapter1);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mListDistrict);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spDistrict.setAdapter(adapter2);
    }

    private void addPost() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        Product product = new Product();
        product.setName(edtName.getText().toString());
        product.setPrice(edtPrice.getText().toString());
        product.setIdSeller(FirebaseAuth.getInstance().getUid());
        product.setIdCity("001");
        product.setIdDistrict("002");
        List<String> listImg = new ArrayList<>();
        listImg.add(downloadUrl.toString());
        product.setListImage(listImg);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        product.setTime(dateFormat2.format(cal.getTime()));
        String id = FirebaseAuth.getInstance().getUid() + dateFormat.format(cal.getTime());
        databaseReference.child(id).setValue(product);
        finish();

    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Glide.with(this).load(imageUri).apply(options).into(img);
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
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
                        downloadUrl = taskSnapshot.getDownloadUrl();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, R.string.no_selected_photo, Toast.LENGTH_SHORT).show();
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
}
