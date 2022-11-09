package Tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import controller.Controller;
import model.Model;
import utilities.Recipe;

public class ControllerTest {
	@Test
	void controllerGeneral() {
		Model m  = new Model();
		Controller c = new Controller(m);
		
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
		
		//adds recipe to the model's list through controller
		c.addToFavorites(recipe1);
		
		//checks the getFavorites method
		ArrayList<Recipe> fav = c.getFavorites();
		assert(fav.get(0).getName().equals("Meatloaf"));
		
		//adds 4 pantry items
		c.addPantryItem("cheese");
		c.addPantryItem("cream cheese");
		c.addPantryItem("yogurt");
		c.addPantryItem("ground beef");
		
		//tests getPantry method
		List<String> pantry = c.getPantry();
		assert(pantry.size() == 4);
		
		//tests removeFromFavorites method
		c.removeFromFavorites(recipe1);
		assert(c.getFavorites().size() == 0);
		
		// tests removePantryItem method
		c.removePantryItem("cheese");
		c.removePantryItem("yogurt");
		assert(c.getPantry().size() == 2);
	}
	
	@Test
	void testControllerMove() {
		Model m = new Model();
		Controller c = new Controller(m);
		
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
		c.addToFavorites(recipe1);
		c.addToFavorites(recipe2);
		c.addToFavorites(recipe3);
		
		c.moveFavoriteDown(0, recipe1);
		c.moveFavoriteUp(2, recipe3);
		assert(c.getFavorites().get(0).getName().equals("Taco"));
		
		System.out.println("All Tests Passed for Controller.");
	}
}
