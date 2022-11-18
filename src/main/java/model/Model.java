package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import utilities.DB;
import utilities.Recipe;

/**
 * This class stores and maintains user/app data and provides tools for interacting with 
 * the database through the <code><a href="../utilities/DB.html">DB</a></code> class. The
 * data this class manages are as follows:
 * <ul>
 * <li>User Data: Favorites List, Virtual Pantry</li>
 * <li>Application Data: List of all ingredients in the database</li>
 * </ul>
 * The interactions between the application and database allowed by this class are:
 * <ul>
 * <li>Searching for recipes by recipe name (standard search)</li>
 * <li>Searching for recipes based on ingredients (pantry search)</li>
 * </ul>
 * 
 * @see <code><a href="../view/GUI.html">GUI</a></code>
 * @see <code><a href="../controller/Controller.html">Controller</a></code>
 * @see <code><a href="../utilities/Recipe.html">Recipe</a></code>
 * @see <code><a href="../utilities/DB.html">DB</a></code>
 * 
 * 
 * @author Katherine Wilson
 * @author Zachary Empkey
 */
public class Model implements Serializable {
	// -------------------------------------------[  FIELDS  ]-----------------------------------------------
	/**
	 * Unique identifier for this class in the serialization process
	 * for writing user data.
	 */
	private static final long serialVersionUID = -7981606873473705713L;
	/**
	 * Class of database tools used by this <code>Model</code> to 
	 * perform searches.
	 */
	private static DB db;
	/**
	 * User's list of favorite recipes.
	 */
	private ArrayList<Recipe> favorites = new ArrayList<Recipe>();
	/**
	 * User's "virtual pantry". Contains a list of ingredients as
	 * <code>Strings</code>.
	 */
	private ArrayList<String> pantryList = new ArrayList<String>();
	/**
	 * Name of the file this class will save or load user data into.
	 */
	private static final String DATA_FILE_NAME = "user_data.dat";
	/**
	 * Name of the file containing a list of all ingredients in the 
	 * database.
	 */
	private static final String INGREDIENTS_FILE_NAME = "ingredients.txt";
	/**
	 * List of all ingredients across all recipes in the database. They
	 * are stored in title case as <code>Strings</code>.
	 */
	private ArrayList<String> allIngredients = new ArrayList<String>();
	
	// ---------------------------------[  INITIALIZATION / DATA  ]---------------------------------------
	/**
	 * Creates a <code>Model</code> object. If a user data file exists
	 * ({@value #DATA_FILE_NAME}) in this project's directory, then its
	 * data will be loaded into this object. If not, a blank <code>Model</code>
	 * will be created.
	 */
	public Model() {
		db = new DB();
		File userData = new File(DATA_FILE_NAME); 
		if (userData.exists()) {
			initializeUserData();
		}
		initializeIngredients();
	}
	
	/**
	 * Creates a <code>Model</code> that skips the initialization of user
	 * data. This is used solely for testing purposes.
	 * 
	 * @param testing Parameter used to set this testing constructor
	 * apart from the real one. Its value is arbitrary.
	 */
	public Model(boolean testing) {
		db = new DB();
		initializeIngredients();
	}
	
	/**
	 * Initializes the favorites list and virtual pantry from the user data 
	 * file. These will be stored as {@link #favorites} and {@link #pantryList}
	 * respectively.
	 * 
	 * @see #DATA_FILE_NAME
	 */
	private void initializeUserData() {
		ObjectInputStream input;
		try {
			input = new ObjectInputStream(new FileInputStream(DATA_FILE_NAME));
			Model model = (Model) input.readObject();	// reads in model object from file
			if (model != null) {
				if (model.favorites != null) {
					this.favorites = (ArrayList<Recipe>) model.favorites.clone();
				}
				if (model.pantryList != null) {
					this.pantryList = (ArrayList<String>) model.pantryList.clone();
				}
			}
			input.close();
		} catch (IOException | ClassNotFoundException e) { }
	}
	
	/**
	 * Initializes the list of all ingredients in the database
	 * ({@link #allIngredients}) from the {@value #INGREDIENTS_FILE_NAME}
	 * file.
	 */
	private void initializeIngredients() {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(INGREDIENTS_FILE_NAME));
			while (scanner.hasNextLine()) {
				allIngredients.add(titleCase(scanner.nextLine().strip()));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the user's favorites list ({@link #favorites}) and
	 * virtual pantry ({@link #pantryList}) to the user data file
	 * ({@value #DATA_FILE_NAME}).
	 * <br>
	 * This should be called whenever the user exits the application
	 * to make sure the data from their current session is not lost.
	 */
	public void saveUserData() {
		try {
			ObjectOutputStream output =  new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME));
			output.writeObject(this);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ----------------------------------------[  PUBLIC METHODS  ]------------------------------------------
	/**
	 * Uses the {@link #db} class to search the database for
	 * recipes with names that match the given query.
	 * 
	 * @param keyword Search query to send to database.
	 * @return a list of recipe search results.
	 */
	public Recipe[] searchDatabase(String keyword) {
		try {
			return db.Search(keyword);
		} catch (Exception e) {
			return new Recipe[0];
		}
	}
	
	/**
	 * Returns the user's list of favorites ({@link #favorites}).
	 * 
	 * @return the user's favorites list as a <code>List</code>
	 * of <code>Recipe</code> objects.
	 */
	public ArrayList<Recipe> getFavorites() {
		return this.favorites;
	}
	
	/**
	 * Adds the given recipe to the user's favorites list.
	 * 
	 * @param recipe Recipe to add to the user's favorites
	 * list.
	 */
	public void addToFavorites(Recipe recipe) {
		favorites.add(recipe);
	}
	
	/**
	 * Removes the given recipe from the user's favorites list.
	 * 
	 * @param recipe Recipe to remove from the user's favorites
	 * list.
	 */
	public void removeFromFavorites(Recipe recipe) {
		favorites.remove(recipe);
	}
	
	/**
	 * Moves a favorite up by one index in the user's favorites
	 * list.
	 * 
	 * @param recipe Recipe to move up.
	 */
	public void moveFavoriteUp(Recipe recipe) {
		int index = favorites.indexOf(recipe);
		if (index != 0) {	// if not first in list
			favorites.remove(index);
			favorites.add(index-1, recipe);
		}
	}
	
	/**
	 * Moves a favorite down by one index in the user's favorites
	 * list.
	 * 
	 * @param recipe Recipe to move down.
	 */
	public void moveFavoriteDown(Recipe recipe) {
		int index = favorites.indexOf(recipe);
		if (index != favorites.size() - 1) { // if not last in list
			favorites.remove(index);
			favorites.add(index+1, recipe);
		}
	}
	
	/**
	 * Returns a list of all ingredients across all recipes
	 * in the database.
	 * 
	 * @return the list of all ingredients as <code>Strings</code>.
	 */
	public List<String> getAllIngredients() {
		return allIngredients;
	}
	
	/**
	 * Returns the user's "virtual pantry".
	 * 
	 * @return the list of ingredients in the user's pantry as
	 * <code>Strings</code>.
	 */
	public List<String> getPantry() {
		return pantryList;
	}
	
	/**
	 * Adds an ingredient to the user's "virtual pantry" 
	 * ({@link #pantryList}). <br>
	 * <i>This function assumes the given ingredient is in
	 * {@link #allIngredients}.</i>
	 * 
	 * @param item Ingredient to add to the pantry.
	 * @return <code>true</code> if the ingredient was 
	 * 				successfully added or <br>
	 * 		   <code>false</code> if the pantry already
	 * 				contains the ingredient.
	 */
	public boolean addPantryItem(String item) {
		if (pantryList.contains(titleCase(item))) {
			return false;
		} else {
			pantryList.add(titleCase(item));
			Collections.sort(pantryList, String.CASE_INSENSITIVE_ORDER);
			return true;
		}
	}
	
	/**
	 * Removes an ingredient from the user's "virtual pantry".
	 * ({@link #pantryList}). <br>
	 * <i>This function assumes the given ingredient is in
	 * {@link #allIngredients}.</i>
	 * 
	 * @param item Ingredient to remove from the pantry.
	 */
	public void removePantryItem(String item) {
		pantryList.remove(item);
	}

	/**
	 * Uses the {@link #db} class to perform a "pantry search".
	 * This is a search for recipes that the user can make based
	 * on the ingredients in their pantry.
	 * 
	 * @return A list of recipes the user can make with what's in
	 * 			their virtual pantry.
	 */
	public List<Recipe> searchWithPantry() {
		try {
			return db.PantryQuery(getPantry());
		} catch (Exception e) {
			return new ArrayList<Recipe>();
		}
	}
	
	// --------------------------------------[  PRIVATE METHODS  ]----------------------------------------
	/**
	 * Converts the given <code>String</code> to title case. This function
	 * will ignore quotation marks at the beginning of the <code>String</code>
	 * and capitalize the first letter it finds.
	 * 
	 * @param str <code>String</code> to be converted to title case.
	 * @return the given <code>String</code> in title case.
	 */
	private String titleCase(String str) {
		boolean hasQuotes = false;
		if (str.length() > 1) {
			if (str.charAt(0) == '"') {
				hasQuotes = true;
			}
			String[] words = str.split(" ");
			String newString = "";
			for (String word : words) {
				word = word.strip();
				if (word.length() > 1) {
					newString += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
				} else if (word.length() == 1) {
					newString += word.toUpperCase() + " ";
				}
			}
			if (hasQuotes) {	// capitalizes first letter after quotation mark
				newString = '"' + newString.substring(1, 2).toUpperCase() + newString.substring(2);
			}
			return newString.substring(0, newString.length() - 1);
		}
		return str.toUpperCase();
	}
}