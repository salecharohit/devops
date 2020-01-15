package com.rohitsalecha.springular.devops.config;

public class Credentials {

	private String username;
	private String password;

	public Credentials() {
		// TODO Auto-generated constructor stub
	}
	
	public Credentials(String userName2, String password2) {
		this.username=userName2;
		this.password=password2;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
