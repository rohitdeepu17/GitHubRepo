package com.example.rohitsingla.lazyloadingimagefromurl;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MyActivity extends Activity {

    ListView mList;
    LazyImageLoadAdapter mAdapter;

    // Image urls used in LazyImageLoadAdapter.java file

    private String[] mStrings= {
            "http://androidexample.com/media/webservice/LazyListView_images/image0.png",
            "http://androidexample.com/media/webservice/LazyListView_images/image1.png",
            "http://androidexample.com/media/webservice/LazyListView_images/image2.png",
            "http://androidexample.com/media/webservice/LazyListView_images/image3.png",
            "http://androidexample.com/media/webservice/LazyListView_images/image4.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mList = (ListView)findViewById(R.id.list);

        // Create custom adapter for listview
        mAdapter=new LazyImageLoadAdapter(this, mStrings);

        //setting adapter to this listview
        mList.setAdapter(mAdapter);

        Button b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(listener);
    }

    @Override
    public void onDestroy(){
        mList.setAdapter(null);
        super.onDestroy();
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Refresh cache directory downloaded images
            mAdapter.mImageLoader.clearCache();
            mAdapter.notifyDataSetChanged();
        }
    };

    public void onItemClick(int mPosition){
        String clickedURLString = mStrings[mPosition];
        Toast.makeText(this,"The clicked item URL is : "+clickedURLString,Toast.LENGTH_SHORT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
