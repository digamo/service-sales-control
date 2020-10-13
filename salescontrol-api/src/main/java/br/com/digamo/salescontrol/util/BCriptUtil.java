package br.com.digamo.salescontrol.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class BCriptUtil {

	/**
	 * Encodes the password with SHA-1 hash encryption algorithm.
	 * @param passwd
	 * @return
	 */
	public static String encript(String passwd) {
		
		if(StringUtils.isEmpty(passwd))
			throw new IllegalArgumentException();
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(passwd);
		
	}

	/**
	 * Returns true if passwords match, false if they don't match.
	 * @param encodedPassword
	 * @param passwd
	 * @return
	 */
	public static boolean match(String encodedPassword, String passwd) {

		if(encodedPassword == null || passwd == null)
			throw new IllegalArgumentException();
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(passwd, encodedPassword);
		
	}

}
