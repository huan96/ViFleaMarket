package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.SellProductListAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ListSellProductActivity extends AppCompatActivity {
    private static final String TAG = "ListSellProductActivity";
    private List<Product> listProduct;
    private List<String> listID;
    private SellProductListAdapter adapter;
    private ImageView btnBack;
    private RecyclerView rvPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sell_product);
        listProduct = new ArrayList<>();
        listID = new ArrayList<>();
        adapter = new SellProductListAdapter(listProduct, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(ListSellProductActivity.this, ShowProductActivity.class);
                Bundle bundle = new Bundle();
                Log.e(TAG, "onClick: " + position + listProduct.get(position).getTieuDe());
                bundle.putSerializable(Constants.PRODUCT, listProduct.get(position));
                intent.putExtra(Constants.PRODUCT, bundle);
                startActivity(intent);
            }
        });
        initView();
        getData();
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvPost = findViewById(R.id.rv_list_product);
        rvPost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPost.setAdapter(adapter);
    }

    private void getData() {
        getListIDSellProduct();

    }

    private void getListIDSellProduct() {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        databaseReference.child(uID).child(Constants.NON_CONFIRM_POST).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = (String) dataSnapshot.getValue();
                listID.add(id);
                Log.e(TAG, "listID.onChildAdded: ");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(Constants.NON_CONFIRM_POST);
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                product.setConfirm(false);
                Log.e(TAG, "NON_CONFIRM_POST.onChildAdded: ");
                listProduct.add(product);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(Constants.CONFIRM_POST);
        databaseReference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                product.setConfirm(true);
                listProduct.add(product);
                adapter.notifyDataSetChanged();
                Log.e(TAG, "CONFIRM_POST.onChildAdded: ");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
