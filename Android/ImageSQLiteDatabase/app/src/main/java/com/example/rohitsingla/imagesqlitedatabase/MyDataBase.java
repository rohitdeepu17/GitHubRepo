package com.example.rohitsingla.imagesqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rohitsingla on 26/01/16.
 */
public class MyDataBase extends SQLiteOpenHelper{
    public static final String TABLE_NAME = "TableImage";
    public static final String COLUMN_IMAGE_NAME = "ImageName";
    public static final String COLUMN_IMAGE_DATA = "ImageData";

    public MyDataBase(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int dbversion) {
        super(context, dbname, factory, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"("+COLUMN_IMAGE_NAME +" VARCHAR(256), "+COLUMN_IMAGE_DATA+" blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
