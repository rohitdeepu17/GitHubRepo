package com.example.mvvmgithub.viewmodels;

import android.os.AsyncTask;
import android.util.Log;

import androidx.databinding.BaseObservable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class ListingViewModel extends BaseObservable {
    private static final String TAG = "MVVMTag";

    public static String getStringValidCheck(String url) {
        Log.d(TAG,"Entered getStringValidCheck");
        String string = "";
        SSLContext sc=null;

        try {

            URL stringurl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) stringurl.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                string = convertStreamToString(conn.getInputStream());
            } else {

            }
        } catch (MalformedURLException e) {
            Log.d(TAG,"MalformedURLException "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG,"IOException "+e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception"+e.toString());
        }

        return string;
    }


    private static String convertStreamToString(InputStream is) {
        if (null == is) {
            return "";
        }
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
