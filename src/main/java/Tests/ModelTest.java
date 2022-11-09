package Tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Model;
import utilities.Recipe;

public class ModelTest {
	
	@Test
	void testModelGeneral() {
		Model m = new Model();
		
		Recipe recipe1 = new Recipe("meatloaf");//first letter gets made uppercase
		
		//adds 4 ingredients
		recipe1.addIngredient("meat");
		recipe1.addIngredient("worchestershire sauce");
		recipe1.addIngredient("salt");
		recipe1.addIngredient("pepper");
		
		//adds 4 directions
		recipe1.addDirection("cook the meatloaf");
		recipe1.addDirection("take out meatloaf");
		recipe1.addDirection("season meatloaf");
		recipe1.addDirection("eat meatloaf");
		
		//adds recipe to the model's list
		m.addToFavorites(recipe1);
		
		//checks the getFavorites method
		ArrayList<Recipe> fav = m.getFavorites();
		assert(fav.get(0).getName().equals("Meatloaf"));
		
		//adds 4 pantry items
		m.addPantryItem("cheese");
		m.addPantryItem("cream cheese");
		m.addPantryItem("yogurt");
		m.addPantryItem("ground beef");
		
		//tests getPantry method
		List<String> pantry = m.getPantry();
		assert(pantry.size() == 4);
		
		//tests removeFromFavorites method
		m.removeFromFavorites(recipe1);
		assert(m.getFavorites().size() == 0);
		
		// tests removePantryItem method
		m.removePantryItem("cheese");
		m.removePantryItem("yogurt");
		assert(m.getPantry().size() == 2);
		
	}
	
	@Test
	void testModelMove() {
		Model m = new Model();
		
		//creates 3 temporary recipes
		Recipe recipe1 = new Recipe("meatloaf");
		String name = recipe1.getName();
		assert(name.equals("Meatloaf"));
		recipe1.addIngredient("meat");
		recipe1.addIngredient("worchestershire sauce");
		recipe1.addIngredient("salt");
		recipe1.addIngredient("pepper");
		recipe1.addDirection("cook the meatloaf");
		recipe1.addDirection("take out meatloaf");
		recipe1.addDirection("season meatloaf");
		recipe1.addDirection("eat meatloaf");
		
		
		Recipe recipe2 = new Recipe("taco");
		recipe2.addIngredient("meat");
		recipe2.addIngredient("tortilla");
		recipe2.addDirection("make taco");
		
		Recipe recipe3 = new Recipe("burrito");
		recipe2.addIngredient("meat");
		recipe2.addIngredient("tortilla");
		recipe2.addDirection("make burrito");
		
		//adds 3 recipes to model's favorites
		m.addToFavorites(recipe1);
		m.addToFavorites(recipe2);
		m.addToFavorites(recipe3);
		
		m.moveFavoriteDown(0, recipe1);
		m.moveFavoriteUp(2, recipe3);
		assert(m.getFavorites().get(0).getName().equals("Meatloaf"));
		
		System.out.println("All Tests Passed for Model.");
	}

}