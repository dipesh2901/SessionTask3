package com.neosoft.task;

import java.io.Serializable;

public class Balance implements Serializable{
	
	public double balance;

	public Balance(double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		
		return "Balance is: "+balance;
	}
	
}
