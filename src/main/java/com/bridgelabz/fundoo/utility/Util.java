package com.bridgelabz.fundoo.utility;

public class Util {

    public static boolean nameMatcher(String name) {
	return name.matches("[a-zA-Z]*");
    }

    public static boolean mobileNumberMatcher(String mobNumber) {

	return mobNumber.matches("[0-9]*");
    }

    public static boolean emailIdMacher(String emailId) {
	String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	return emailId.matches(regex);

    }

    public static boolean passwordMatcher(String password) {
	String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	return password.matches(regex);
    }

}
