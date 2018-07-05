package com.panic.root.panic;

public class CustomerModel extends DatabaseModel
{
	private String name;
	private String phone;
	private String email;
	
	public CustomerModel() {
		//Empty constructor
		super();
	}	
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String getName() {
		return this.name;
	}


	public String getPhone() {
		return this.phone;
	}

	public String getEmail() {
		return this.email;
	}
	
	public void setName(String name) {
		this.name = name;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	


}
