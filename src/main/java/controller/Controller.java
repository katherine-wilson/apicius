package controller;

import utilities.Recipe;

import java.util.ArrayList;
import java.util.List;

import model.Model;

public class Controller {
	
	Model model;		// model this controller interacts with
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public void saveUserData() {
		model.saveUserData();
	}
	
	public boolean addToFavorites(Recipe recipe) {
		if (model.getFavorites().contains(recipe)) {
			return false;
		}
		model.addToFavorites(recipe);
		return true;
	}
	
	public void removePantryItem(String item) {
		model.removePantryItem(item);
	}

	public boolean addPantryItem(String item) {
		if (model.getAllIngredients().contains(item)) { // ensures new item is valid
			return model.addPantryItem(item);
		}
		return false;
	}

	public List<String> getPantry() {
		return model.getPantry();
	}

	public List<Recipe> searchWithPantry() throws Exception {
		return model.searchWithPantry();
	}

	public void removeFromFavorites(Recipe recipe) {
		model.removeFromFavorites(recipe);
	}
	
	public ArrayList<Recipe> getFavorites() {
		return model.getFavorites();
	}
	
	public Recipe[] searchRecipes(String keyword) throws Exception {
		return model.searchDatabase(keyword.toLowerCase());
	}
	
	public void moveFavoriteUp(int index, Recipe recipe) {
		model.moveFavoriteUp(index, recipe);
	}
	
	public void moveFavoriteDown(int index, Recipe recipe) {
		model.moveFavoriteDown(index, recipe);
	}
	
	public List<String> getAllIngredients() {
		return model.getAllIngredients();
	}
}