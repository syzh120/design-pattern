package com.mamba.test;

import java.io.UnsupportedEncodingException;

import com.mamba.util.DannyUtils;

public class TestStrSpilt {
	public static void main(String[] args) {
		String str = "HellokiWorldkiShandongkiUniversityki";
		String[] strs = str.split("ki");
		for (String s : strs)
			System.out.println(s + ",");

		System.out.println("0ABC".codePointAt(0));

		String foo = "你好";
		byte[] bs = null;
		try {
			bs = foo.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DannyUtils.printBytes("bsUnicode", bs);

		try {
			bs = foo.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		DannyUtils.printBytes("bsISO-8859-1", bs);

	}

}
