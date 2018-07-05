package com.panic.root.panic;

import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;

public class ItemTextWatcher implements TextWatcher
{
	private SimpleCursorAdapter adapter;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		// When user changed the Text
		adapter.getFilter().filter(cs);
	}
	
	//These methods MUST be called prior to set a text change listener
	public void setAdapter(SimpleCursorAdapter adapter) {
		this.adapter = adapter;
	}
}
