package tests;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import utilities.Recipe;

/*
 *  JUnit testing class that is designed to test the Recipe class.
 *  100% Statement Coverage, 96% Branch Coverage.
 *  
 *  Missing one branch in near-impossible titleCase() scenario.
 */
public class RecipeTests {
	@Test
	void construction() {	// checks setters/getters
		Recipe recipe = new Recipe("meatloaf");
		recipe.setLength(5);
		recipe.addIngredient("meat");
		recipe.addIngredient("worchestershire sauce");
		recipe.addIngredient("salt");
		recipe.addIngredient("pepper");
		recipe.addDirection("cook the meatloaf");
		recipe.addDirection("take out meatloaf");
		recipe.addDirection("season meatloaf");
		recipe.addDirection("eat meatloaf");
		
		assertEquals(recipe.getLength(), 5);
		assertEquals(recipe.getNumIngredients(), 4);
		assertEquals(recipe.getSteps(), 4);
		assertEquals(recipe.getDirections().size(), 4);
		assertEquals(recipe.getIngredients().size(), 4);
	}
	
	@Test
	void stringCase() {		// verifies that all fields (except directions) are stored in title case
		Recipe recipe = new Recipe("meatloaf");
		recipe.addIngredient("meat");
		recipe.addIngredient("worchestershire sauce");
		recipe.addIngredient("salt");
		recipe.addIngredient("pepper");
		recipe.addIngredient("\"apple\"");
		recipe.addIngredient("x");
		recipe.addIngredient("mac n cheese");
		recipe.addDirection("cook the meatloaf");
		recipe.addDirection("take out meatloaf");
		recipe.addDirection("season meatloaf");
		recipe.addDirection("eat meatloaf");
		
		assert(recipe.getName().equals("Meatloaf"));
		assertEquals(recipe.getIngredients().get(0), "Meat");
		assertEquals(recipe.getIngredients().get(1), "Worchestershire Sauce");
		assertEquals(recipe.getIngredients().get(2), "Salt");
		assertEquals(recipe.getIngredients().get(3), "Pepper");
		assertEquals(recipe.getIngredients().get(4), "\"Apple\"");
		assertEquals(recipe.getIngredients().get(5), "X");
		assertEquals(recipe.getIngredients().get(6), "Mac N Cheese");
		assertEquals(recipe.getDirections().get(0), "cook the meatloaf");
		assertEquals(recipe.getDirections().get(1), "take out meatloaf");
		assertEquals(recipe.getDirections().get(2), "season meatloaf");
		assertEquals(recipe.getDirections().get(3), "eat meatloaf");
		assertEquals(recipe.getIngredientString(), "Meat\nWorchestershire Sauce\nSalt\nPepper\n\"Apple\"\nX\nMac N Cheese");
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equals() {		// performs multiple comparisons between different Recipe objects
		Recipe recipe1 = new Recipe("meatloaf");
		recipe1.addIngredient("meat");
		recipe1.addIngredient("worchestershire sauce");
		recipe1.addIngredient("salt");
		recipe1.addIngredient("pepper");
		recipe1.addDirection("cook the meatloaf");
		recipe1.addDirection("take out meatloaf");
		recipe1.addDirection("season meatloaf");
		recipe1.addDirection("eat meatloaf");
		recipe1.setLength(5);
		assert(recipe1.equals(recipe1));		// equals itself
		assert(!recipe1.equals("Meatloaf"));	// doesn't equal another recipe just because they have same name
		
		Recipe recipe2 = new Recipe("meatloaf");
		assert(!recipe1.equals(recipe2));
		recipe2.addIngredient("meat");
		recipe2.addIngredient("worchestershire sauce");
		recipe2.addIngredient("salt");
		recipe2.addIngredient("pepper");
		assert(!recipe1.equals(recipe2));
		recipe2.addDirection("cook the meatloaf");
		recipe2.addDirection("take out meatloaf");
		recipe2.addDirection("season meatloaf");
		recipe2.addDirection("eat meatloaf");
		assert(!recipe1.equals(recipe2));
		recipe2.setLength(5);
		assert(recipe1.equals(recipe2));		// once all fields are exact, they are considered equal
		
		Recipe recipe3 = new Recipe("meatloafs");
		recipe3.addIngredient("meat and loaf");
		recipe3.addIngredient("sauce");
		recipe3.addDirection("make a meatloaf");
		recipe3.addDirection("eat the meatloaf");
		assert(!recipe1.equals(recipe3));		// different recipes aren't equal
		
		Recipe recipe4 = new Recipe("meat loaf");
		recipe4.addIngredient("meat");
		recipe4.addIngredient("worchestershire sauce");
		recipe4.addIngredient("salt");
		recipe4.addIngredient("pepper");
		recipe4.addDirection("cook the meatloaf");
		recipe4.addDirection("take out meatloaf");
		recipe4.addDirection("season meatloaf");
		recipe4.addDirection("eat meatloaf");
		recipe4.setLength(5);
		assert(!recipe1.equals(recipe4));		// everything matches except for name -- still not equal
		recipe4.setName("meatloaf");
		assert(recipe1.equals(recipe4));		// name changed to match -- now equal
	}
	
	@Test
	void compareTo() {		// tests compareTo method between varying Recipe objects
		Recipe a = new Recipe("a");
		Recipe A = new Recipe("A");
		Recipe b = new Recipe("b");
		Recipe c = new Recipe("c");
		
		assert(a.compareTo(a) == 0);
		assert(a.compareTo(A) == 0);
		assert(a.compareTo(b) < 0);
		assert(a.compareTo(c) < 0);
		assert(b.compareTo(a) > 0);
		assert(b.compareTo(c) < 0);
		assert(c.compareTo(a) > 0);
		assert(c.compareTo(b) > 0);
	}
}