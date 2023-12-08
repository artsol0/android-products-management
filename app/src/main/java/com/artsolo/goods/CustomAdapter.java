package com.artsolo.goods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Activity activity;
    private Context context;
    private List<Product> products;
    private Animation translate_anim;

    public CustomAdapter(Activity activity, Context context, List<Product> products) {
        this.activity = activity;
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        final Product product = products.get(position);

        byte[] imageBytes = product.getImageBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        holder.productImage.setImageBitmap(bitmap);
        holder.productTitle.setText(product.getTitle());
        holder.productSpecial.setText(product.getSpecial());
        holder.productPrice.setText(String.valueOf(product.getPrice()).replaceAll("\\.0$", ""));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", product.getId());
                bundle.putString("special", product.getSpecial());
                bundle.putString("title", product.getTitle());
                bundle.putDouble("price", product.getPrice());
                bundle.putByteArray("image", product.getImageBytes());

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("productData", bundle);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productSpecial, productPrice;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productSpecial = itemView.findViewById(R.id.productSpecial);
            productPrice = itemView.findViewById(R.id.productPrice);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            translate_anim.setDuration(800);
            mainLayout.setAnimation(translate_anim);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredProducts(List<Product> filteredProducts) {
        this.products = filteredProducts;
        notifyDataSetChanged();
    }
}
