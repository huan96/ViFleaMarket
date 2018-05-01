package com.haui.huantd.vifleamarket.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.haui.huantd.vifleamarket.R;

public class AddProductActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initComponents();
    }

    private void initComponents() {

    }

    private void initSpinner() {

    }

    /*private void addPost() {
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

    }*/

   /* protected void onActivityResult(int reqCode, int resultCode, Intent data) {
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
    }*/
}
