package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Model;
import utilities.Recipe;

/*
 *  JUnit testing class that is designed to test the Model class.
 *  95.6% Statement Coverage, 82.1% Branch Coverage.
 *  
 *  The conditions for some branches could not be replicated during testing 
 *  (Ex. near-impossible IO and DB-related exceptions).
 */
public class ModelTest {
	@Test
	void addToFavorites() {		// tests adding a recipe to favorites
		Model m = new Model(true);
		
		Recipe recipe = new Recipe("meatloaf");
		recipe.addIngredient("meat");
		recipe.addIngredient("worchestershire sauce");
		recipe.addIngredient("salt");
		recipe.addIngredient("pepper");
		recipe.addDirection("cook the meatloaf");
		recipe.addDirection("take out meatloaf");
		recipe.addDirection("season meatloaf");
		recipe.addDirection("eat meatloaf");
		
		// adds recipe to the model's list
		m.addToFavorites(recipe);
		
		// checks the getFavorites method
		ArrayList<Recipe> fav = m.getFavorites();
		assert(fav.get(0).getName().equals("Meatloaf"));
	}
	
	@Test
	void addManyToFavorites() {		// tests adding many recipes to favorites
		Model m = new Model(true);
		
		Recipe recipe1 = new Recipe("meatloaf");
		Recipe recipe2 = new Recipe("taco");
		Recipe recipe3 = new Recipe("burrito");
		
		// adds recipes to the model's list
		m.addToFavorites(recipe1);
		m.addToFavorites(recipe2);
		m.addToFavorites(recipe3);
		
		// checks the getFavorites method
		ArrayList<Recipe> fav = m.getFavorites();
		assert(fav.size() == 3);
		// checks order (should be chronological)
		assert(fav.get(0).getName().equals("Meatloaf"));
		assert(fav.get(1).getName().equals("Taco"));
		assert(fav.get(2).getName().equals("Burrito"));
	}
	
	@Test
	void removeFromFavorites() {	// removes valid recipe from favorites
		Model m = new Model(true);
		
		Recipe recipe = new Recipe("meatloaf");
		recipe.addIngredient("meat");
		recipe.addIngredient("worchestershire sauce");
		recipe.addIngredient("salt");
		recipe.addIngredient("pepper");
		recipe.addDirection("cook the meatloaf");
		recipe.addDirection("take out meatloaf");
		recipe.addDirection("season meatloaf");
		recipe.addDirection("eat meatloaf");
		m.addToFavorites(recipe);
		
		// tests removeFromFavorites method
		m.removeFromFavorites(recipe);
		assert(m.getFavorites().size() == 0);
	}
	
	@Test
	void removeNonsenseFromFavorites() {	// removes invalid recipe from favorites
		Model m = new Model(true);
		
		Recipe recipe = new Recipe("meatloaf");
		recipe.addIngredient("meat");
		recipe.addIngredient("worchestershire sauce");
		recipe.addIngredient("salt");
		recipe.addIngredient("pepper");
		recipe.addDirection("cook the meatloaf");
		recipe.addDirection("take out meatloaf");
		recipe.addDirection("season meatloaf");
		recipe.addDirection("eat meatloaf");
		m.addToFavorites(recipe);
		
		m.removeFromFavorites(new Recipe("www"));
		assert(m.getFavorites().size() == 1);
	}
	
	@Test
	void removeFromEmptyFavorites() {	// removes recipe from empty favorites
		Model m = new Model(true);
		Recipe nonsense = new Recipe("www");
		m.removeFromFavorites(nonsense);
		assert(m.getFavorites().size() == 0);
	}
	
	@Test
	void addToPantry() {	// adds items to pantry -- checks case
		Model m = new Model(true);
		
		// adds 4 pantry items
		m.addPantryItem("cheese");
		m.addPantryItem("cream cheese");
		m.addPantryItem("yogurt");
		m.addPantryItem("ground beef");
		
		// tests getPantry method
		List<String> pantry = m.getPantry();
		assert(pantry.size() == 4);
		// checks for title case
		assert(pantry.contains("Cheese"));
		assert(pantry.contains("Cream Cheese"));
		assert(pantry.contains("Yogurt"));
		assert(pantry.contains("Ground Beef"));
		// checks for alphabetical order
		assert(pantry.get(0).equals("Cheese"));
		assert(pantry.get(1).equals("Cream Cheese"));
		assert(pantry.get(2).equals("Ground Beef"));
		assert(pantry.get(3).equals("Yogurt"));
	}
	
	@Test
	void addIdenticalToPantry() {	// adds identical items to pantry
		Model m = new Model(true);
		
		// adds 2 identical pantry items
		assert(m.addPantryItem("cheese"));
		assert(m.getPantry().contains("Cheese"));
		assertEquals(m.getPantry().size(), 1);
		
		assert(!m.addPantryItem("cheese"));
		assert(m.getPantry().contains("Cheese"));
		assertEquals(m.getPantry().size(), 1);
	}
	
	@Test	
	void removeFromPantry() {	// removes valid items from pantry
		Model m = new Model(true);
		
		// adds 4 pantry items
		m.addPantryItem("Cheese");
		m.addPantryItem("Cream Cheese");
		m.addPantryItem("Yogurt");
		m.addPantryItem("Ground Beef");
		assert(m.getPantry().size() == 4);
		
		// tests removePantryItem method
		m.removePantryItem("Cheese");
		m.removePantryItem("Yogurt");
		assert(m.getPantry().size() == 2);
	}
	
	@Test
	void testModelMove() {	// tests the move up and down methods
		Model m = new Model(true);
		
		//creates 3 temporary recipes
		Recipe recipe1 = new Recipe("meatloaf");
		Recipe recipe2 = new Recipe("taco");
		Recipe recipe3 = new Recipe("burrito");
		
		//adds 3 recipes to model's favorites
		m.addToFavorites(recipe1);
		m.addToFavorites(recipe2);
		m.addToFavorites(recipe3);
		
		m.moveFavoriteDown(recipe1);
		m.moveFavoriteUp(recipe3);
		assert(m.getFavorites().get(0).getName().equals("Taco"));
		assert(m.getFavorites().get(1).getName().equals("Burrito"));
		assert(m.getFavorites().get(2).getName().equals("Meatloaf"));
	}
	
	@Test
	void testModelMoveBoundaries() {	// tests the move up and down methods at the beginning/end of the list
		Model m = new Model(true);
		
		//creates 3 temporary recipes
		Recipe recipe1 = new Recipe("meatloaf");
		
		//adds 3 recipes to model's favorites
		m.addToFavorites(recipe1);
		
		m.moveFavoriteDown(recipe1);
		m.moveFavoriteUp(recipe1);
		assert(m.getFavorites().get(0).getName().equals("Meatloaf"));
		
		Recipe recipe2 = new Recipe("taco");
		m.addToFavorites(recipe2);
		m.moveFavoriteUp(recipe2);
		assert(m.getFavorites().get(0).getName().equals("Taco"));
		m.moveFavoriteUp(recipe2);
		assert(m.getFavorites().get(0).getName().equals("Taco"));
	}
	
	@Test
	void getAllIngredients() {	// all 1857 ingredients should be stored in model
		Model m = new Model(true);
		assertEquals(m.getAllIngredients().size(), 1857);
	}
	
	@Test
	void titleCase() {	// tests titleCase
		Model m = new Model(true);
		m.addPantryItem("x");
		assertEquals(m.getPantry().get(0), "X");
	}
	
	@Test
	void searchDB() {	// searches DB for one item
		Model m = new Model(true);
		Recipe[] results = m.searchDatabase("forgotten");
		assertEquals(results.length, 1);
		assertEquals(results[0].getName(), "Forgotten Minestrone");
	}
	
	@Test
	void searchNothingDB() {	// searches DB for nonsense
		Model m = new Model(true);
		Recipe[] results = m.searchDatabase("xxx");
		assertEquals(results.length, 0);
	}
	
	@Test
	void pantrySearch() {	// pantry search with valid ingredient combo
		Model m = new Model(true);
		m.addPantryItem("Butter");
		m.addPantryItem("French Bread");
		m.addPantryItem("Dry Ranch Dressing Mix");
		List<Recipe> results = m.searchWithPantry();
		assertEquals(results.size(), 1);
		System.out.println(results.get(0).getName());
		assertEquals(results.get(0).getName(), "Grilled Ranch Bread");
	}
	
	@Test
	void blankPantrySearch() {	// pantry search with no ingredients
		Model m = new Model(true);
		List<Recipe> results = m.searchWithPantry();
		assertEquals(results.size(), 0);
	}
	
	@Test
	void partialPantrySearch() {	// pantry search with ingredients that don't form a combo
		Model m = new Model(true);
		m.addPantryItem("Butter");
		m.addPantryItem("French Bread");
		List<Recipe> results = m.searchWithPantry();
		assertEquals(results.size(), 0);
	}
	
	@Test
	void nonsensePantrySearch() {	// pantry search with ingredients that don't form a combo
		Model m = new Model(true);
		m.addPantryItem("Butter");
		m.addPantryItem("French Bread");
		m.addPantryItem("Rosemary");
		m.addPantryItem("Chicken");
		List<Recipe> results = m.searchWithPantry();
		assertEquals(results.size(), 0);
	}
	
	@Test
	void saveAndLoadUserData() {	// tests how user data is saved and loaded
		Model m = new Model(true);
		
		m.addToFavorites(new Recipe("A"));
		m.getFavorites().get(0).addIngredient("Bread");
		m.addToFavorites(new Recipe("B"));
		m.addToFavorites(new Recipe("C"));
		
		m.addPantryItem("French Bread");
		m.addPantryItem("Butter");
		m.addPantryItem("Garlic");
		m.addPantryItem("Rosemary");
		
		m.saveUserData();
		
		File userDataFile = new File("user_data.dat");
		assert(userDataFile.exists());
		
		Model newM = new Model();
		assertEquals(newM.getFavorites().size(), 3);
		assertEquals(newM.getFavorites().get(0).getName(), "A");
		assertEquals(newM.getFavorites().get(0).getIngredients().get(0), "Bread");
		assertEquals(newM.getFavorites().get(1).getName(), "B");
		assertEquals(newM.getFavorites().get(2).getName(), "C");
		assertEquals(newM.getPantry().size(), 4);
	}	
}