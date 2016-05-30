package com.example.rohitsingla.autocompletetextviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

//Reference : http://www.tutorialspoint.com/android/android_auto_complete.htm
public class MyActivity extends Activity {
    AutoCompleteTextView mACTV;
    MultiAutoCompleteTextView multiACTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mACTV = (AutoCompleteTextView)findViewById(R.id.auto_compete_text_view);
        multiACTV = (MultiAutoCompleteTextView)findViewById(R.id.multi_auto_complete_text_view);
        String[] countries = getResources().getStringArray(R.array.list_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,countries);
        mACTV.setAdapter(adapter);
        mACTV.setThreshold(1);

        multiACTV.setAdapter(adapter);
        multiACTV.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
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
