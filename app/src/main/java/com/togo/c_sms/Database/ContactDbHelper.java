/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.togo.c_sms.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class ContactDbHelper extends SQLiteOpenHelper {

	private static final String TAG = ContactDbHelper.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "csmsgroup";

	// Login table name
	private static final String TABLE_NAME = "group_contact";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_NAME = "name";
	private static final String KEY_HASH_KEY= "hash_key";
	private static final String KEY_GNAME= "groupe";
	private static final String KEY_CREATED_AT = "created_at";

	public ContactDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PHONE + " TEXT UNIQUE," + KEY_HASH_KEY+ " TEXT,"+ KEY_GNAME+ " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addcontacttogroup(String name , String phone,String gname,String hash) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, phone); // Phone
		values.put(KEY_NAME, name); // name
		values.put(KEY_GNAME, gname); // gname
		values.put(KEY_HASH_KEY, hash); // Id

		// Inserting Row
		long id = db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}


	/**
	 * Getting user data from database
	 * */

	public  Cursor viewAllContactof(String gname){
	    SQLiteDatabase db = this.getReadableDatabase();
	    String query = "Select * from "+TABLE_NAME+" WHERE " + KEY_GNAME +" = '" + gname +"'" ;
	    Cursor cursor = db.rawQuery(query, null);

	    return cursor;
	}



	public HashMap<String, String> getContact() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("phone", cursor.getString(2));
			user.put("hash_key", cursor.getString(3));
			user.put("groupe", cursor.getString(4));
			user.put("created_at", cursor.getString(5));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_NAME, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
