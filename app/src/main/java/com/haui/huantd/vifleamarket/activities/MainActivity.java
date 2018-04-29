package com.haui.huantd.vifleamarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.PostListAdapter;
import com.haui.huantd.vifleamarket.models.Account;
import com.haui.huantd.vifleamarket.models.Product;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageView btnAddProduct;
    private CircleImageView imgAvatar;
    private TextView tvName;
    private TextView tvInfo;
    private List<Product> listProduct;
    private RecyclerView recyclerView;
    private PostListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        imgAvatar = header.findViewById(R.id.img_avatar);
        tvName = header.findViewById(R.id.tv_name);
        tvInfo = header.findViewById(R.id.tv_address);

        imgAvatar.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnAddProduct.setOnClickListener(this);
        listProduct = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PostListAdapter(listProduct, this, new PostListAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) listProduct);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                listProduct.add(product);
                Log.e("MainActivity", product.getName());
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

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            btnAddProduct.setVisibility(View.GONE);
            tvName.setText(R.string.no_information);
            tvInfo.setText(R.string.no_information);
            Glide.with(this).load(R.drawable.ic_avatar2).into(imgAvatar);
        } else {
            btnAddProduct.setVisibility(View.VISIBLE);
            String uID = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            Query queryUser = databaseReference.orderByChild("uid").equalTo(uID);
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do with your result
                            Log.e("have account", issue.getValue().toString());
                            Account currentAccount = issue.getValue(Account.class);
                            updateView(currentAccount);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateView(Account currentAccount) {
        if (currentAccount.getUrlAvatar() != null) {
            Glide.with(MainActivity.this).load(currentAccount.getUrlAvatar()).into(imgAvatar);
        }
        if (currentAccount.getName() != null) {
            tvName.setText(currentAccount.getName());
        }
        if (currentAccount.getAddress() != null) {
            tvInfo.setText(currentAccount.getAddress());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            // Handle the camera action
        } else if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_vote) {

        } else if (id == R.id.img_avatar) {
            Log.e("CircleImageView", "xxxx");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
            case R.id.tv_name:
            case R.id.tv_address:
                showActivityInfo();
                break;
            case R.id.btn_add_product:
                showActivityAddProduct();
                break;
        }
    }

    private void showActivityAddProduct() {
        startActivity(new Intent(this, AddProductActivity.class));
    }

    private void showActivityInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            startActivity(new Intent(this, InfoActivity.class));
        }
    }
}
