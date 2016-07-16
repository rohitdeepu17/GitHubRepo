package com.example.rohitsingla.mysmartprix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rohitsingla on 16/07/16.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder>{
    private ArrayList<StoreDataForList> storeList;
    private static final String TAG = ProductAdapter.class.getName();
    private static MyClickListener myClickListener;
    Context mContext;

    public StoreAdapter(Context c, ArrayList<StoreDataForList> storeList){
        mContext = c;
        this.storeList = storeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.store_item_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final StoreDataForList store = storeList.get(i);
        myViewHolder.price.setText("Rs." + store.getPrice());
        new DownloadImageTask(myViewHolder.image).execute(store.getImgLogoUrl());
        myViewHolder.buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Taking user to url "+store.getBuyUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(store.getBuyUrl()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView price;
        public ImageView image;
        public Button buttonBuy;

        public MyViewHolder(View view) {
            super(view);
            buttonBuy = (Button) view.findViewById(R.id.buttonBuyNow);
            price = (TextView) view.findViewById(R.id.storePrice);
            image = (ImageView) view.findViewById(R.id.storeLogo);
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

}
