package com.example.rohitsingla.webserviceexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//Reference : https://trinitytuts.com/load-data-from-soap-web-service-in-android-application/
public class MyActivity extends Activity {
    String TAG = "Response";
    Button bt;
    EditText celcius;
    String getCel;
    SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        bt = (Button) findViewById(R.id.bt);
        celcius = (EditText) findViewById(R.id.cel);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCel = celcius.getText().toString();
                if(getCel == null || getCel.trim().equalsIgnoreCase("")){
                    celcius.setError("Please enter some value");
                }else{
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute(){
            Log.d(TAG,"inside onPreExecute()");
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.d(TAG,"inside onPostExecute");
            Toast.makeText(MyActivity.this, "The value of temperature in Fahrenheit = "+resultString.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void calculate(){
        String SOAP_ACTION = "http://www.w3schools.com/xml/CelsiusToFahrenheit";
        String METHOD_NAME = "CelsiusToFahrenheit";
        String NAMESPACE = "http://www.w3schools.com/xml/";
        String URL = "http://www.w3schools.com/xml/tempconvert.asmx";

        try{
            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
            Request.addProperty("Celsius",getCel);

            SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapSerializationEnvelope.dotNet = true;
            soapSerializationEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION,soapSerializationEnvelope);

            resultString = (SoapPrimitive) soapSerializationEnvelope.getResponse();
            Log.d(TAG, "Result Celsius: " + resultString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
