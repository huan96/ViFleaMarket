package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.haui.huantd.vifleamarket.adapters.PostListAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ListFavouriteActivity extends AppCompatActivity {

    private static final String TAG = "ListFavouriteActivity";
    private List<Product> listProduct;
    private List<Product> listProductShow;
    private List<String> listID;
    private PostListAdapter adapter;
    private ImageView btnBack;
    private RecyclerView rvPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favourite);
        listProduct = new ArrayList<>();
        listProductShow = new ArrayList<>();
        listID = new ArrayList<>();
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
        adapter = new PostListAdapter(rvPost, listProductShow, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(ListFavouriteActivity.this, ShowProductActivity.class);
                Bundle bundle = new Bundle();
                Log.e(TAG, "onClick: " + position + listProductShow.get(position).getTieuDe());
                bundle.putSerializable(Constants.PRODUCT, listProductShow.get(position));
                intent.putExtra(Constants.PRODUCT, bundle);
                startActivity(intent);
            }
        });
        adapter.setLoadMore(new PostListAdapter.ILoadMore() {
            @Override
            public void onLoadMore() {
                listProductShow.add(null);
                adapter.notifyItemInserted(listProductShow.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listProductShow.remove(listProductShow.size() - 1);
                        adapter.notifyItemRemoved(listProductShow.size());
                        addNewPost();
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                    }
                }, 300); // Time to load
            }
        });
        rvPost.setAdapter(adapter);
    }

    private void addNewPost() {
        int sizeListProductShow = listProductShow.size() - 1;
        int sizeListProduct = listProduct.size() - 1;
        if ((sizeListProduct - sizeListProductShow) >= 10) {
            int sizeListProductShowNew = sizeListProductShow + 10;
            for (int i = sizeListProductShow; i < sizeListProductShowNew; i++) {
                listProductShow.add(listProduct.get(i));
            }
        } else {
            for (int i = sizeListProductShow; i < sizeListProduct; i++) {
                listProductShow.add(listProduct.get(i));
            }
        }
    }

    private void getData() {
        getListIDProduct();

    }

    private void getListIDProduct() {
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        databaseReference.child(uID).child(Constants.LIST_PRODUCT_LIKE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
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

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(Constants.CONFIRM_POST);
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                Log.e(TAG, "CONFIRM_POST.onChildAdded: ");
                listProduct.add(product);
                if (listProductShow.size() < 10) {
                    listProductShow.add(product);
                }
                Log.e("onChildAdded", "xxxxxxxx");
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

    }

}
