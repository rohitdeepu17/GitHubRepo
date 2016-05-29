package com.example.rohitsingla.imeinumber;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MyActivity extends Activity {
    TelephonyManager tel;
    TextView imsi,imei,phoneType, simType, networkType;

    String ModelNumber, Board, Brand, Display, FingerPrint, ID, TAGS, Type,
            AndroidVersion, APILevel, CodeName, INCREMENTAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imsi = (TextView) findViewById(R.id.imsi);
        imei = (TextView) findViewById(R.id.imei);
        phoneType = (TextView)findViewById(R.id.phoneType);
        simType = (TextView)findViewById(R.id.simType);
        networkType = (TextView)findViewById(R.id.networkType);
        imsi.setText("IMSI Number : "+tel.getSubscriberId());
        imei.setText("IMEI Number : "+tel.getDeviceId().toString());

        ModelNumber = android.os.Build.MODEL;
        Board = android.os.Build.BOARD;
        Brand = android.os.Build.BRAND;
        Display = android.os.Build.DISPLAY;
        FingerPrint = android.os.Build.FINGERPRINT;
        ID = android.os.Build.ID;
        TAGS = android.os.Build.TAGS;
        Type = android.os.Build.TYPE;

        AndroidVersion = android.os.Build.VERSION.RELEASE;
        APILevel = android.os.Build.VERSION.SDK;
        CodeName = android.os.Build.VERSION.CODENAME;
        INCREMENTAL = android.os.Build.VERSION.INCREMENTAL;
        phoneType.setText(Html.fromHtml("Phone Type :-" +
                "<br/><br/><font color = 'red';>Model Number : </font></font>" + ModelNumber
                + "<br/><font color = 'red';>Board : </font>" + Board
                + "<br/><font color = 'red';>Brand : </font>" + Brand
                + "<br/><font color = 'red';>Display : </font>" + Display
                + "<br/><font color = 'red';>FingerPrint : </font>" + FingerPrint
                + "<br/><font color = 'red';>ID : </font>" + ID
                + "<br/><font color = 'red';>TAGS : </font>" + TAGS
                + "<br/><font color = 'red';>Type : </font>" + Type

                + "<br/>"

                + "<br/><font color = 'red';>Android Version : </font>" + AndroidVersion
                + "<br/><font color = 'red';>API Level : </font>" + APILevel
                + "<br/><font color = 'red';>CodeName : </font>" + CodeName
                + "<br/><font color = 'red';>INCREMENTAL : </font>" + INCREMENTAL));

        simType.setText("Sim Type :- \nOperator Code : " + tel.getSimOperator().toString()
                + "\nOperator Name : " + tel.getSimOperatorName().toString()
                + "\nCountry ISO : " + tel.getSimCountryIso().toString());

        networkType.setText("Network Type :-\nOperator Code : " + tel.getNetworkOperator().toString()
                + "\nSubscriber ID : " + tel.getSubscriberId().toString()
                + "\nOperator Name : " + tel.getNetworkOperatorName().toString()
                + "\nNetwork Type : " + tel.getNetworkType()
                + "\nCountry ISO : " + tel.getNetworkCountryIso().toString());
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
