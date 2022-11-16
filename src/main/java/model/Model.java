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

public class Model implements Serializable {
	
	private static final long serialVersionUID = -7981606873473705713L;
	private static DB db;
	private ArrayList<Recipe> favorites = new ArrayList<Recipe>();

	private ArrayList<String> pantryList = new ArrayList<String>();

	private static final String DATA_FILE_NAME = "user_data.dat";
	
	private static final String INGREDIENTS_FILE_NAME = "ingredients.txt";
	
	private ArrayList<String> allIngredients = new ArrayList<String>();
	
	public Model() {
		db = new DB();
		File userData = new File(DATA_FILE_NAME); 
		if (userData.exists()) {
			initializeUserData();
		}
		initializeIngredients();
	}
	
	// skips initialization of user data for testing
	public Model(boolean testing) {
		db = new DB();
		initializeIngredients();
	}
	
	public Recipe[] searchDatabase(String keyword) throws Exception {
		return db.Search(keyword);
	}

	public void removePantryItem(String item) {
		pantryList.remove(item);
	}

	public boolean addPantryItem(String item) {
		if (pantryList.contains(item)) {
			return false;
		} else {
			pantryList.add(titleCase(item));
			Collections.sort(pantryList, String.CASE_INSENSITIVE_ORDER);
			return true;
		}
	}
	
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
		return str;
	}

	public List<String> getPantry() {
		return pantryList;
	}

	public List<Recipe> searchWithPantry() throws Exception {
		return db.PantryQuery(getPantry());
	}
	
	public void saveUserData() {
		try {
			ObjectOutputStream output =  new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME));
			output.writeObject(this);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public void addToFavorites(Recipe recipe) {
		favorites.add(recipe);
	}
	
	public void removeFromFavorites(Recipe recipe) {
		favorites.remove(recipe);
	}
	
	public ArrayList<Recipe> getFavorites() {
		return this.favorites;
	}
	
	public void moveFavoriteUp(int index, Recipe recipe) {
		favorites.remove(index);
		favorites.add(index-1, recipe);
	}
	
	public void moveFavoriteDown(int index, Recipe recipe) {
		favorites.remove(index);
		favorites.add(index+1, recipe);
	}
	
	public List<String> getAllIngredients() {
		return allIngredients;
	}
}
