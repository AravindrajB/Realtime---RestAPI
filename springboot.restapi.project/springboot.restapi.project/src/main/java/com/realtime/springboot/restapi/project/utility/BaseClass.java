package com.realtime.springboot.restapi.project.utility;

import java.util.Base64;
import java.util.regex.Pattern;

public class BaseClass {

	public final static String email_regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	public final static String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
//	public final static String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

	public static String encoder(String value) {

		String result = Base64.getEncoder().encodeToString(value.getBytes());

		return result;
	}

	public static String decoder(String value) {

		byte[] decodedBytes = Base64.getDecoder().decode(value);
		String result = new String(decodedBytes);
		return result;

	}

	public static Pattern pattern(String input) {

		Pattern result = Pattern.compile(input);

		return result;
	}

}
