package com.panic.root.panic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivityDos extends Activity implements OnTaskCompleted, OnProgressUpdate
{
	private ProgressDialog barProgressDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dos);
        
    	Button b1 = (Button)findViewById(R.id.Button01);
    	Button b4 = (Button)findViewById(R.id.Button04);

    	b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), Customer.class));
			}
		});
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.menu.imports, menu);
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//If products selected, first we need to choose a company
		if(item.getItemId() == R.id.action_importProducts)
		{
			//Intent myIntent = new Intent(getApplicationContext(), Company.class);
			//myIntent.putExtra("requestCode", R.integer.REQUEST_COMPANY);
			//startActivityForResult(myIntent, R.integer.REQUEST_COMPANY);
			Toast.makeText(getApplicationContext(), R.string.select_company, Toast.LENGTH_SHORT).show();
		}	
		else
		{
			//Intent intent = new Intent(getApplicationContext(),FileExplorer.class);
			switch(item.getItemId()) {
			case R.id.action_importCompanies:
				//intent.putExtra("item", ImportExcel.TYPE_COMPANY);
				break;
			case R.id.action_importCustomers:
				//intent.putExtra("item", ImportExcel.TYPE_CUSTOMER);
				break;
			default:
				break;
			}
			//startActivityForResult(intent, R.integer.REQUEST_FILE);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK)
		{
			if(requestCode == R.integer.REQUEST_FILE) 
			{
				final String chosenFile = data.getStringExtra("chosenFile");
				final int item = data.getIntExtra("item", 0);
				final int companyID = data.getIntExtra("companyID", 0);
				if(chosenFile != null && chosenFile != "") 
				{
					barProgressDialog = new ProgressDialog(MainActivityDos.this);
					barProgressDialog.setTitle("Por favor espere...");
					barProgressDialog.setMessage("importando datos...");
					barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
					barProgressDialog.setProgress(0);
					barProgressDialog.show();
					
					/*ImportExcel importer = new ImportExcel(getApplicationContext());
					importer.setFilePath(chosenFile);
					importer.setType(item);
					importer.setCompanyID(companyID);	
					importer.setOnTaskCompletedListener(this);
					importer.setOnProgressUpdateListener(this);
					importer.setProgressDialog(barProgressDialog);
					importer.execute(0);
					*/
				}
			}
			
			if(requestCode == R.integer.REQUEST_COMPANY)
			{	
				/*Intent intent = new Intent(getApplicationContext(),FileExplorer.class);
				intent.putExtra("item", ImportExcel.TYPE_PRODUCT);
				intent.putExtra("companyID", data.getIntExtra("itemID", 0));
				startActivityForResult(intent, R.integer.REQUEST_FILE);*/
			}			
		}
	}

	@Override
	public void onProgressUpdate(int value) 
	{
		barProgressDialog.setProgress(value);
	}

	@Override
	public void onTaskCompleted(int result) 
	{
		barProgressDialog.dismiss();
		
		if(result == 0)
			Toast.makeText(getApplicationContext(), "�Datos importados con �xito!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "Ha ocurrido un error al importar datos", Toast.LENGTH_SHORT).show();
	}
}
