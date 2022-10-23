package utilities;

import java.util.ArrayList;
import java.util.List;

// object that stores recipe data

public class Recipe {
	
	String name;	// name of recipe
	List<String> ingredients; // ingredients in the recipe
	
	public Recipe(String name) {
		this.name = name;
		this.ingredients = new ArrayList<String>();
	}
	
	public String toString() {
		return name;
	}
	
	// Adds an ingredient to the ingredient array for the recipe
	public void addIngredient(String ingredient) 
	{
		this.ingredients.add(ingredient);
	}
	
	public List<String> getIngredients() 
	{
		return this.ingredients;
	}
	
	public String getIngredientString() 
	{
		String ingredientString = "";
		for (int i = 0; i < this.ingredients.size(); i++) 
		{
			if (i == this.ingredients.size()-1) 
			{
				ingredientString = ingredientString + this.ingredients.get(i);
			}
			else 
			{
				
				ingredientString = ingredientString + this.ingredients.get(i) + ", ";
			}
		}
		return ingredientString;
	}
	
}