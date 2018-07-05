package com.panic.root.panic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ItemAdd extends Activity
{
	private EditText name;
	private EditText phone;
	private EditText email;
	private int DataType;
	private int itemID;
	private DBAdapter db;
	
	public static void main(String[] args) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		
		Intent myIntent = getIntent();
		DataType = myIntent.getIntExtra("DataType", 0);
		//companyID = myIntent.getIntExtra("companyID", 0);
		itemID = myIntent.getIntExtra("id", 0);
		
		switch(DataType)
		{
			case R.integer.company_type:
				//setContentView(R.layout.company_add);
				name = (EditText)findViewById(R.id.editName);

				phone = (EditText)findViewById(R.id.editPhone);
				email = (EditText)findViewById(R.id.editEmail);
				if(itemID > 0)
				{
					db = new DBAdapter(this);
					db.open();
					//CompanyModel company = db.fetchCompany(itemID);
					//name.setText(company.getName());
					//phone.setText(company.getPhone());
					//email.setText(company.getEmail());
					db.close();
				}
				break;
			case R.integer.customer_type:
				setContentView(R.layout.customer_add);
				name = (EditText)findViewById(R.id.editName);
				phone = (EditText)findViewById(R.id.editPhone);
				email = (EditText)findViewById(R.id.editEmail);
				if(itemID > 0)
				{
					db = new DBAdapter(this);
					db.open();
					CustomerModel customer = db.fetchCustomer(itemID);
					name.setText(customer.getName());
					phone.setText(customer.getPhone());
					email.setText(customer.getEmail());
					db.close();
				}				
				break;

			default:
				break;
		}
		
		Button bSave = (Button)findViewById(R.id.ButtonSave);
		//Invoice does not have save button...
		if(bSave!=null)
		{
			bSave.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					saveData();
					Toast.makeText(getApplicationContext(), R.string.item_saved, Toast.LENGTH_SHORT).show();
					finish();
				}
			});
		}
	}
	
	private void saveData()
	{
		db = new DBAdapter(this);
		db.open();
		
		switch(DataType) {
		case R.integer.customer_type:
			//create new company model object
			CustomerModel cust = new CustomerModel();
			cust.setName(name.getText().toString());
			cust.setPhone(phone.getText().toString());
			cust.setEmail(email.getText().toString());
			if(itemID > 0) {
				cust.setID(itemID);
				db.updateRecord(cust);
			}
			else
				db.insertRecord(cust);
			break;

		}
		
		db.close();
	}
}
