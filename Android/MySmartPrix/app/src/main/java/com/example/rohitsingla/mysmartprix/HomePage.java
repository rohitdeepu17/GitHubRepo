package com.example.rohitsingla.mysmartprix;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


public class HomePage extends Activity {
    private EditText searchBox;
    private Button bGo;
    private ListView categoriesList;
    public List<String> categories;

    // the Rotten Tomatoes API key
    //private static final String API_KEY = "8q6wh77s65aw435cab9rbzsq";
    private static final String API_KEY = "NVgien7bb7P5Gsc8DWqc";
    // the number of movies to show in the list

    private static final String TAG = HomePage.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        InitializeVariables();
    }

    private void InitializeVariables() {

        searchBox = (EditText) findViewById(R.id.etSearchBox);
        bGo = (Button) findViewById(R.id.bGo);
        categoriesList = (ListView) findViewById(R.id.list_categories);

        String url = "http://api.smartprix.com/simple/v1?type=categories&key="
                +API_KEY
                +"&indent=1";
        new FetchCategoriesTask()
                .execute(url);
        Log.d(TAG, url);

        bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this,"Sorry, This functionality is not yet supported",Toast.LENGTH_SHORT).show();
            }
        });
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(HomePage.this, ProductList.class);
                String newCategoryString = categories.get(position);
                newCategoryString = newCategoryString.replace(" ","%20");
                intent.putExtra("categoryName", newCategoryString);
                startActivity(intent);
            }
        });
    }

    private void fillSupportedCategories(List<String> movieTitles) {
        Log.d(TAG,"Inside refeshMoviesList and size of movieTitles = "+movieTitles.size());
        categoriesList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, movieTitles
                .toArray(new String[movieTitles.size()])));
    }

    private class FetchCategoriesTask extends AsyncTask<String, String, String>{

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

            try {
                // convert the String response to a JSON object
                Log.d(TAG,"Response = "+response);
                JSONObject jsonResponse = new JSONObject(response);

                // fetch the array of movies in the response
                JSONArray jArray = jsonResponse.getJSONArray("request_result");
                Log.d(TAG,"jArray = "+jArray.toString());
                // add each movie's title to a list
                categories = new ArrayList<String>();

                for (int i = 0; i < jArray.length(); i++) {

                    Log.d(TAG,"category = "+jArray.get(i));
                    categories.add(jArray.get(i).toString());
                    /*movieTitles.add(movie.getString("title"));


                    movieSynopsis.add(movie.getString(movie.getString("synopsis")));
                    movieImgUrl.add(movie.getString(movie.getString("id")));*/
                }
                // refresh the ListView
                fillSupportedCategories(categories);
            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
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
