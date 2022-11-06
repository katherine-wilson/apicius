package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilities.DB;
import utilities.Recipe;

public class Model implements Serializable {
	
	private static final long serialVersionUID = -7981606873473705713L;
	private static DB db;
	private ArrayList<Recipe> favorites = new ArrayList<Recipe>();

	private ArrayList<String> pantry_list = new ArrayList<String>();

	private static final String DATA_FILE_NAME = "user_data.dat";
	
	public Model() {
		db = new DB();
	}
	
	public Model(String dataFile) throws FileNotFoundException, IOException, ClassNotFoundException {
		db = new DB();
		initializeUserData(dataFile);
	}
	
	public Recipe[] searchDatabase(String keyword) throws Exception {
		// search data base
		System.out.println("Searching for... " + keyword);
		return db.Search(keyword);
		
	}

	public void removePantryItem(String item) {
		pantry_list.remove(item);
	}

	public void addPantryItem(String item) {
		pantry_list.add(item);
	}

	public List<String> getPantry() {
		return pantry_list;
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
	
	private void initializeUserData(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
		Model model = (Model) input.readObject();	// reads in model object from file
		if (model != null) {
			this.favorites = (ArrayList<Recipe>) model.favorites.clone();
			this.pantry_list = (ArrayList<String>) model.pantry_list.clone();
		}
		input.close();
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
}
