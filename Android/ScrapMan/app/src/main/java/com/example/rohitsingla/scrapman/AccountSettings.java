package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class AccountSettings extends Activity {
    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    EditText editTextName, editTextPhone, editTextAddress;
    Button buttonUpdate, buttonChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        editTextName = (EditText)findViewById(R.id.edit_text_name);
        editTextPhone = (EditText)findViewById(R.id.edit_text_phone);
        editTextAddress = (EditText)findViewById(R.id.edit_text_address);
        buttonUpdate = (Button)findViewById(R.id.button_update);
        buttonChangePassword = (Button)findViewById(R.id.button_change_password);

        //Temporarily setting default values
        editTextName.setText("Rohit Singla");
        editTextPhone.setText("7259890479");
        editTextAddress.setText("4073, Sobha Daisy");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update details in the database
                try {
                    mScrapDatabaseAdapter.updateProfile("rohitdeepu17@gmail.com", editTextName.getText().toString(), editTextPhone.getText().toString(), editTextAddress.getText().toString());
                    Toast.makeText(AccountSettings.this, "Profile Updated Successfully", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(AccountSettings.this, HomePage.class);
                    startActivity(intent);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettings.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_settings, menu);
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
        else if(id == R.id.logout){
            //logout from the session and go to login page
            Intent intent = new Intent(this,MyActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
