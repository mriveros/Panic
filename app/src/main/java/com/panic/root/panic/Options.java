package com.panic.root.panic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Options extends Activity
{	
	private Button bSave;
	private Float discount;
	private String observations;
	private TextView textDiscount;
	private TextView textObservations;
	
	public static void main(String[] args) {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		//Try to get default values
		Intent i = getIntent();
		discount = i.getFloatExtra("discount", 0.0f);
		observations = i.getStringExtra("observation");		
		
		//Set default values, if any
		textDiscount = (TextView)findViewById(R.id.textDiscount);
		textDiscount.setText(discount.toString());
		textObservations = (TextView)findViewById(R.id.textObservations);
		textObservations.setText(observations);
		
		bSave = (Button)findViewById(R.id.buttonSave);
		bSave.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v) {
				//Get discount & observations values
				TextView textDiscount = (TextView)findViewById(R.id.textDiscount);
				discount = Float.valueOf(textDiscount.getText().toString());
				TextView textObservations = (TextView)findViewById(R.id.textObservations);
				observations = textObservations.getText().toString();
				
				Intent myIntent = new Intent();
				myIntent.putExtra("discount", discount);
				myIntent.putExtra("observations", observations);
				setResult(RESULT_OK, myIntent);
				finish();			
			}
		});
	}
}
