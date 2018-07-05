package com.panic.root.panic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemList extends Activity
{
	private SimpleCursorAdapter adapter;
    private EditText inputSearch;
    private int DataType;
    private String[] from;
    private DBAdapter db;
    private ListView listView;
    private Button bCancel;
    private Button bAdd;
    private ItemTextWatcher watcher;
    private Cursor dbCursor;
    private String tableName;
    private String fieldName;
    private String[] fields;
    private int companyID;
    
	public static void main(String[] args) {
		//TODO
	}

	public void setDataType(int DataType) {
		this.DataType = DataType;
	}

	@Override
	protected void onResume() {		
		super.onResume();
		//Refresh the list view by clearing the search text
		//this runs the runQuery filter ;)
		EditText t = (EditText)findViewById(R.id.searchItem);
		t.setText("");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);
		
		listView = (ListView)findViewById(R.id.listItems);
		listView.setClickable(true);		
		bCancel = (Button)findViewById(R.id.ButtonCancel);
		bAdd = (Button)findViewById(R.id.ButtonAdd);
    	inputSearch = (EditText)findViewById(R.id.searchItem);
    	fields = new String[2];
    	
    	//Get content from Database
		db = new DBAdapter(getApplicationContext());
		db.open();
		
		switch(DataType) 
		{
			case R.integer.company_type:				
				tableName = DBOpenHelper.TABLE_COMPANY;
				fieldName = DBOpenHelper.COMPANY_COL_NAME;
				from = new String[] { "_id", "name" };
				dbCursor = db.getCursor(tableName,null,null,null);
				break;
			case R.integer.customer_type:				
				tableName = DBOpenHelper.TABLE_CUSTOMER;
				fieldName = DBOpenHelper.CUSTOMER_COL_NAME;
				from = new String[] { "_id", "name" };
				dbCursor = db.getCursor(tableName,null,null,null);
				break;
			case R.integer.product_type:				
				tableName = DBOpenHelper.TABLE_PRODUCT;
				fieldName = DBOpenHelper.PRODUCT_COL_NAME;
				from = new String[] { "_id", "name" };
				dbCursor = db.getCursor(tableName,null,null,null);
				break;
			case R.integer.invoice_type:
				tableName = DBOpenHelper.TABLE_INVOICE;
				fieldName = DBOpenHelper.INVOICE_COL_FOLIO;
				from = new String[] { "_id", "folio" };
				fields[0] = "_id";
				fields[1] = "'Folio#'||folio||' - '||date AS folio";
				dbCursor = db.getCursor(tableName,fields,null,null);
				break;
			default:				
				break;
		}
		
		adapter = new SimpleCursorAdapter(
				this, 
				R.layout.list_item, 
				dbCursor, 
				from, 
		        new int[] { R.id.itemID, R.id.itemName },
		        SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
	        public Cursor runQuery(CharSequence constraint) {
	        	return (DataType == R.integer.invoice_type) ? 
	        			db.getCursor(tableName, fields, fieldName, constraint) :
	        			db.getCursor(tableName, null, fieldName, constraint);	
	        }
	    });
		
		//Set adapters and text watcher
    	watcher = new ItemTextWatcher();
    	watcher.setAdapter(adapter);
        inputSearch.addTextChangedListener(watcher);
        listView.setAdapter(adapter);
        
        //---------------------------------------------------------------------
        //LISTENERS SECTION
        //---------------------------------------------------------------------
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				TextView itemID = (TextView)view.findViewById(R.id.itemID);
				Intent i = getIntent();
				int requestCode = i.getIntExtra("requestCode", 0);
				int customerID = i.getIntExtra("customerID", 0);
				
				//Check if this activity was initiated for result
				if(requestCode > 0)
				{					
					Intent myIntent = new Intent();
					if(customerID > 0)
						myIntent.putExtra("customerID", customerID);
					myIntent.putExtra("itemID", Integer.valueOf(itemID.getText().toString()));
					setResult(RESULT_OK, myIntent);
					finish();
				}
				else
				{
					Intent myIntent = new Intent(getApplicationContext(), ItemDetail.class);
					myIntent.putExtra("DataType", DataType);
					myIntent.putExtra("id", Integer.valueOf(itemID.getText().toString()));
					startActivity(myIntent);
				}
			}
		});
		
		//Deletes a record
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
				final TextView itemID = (TextView)view.findViewById(R.id.itemID);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
				builder.setMessage("Borrar elemento?");
				builder.setCancelable(true);
				builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(DataType) {
						case R.integer.company_type:
		//					CompanyModel co = new CompanyModel();
		//					co.setID(Integer.valueOf(itemID.getText().toString()));
		//					db.deleteRecord(co);
							break;
						case R.integer.customer_type:
							CustomerModel cust = new CustomerModel();
							cust.setID(Integer.valueOf(itemID.getText().toString()));
							db.deleteRecord(cust);
							break;
						}
						dialog.cancel();
						//refresh the list
						onResume();
					}
				} );
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				
				return true;
			}
		});
        
    	bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Clear search text
				EditText t = (EditText)findViewById(R.id.searchItem);
				t.setText("");
			}
		});
    	
    	bAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Add new item
				Intent intentAdd = null;
				
				switch(DataType) {
				case R.integer.invoice_type:
					//Call ActivityForResult customer
					intentAdd = new Intent(getApplicationContext(), Customer.class);
					intentAdd.putExtra("requestCode", R.integer.REQUEST_CUSTOMER);
					startActivityForResult(intentAdd, R.integer.REQUEST_CUSTOMER);
					Toast.makeText(getApplicationContext(), R.string.select_customer, Toast.LENGTH_SHORT).show();
					break;

				default:
					intentAdd = new Intent(getApplicationContext(), ItemAdd.class);
					//if(companyID > 0) intentAdd.putExtra("companyID", companyID);
					intentAdd.putExtra("DataType", DataType);
					startActivity(intentAdd);
					break;
				}				
			}
		});        
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == R.integer.REQUEST_COMPANY) 
		{
			if(resultCode == RESULT_OK && DataType == R.integer.product_type) 
			{
				Intent myIntent = new Intent(getApplicationContext(), ItemAdd.class);
				myIntent.putExtra("companyID", data.getIntExtra("itemID", 0));
				myIntent.putExtra("DataType", DataType);
				startActivity(myIntent);
			}
			else if(resultCode == RESULT_OK && DataType == R.integer.invoice_type)
			{
				//Intent myIntent = new Intent(getApplicationContext(), InvoiceAdd.class);
				Log.w("Extras customerID:", String.valueOf(data.getIntExtra("customerID",0)));
				Log.w("Extras company ID:", String.valueOf(data.getIntExtra("itemID",0)));
			//	myIntent.putExtra("customerID", data.getIntExtra("customerID",0));
			//	myIntent.putExtra("companyID", data.getIntExtra("itemID",0));
				//	startActivity(myIntent);
			}
		}
		else if(requestCode == R.integer.REQUEST_CUSTOMER)
		{
			if(resultCode == RESULT_OK)
			{
				//Call ActivityForResult company
				//Intent myIntent = new Intent(getApplicationContext(), Company.class);
				//myIntent.putExtra("requestCode", R.integer.REQUEST_COMPANY);
				//myIntent.putExtra("customerID", data.getIntExtra("itemID",0));
				//startActivityForResult(myIntent,R.integer.REQUEST_COMPANY);
				//Toast.makeText(getApplicationContext(), R.string.select_company, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
