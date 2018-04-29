package com.haui.huantd.vifleamarket.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Account;

public class InfoActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    private FirebaseAuth auth;
    private ImageView btnBack;
    private ImageView btnSignOut;
    private TextView tvName, tvPhone, tvAddress;
    private Button btnEdit;
    private ImageView imgAvatar;
    private String uID;
    private FirebaseUser currentUser;
    private Uri imageUri;
    private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initComponents();


    }

    private void initComponents() {
        btnBack = findViewById(R.id.btn_back);
        btnSignOut = findViewById(R.id.btn_sign_out);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        btnEdit = findViewById(R.id.btn_edit);
        imgAvatar = findViewById(R.id.img_avatar);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        uID = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query queryUser = databaseReference.orderByChild("uid").equalTo(uID);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do with your result
                        Log.e("have account", issue.getValue().toString());
                        currentAccount = issue.getValue(Account.class);
                        setValues(currentAccount);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setOnClick();

    }

    private void setOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvName.isEnabled()) {
                    tvName.setEnabled(true);
                    tvAddress.setEnabled(true);
                    btnEdit.setText(getString(R.string.save));
                } else {
                    tvName.setEnabled(false);
                    tvAddress.setEnabled(false);
                    btnEdit.setText(getString(R.string.edit));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.child(uID).child("name").setValue(tvName.getText().toString());
                    databaseReference.child(uID).child("address").setValue(tvAddress.getText().toString());
                    databaseReference.child(uID).child("phone").setValue(currentUser.getPhoneNumber());
                    databaseReference.child(uID).child("uid").setValue(uID);
                }
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    public void setValues(Account values) {
        if (values.getName() != null) {
            tvName.setText(values.getName());
        }
        if (values.getAddress() != null) {
            tvAddress.setText(values.getAddress());
        }
        tvPhone.setText(values.getPhone());
        //StorageReference reference = FirebaseStorage.getInstance().getReference().child(values.getUrlAvatar());
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this).load(values.getUrlAvatar()).apply(options).into(imgAvatar);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imgAvatar);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagesAvatar").child((currentUser.getPhoneNumber() + ".jpg"));
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
                        FirebaseDatabase.getInstance().getReference().child("users").child(uID).child("urlAvatar").setValue(downloadUrl.toString());
                        //FirebaseDatabase.getInstance().getReference().child("users").child(uID).child("urlAvatar").setValue("imagesAvatar/" + currentUser.getPhoneNumber() + ".jpg");
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
