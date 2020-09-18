package br.com.digamo.salescontrol.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCriptUtil {

	public static String encript(String passwd) {
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(passwd);
		
	}
	
}
