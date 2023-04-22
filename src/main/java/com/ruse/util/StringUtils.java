package com.ruse.util;

import java.security.SecureRandom;

/**
 * @author Suic
 * @since 29.06.2019
 */

public final class StringUtils {

	public static final String capitalizeFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static final String capitalizeEachFirst(String str) {

		String[] words = str.split(" ");

		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}
		return str = String.join(" ", words);
	}

	public static final String usToSpace(String str) {
		return str.replaceAll("_", " ");
	}

	public static final String percentToSymbol(String str) {
		return str.toLowerCase().replaceAll("percent", "%");
	}

	public static @org.jetbrains.annotations.NotNull
	String createRandomString(int length){
		int leftLimit = 48;
		int rightLimit = 122;
		SecureRandom random = new SecureRandom();

		return random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

}
