package controller;

import utilities.Recipe;

import java.util.ArrayList;
import java.util.List;

import model.Model;

/**
 * This class is designed to mediate any necessary interactions between
 * the <code>Model</code> and <code>View</code> (<code><a href="../view/GUI.html">GUI</a></code>)
 * in the <i>Apicius</i> application. The primary functions of this controller are
 * as follows:
 * <ul>
 * <li>Take a pantry/normal search query from the <code>GUI</code>, send it to the <code>Model</code>
 * 	   to search the database with, and return the <code>Model's</code> results to the <code>GUI</code>.</li>
 * <li>Send requests from the <code>GUI</code> to add, remove, or rearrange recipes in the user's favorite's
 * 	   list in the <code>Model</code>.</li>
 * <li>Send requests from the <code>GUI</code> to add or remove ingredients from the user's virtual pantry
 *     in the <code>Model</code>.</li>
 * <li>Return data structures from the <code>Model</code> such as the user's favorites list or virtual pantry.</li>
 * <li>Signal to the <code>Model</code> that it is time to save the user's data.</li>
 * </ul>
 * 
 * @see <code><a href="../view/GUI.html">GUI</a></code>
 * @see <code><a href="../Model/Model.html">Model</a></code>
 * @see <code><a href="../utilities/Recipe.html">Recipe</a></code>
 * 
 * @author Katherine Wilson
 * @author Kyle Clements
 * @author Zachary Empkey
 */
public class Controller {
	// -------------------------------------------[  FIELDS  ]-----------------------------------------------
	/**
	 * <code>Model</code> that this <code>Controller</code> is
	 * designed to interact with. This model contains user data,
	 * app data, and tools for interacting with the recipe database.
	 */
	Model model;
	
	// ---------------------------------------[  INITIALIZATION  ]-------------------------------------------
	/**
	 * Creates a <code>Controller</code> object. A reference to the 
	 * <code><a href="../model/Model.html">Model</a></code> argument
	 * given will be stored as {@link #model} and will act as the 
	 * specific <code>Model</code> instance that this class interacts
	 * with.
	 * 
	 * @param model <code>Model</code> instance associated with this 
	 * 				<code>Controller</code> instance.
	 */
	public Controller(Model model) {
		this.model = model;
	}
	
	// ----------------------------------------[  PUBLIC METHODS  ]------------------------------------------
	/**
	 * Forwards the given search query to the <code>Model</code> for a 
	 * list of recipes with names that match the given keyword from the
	 * database.
	 * 
	 * @param keyword Keyword used to search the <code>Model's</code>
	 * 				  database.
	 * @return A list of <code>Recipe</code> objects with names that
	 * 			match the given query.
	 */
	public Recipe[] searchRecipes(String keyword) {
		try {
			return model.searchDatabase(keyword.toLowerCase());
		} catch (Exception e) {
			return new Recipe[0];
		}
	}
	
	/**
	 * Sends a recipe from the <code>View</code> to the <code>Model</code>
	 * to add to its user favorites list.
	 * 
	 * @param recipe <code>Recipe</code> to be added to the user's
	 * 				 favorites list.
	 * @return <code>true</code> if the <code>Recipe</code> was
	 * 				successfully added or<br>
	 * 		   <code>false</code> if the operation was unsuccessful.
	 */
	public boolean addToFavorites(Recipe recipe) {
		if (model.getFavorites().contains(recipe)) {
			return false;
		}
		model.addToFavorites(recipe);
		return true;
	}
	
	/**
	 * Sends a recipe from the <code>View</code> to the <code>Model</code>
	 * to remove from its user favorites list.
	 * 
	 * @param recipe <code>Recipe</code> to be removed from the user's
	 * 				 favorites list.
	 */
	public void removeFromFavorites(Recipe recipe) {
		model.removeFromFavorites(recipe);
	}
	
	/**
	 * Returns the user's favorites list from the <code>Model</code>.
	 * 
	 * @return the list of <code>Recipes</code> that the user has added
	 * 			to their favorites list.
	 */
	public ArrayList<Recipe> getFavorites() {
		return model.getFavorites();
	}
	
	/**
	 * Moves the given recipe up by one index in the user's favorites list.
	 * 
	 * @param index Index of the recipe in the user's favorites list.
	 * @param recipe Recipe to be moved up.
	 */
	public void moveFavoriteUp(int index, Recipe recipe) {
		model.moveFavoriteUp(index, recipe);
	}
	
	/**
	 * Moves the given recipe down by one index in the user's favorites list.
	 * 
	 * @param index Index of the recipe in the user's favorites list.
	 * @param recipe Recipe to be moved down.
	 */
	public void moveFavoriteDown(int index, Recipe recipe) {
		model.moveFavoriteDown(index, recipe);
	}
	
	/**
	 * Returns a list of all possible ingredients across every recipe in the
	 * database.
	 * 
	 * @return list of all ingredients as <code>Strings</code>.
	 */
	public List<String> getAllIngredients() {
		return model.getAllIngredients();
	}
	
	/**
	 * Sends an ingredient to the <code>Model</code> to be added to the user's 
	 * "virtual pantry".
	 * 
	 * @param item <code>String</code> representing an ingredient in the database's
	 * 				ingredient's list.
	 * @return <code>true</code> if the ingredient was added successfully or <br>
	 * 			<code>false</code> if the operation was unsuccessful.
	 */
	public boolean addPantryItem(String item) {
		if (model.getAllIngredients().contains(item)) { // ensures new item is valid
			return model.addPantryItem(item);
		}
		return false;
	}
	
	/**
	 * Sends an ingredient to the <code>Model</code> to be removed from the user's 
	 * "virtual pantry".
	 * 
	 * @param item Ingredient to be removed from the user's virtual pantry.
	 */
	public void removePantryItem(String item) {
		model.removePantryItem(item);
	}

	/**
	 * Returns a list of all ingredients in the user's virtual pantry
	 * as <code>Strings</code>.
	 * 
	 * @return a list of <code>Strings</code> representing the user's 
	 * 			virtual pantry.
	 */
	public List<String> getPantry() {
		return model.getPantry();
	}

	/**
	 * Forwards a pantry search request to the <code>Model</code>.
	 * The <code>Model</code> will search its database for recipes with
	 * ingredients that the user's pantry contains. The results will be
	 * returned as a list of those <code>Recipe</code> objects.
	 * 
	 * @return A list of <code>Recipe</code> objects that the user can make
	 * 			based on what's in their virtual pantry.
	 */
	public List<Recipe> searchWithPantry() {
		try {
			return model.searchWithPantry();
		} catch (Exception e) {
			return new ArrayList<Recipe>();
		}
	}
	
	/**
	 * Sends a signal to the <code>Model</code> that is time to save
	 * the user's data to the user data file.
	 */
	public void saveUserData() {
		model.saveUserData();
	}
}