package com.example.rohitsingla.mysmartprix;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rohitsingla on 16/07/16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private ArrayList<ProductDataForList> productList;
    private static final String TAG = ProductAdapter.class.getName();
    private static MyClickListener myClickListener;

    public ProductAdapter(ArrayList<ProductDataForList> productList){
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.product_item_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ProductDataForList product = productList.get(i);
        myViewHolder.name.setText(product.getName());
        myViewHolder.price.setText("Rs."+product.getPrice());
        new DownloadImageTask(myViewHolder.image).execute(product.getImgUrl());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.productName);
            price = (TextView) view.findViewById(R.id.productPrice);
            image = (ImageView) view.findViewById(R.id.productImg);
            Log.d(TAG, "Adding listener to item");
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }

    public void setData(ArrayList<ProductDataForList> array) {
        productList.addAll(array);
    }

}
