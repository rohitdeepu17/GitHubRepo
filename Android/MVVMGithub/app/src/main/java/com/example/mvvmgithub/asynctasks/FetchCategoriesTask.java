package com.example.mvvmgithub.asynctasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mvvmgithub.models.GithubModel;
import com.example.mvvmgithub.models.LicenseModel;
import com.example.mvvmgithub.models.PermissionsModel;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FetchCategoriesTask extends AsyncTask<String, String, String> {
    private static final String TAG = "MVVMTag";

    // reference to mainHandler from the mainThread
    private Handler parentHandler;

    public FetchCategoriesTask(Handler parentHandler) {
        this.parentHandler = parentHandler;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            // make a HTTP request
            response = httpclient.execute(new HttpGet(params[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                Log.d(TAG,"HTTP request status is OK");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                // close connection
                Log.d(TAG,"HTTP request status is close connection");
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't make a successful request!"+e);
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        //send response to main thread
        Log.d(TAG,"Response = "+response);
        Log.d(TAG,"Response length = "+response.length());
        Log.d(TAG,"Response last 5 characters = "+response.substring(response.length()-5));
        Message messageToParent = new Message();
        messageToParent.what = 0;

        Bundle messageData = new Bundle();
        messageData.putString("response",response);
        messageToParent.setData(messageData);

        // send message to mainThread
        parentHandler.sendMessage(messageToParent);

    }

}
