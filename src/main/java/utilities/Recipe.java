package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store <code>Recipe</code> data for <i>one</i> recipe.
 * This class stores the following data:
 * <ul>
 * <li>Recipe name</li>
 * <li>Number of minutes it takes to make</li>
 * <li>List of ingredients it takes to make</li>
 * <li>List of directions for making it</li>
 * </ul>
 * <br>
 * <i>This class is <code>Serializable</code> so that it may be saved to
 * and loaded from files.</i>
 * 
 * @author Katherine Wilson
 * @author Kyle Clements
 */
public class Recipe implements Serializable, Comparable<Recipe> {
	// -------------------------------------------[  FIELDS  ]-----------------------------------------------
	/**
	 * Unique identifier for this class in the serialization process
	 * for writing user data.
	 */
	private static final long serialVersionUID = 150613706091702109L;
	/**
	 * Name of this recipe.
	 */
	private String name;
	/**
	 * Amount of time (minutes) this recipe takes to make.
	 */
	private int length;
	/**
	 * List of ingredients needed to make this recipe. These are
	 * stored in title case.
	 */
	private List<String> ingredients;
	/**
	 * List of directions for making this recipe.
	 */
	private List<String> directions;
	
	// ---------------------------------------[  INITIALIZATION  ]-------------------------------------------
	/**
	 * Constructs a <code>Recipe</code> object for storing
	 * recipe data. Initializes data to default/undefined 
	 * values if not given.
	 * 
	 * @param name Name of the recipe.
	 */
	public Recipe(String name) {
		this.name = titleCase(name);
		this.length = 0;
		this.ingredients = new ArrayList<String>();
		this.directions = new ArrayList<String>();
	}

	// ----------------------------------------[  PUBLIC METHODS  ]------------------------------------------
	/**
	 * Returns the name of this recipe.
	 * 
	 * @return the name of this recipe as a <code>String</code>.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the amount of time it takes to make this recipe.
	 * 
	 * @return the length of this recipe (minutes).
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Returns the number of steps involved in making this
	 * recipe.
	 * 
	 * @return the number of steps it takes to make this recipe.
	 */
	public int getSteps() {
		return directions.size();
	}
	
	/**
	 * Returns the number of ingredients involved in making
	 * this recipe.
	 * 
	 * @return the number of ingredients it takes to make this
	 * recipe.
	 */
	public int getNumIngredients() {
		return ingredients.size();
	}
	
	/**
	 * Returns the ingredients required to make this recipe.
	 * 
	 * @return a list of ingredients (as <code>Strings</code>)
	 * used in this recipe.
	 */
	public List<String> getIngredients() {
		return this.ingredients;
	}
	
	/**
	 * Returns this recipe's list of ingredients as one
	 * <code>String</code>. Each ingredient is separated
	 * by a newline character.
	 * 
	 * @return a <code>String</code> of all ingredients needed.
	 * 
	 */
	public String getIngredientString() {
		String ingredientString = "";
		for (int i = 0; i < this.ingredients.size(); i++) {
			String ingredient = this.ingredients.get(i);
			if (i == this.ingredients.size()-1) {
				ingredientString += ingredient;
			} else {
				ingredientString += ingredient + "\n";
			}
		}
		return ingredientString;
	}
	
	/**
	 * Returns the directions for this recipe.
	 * 
	 * @return a list of directions (as <code>Strings</code>)
	 * for making this recipe.
	 */
	public List<String> getDirections() {
		return this.directions;
	}
	
	/**
	 * Changes the name of this recipe.
	 * 
	 * @param name New name of this recipe.
	 */
	public void setName(String name) {
		this.name = titleCase(name);
	}
	
	/**
	 * Changes the amount of time it takes to make this recipe.
	 * 
	 * @param length Number of minutes to make this recipe.
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Adds an ingredient to the {@link #ingredients} of this
	 * recipe.
	 * 
	 * @param ingredient Ingredient to add to this recipe.
	 */
	public void addIngredient(String ingredient) {
		this.ingredients.add(titleCase(ingredient));
	}
	
	/**
	 * Adds a step to the {@link #directions} of this recipe.
	 * This will be added to the end of the directions list.
	 * 
	 * @param direction Instruction to add to this recipe.
	 */
	public void addDirection(String direction) {
		this.directions.add(direction);
	}
	
	/**
	 * Determines if the given <code>Object</code> is equal to
	 * this <code>Recipe</code>.
	 * 
	 * @param obj Object to compare to this <code>Recipe</code>.
	 * 
	 * @return <code>true</code> if the given <code>Object</code> is equal
	 * 			to this <code>Recipe</code> based on their numerical fields
	 * 			or <br><code>false</code> if they are not equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Recipe)) {
			return false;
		} else {
			Recipe recipe = (Recipe) obj;
			if (recipe.getName().equals(this.getName()) &&
					recipe.getNumIngredients() == this.getNumIngredients() &&
					recipe.getSteps() == this.getSteps() &&
					recipe.getLength() == this.getLength()) {
				return true;
			}
			return false;
		}
	}
	
	@Override
	/**
	 * Compares two recipes by their names.
	 * 
	 * @param r <code>Recipe</code> to compare to this one.
	 * 
	 * @return an <code>int</code> representing the results
	 * of the comparison. If the value is 0, they are equal.
	 * If the value is less than 0, this object is lexicographically
	 * less than the given <code>Recipe</code>. The opposite
	 * is true for a value greater than 0.
	 * 
	 */
	public int compareTo(Recipe r) {
		return this.getName().compareTo(r.getName());
	}
	
	// ---------------------------------------[  PRIVATE METHODS  ]------------------------------------------
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