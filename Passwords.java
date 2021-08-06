package com.neosoft.task;

import java.io.Serializable;

public class Passwords implements Serializable {

	public String password;

	public Passwords(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return password;
	}
}
