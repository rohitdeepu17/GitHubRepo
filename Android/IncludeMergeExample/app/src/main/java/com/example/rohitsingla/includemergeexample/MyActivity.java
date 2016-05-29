package com.example.rohitsingla.includemergeexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

//Reference 1 : http://www.mysamplecode.com/2012/06/android-include-merge-layout-example.html
//Reference 2 : https://alltechsolution.wordpress.com/2012/06/13/difference-between-merge-includeand-tag-in-android/
public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        LinearLayout myLayout = (LinearLayout)findViewById(R.id.linearLayout1);
        View itemInfo1 = getLayoutInflater().inflate(R.layout.item, myLayout, true);
        View itemInfo2 = getLayoutInflater().inflate(R.layout.item, myLayout, true);
        View itemInfo3 = getLayoutInflater().inflate(R.layout.item, myLayout, true);
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
