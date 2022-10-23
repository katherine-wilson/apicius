package main.java.model;

import main.java.utilities.DB;
import main.java.utilities.Recipe;

public class Model {
	
	DB db;
	
	public Model() {
		// initialize data base
		// load user data
		db = new DB();
	}
	
	public Recipe[] searchDatabase(String keyword) throws Exception {
		// search data base
		System.out.println("Searching for... " + keyword);
		return db.Search(keyword);
		
	}
}
