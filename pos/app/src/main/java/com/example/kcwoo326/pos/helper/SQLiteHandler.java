package com.example.kcwoo326.pos.helper;

/**
 * Created by KimJinWoo on 2016-10-05.
*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "postable";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_CCARE_TABLE = "ccaretable";

    // Login Table Columns names
    private static final String KEY_SHOP_NUM = "shopnum";
    private static final String KEY_NAME = "name";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_WAITING_NUM = "waitingnum";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_PERSONS = "persons";
    private static final String KEY_ISSUING_TIME = "issuingtime";
    private static final String KEY_WHETHER_THE_CALL = "whetherthecall";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_SHOP_NUM + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_UID + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

       /* String CREATE_CCARE_TABLE = "CREATE TABLE " + TABLE_CCARE_TABLE + "("
                + KEY_WAITING_NUM + "TEXT, " + KEY_USER_ID + " TEXT,"
                + KEY_PERSONS + " TEXT," + KEY_ISSUING_TIME + " TEXT"
                + KEY_WHETHER_THE_CALL + " TEXT"+ ")";
        db.execSQL(CREATE_CCARE_TABLE);*/

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CCARE_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String shopnum, String name, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SHOP_NUM, shopnum); // Name
        values.put(KEY_NAME , name); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addCCareTabler(String waitingnum, String userid, String persons, String issuingtime, String whetherthecall) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WAITING_NUM, waitingnum); // waitingnum
        values.put(KEY_USER_ID , userid); // userid
        values.put(KEY_PERSONS, persons); // persons
        values.put(KEY_ISSUING_TIME, issuingtime); // issuingtime
        values.put(KEY_WHETHER_THE_CALL, whetherthecall); // whetherthecall

        // Inserting Row
        long id = db.insert(TABLE_CCARE_TABLE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New ccare inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("shopnum", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("created_at", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getCCareDetails() {
        HashMap<String, String> ccare = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_CCARE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ccare.put("waitingnum", cursor.getString(0));
            ccare.put("userid", cursor.getString(1));
            ccare.put("persons", cursor.getString(2));
            ccare.put("issuingtime", cursor.getString(3));
            ccare.put("whetherthecall", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + ccare.toString());

        return ccare;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteCCare() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CCARE_TABLE, null, null);
        db.close();

        Log.d(TAG, "Deleted all ccaretable info from sqlite");
    }

}