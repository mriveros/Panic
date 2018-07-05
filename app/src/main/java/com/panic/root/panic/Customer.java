package com.panic.root.panic;

import android.os.Bundle;

public class Customer extends ItemList 
{
    public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.setDataType(R.integer.customer_type);
		super.onCreate(savedInstanceState);
	}
}
