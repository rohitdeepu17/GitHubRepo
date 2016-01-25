package com.example.rohitsingla.imagesqlitedatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class MyActivity extends Activity {
    protected static TextView textView;
    protected static ImageView imageView1;
    protected Button get_image, save_image, read_image, clear_table;
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 1;
    private MyDataBase myDB = null;
    private SQLiteDatabase db = null;
    String DB_NAME = "testImage.db";
    //String TABLE_NAME = "mytable";
    public static final int DATABASE_VERSION = 1;
    private byte[] img;
    private String imageName;

    String TAG = "ImageSQLiteDatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        textView = (TextView) findViewById(R.id.text_view_image_name);
        imageView1 = (ImageView) findViewById(R.id.image_view);

        get_image = (Button) findViewById(R.id.get_image);
        get_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        SELECT_PICTURE);
            }
        });

        save_image = (Button) findViewById(R.id.save_image);
        save_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //createTable();
                saveInDB();
            }
        });

        read_image = (Button) findViewById(R.id.read_image);
        read_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromDB();
            }
        });

        clear_table = (Button)findViewById(R.id.clear_database);
        clear_table.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db = myDB.getWritableDatabase();
                db.execSQL("delete from " + MyDataBase.TABLE_NAME);          // clearing the table
                Toast.makeText(MyActivity.this, "Table cleared successfully",Toast.LENGTH_LONG).show();
                imageView1.setVisibility(View.GONE);
                db.close();
            }
        });

        myDB = new MyDataBase(getApplicationContext(), DB_NAME, null, DATABASE_VERSION);
        //db = myDB.getWritableDatabase();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.d(TAG, "Image Path : " + selectedImagePath);
                textView.setText("Selected Image Path : "+selectedImagePath);
                imageView1.setVisibility(View.VISIBLE);
                imageView1.setImageURI(selectedImageUri);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /*void createTable() {
        SQLiteDatabase myDb = openOrCreateDatabase(DB_NAME,
                Context.MODE_PRIVATE, null);
        String MySQL = "create table if not exists "
                + TABLE_NAME
                + " (_id INTEGER primary key autoincrement, name TEXT not null, image BLOB);";
        myDb.execSQL(MySQL);
        myDb.close();
    }*/

    /**
     * This function keeps on adding images to the database one by one.
     */
    void saveInDB() {
        db = myDB.getWritableDatabase();
        byte[] byteImage1 = null;
        ContentValues newValues = new ContentValues();
        String name = selectedImagePath;
        newValues.put(MyDataBase.COLUMN_IMAGE_NAME, name);
        try {
            FileInputStream instream = new FileInputStream(selectedImagePath);
            BufferedInputStream bif = new BufferedInputStream(instream);
            byteImage1 = new byte[bif.available()];
            bif.read(byteImage1);
            newValues.put(MyDataBase.COLUMN_IMAGE_DATA, byteImage1);
            long ret = db.insert(MyDataBase.TABLE_NAME, null, newValues);
            if (ret != -1) {
                imageView1.setVisibility(View.GONE);
                Toast.makeText(this, "Image saved into table successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Sorry, some error occured in saving image", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    /**
     * This function reads the latest image inserted into the db
     */
    void readFromDB(){
        db = myDB.getReadableDatabase();
        String[] col={MyDataBase.COLUMN_IMAGE_NAME, MyDataBase.COLUMN_IMAGE_DATA};
        Cursor c=db.query(MyDataBase.TABLE_NAME, col, null, null, null, null, null);

        if(c!=null){
            c.moveToFirst();
            int count = 0;
            do{
                imageName = c.getString(c.getColumnIndex(MyDataBase.COLUMN_IMAGE_NAME));
                img=c.getBlob(c.getColumnIndex(MyDataBase.COLUMN_IMAGE_DATA));
                count++;
            }while(c.moveToNext());
            Log.d(TAG, "The value of count = "+count);
            Bitmap b1= BitmapFactory.decodeByteArray(img, 0, img.length);
            imageView1.setVisibility(View.VISIBLE);
            imageView1.setImageBitmap(b1);
            Toast.makeText(MyActivity.this, "Retrieved successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Either empty table or not able to fetch",Toast.LENGTH_LONG);
        }

        db.close();
    }

}
