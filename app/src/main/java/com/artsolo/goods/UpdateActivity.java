package com.artsolo.goods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class UpdateActivity extends AppCompatActivity {
    int id;
    double price;
    String special, title;
    byte[] imageBytes;
    ImageView updatedProductImage;
    EditText updatedSpecial, updatedName, updatedPrice;
    Button update_button;
    ImageSetter imageSetter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updatedProductImage = findViewById(R.id.updatedProductImage);

        updatedSpecial = findViewById(R.id.updatedSpecial);
        updatedName = findViewById(R.id.updatedName);
        updatedPrice = findViewById(R.id.updatedPrice);

        update_button = findViewById(R.id.update_button);

        getIntentData();
        setData();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(UpdateActivity.this)) {

                    myDatabaseHelper.updateProduct(
                            id,
                            updatedSpecial.getText().toString().trim(),
                            updatedName.getText().toString().trim(),
                            Double.parseDouble(updatedPrice.getText().toString().trim()),
                            ImageViveToByte(updatedProductImage)
                    );

                    Intent resultIntent = createResultIntent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (IllegalStateException e) {
                    Toast.makeText(update_button.getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
                }
            }
        });

        updatedProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSetter = new ImageSetter(UpdateActivity.this, updatedProductImage.getContext());
                imageSetter.setImage();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    void getIntentData() {
        if (getIntent().hasExtra("productData")) {
            Bundle bundle = getIntent().getBundleExtra("productData");
            if (bundle != null) {
                id = bundle.getInt("id");
                special = bundle.getString("special");
                title = bundle.getString("title");
                price = bundle.getDouble("price");
                imageBytes = bundle.getByteArray("image");
            } else {
                Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_LONG).show();
        }
    }

    void setData() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        updatedProductImage.setImageBitmap(bitmap);
        updatedName.setText(title);
        updatedSpecial.setText(special);
        updatedPrice.setText(String.valueOf(price));
    }

    private byte[] ImageViveToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    private Intent createResultIntent() {
        Bundle updatedData = new Bundle();
        updatedData.putInt("id", id);
        updatedData.putString("special", updatedSpecial.getText().toString().trim());
        updatedData.putString("title", updatedName.getText().toString().trim());
        updatedData.putDouble("price", Double.parseDouble(updatedPrice.getText().toString().trim()));
        updatedData.putByteArray("image", ImageViveToByte(updatedProductImage));

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedProductData", updatedData);
        return resultIntent;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent resultIntent = new Intent();
            if (getIntent().hasExtra("productData")) {
                Bundle bundle = getIntent().getBundleExtra("productData");
                resultIntent.putExtra("updatedProductData", bundle);
            }
            setResult(RESULT_OK, resultIntent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSetter.handleImageActivityResult(requestCode, resultCode, data, updatedProductImage);
    }
}