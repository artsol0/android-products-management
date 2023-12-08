package com.artsolo.goods;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    Product product;
    ImageView productSetPhoto;
    TextView productSetName, productSetSpecial, productSetPrice;
    Button addSizeButton;
    RecyclerView recyclerSizes;
    MyDatabaseHelper myDatabaseHelper;
    List<Size> sizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productSetPhoto = findViewById(R.id.productSetPhoto);

        productSetName = findViewById(R.id.productSetName);
        productSetSpecial = findViewById(R.id.productSetSpecial);
        productSetPrice = findViewById(R.id.productSetPrice);

        addSizeButton = findViewById(R.id.addSizeButton);
        addSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AddSizeActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("title", product.getTitle());
                updateSizesProduct.launch(intent);
            }
        });

        recyclerSizes = findViewById(R.id.recyclerSizes);

        getIntentData();
        setData();

        myDatabaseHelper = new MyDatabaseHelper(ProductActivity.this);
        sizes = getSizesInList();
        ProductAdapter productAdapter = new ProductAdapter(ProductActivity.this, this, sizes);
        recyclerSizes.setAdapter(productAdapter);
        recyclerSizes.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            confirmDialog();
        } else if (item.getItemId() == R.id.edit_item) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", product.getId());
            bundle.putString("special", product.getSpecial());
            bundle.putString("title", product.getTitle());
            bundle.putDouble("price", product.getPrice());
            bundle.putByteArray("image", product.getImageBytes());

            Intent intent = new Intent(ProductActivity.this, UpdateActivity.class);
            intent.putExtra("productData", bundle);
            updateActivityProduct.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void getIntentData() {
        if (getIntent().hasExtra("productData")) {
            Bundle bundle = getIntent().getBundleExtra("productData");
            if (bundle != null) {
                product = new Product(bundle.getInt("id"),
                        bundle.getString("special"),
                        bundle.getString("title"),
                        bundle.getDouble("price"),
                        bundle.getByteArray("image"));
            } else {
                Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_LONG).show();
        }
    }

    void setData() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImageBytes(), 0, product.getImageBytes().length);
        productSetPhoto.setImageBitmap(bitmap);
        productSetName.setText(product.getTitle());
        productSetSpecial.setText(product.getSpecial());
        productSetPrice.setText(String.valueOf(product.getPrice()).replaceAll("\\.0$", ""));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(product.getTitle());
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete) + " " + product.getTitle() + "?");
        builder.setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try (MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(ProductActivity.this)) {
                    myDatabaseHelper.deleteProduct(product.getId());
                    Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (IllegalStateException e) {
                    Toast.makeText(ProductActivity.this, R.string.error_occurred, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(R.string.answer_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    List<Size> getSizesInList() {
        List<Size> list = new ArrayList<>();
        Cursor cursor = myDatabaseHelper.getAllSizes(product.getId());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new Size(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3))
                        // maybe for fixed sizes
                );
            }
        }

        return list;
    }

    private ActivityResultLauncher<Intent> updateActivityProduct = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Bundle updatedData = result.getData().getBundleExtra("updatedProductData");
                        if (updatedData != null) {
                            product = new Product(updatedData.getInt("id"),
                                    updatedData.getString("special"),
                                    updatedData.getString("title"),
                                    updatedData.getDouble("price"),
                                    updatedData.getByteArray("image"));

                            setData();
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> updateSizesProduct = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    recreate();
                }
            });
}