package com.panic.root.panic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemDetail extends Activity
{
	private DBAdapter db;
	private TextView textName;
	private TextView textAddress;
	private TextView textCity;
	private TextView textState;
	private TextView textZIP;
	private TextView textRFC;
	private TextView textPhone;
	private TextView textEmail;
	private TextView textCode;
	private TextView textPrice;
	private TextView textTax;
	private TextView textDate;
	private TextView textFolio;
	private TextView textObservations;
	private TextView textDiscount;
	private TextView textTotal;
	private CustomerModel customer;
	//private CompanyModel company;
	//private InvoiceModel invoice;
	//private ArrayList<InvoiceDetailModel> invDetail;
	private int dataType;
	
	public static void main(String[] args) {
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onCreate(null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		
		Intent myIntent = getIntent();
		//final int DataType = myIntent.getIntExtra("DataType", 0);
		dataType = myIntent.getIntExtra("DataType", 0);
		final int id = myIntent.getIntExtra("id", 0);
		
		db = new DBAdapter(this);
		db.open();
		
		switch(dataType)
		{

			case R.integer.customer_type:
				//Get views
				setContentView(R.layout.customer_detail);
				textName = (TextView)findViewById(R.id.textName);
				textPhone = (TextView)findViewById(R.id.textPhone);
				textEmail = (TextView)findViewById(R.id.textEmail);
				//Get data
				customer = db.fetchCustomer(id);
				//Set data
				textName.setText(customer.getName());
				textPhone.setText(customer.getPhone());	
				textEmail.setText(customer.getEmail());
				break;
			default:
				break;
		}		
		db.close();
		
		Button bEdit = (Button)findViewById(R.id.ButtonEdit);
		//Invoice does not have edit button...
		if(bEdit!=null)
		{
			bEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//Get data for edit here
					Intent myIntent = new Intent(getApplicationContext(), ItemAdd.class);
					myIntent.putExtra("DataType", dataType);
					myIntent.putExtra("id", id);
					startActivity(myIntent);
				}
			});
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(dataType == R.integer.invoice_type) {
			getMenuInflater().inflate(R.menu.print, menu);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent shareIntent;

		
		switch(item.getItemId()) {
		case R.id.action_signature:
			shareIntent = new Intent(getApplicationContext(), Signature.class);
			startActivityForResult(shareIntent, R.integer.REQUEST_SIGNATURE);
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK) 
		{
			if(requestCode == R.integer.REQUEST_SIGNATURE)
			{
				Bundle bundle = data.getExtras();
				String ID = bundle.getString("ID");
				db.open();
				//invoice.setSignatureID(ID);
				//db.updateRecord(invoice);
				db.close();
				Toast.makeText(this, "Firma guardada con �xito!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
