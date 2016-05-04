package com.mamba.test;

import java.util.regex.Pattern;

public class TestStrRegex {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "This is the text to be searched for occurrence of the Jen pattern.";
		String pat = ".*Jen.*";
		boolean flag = Pattern.matches(pat, text);
		System.out.println(flag);
	}
}
