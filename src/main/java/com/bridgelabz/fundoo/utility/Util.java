package com.bridgelabz.fundoo.utility;
public class Util {
	
	public boolean nameMatcher(String name) {
		return name.matches("[a-zA-Z]*");
	}
	
	public boolean mobileNumberMatcher(String mobNumber) {
		
		return mobNumber.matches("[0-9]*");
	}
	
	public boolean emailIdMacher(String emailId) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	    return emailId.matches(regex);
	    
	}
	
	public boolean passwordMatcher(String password) {
		String regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		return password.matches(regex);
	}

}
