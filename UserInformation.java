package com.neosoft.task;

import java.io.Serializable;

public class UserInformation implements Serializable{
	public String name,address;
	public String number;
	

	public UserInformation(String name, String address, String number) {
		this.name = "AccountHolder Name: "+name+"\n";
		this.address = "AccountHolder Address: "+address+"\n";
		this.number ="AccountHolder Number: " +number+"\n";
	}
	@Override
	public String toString() {
		
		return name+" "+address+" "+number;
	}
}
