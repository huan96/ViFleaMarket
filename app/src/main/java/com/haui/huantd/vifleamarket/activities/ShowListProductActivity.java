package com.haui.huantd.vifleamarket.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.haui.huantd.vifleamarket.R;

public class ShowListProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_product);
    }
}
/*
}
package com.haui.huantd.vifleamarket.activities;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Parcelable;
        import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

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
        import com.haui.huantd.vifleamarket.activities.list_activity_add_product.AddProductActivity;
        import com.haui.huantd.vifleamarket.adapters.PostListAdapter;
        import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
        import com.haui.huantd.vifleamarket.models.Account;
        import com.haui.huantd.vifleamarket.models.Product;

        import java.util.ArrayList;
        import java.util.List;

        import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1234;
    private ImageView btnAddProduct;
    private CircleImageView imgAvatar;
    private TextView tvName;
    private TextView tvInfo;
    private List<Product> listProduct;
    private List<Product> listProductShow;
    private RecyclerView recyclerView;
    private PostListAdapter adapter;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        checkPermission();
    }

    private void initComponents() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        listProductShow = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PostListAdapter(recyclerView, listProductShow, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) listProductShow);
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
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                listProduct.add(product);
                if (listProductShow.size() < 10) {
                    listProductShow.add(product);
                }
                Log.e("MainActivity", product.getTieuDe());
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

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Please allow the app to read from external " +
                            "storage to update the playlist", Toast.LENGTH_SHORT).show();
                    checkPermission();
                }
                return;
            }
        }
    }
}
*/
