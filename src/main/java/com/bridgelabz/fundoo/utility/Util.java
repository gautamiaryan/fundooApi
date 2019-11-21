package com.bridgelabz.fundoo.utility;
import java.util.regex.*;
public class Util {
	
	public boolean nameMatcher(String name) {
		return Pattern.matches("[a-zA-Z]", name);
	}
	
	public boolean mobileNumberMatcher(String mobNumber) {
		
		return Pattern.matches("[0-9]", mobNumber);
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
