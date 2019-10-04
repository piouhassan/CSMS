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

public class UserDbHelper extends SQLiteOpenHelper {

	private static final String TAG = UserDbHelper.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "csms";

	// Login table name
	private static final String TABLE_USER = "users";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_UID = "uid";
	private static final String KEY_ADDRESS= "address";
	private static final String KEY_FULLNAME= "fullname";
	private static final String KEY_HASH_KEY= "hash_key";
	private static final String KEY_CREATED_AT = "created_at";

	public UserDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
				+ KEY_PHONE + " TEXT UNIQUE," + KEY_UID + " TEXT,"+ KEY_ADDRESS + " TEXT,"+ KEY_FULLNAME+ " TEXT,"+ KEY_HASH_KEY+ " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String username , String phone, String uid,String fullname,String hash,String address ,String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHONE, phone); // Phone
		values.put(KEY_USERNAME, username); // username
		values.put(KEY_UID, uid); // Id
		values.put(KEY_FULLNAME, fullname); // Id
		values.put(KEY_HASH_KEY, hash); // Id
		values.put(KEY_ADDRESS, address); // address
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
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
			user.put("username", cursor.getString(1));
			user.put("phone", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("address", cursor.getString(4));
			user.put("fullname", cursor.getString(5));
			user.put("hash_key", cursor.getString(6));
			user.put("created_at", cursor.getString(7));
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
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
