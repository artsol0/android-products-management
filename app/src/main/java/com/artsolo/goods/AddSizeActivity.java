package com.artsolo.goods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddSizeActivity extends AppCompatActivity {

    EditText productSizeName, productSizeAmount;
    Button addNewSizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_size);

        productSizeName = findViewById(R.id.productSizeName);
        productSizeAmount = findViewById(R.id.productSizeAmount);

        addNewSizeButton = findViewById(R.id.addNewSizeButton);

        int productId = getIntent().getIntExtra("id", -1);
        String productTitle = getIntent().getStringExtra("title");

        addNewSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsValid()) {
                    try (MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(AddSizeActivity.this)) {
                        myDatabaseHelper.addNewSize(productId,
                                productSizeName.getText().toString().trim(),
                                Integer.parseInt(productSizeAmount.getText().toString().trim()));

                        productSizeName.setText("");
                        productSizeAmount.setText("");

                    } catch (IllegalStateException e) {
                        Toast.makeText(addNewSizeButton.getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(productTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    boolean dataIsValid() {
        if (productSizeName.getText().toString().isEmpty()) {
            Toast.makeText(this, this.getString(R.string.size) + " " + this.getString(R.string.is_mandatory), Toast.LENGTH_LONG).show();
            return false;
        } else if (productSizeAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, this.getString(R.string.amount) + " " + this.getString(R.string.is_mandatory), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}