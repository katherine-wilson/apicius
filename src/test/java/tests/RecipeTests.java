package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import utilities.Recipe;
public class RecipeTests {
	//tests one recipe for all general methods
	@Test
	void testRecipe1() {
		Recipe recipe1 = new Recipe("meatloaf");
		String name = recipe1.getName();
		assert(name.equals("Meatloaf"));
		recipe1.addIngredient("meat");
		recipe1.addIngredient("worchestershire sauce");
		recipe1.addIngredient("salt");
		recipe1.addIngredient("pepper");
		String ingredients = recipe1.getIngredientString();
		recipe1.addDirection("cook the meatloaf");
		recipe1.addDirection("take out meatloaf");
		recipe1.addDirection("season meatloaf");
		recipe1.addDirection("eat meatloaf");
		List<String> directions = recipe1.getDirections();
		System.out.println("recipe: " + name);
		System.out.println("ingredients: \n" + ingredients);
		System.out.println("directions: \n" + directions);
		int ingredientsLen = recipe1.getNumIngredients();
		assert(ingredientsLen == 4);
		
		System.out.println("all setters and getters functiong correctly.");
		
	}
	//tests equals method
	@Test
	void testEquals() {
		Recipe recipe1 = new Recipe("meatloaf");
		recipe1.addIngredient("meat");
		recipe1.addIngredient("worchestershire sauce");
		recipe1.addIngredient("salt");
		recipe1.addIngredient("pepper");
		recipe1.addDirection("cook the meatloaf");
		recipe1.addDirection("take out meatloaf");
		recipe1.addDirection("season meatloaf");
		recipe1.addDirection("eat meatloaf");
		
		Recipe recipe2 = new Recipe("meatloaf");
		recipe2.addIngredient("meat");
		recipe2.addIngredient("worchestershire sauce");
		recipe2.addIngredient("salt");
		recipe2.addIngredient("pepper");
		recipe2.addDirection("cook the meatloaf");
		recipe2.addDirection("take out meatloaf");
		recipe2.addDirection("season meatloaf");
		recipe2.addDirection("eat meatloaf");
		
		Boolean eq = recipe1.equals(recipe2);
		assert(eq == true);
		
		
		Recipe recipe3 = new Recipe("meatloafs");
		recipe3.addIngredient("meat and loaf");
		recipe3.addIngredient("sauce");
		recipe3.addDirection("make a meatloaf");
		recipe3.addDirection("eat the meatloaf");
		
		Boolean eq2 = recipe1.equals(recipe3);
		assert(eq2 == false);
		System.out.println("equals method functioning correctly");
	}

}
