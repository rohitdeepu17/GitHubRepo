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


public class ProductList extends Activity {
    private EditText searchBox;
    private Button bGo;
    private RecyclerView productList;
    private ProductAdapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager;
    public ArrayList<ProductDataForList> products;
    TextView tvCategoryName;
    private String categoryName;
    private boolean firstLoad = true;
    private boolean mIsLoading = false;
    private int currentPage = 0;

    private static final String TAG = ProductList.class.getName();
    private static final String API_KEY = "NVgien7bb7P5Gsc8DWqc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        InitializeVariables();
    }

    private void InitializeVariables() {
        searchBox = (EditText) findViewById(R.id.etSearchBox);
        bGo = (Button) findViewById(R.id.bGo);
        tvCategoryName = (TextView)findViewById(R.id.category_name);
        products = new ArrayList<ProductDataForList>();

        productList = (RecyclerView) findViewById(R.id.list_products);
        mLayoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(mLayoutManager);
        mAdapter = new ProductAdapter(products);
        productList.setAdapter(mAdapter);
        productList.addOnScrollListener(paginationListener);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //Log.d(TAG,"The category selected is "+bundle.getString("categoryName"));
        tvCategoryName.setText(bundle.getString("categoryName").replace("%20"," "));
        categoryName = bundle.getString("categoryName");

        String url = "http://api.smartprix.com/simple/v1?type=search&key="
                    +API_KEY
                    +"&category="
                    +categoryName
                    +"&q=3g&start=0&indent=1";
        Log.d(TAG, url);
        new FetchProductsTask()
                .execute(url);

        bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductList.this, "Sorry, This functionality is not yet supported", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            Log.d(TAG,"visibleItemCount = "+visibleItemCount+", firstVisibleItemPosition = "+firstVisibleItemPosition
            +", totalItemCount = "+totalItemCount);
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                //if(!mIsLoading){
                //wait until the earlier ones are loading
                while(mIsLoading){
                    //wait
                }
                loadMoreItems();
                fillProductList(products);
                mIsLoading = false;
            }
        }
    };

    private void loadMoreItems() {
        mIsLoading = true;
        Log.d(TAG,"inside loadMoreItems with currentPage = "+currentPage);
        String url = "http://api.smartprix.com/simple/v1?type=search&key="
                +API_KEY
                +"&category="
                +categoryName
                +"&q=3g&start="
                +currentPage*10
                +"&indent=1";
        Log.d(TAG, "The url to get Response : "+url);
        new FetchProductsTask()
                .execute(url);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ((ProductAdapter)mAdapter).setOnItemClickListener(new ProductAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Log.d(TAG,"Item clicked = "+products.get(position).getName());
                Intent intent;
                intent = new Intent(ProductList.this, ProductDetails.class);
                intent.putExtra("id", products.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void fillProductList(ArrayList<ProductDataForList> movieTitles) {
        Log.d(TAG,"Inside fillProductsList and size of list = "+movieTitles.size());
        //mAdapter.setData(movieTitles);
        mAdapter.notifyDataSetChanged();
    }

    private class FetchProductsTask extends AsyncTask<String, String, String> {

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
            currentPage++;
            Log.d("Rohit","CurrentPage = "+currentPage);
            try {
                // convert the String response to a JSON object
                //Log.d(TAG,"Response = "+response);
                JSONObject jsonResponse = new JSONObject(response);

                // fetch the array of movies in the response
                JSONArray jArray = jsonResponse.getJSONObject("request_result").getJSONArray("results");
                //Log.d(TAG,"jArray = "+jArray.toString());


                for (int i = 0; i < jArray.length(); i++) {
                    //Log.d(TAG,"product = "+jArray.get(i));
                    JSONObject product = jArray.getJSONObject(i);
                    products.add(new ProductDataForList(product.getString("id"),product.getString("name"),product.getString("img_url"),product.getInt("price")));
                }
                // refresh the ListView
                fillProductList(products);
            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_list, menu);
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
