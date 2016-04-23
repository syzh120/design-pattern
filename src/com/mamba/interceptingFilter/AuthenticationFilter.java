package com.mamba.interceptingFilter;

public class AuthenticationFilter implements Filter {

	@Override
	public void execute(String request) {
		System.out.println("requset log:" + request);
	}

}
