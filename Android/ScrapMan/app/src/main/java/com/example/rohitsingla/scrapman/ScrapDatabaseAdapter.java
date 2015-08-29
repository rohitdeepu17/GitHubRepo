package com.example.rohitsingla.scrapman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by rohitsingla on 29/08/15.
 */
public class ScrapDatabaseAdapter {
    // Database fields
    private SQLiteDatabase db;
    private ScrapHelper scrapDatabaseHelper;
    private static final String TAG = "ScrapDatabaseAdapter";

    public ScrapDatabaseAdapter(Context context) {
        scrapDatabaseHelper = new ScrapHelper(context);
    }

    void getPassword(String username) throws SQLException {
        openDBReadable();
        //columns to be selected from user table
        String[] selectedColumns = {
                ScrapHelper.PASSWD};
        Cursor c = db.query(scrapDatabaseHelper.TABLE_NAME_USER, selectedColumns, scrapDatabaseHelper.USERNAME + " =?", new String[]{username}, null, null, null, null);
        c.moveToFirst();
        Log.d(TAG, "Password is : "+c.getColumnName(c.getColumnIndex(ScrapHelper.PASSWD)));
        c.close();
        closeDB();

    }

    /**
     * function to verify login credentials from login page
     *
     * @param username
     * @param passwd
     * @return returns true if the login credentials are true, otherwise returns false
     */
    boolean verifyLoginCredentials(String username, String passwd) throws SQLException {
        /*openDBReadable();
        //columns to be selected from user table
        String[] selectedColumns = {ScrapHelper.USERNAME,
                ScrapHelper.PASSWD};
        Cursor c = db.query(scrapDatabaseHelper.TABLE_NAME_USER, selectedColumns, scrapDatabaseHelper.USERNAME + " =? AND " + scrapDatabaseHelper.PASSWD + " =?", new String[]{username, passwd}, null, null, null, null);
        c.moveToFirst();
        Log.d(TAG,"Verifying Login Credentials");
        //if no user found with input credentials, return false. Otherwise, return true
        if (c == null){
            c.close();
            closeDB();
            return false;
        }
        else{
            c.close();
            closeDB();
            return true;
        }*/

        //Will try this also to check whether we can close database before processing cursor. I think it should also work because we are just checking whether its null or not
        /*
        c.close();
        closeDB();
        if(c == null)
            return false;
        else
            return true;*/

        openDBReadable();

        Cursor mCount= db.rawQuery("SELECT COUNT(*) FROM "+scrapDatabaseHelper.TABLE_NAME_USER+" WHERE "+scrapDatabaseHelper.USERNAME+" = "+"\""+username+"\""+" AND "+scrapDatabaseHelper.PASSWD+" = "+"\""+passwd+"\"", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        closeDB();

        if(count>0)
            return true;
        else
            return false;
    }

    boolean checkIfUserAlreadyExists(String username) throws SQLException {
        openDBReadable();

        Cursor mCount= db.rawQuery("SELECT COUNT(*) FROM "+scrapDatabaseHelper.TABLE_NAME_USER+" WHERE "+scrapDatabaseHelper.USERNAME+" = "+"\""+username+"\"", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        closeDB();
        if(count>0)
            return true;
        else
            return false;
    }

    /**
     * This function create a creates a new user profile with the input parameters
     *
     * @param username
     * @param passwd
     * @param name
     * @param phone
     * @param address
     */
    void createNewUser(String username, String passwd, String name, String phone, String address) throws SQLException {
        openDBWritable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(scrapDatabaseHelper.USERNAME, username);
        contentValues.put(scrapDatabaseHelper.PASSWD, passwd);
        contentValues.put(scrapDatabaseHelper.NAME, name);
        contentValues.put(scrapDatabaseHelper.PHONE, phone);
        contentValues.put(scrapDatabaseHelper.ADDRESS, address);
        db.insert(scrapDatabaseHelper.TABLE_NAME_USER, null, contentValues);
        closeDB();
    }

    /**
     * This function create a price list for different categories of scrap
     *
     * @param categoryNames category names
     * @param unitPrices    unit price for each category
     * @param n             number of categories
     */
    void createPriceList(String[] categoryNames, double[] unitPrices, int n) throws SQLException {
        openDBWritable();
        for(int i=0;i<n;i++)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(scrapDatabaseHelper.CATEGORY_NAME, categoryNames[i]);
            contentValues.put(scrapDatabaseHelper.UNIT_PRICE, unitPrices[i]);
            db.insert(scrapDatabaseHelper.TABLE_NAME_SCRAP_CATEGORY, null, contentValues);
        }
        closeDB();
    }

    //Next 4 Functions to be called when requesting for a pickup

    /**
     * Function to create a new pickup request
     *
     * @param day
     * @param timeSlot
     * @return requestId for the inserted row/entry
     */
    long insertNewPickupRequest(String day, String timeSlot) throws SQLException {
        openDBWritable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(scrapDatabaseHelper.DAY, day);
        contentValues.put(scrapDatabaseHelper.TIME_SLOT, timeSlot);
        long requestId = db.insert(scrapDatabaseHelper.TABLE_NAME_PICKUP_REQUEST, null, contentValues);
        closeDB();
        return requestId;
    }

    /**
     * This function inserts a mapping entry (requestId, username) into TABLE_NAME_MAKES_PICKUP_REQUEST
     *
     * @param username
     * @param requestId
     * @throws SQLException
     */
    void insertMakesPickupRequest(String username, long requestId) throws SQLException {
        openDBWritable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(scrapDatabaseHelper.USERNAME, username);
        contentValues.put(scrapDatabaseHelper.REQUEST_ID, requestId);
        db.insert(scrapDatabaseHelper.TABLE_NAME_MAKES_PICKUP_REQUEST, null, contentValues);
        closeDB();
    }

    /**
     * This function inserts approximate weights corresponding to different categories for a particular requestId
     *
     * @param requestId     request id
     * @param categoryNames category names
     * @param weights        weight for a category
     * @param n             number of categories
     */
    void insertPickupRequestItems(long requestId, String[] categoryNames, double[] weights, int n) throws SQLException {
        openDBWritable();
        for(int i=0;i<n;i++)
        {
            //insert only if weight for this particular category is more than 0
            if(weights[i] > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(scrapDatabaseHelper.REQUEST_ID, requestId);
                contentValues.put(scrapDatabaseHelper.CATEGORY_NAME, categoryNames[i]);
                contentValues.put(scrapDatabaseHelper.WEIGHT, weights[i]);
                db.insert(scrapDatabaseHelper.TABLE_NAME_PICKUP_REQUEST_ITEMS, null, contentValues);
            }
        }
        closeDB();
    }

    /**
     * This function calls all the 3 above defined functions to make a pickup request
     *
     * @param day
     * @param timeSlot
     * @param username
     * @param categoryNames
     * @param weights
     * @param n
     * @throws SQLException
     */
    void requestPickup(String day, String timeSlot, String username, String[] categoryNames, double[] weights, int n) throws SQLException {
        long requestId = insertNewPickupRequest(day, timeSlot);
        insertMakesPickupRequest(username,requestId);
        insertPickupRequestItems(requestId, categoryNames, weights, n);
    }

    /**
     * This function opens database in writable mode
     *
     * @throws SQLException
     */
    public void openDBWritable() throws SQLException {
        db = scrapDatabaseHelper.getWritableDatabase();
    }

    /**
     * This function opens database in readable mode
     *
     * @throws SQLException
     */
    public void openDBReadable() throws SQLException {
        db = scrapDatabaseHelper.getReadableDatabase();
    }

    /**
     * This function closes database
     *
     * @throws SQLException
     */
    public void closeDB() throws SQLException {
        scrapDatabaseHelper.close();
    }

    //Helper class to create database and all the tables and upgrade when required
    private static class ScrapHelper extends SQLiteOpenHelper {
        //Database related
        private static final String DATABASE_NAME = "ScrapDB";
        private static final int DATABASE_VERSION = 1;
        //Tables
        private static final String TABLE_NAME_USER = "User";
        private static final String TABLE_NAME_MAKES_PICKUP_REQUEST = "MakesPickupRequest";
        private static final String TABLE_NAME_PICKUP_REQUEST = "PickupRequest";
        private static final String TABLE_NAME_PICKUP_REQUEST_ITEMS = "PickupRequestItems";
        private static final String TABLE_NAME_SCRAP_CATEGORY = "ScrapCategory";

        //Primary keys of Entities
        private static String USERNAME = "Username";            //TABLE_NAME_USER
        private static String REQUEST_ID = "RequestId";         //TABLE_NAME_PICKUP_REQUEST
        private static String CATEGORY_NAME = "CategoryName";   //TABLE_NAME_SCRAP_CATEGORY

        //Fields : TABLE_NAME_USER                              Entity
        //private static String USERNAME = "Username";          //defined in Primary Keys
        private static String PASSWD = "Passwd";
        private static String NAME = "Name";
        private static String PHONE = "Phone";
        private static String ADDRESS = "Address";

        //Fields : TABLE_NAME_PICKUP_REQUEST                    Entity
        //private static String REQUEST_ID = "RequestId";       //defined in Primary Keys
        private static String DAY = "Day";
        private static String TIME_SLOT = "TimeSlot";

        //Fields : TABLE_NAME_SCRAP_CATEGORY                    Entity
        //private static String CATEGORY_NAME = "CategoryName";                 //defined in Primary Keys
        private static String UNIT_PRICE = "UnitPrice";

        //Fields : TABLE_NAME_MAKES_PICKUP_REQUEST              Relation
        //private static String USERNAME = "Username"; //Refers to TABLE_NAME_USER->USERNAME                //defined in Primary Keys
        //private static String REQUEST_ID = "RequestId";	//Refers to TABLE_NAME_PICKUP_REQUEST->REQUEST_ID   //define in Primary Keys

        //Fields : TABLE_NAME_PICKUP_REQUEST_ITEMS              Relation
        //private static String REQUEST_ID = "RequestId";	//Refers to TABLE_NAME_PICKUP_REQUEST->REQUEST_ID           //defined in Primary Keys
        //private static String CATEGORY_NAME = "CategoryName";	//Refers to TABLE_NAME_SCRAP_CATEGORY->CATEGORY_NAME    //defined in Primary Keys
        private static String WEIGHT = "Weight";

        //Create Table Queries
        private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME_USER +
                " ( " +
                USERNAME + " VARCHAR(255) PRIMARY KEY, " +
                PASSWD + " VARCHAR(255), " +
                NAME + " VARCHAR(255), " +
                PHONE + " VARCHAR(255), " +
                ADDRESS + " VARCHAR(255) " +
                ");";

        private static final String CREATE_TABLE_PICKUP_REQUEST = "CREATE TABLE " + TABLE_NAME_PICKUP_REQUEST +
                " ( " +
                REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DAY + " VARCHAR(255), " +
                TIME_SLOT + " VARCHAR(255) " +
                ");";

        private static final String CREATE_TABLE_SCRAP_CATEGORY = "CREATE TABLE " + TABLE_NAME_SCRAP_CATEGORY +
                "(" + CATEGORY_NAME + " VARCHAR(255) PRIMARY KEY, " +
                UNIT_PRICE + " REAL " +
                ");";

        private static final String CREATE_TABLE_MAKES_PICKUP_REQUEST = "CREATE TABLE " + TABLE_NAME_MAKES_PICKUP_REQUEST +
                " ( " +
                USERNAME + " VARCHAR(255), " +
                REQUEST_ID + " INTEGER PRIMARY KEY, " +
                "FOREIGN KEY (" + USERNAME + ") REFERENCES " + TABLE_NAME_USER + "(" + USERNAME + "), " +
                "FOREIGN KEY (" + REQUEST_ID + ") REFERENCES " + TABLE_NAME_PICKUP_REQUEST + "(" + REQUEST_ID + ") " +
                ");";

        private static final String CREATE_TABLE_PICKUP_REQUEST_ITEMS = "CREATE TABLE " + TABLE_NAME_PICKUP_REQUEST_ITEMS +
                " ( " +
                CATEGORY_NAME + " VARCHAR(255), " +
                REQUEST_ID + " INTEGER, " +
                WEIGHT + " REAL, " +
                "FOREIGN KEY (" + CATEGORY_NAME + ") REFERENCES " + TABLE_NAME_SCRAP_CATEGORY + "(" + CATEGORY_NAME + "), " +
                "FOREIGN KEY (" + REQUEST_ID + ") REFERENCES " + TABLE_NAME_PICKUP_REQUEST + "(" + REQUEST_ID + "), " +
                "PRIMARY KEY (" + REQUEST_ID + ", " + CATEGORY_NAME + ") " +
                ");";

        //Drop Table Queries
        private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
        private static final String DROP_TABLE_PICKUP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME_PICKUP_REQUEST;
        private static final String DROP_TABLE_SCRAP_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_NAME_SCRAP_CATEGORY;
        private static final String DROP_TABLE_MAKES_PICKUP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME_MAKES_PICKUP_REQUEST;
        private static final String DROP_TABLE_PICKUP_REQUEST_ITEMS = "DROP TABLE IF EXISTS " + TABLE_NAME_PICKUP_REQUEST_ITEMS;

        private Context c;

        /**
         * Constructor for this helper class
         *
         * @param context the context from which this constructor is called
         */
        public ScrapHelper(Context context) {
            // TODO Auto-generated constructor stub
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.c = context;
            //Message.message(c,"Constructor was called");
            //onUpgrade(DATABASE_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                //db = getWritableDatabase();
                db.execSQL(CREATE_TABLE_USER);
                db.execSQL(CREATE_TABLE_PICKUP_REQUEST);
                db.execSQL(CREATE_TABLE_SCRAP_CATEGORY);
                db.execSQL(CREATE_TABLE_MAKES_PICKUP_REQUEST);
                db.execSQL(CREATE_TABLE_PICKUP_REQUEST_ITEMS);
            } catch (Exception e) {
                Toast.makeText(c, "Sorry, some error occurred during creation of tables", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(DROP_TABLE_USER);
            db.execSQL(DROP_TABLE_PICKUP_REQUEST);
            db.execSQL(DROP_TABLE_SCRAP_CATEGORY);
            db.execSQL(DROP_TABLE_MAKES_PICKUP_REQUEST);
            db.execSQL(DROP_TABLE_PICKUP_REQUEST_ITEMS);
            onCreate(db);
        }

    }
}