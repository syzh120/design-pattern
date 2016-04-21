package com.mamba.builderPattern;

public class MealBuilder {
	public Meal prepareVegMeal() {
		Meal meal = new Meal();
		meal.addItem(new VegBurger());
		meal.addItem(new Coke());
		return meal;
	}

	public Meal prepareNoVegMeal() {
		Meal meal = new Meal();
		meal.addItem(new ChicknBurger());
		meal.addItem(new Pepsi());
		return meal;
	}

}
