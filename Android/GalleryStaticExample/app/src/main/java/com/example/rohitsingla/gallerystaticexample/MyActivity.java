package com.example.rohitsingla.gallerystaticexample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class MyActivity extends Activity implements ViewSwitcher.ViewFactory{
    TextView mySelection;
    Gallery myGallery;
    ImageView img;

    ImageSwitcher imgSwitcher;

    final int[] myImageIds = {
            R.drawable.bear,
            R.drawable.bonobo,
            R.drawable.eagle,
            R.drawable.horse,
            R.drawable.owl,
            R.drawable.wolf
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        /*imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imgSwitcher.setFactory(MyActivity.this);
        imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
*/


        mySelection = (TextView) findViewById(R.id.textView2);
        myGallery = (Gallery) findViewById(R.id.myGallery);
        img = (ImageView) findViewById(R.id.imageView1);

        myGallery.setAdapter(new ImageAdapter(this));

        myGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {

                mySelection.setText(" selected option: " + position );

                img.setImageResource(myImageIds[position]);

                //imgSwitcher.setImageResource(myImageIds[position]);
            }

            public void onNothingSelected(AdapterView<?> parent) {

                mySelection.setText("Nothing selected");
            }
        });
    }

    @Override
    public View makeView() {
        ImageView iView = new ImageView(this);
        iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new ImageSwitcher.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iView.setBackgroundColor(0xFF000000);
        return iView;
    }

    public class ImageAdapter extends BaseAdapter {

        private Context myContext;

        /*private int[] myImageIds = {
                R.drawable.IMG_0768,
                R.drawable.IMG_0769,
                R.drawable.IMG_0770,
                R.drawable.IMG_0772,
                R.drawable.IMG_0773,
                R.drawable.IMG_0776,
                R.drawable.IMG_0779,
                R.drawable.IMG_0782,
                R.drawable.IMG_0786
        };*/

        public ImageAdapter(Context c) {
            this.myContext = c;
        }

        public int getCount() {
            return myImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        // Returns a new ImageView to be displayed,
        public View getView(int position, View convertView,
                            ViewGroup parent) {

            ImageView iv = new ImageView(this.myContext);
            iv.setImageResource(myImageIds[position]);

            iv.setScaleType(ImageView.ScaleType.FIT_END);

            // Set the Width & Height of the individual images
            iv.setLayoutParams(new Gallery.LayoutParams(100, 70));

            return iv;
        }
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
