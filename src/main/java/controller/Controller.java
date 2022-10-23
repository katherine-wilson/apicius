package main.java.controller;

import main.java.model.Model;
import main.java.utilities.Recipe;

public class Controller {
	
	Model model;		// model this controller interacts with
	
	public Controller(Model model) {
		this.model = model;
	
		
	}
	
	public Recipe[] searchRecipes(String keyword) throws Exception {
		return model.searchDatabase(keyword);
	}
	
	
	
}