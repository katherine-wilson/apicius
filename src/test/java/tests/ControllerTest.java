package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import controller.Controller;
import model.Model;
import utilities.Recipe;

/*
 *  JUnit testing class that is designed to test the Controller class.
 *  100% Branch and Statement Coverage.
 */
public class ControllerTest {
	Model m = new Model(true);
	Controller c = new Controller(m);

	@Test	// tests adding a favorite
	void addToFavorites() {		
		Recipe recipe = new Recipe("meatloaf");
		
		// adds recipe to the model's list through controller
		c.addToFavorites(recipe);
		
		// checks the getFavorites method
		ArrayList<Recipe> fav = c.getFavorites();
		assert(fav.contains(recipe));
	}
	
	@Test	// adds two of the same favorite
	void doubleFavorite() {		
		Recipe recipe = new Recipe("chicken");
		
		// adds recipe to the model's list through controller
		assertEquals(c.addToFavorites(recipe), true);
		assertEquals(c.addToFavorites(recipe), false);
	}
	
	@Test	// tests removing a favorite
	void removeFromFavorites() {
		Recipe recipe = new Recipe("meatloaf");
		
		// tests removeFromFavorites method
		c.removeFromFavorites(recipe);
		assert(c.getFavorites().size() == 0);
	}
	
	@Test	// adds a valid ingredient to the pantry
	void addToPantry() {
		// adds 4 pantry items
		assertEquals(c.addPantryItem("Butter"), true);
		assertEquals(c.addPantryItem("Rosemary"), true);
		assertEquals(c.addPantryItem("Garlic"), true);
		assertEquals(c.addPantryItem("Chicken"), true);
		
		// tests getPantry method
		assert(c.getPantry().size() == 4);
	}
	
	@Test	// tests adding a random string to pantry
	void addToPantryNonsense() {
		int previousSize = c.getPantry().size();
		// adds 4 pantry items
		assertEquals(c.addPantryItem("wwww"), false);
		
		// tests getPantry method
		assert(c.getPantry().size() == previousSize);
	}
	
	@Test	// removes actual ingredients from pantry
	void removeFromPantry() {
		int previousSize = c.getPantry().size();
		c.removePantryItem("Rosemary");
		c.removePantryItem("Chicken");
		assert(c.getPantry().size() == previousSize);
	}
	
	@Test	// attempts to remove non-existent ingredient from pantry
	void removeNonesenseFromPantry() {
		int previousSize = c.getPantry().size();
		c.removePantryItem("wwww");
		assert(c.getPantry().size() == previousSize);
	}
	
	@Test	// moves favorites up and down in the list
	void moveFavorite() {
		Model m = new Model(true);
		Controller c = new Controller(m);
		
		//creates 3 temporary recipes
		Recipe recipe1 = new Recipe("meatloaf");
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
		
		c.moveFavoriteDown(recipe1);
		c.moveFavoriteUp(recipe3);
		assert(c.getFavorites().get(0).getName().equals("Taco"));
		assert(c.getFavorites().get(1).getName().equals("Burrito"));
		assert(c.getFavorites().get(2).getName().equals("Meatloaf"));
	}

	@Test	// test favorites list boundaries
	void moveFavoriteBoundaries() {
		Model m = new Model(true);
		Controller c = new Controller(m);
		
		Recipe recipe = new Recipe("taco");
		recipe.addIngredient("meat");
		recipe.addIngredient("tortilla");
		recipe.addDirection("make taco");
		
		//adds 3 recipes to model's favorites
		c.addToFavorites(recipe);
		
		try {
			c.moveFavoriteDown(recipe);
			c.moveFavoriteUp(recipe);
		} catch (Exception e) {
			fail();
		}
		assert(c.getFavorites().size() == 1);
		assert(c.getFavorites().get(0).equals(recipe));
	}
	
	@Test	// verifies that all 1857 ingredients have been read into the model
	void getAllIngredients() {
		assertEquals(c.getAllIngredients().size(), 1857);
	}
	
	@Test	// tests search function using controller -- can't force an exception though
	void searchNonsense() {
		Recipe[] results = c.searchRecipes("xxxx");
		assertEquals(results.length, 0);
	}
	
	@Test	// makes a genuine search for one recipe, should return it
	void searchReal() {
		Recipe[] results = c.searchRecipes("forgotten");
		assertEquals(results.length, 1);
		assert(results[0].getName().equals("Forgotten Minestrone"));
	}
	
	@Test	// tests pantry search that should yield no results
	void pantrySearchBlank() {
		List<Recipe> results = c.searchWithPantry();
		assertEquals(results.size(), 0);
	}
	
	@Test	// tests pantry search that should yield a result
	void pantrySearch() {
		assertEquals(c.addPantryItem("Butter"), true);
		assertEquals(c.addPantryItem("French Bread"), true);
		assertEquals(c.addPantryItem("Dry Ranch Dressing Mix"), true);
		List<Recipe> results = c.searchWithPantry();
		assertEquals(results.size(), 1);
		assertEquals(results.get(0).getName(), "Grilled Ranch Bread");
	}
	
	@Test	// tests that data is saved properly by model when signaled by controller
	void saveData() {
		Model m = new Model(true);
		Controller c = new Controller(m);
		
		c.saveUserData();
		
		File userDataFile = new File("user_data.dat");
		assert(userDataFile.exists());
	}
}
