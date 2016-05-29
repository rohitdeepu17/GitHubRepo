package com.example.rohitsingla.threadcommunication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by rohitsingla on 28/05/16.
 */
public class MyThread extends Thread {
    // constants with display size info
    private static final double STOP_HEIGHT_RATIO = 0.8;
    private static final int HEIGHT = 480;
    private static final int WIDTH = 320;

    // instance vars for managing text move
    private String myText;
    private int currentPosition;
    private boolean moveLeft;

    private static final String TAG = "MyThread";

    // reference to mainHandler from the mainThread
    private Handler parentHandler;
    // local Handler manages messages for MyThread
    // received from mainThread
    private Handler myThreadHandler = new Handler(){
        public void handleMessage(Message msg) {
            Log.d(TAG,"Message received by handler of mMThread");
            if (msg.what == 0){
                Log.d(TAG,"x = "+(int)msg.getData().getFloat("location_x")+", y = "+(int)msg.getData().getFloat("location_y"));
                // stop this Thread if bottom side of the screen was touched
                if ((int)msg.getData().getFloat("location_y") > (STOP_HEIGHT_RATIO * HEIGHT)){
                    Log.d(TAG,"Touched at the bottom. So interrupting thread");
                    interrupt();
                }
                // set moving direction for the text
                if ((int)msg.getData().getFloat("location_x") < WIDTH/2){
                    Log.d(TAG,"moving left");
                    moveLeft = true;
                }else{
                    Log.d(TAG,"moving right");
                    moveLeft = false;
                }

            }
        }
    };

    //constructor
    public MyThread(Handler parentHandler){
        // initialize instance vars
        this.parentHandler = parentHandler;
        currentPosition = 0;
        moveLeft = true;
        myText = new String("This is a text changed from MyThread! Do you like it? ");
    }

    @Override
    public void run(){
        super.run();
        try{
            //Thread loop
            while(true){
                Message messageToParent = new Message();
                messageToParent.what = 0;

                Bundle messageData = new Bundle();
                messageData.putString("text",updateText(myText));
                messageToParent.setData(messageData);

                // send message to mainThread
                parentHandler.sendMessage(messageToParent);

                // update currentPosition value for moving text
                // ternary if form used
                if (moveLeft)
                    currentPosition = (currentPosition == myText.length()) ? 0 : currentPosition + 1;
                else
                    currentPosition = (currentPosition == 0) ? myText.length() : currentPosition - 1;
                if((moveLeft && currentPosition == 0) || (!moveLeft && currentPosition == myText.length()))
                    sleep(2000);
                else
                    sleep(100);
            }
        }catch (Exception e) {
            // Logging exception
            Log.e("TestingAreaLOG", "Main loop exception - " + e);
        }
    }

    // getter for local Handler
    public Handler getHandler() {
        return myThreadHandler;
    }

    // updates the text based on the currentPosition
    private String updateText(String text){
        return text.substring(currentPosition) + text.substring(0,currentPosition);
    }

}
