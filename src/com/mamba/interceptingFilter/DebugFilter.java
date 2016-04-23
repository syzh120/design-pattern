package com.mamba.interceptingFilter;

public class DebugFilter implements Filter {

	@Override
	public void execute(String request) {
		System.out.println("Debug request log: " + request);
	}

}
