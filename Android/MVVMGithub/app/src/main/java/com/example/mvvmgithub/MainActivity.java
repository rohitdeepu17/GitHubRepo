package com.example.mvvmgithub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mvvmgithub.adapters.GithubAdapter;
import com.example.mvvmgithub.asynctasks.FetchCategoriesTask;
import com.example.mvvmgithub.models.GithubModel;
import com.example.mvvmgithub.models.LicenseModel;
import com.example.mvvmgithub.models.PermissionsModel;
import com.example.mvvmgithub.viewmodels.ListingViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MVVMTag";

    private RecyclerView recyclerView;

    ArrayList<GithubModel> githubModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"entered onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.list_items);

        String url = "https://api.github.com/orgs/octokit/repos?page=1&per_page=10";
        ListingViewModel lobj = new ListingViewModel();
        new FetchCategoriesTask(mainHandler)
                .execute(url);
    }

    /** The main handler. */
    public Handler mainHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //Log.d(TAG,"Message received by handler of MainActivity");
            if (msg.what == 0) {
                //threadModifiedText.setText(msg.getData().getString("text"));
                Log.d(TAG,msg.toString());
                Log.d(TAG,msg.getData().toString());

                String response = msg.getData().getString("response");

                try{
                    //JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray!=null){
                        int n = jsonArray.length();
                        Log.d(TAG,"jsonArray size : "+n);
                        for(int i=0;i<n;i++){
                            Log.d(TAG, "Processing element no. : "+i);
                            Object obj = jsonArray.get(i);
                            Log.d(TAG,"Generic Object : "+obj.toString());

                            JSONObject curElement = jsonArray.getJSONObject(i);

                            JSONObject permissionsObj = curElement.getJSONObject("permissions");
                            JSONObject licenseObj = curElement.getJSONObject("license");
                            Log.d(TAG,permissionsObj.toString());
                            Log.d(TAG,licenseObj.toString());

                            PermissionsModel permissionsModel = new PermissionsModel(permissionsObj.getBoolean("admin"),
                                    permissionsObj.getBoolean("push"),permissionsObj.getBoolean("pull"));
                            LicenseModel licenseModel = new LicenseModel(licenseObj.getString("key"),licenseObj.getString("name"),
                                    licenseObj.getString("spdx_id"),licenseObj.getString("url"),licenseObj.getString("node_id"));
                            GithubModel githubModel = new GithubModel(curElement.getInt("open_issues_count"),curElement.getString("name"),
                                    curElement.getString("description"),permissionsModel,licenseModel);

                            Log.d(TAG,githubModel.toString());
                            githubModelArrayList.add(githubModel);
                        }
                    }
                    else
                        Log.d(TAG, "jsonArray is null");
                }catch (JSONException e){
                    Log.d(TAG, "Couldn't successfully parse the JSON response!");
                }finally {
                    Log.d(TAG,"Size of list : "+githubModelArrayList.size()+", models : "+githubModelArrayList.toString());

                    GithubAdapter githubAdapter = new GithubAdapter(githubModelArrayList);
                    recyclerView.setAdapter(githubAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }
        };
    };
}