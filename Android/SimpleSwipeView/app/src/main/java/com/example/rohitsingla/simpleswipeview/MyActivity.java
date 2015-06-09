package com.example.rohitsingla.simpleswipeview;

        import android.app.Activity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.widget.Toast;


public class MyActivity extends Activity {

    private static final String TAG = "swipe";
    private int swipeMinDistance = 100;
    float x1,x2;
    float y1,y2;
    float xDistance, yDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction()){
            //when the user touches the screen for the first time, save x and y coordinates
            case MotionEvent.ACTION_DOWN:
            {
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            }
            //when the user leaves the screen, save x and y coordinates
            case MotionEvent.ACTION_UP:
            {
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                xDistance = Math.abs(x1-x2);
                yDistance = Math.abs(y1-y2);

                Log.d(TAG, "Point 1 : x1 = "+x1+", y1 = "+y1+", Point 2 :  x2 = "+x2+", y2 = "+y2);

                String str = "";

                //horizontal swipe
                if(xDistance > swipeMinDistance)
                {
                    if(x1 < x2)					//right swipe
                        str = "Left to Right";
                    else						//left swipe
                        str = "Right to Left";
                }
                else if(yDistance > swipeMinDistance)
                {
                    if(y1 < y2)					//down swipe
                        str = "Up to Down";
                    else						//up swipe
                        str = "Down to Up";
                }

                Toast.makeText(this, str + " swipe", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return false;
    }
}
