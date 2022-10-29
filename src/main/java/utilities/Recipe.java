package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// object that stores recipe data

public class Recipe implements Serializable {
	
	private static final long serialVersionUID = 150613706091702109L;
	private String name;				// name of recipe
	private int length;					// amount of time (minutes) to make
	private int steps;					// number of steps to make
	private List<String> ingredients; 	// ingredients needed
	private List<String> directions;	// steps to follow to make recipe
	
	// TODO: add constructor with more args?
	public Recipe(String name) {
		this.name = titleCase(name);
		this.length = -1;
		this.steps = -1;
		this.ingredients = new ArrayList<String>();
		this.directions = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getSteps() {
		return steps;
	}
	
	public int getNumIngredients() {
		return ingredients.size();
	}
	
	public List<String> getIngredients() {
		return this.ingredients;
	}
	
	public List<String> getDirections() {
		return this.directions;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	// Adds an ingredient to the ingredient array for the recipe
	public void addIngredient(String ingredient) {
		this.ingredients.add(titleCase(ingredient));
	}
	
	public void addDirection(String direction) {
		this.directions.add(direction);
	}
	
	public String getIngredientString() {
		String ingredientString = "";
		for (int i = 0; i < this.ingredients.size(); i++) {
			String ingredient = this.ingredients.get(i).substring(0,1).toUpperCase() + 
					this.ingredients.get(i).substring(1).toLowerCase();	// capitalizes first letter
			if (i == this.ingredients.size()-1) {
				ingredientString = ingredientString + ingredient;
			} else {
				ingredientString = ingredientString + ingredient + "\n";
			}
		}
		return ingredientString;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof Recipe)) {
			return false;
		}
		
		Recipe recipe = (Recipe) obj;
		if (recipe.getName().equals(this.getName()) &&
				recipe.getNumIngredients() == this.getNumIngredients() &&
				recipe.getSteps() == this.getSteps() &&
				recipe.getLength() == this.getLength()) {
			return true;
		}
		return false;
	}
	
	private String titleCase(String str) {
		if (str.length() > 1) {
			String[] words = str.split(" ");
			String newString = "";
			for (String word : words) {
				word = word.strip();
				if (word.length() > 1) {
					newString += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
				} else  if (word.length() == 1) {
					newString += word.toUpperCase() + " ";
				}
			}
			return newString.substring(0, newString.length() - 1);
		}
		return str;
	}
	
}