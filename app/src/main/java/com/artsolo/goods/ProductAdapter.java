package com.artsolo.goods;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<Size> sizes;
    private final MyDatabaseHelper myDatabaseHelper;

    public ProductAdapter(Activity activity, Context context, List<Size> sizes) {
        this.context = context;
        this.sizes = sizes;
        this.myDatabaseHelper = new MyDatabaseHelper(activity);
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.size_row, parent, false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        Size size = sizes.get(position);

        holder.imageDeleteSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.delete) + " " + size.getSizeName() + " " + context.getString(R.string.size).toLowerCase() + "?");
                builder.setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDatabaseHelper.deleteSize(size.getSizeId());
                        sizes.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton(R.string.answer_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

        holder.sizeOfProduct.setText(size.getSizeName());
        holder.amountOfSizes.setText(String.valueOf(size.getSizeAmount()));
        // fixed sizes

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size.decreaseAmount();
                myDatabaseHelper.changeSizeAmount(size.getSizeId(), size.getSizeAmount());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size.increaseAmount();
                myDatabaseHelper.changeSizeAmount(size.getSizeId(), size.getSizeAmount());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageDeleteSize;
        TextView sizeOfProduct, amountOfSizes;
        Button decreaseButton, increaseButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDeleteSize = itemView.findViewById(R.id.imageDeleteSize);
            sizeOfProduct = itemView.findViewById(R.id.sizeOfProduct);
            amountOfSizes = itemView.findViewById(R.id.amountOfSizes);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
        }
    }
}
