package com.artsolo.goods;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class AddActivity extends AppCompatActivity {
    ImageSetter imageSetter;
    ImageView newProductImage;
    EditText editTextNumber, editTextName, editTextPrice;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        newProductImage = findViewById(R.id.newProductImage);

        editTextNumber = findViewById(R.id.editTextNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);

        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsValid()) {
                    try (MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(AddActivity.this)) {
                        myDatabaseHelper.addNewProduct(
                                editTextNumber.getText().toString().trim(),
                                editTextName.getText().toString().trim(),
                                Double.parseDouble(editTextPrice.getText().toString().trim()),
                                ImageViveToByte(newProductImage)
                        );

                        newProductImage.setImageResource(R.drawable.baseline_add_a_photo_24);
                        editTextNumber.setText("");
                        editTextName.setText("");
                        editTextPrice.setText("");
                    } catch (IllegalStateException e) {
                        Toast.makeText(add_button.getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSetter = new ImageSetter(AddActivity.this, newProductImage.getContext());
                imageSetter.setImage();
            }
        });
    }

    private byte[] ImageViveToByte(ImageView image) {
        Bitmap bitmap = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if (image.getDrawable() instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        } else {
            Drawable drawable = ContextCompat.getDrawable(AddActivity.this, R.drawable.baseline_add_a_photo_24);
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        }

        return stream.toByteArray();
    }

    boolean dataIsValid() {
        if (editTextNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, this.getString(R.string.product_number) + " " + this.getString(R.string.is_mandatory), Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextName.getText().toString().isEmpty()) {
            Toast.makeText(this, this.getString(R.string.product_name) + " " + this.getString(R.string.is_mandatory), Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, this.getString(R.string.price) + " " + this.getString(R.string.is_mandatory), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSetter.handleImageActivityResult(requestCode, resultCode, data, newProductImage);
    }
}