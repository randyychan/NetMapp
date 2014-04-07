package com.example.searchbar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class EmployeesDbAdapter {
 
    public static final String KEY_EMPLOYEE_NUM = "employee_num";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_DEPT_NAME = "dept_name";
    public static final String KEY_OFFICE_NUM = "office_num";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_JOB_TITLE = "job_title";
    public static final String KEY_SEARCH = "searchData";
    public static final String KEY_PHONE_NUM = "phone_num";
 
    private static final String TAG = "CustomersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
 
    private static final String DATABASE_NAME = "CustomerData";
    private static final String FTS_VIRTUAL_TABLE = "CustomerInfo";
    private static final int DATABASE_VERSION = 7;
 
    //Create a FTS3 Virtual Table for fast searches
    private static final String DATABASE_CREATE =
        "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3(" +
        KEY_EMPLOYEE_NUM + "," +
        KEY_FIRST_NAME + "," +
        KEY_LAST_NAME + "," +
        KEY_DEPT_NAME + "," +
        KEY_OFFICE_NUM + "," +
        KEY_EMAIL + "," +
        KEY_USERNAME + "," +
        KEY_JOB_TITLE + "," +
        KEY_PHONE_NUM + "," +
        KEY_SEARCH + "," +
        " UNIQUE (" + KEY_EMPLOYEE_NUM + "));";
 
 
    private final Context mCtx;    
 
    public EmployeesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
 
    public EmployeesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
 
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
 
    public long createEmployee(String employee_num, String first_name, String last_name, String dept_name, String office_num, String email, String username, String job_title, String phone_num) {
 
        ContentValues initialValues = new ContentValues();
        String searchValue =     employee_num + " " + 
                                first_name + " " + 
                                last_name + " " +
                                dept_name + " " + 
                                email + " " + 
                                username + " " + 
                                office_num + " " +
                                phone_num + " " +
                                job_title;
        initialValues.put(KEY_EMPLOYEE_NUM, employee_num);
        initialValues.put(KEY_FIRST_NAME, first_name);
        initialValues.put(KEY_LAST_NAME, last_name);
        initialValues.put(KEY_DEPT_NAME, dept_name);
        initialValues.put(KEY_OFFICE_NUM, office_num);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_JOB_TITLE, job_title);
        initialValues.put(KEY_PHONE_NUM, phone_num);
        initialValues.put(KEY_SEARCH, searchValue);
 
        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }
 
 
    public Cursor searchCustomer(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT docid as _id," + 
        KEY_EMPLOYEE_NUM + "," +
        KEY_FIRST_NAME + "," +
        KEY_LAST_NAME + "," +
        KEY_DEPT_NAME + "," +
        KEY_OFFICE_NUM + "," +
        KEY_EMAIL + "," +
        KEY_USERNAME + "," +
        KEY_PHONE_NUM + "," +
        KEY_JOB_TITLE +
        " from " + FTS_VIRTUAL_TABLE +
        " where " +  KEY_SEARCH + " MATCH '" + inputText + "';";
        Log.w(TAG, query);
        
        Cursor mCursor = mDb.rawQuery(query,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
 
    }
 
 
    public boolean deleteAllEmployees() {
 
        int doneDelete = 0;
        doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
 
    }
 
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	 
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
 
 
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "c database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }
}