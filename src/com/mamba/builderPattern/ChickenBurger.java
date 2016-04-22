package com.mamba.builderPattern;

public class ChickenBurger extends Burger {
	@Override
	public String name() {
		return "Chick Burger";
	}

	@Override
	public float price() {
		return 30.0f;
	}
}
