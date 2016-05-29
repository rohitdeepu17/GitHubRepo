package com.example.rohitsingla.threadcommunication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

//Reference 1 : http://indyvision.net/2010/02/android-threads-tutorial-part-3/
//Reference 2 : http://techtej.blogspot.in/2011/02/android-passing-data-between-main.html

public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";
    private MyThread myThread;
    // text view influenced by the Thread
    private TextView threadModifiedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        threadModifiedText = (TextView) findViewById(R.id.text);

        myThread = new MyThread(mainHandler);
        myThread.start();
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"Inside onTouchEvent");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG,"inside Action_down");
            // prepare a message with touch location data
            Message messageToThread = new Message();
            messageToThread.what = 0;

            Bundle messageData = new Bundle();
            messageData.putFloat("location_x", event.getX());
            messageData.putFloat("location_y", event.getY());

            messageToThread.setData(messageData);

            // sending message to MyThread
            myThread.getHandler().sendMessage(messageToThread);
        }

        return super.onTouchEvent(event);
    }

    /** The main handler. */
    public Handler mainHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //Log.d(TAG,"Message received by handler of MainActivity");
            if (msg.what == 0) {
                threadModifiedText.setText(msg.getData().getString("text"));
            }
        };
    };


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
