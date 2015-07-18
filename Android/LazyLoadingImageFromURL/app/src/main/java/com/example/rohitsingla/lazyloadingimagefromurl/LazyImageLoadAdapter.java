package com.example.rohitsingla.lazyloadingimagefromurl;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rohitsingla on 18/07/15.
 */
public class LazyImageLoadAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity mActivity;
    private String[] mData;
    private static LayoutInflater mLayoutInflater = null;
    public ImageLoader mImageLoader;

    public LazyImageLoadAdapter(Activity a, String[] d){
        mActivity = a;
        mData = d;
        mLayoutInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        mImageLoader = new ImageLoader(mActivity.getApplicationContext());
    }

    public int getCount(){
        return mData.length;
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView textWide;
        public ImageView image;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        View mView = convertView;
        ViewHolder mViewHolder;

        if(convertView == null){
            //inflating layout file for each row
            mView = mLayoutInflater.inflate(R.layout.listview_row, null);

            //creating ViewHolder object to fill all elements of layout
            mViewHolder = new ViewHolder();
            mViewHolder.text = (TextView)mView.findViewById(R.id.text);
            mViewHolder.text1=(TextView)mView.findViewById(R.id.text1);
            mViewHolder.image=(ImageView)mView.findViewById(R.id.image);

            //setting holder with layout inflater
            mView.setTag(mViewHolder);
        }
        else{
            mViewHolder = (ViewHolder)mView.getTag();
        }

        mViewHolder.text.setText("Company : "+position);
        mViewHolder.text1.setText("Company Description : "+position);
        ImageView mImageView = mViewHolder.image;

        //calling DisplayImage function from ImageLoader class
        mImageLoader.DisplayImage(mData[position],mImageView);

        //setting onClickListener on each row of the listview
        mView.setOnClickListener(new OnItemClickListener(position));
        return mView;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            MyActivity sct = (MyActivity)mActivity;
            sct.onItemClick(mPosition);
        }
    }
}
