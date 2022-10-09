package controller;

import model.Model;
import utilities.Recipe;

public class Controller {
	
	Model model;		// model this controller interacts with
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public Recipe[] searchRecipes(String keyword) {
		return model.searchDatabase(keyword);
	}
	
	
	
}
