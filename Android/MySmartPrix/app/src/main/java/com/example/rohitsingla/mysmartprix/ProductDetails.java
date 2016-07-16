package com.example.rohitsingla.mysmartprix;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;


public class ProductDetails extends Activity {
    private EditText searchBox;
    private Button bGo;
    private TextView tvProductName, tvBestPrice, tvNumStores;
    private ImageView productImageView;
    private RecyclerView storeList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String productId;
    private ArrayList<StoreDataForList> stores;

    private static final String TAG = ProductDetails.class.getName();
    private static final String API_KEY = "NVgien7bb7P5Gsc8DWqc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        InitializeVariables();
    }

    private void InitializeVariables() {
        searchBox = (EditText) findViewById(R.id.etSearchBox);
        bGo = (Button) findViewById(R.id.bGo);
        tvProductName = (TextView)findViewById(R.id.product_name);
        productImageView = (ImageView)findViewById(R.id.product_image);
        tvBestPrice = (TextView)findViewById(R.id.best_price);
        tvNumStores = (TextView)findViewById(R.id.num_stores);
        stores = new ArrayList<StoreDataForList>();

        storeList = (RecyclerView) findViewById(R.id.list_stores);
        mLayoutManager = new LinearLayoutManager(this);
        storeList.setLayoutManager(mLayoutManager);
        mAdapter = new StoreAdapter(ProductDetails.this, stores);
        storeList.setAdapter(mAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        productId = bundle.getString("id");

        String url = "http://api.smartprix.com/simple/v1?type=product_full&key="
                    +API_KEY
                    +"&id="
                    +productId
                    +"&indent=1";
        new FetchProductDetailsTask()
                .execute(url);
        Log.d(TAG, url);

        bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetails.this, "Sorry, This functionality is not yet supported", Toast.LENGTH_SHORT).show();
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_details, menu);
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

    private void fillStoreList(List<StoreDataForList> movieTitles) {
        Log.d(TAG,"Inside fillProductsList and size of list = "+movieTitles.size());
        mAdapter.notifyDataSetChanged();
    }

    private class FetchProductDetailsTask extends AsyncTask<String,String, String>{
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
        protected void onPostExecute(String response){
            super.onPostExecute(response);
            try{
                Log.d(TAG,"Response = "+response);
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject requestResult = jsonResponse.getJSONObject("request_result");
                tvProductName.setText(requestResult.getString("name"));
                new DownloadImageTask(productImageView).execute(requestResult.getString("img_url"));
                //productImageView.setImageResource(R.drawable.ic_launcher);


                JSONArray pricesAtStores = requestResult.getJSONArray("prices");
                if(pricesAtStores.length() != 0){
                    for(int i=0;i<pricesAtStores.length();i++){
                        JSONObject storeData = pricesAtStores.getJSONObject(i);
                        stores.add(new StoreDataForList(Integer.parseInt(storeData.getString("price")),storeData.getString("logo"),storeData.getString("link")));
                    }
                }
                fillStoreList(stores);
                /*if(pricesAtStores.length() == 0)
                    tvBestPrice.setText(requestResult.getString("price"));
                else
                    tvBestPrice.setText(getBestPrice(pricesAtStores));
                tvNumStores.setText(pricesAtStores.length());*/

            }catch (JSONException e){
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            }
        }
    }

    private int getBestPrice(JSONArray pricesAtStores) throws JSONException {
        int minPrice = Integer.MAX_VALUE;
        for(int i=0;i<pricesAtStores.length();i++){
            JSONObject storeInfo = pricesAtStores.getJSONObject(i);
            int price = Integer.parseInt(storeInfo.getString("price"));
            if(minPrice<price)
                minPrice = price;
        }
        return minPrice;
    }

    /*private Handler myThreadHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 0){

            }
        }
    };

    public Handler getHandler(){
        return myThreadHandler;
    }*/
}
