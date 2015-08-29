package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class MyActivity extends Activity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonSignUp;

    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    String TAG = "MyActivity";

    String[] categoryNames = new String[]{
            "Paper",
            "Cardbox",
            "Iron",
            "Tin",
            "Plastic"
    };

    double[] prices = new double[]{
            9.00,
            7.00,
            18.00,
            50.00,
            12.50,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Log.d(TAG, "Inside onCreate of MyActivity");
        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        //For the time being, create price list on the user end
        try {
            mScrapDatabaseAdapter.createPriceList(categoryNames, prices, categoryNames.length);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //debugging starts here
        try {
            mScrapDatabaseAdapter.getPassword("rohitdeepu17@gmail.com");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //debugging ends here

        editTextUsername = (EditText)findViewById(R.id.edit_text_username);
        editTextPassword = (EditText)findViewById(R.id.edit_text_password);

        buttonLogin = (Button)findViewById(R.id.button_login);
        buttonSignUp = (Button)findViewById(R.id.button_sign_up);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login with proper error handling
                //if correct credentials, take user to HomePage
                try {
                    String username = editTextUsername.getText().toString();
                    String passwd = editTextPassword.getText().toString();
                    Log.d(TAG, "username : "+username+", password : "+passwd);
                    if(username != null && username.length()!=0 && passwd != null && passwd.length()!=0 && mScrapDatabaseAdapter.verifyLoginCredentials(username, passwd))
                    {
                        Intent intent = new Intent(MyActivity.this,HomePage.class);
                        intent.putExtra("username", editTextUsername.getText().toString());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MyActivity.this,"Incorrect username/password",Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //else show toast
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MyActivity.this, SignUpPage.class);
                startActivity(mIntent);
            }
        });
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
