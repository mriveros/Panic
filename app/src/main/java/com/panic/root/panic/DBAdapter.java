package com.panic.root.panic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBAdapter 
{
	private Context context;
	private SQLiteDatabase database;
	private DBOpenHelper dbHelper;
	
	public DBAdapter(Context context) {
		this.context = context;
	}
	
	public DBAdapter open() throws SQLException
	{
		dbHelper = new DBOpenHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor getCursor(String tableName, String[] fields, String whereFieldName, CharSequence constraint)
	{
		String query;
		String selection = "*";
		if(fields != null) 
		{
			int i=0;
			selection = "";
			for(i=0; i<fields.length-1; i++) {
				selection += fields[i];
				selection += ",";
			}
			selection += fields[i];
		}
		
		if(constraint != null) {
			query = "SELECT " + selection + " FROM " + tableName + " WHERE " + whereFieldName + " LIKE '%" + constraint.toString() + "%'";
		}
		else {
			query = "SELECT " + selection + " FROM " + tableName;
		}		
		Cursor cursor = database.rawQuery(query,  null);
		return cursor;
	}
	
	public void deleteTable(String tableName)
	{
		database.delete(tableName, null, null);
	}
	
	//-------------------------------------------------------------------------
	// CUSTOMER TABLE
	//-------------------------------------------------------------------------
	public int insertRecord(CustomerModel customer) 
	{
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.CUSTOMER_COL_NAME, customer.getName());
		values.put(DBOpenHelper.CUSTOMER_COL_PHONE, customer.getPhone());
		values.put(DBOpenHelper.CUSTOMER_COL_EMAIL, customer.getEmail());
		
		return (int)database.insert(DBOpenHelper.TABLE_CUSTOMER, null, values);
	}
	
	public boolean updateRecord(CustomerModel customer)
	{
		String[] id = { String.valueOf(customer.getID()) };
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.CUSTOMER_COL_NAME, customer.getName());
		values.put(DBOpenHelper.CUSTOMER_COL_PHONE, customer.getPhone());
		values.put(DBOpenHelper.CUSTOMER_COL_EMAIL, customer.getEmail());
		
		return database.update(DBOpenHelper.TABLE_CUSTOMER, values, DBOpenHelper.CUSTOMER_KEY_ID + "=?", id) > 0;
	}

	public boolean deleteRecord(CustomerModel customer) 
	{
		String[] id = { String.valueOf(customer.getID()) };
		
		return database.delete(DBOpenHelper.TABLE_CUSTOMER, DBOpenHelper.CUSTOMER_KEY_ID + "=?", id) > 0;
	}
	
	public ArrayList<CustomerModel> fetchAllCustomers()
	{
		ArrayList<CustomerModel> records = new ArrayList<CustomerModel>();
		String query = "SELECT * FROM " + DBOpenHelper.TABLE_CUSTOMER;
		
		Cursor cursor = database.rawQuery(query,  null);
		if(cursor.moveToFirst())
		{
			do {			
				CustomerModel cust = new CustomerModel();
				cust.setID(cursor.getInt((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_KEY_ID))));
				cust.setName(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_NAME))));
				cust.setPhone(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_PHONE))));
				cust.setEmail(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_EMAIL))));
				records.add(cust);
			} while(cursor.moveToNext());
		}
		
		cursor.close();
		return records;
	}	

	public CustomerModel fetchCustomer(int id) 
	{
		String query = "SELECT * FROM " + DBOpenHelper.TABLE_CUSTOMER + " WHERE " + DBOpenHelper.CUSTOMER_KEY_ID + "=" + id;
		Cursor cursor = database.rawQuery(query,  null);
		
		if (cursor != null) 
		{
			cursor.moveToFirst();
			
			CustomerModel cust = new CustomerModel();
			cust.setID(cursor.getInt((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_KEY_ID))));
			cust.setName(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_NAME))));
			cust.setPhone(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_PHONE))));
			cust.setEmail(cursor.getString((cursor.getColumnIndex(DBOpenHelper.CUSTOMER_COL_EMAIL))));		
			
			return cust;
		}
		
		return null;
	}

}
