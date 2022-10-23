package main.java.view;

import java.util.Scanner;


import main.java.controller.Controller;
import main.java.model.Model;
import main.java.utilities.Recipe;

// console-based test view 

public class TextView {
	public static void main(String[] args) throws Exception {
		Model model = new Model();
		Controller controller = new Controller(model);
		boolean cont = true;
		while (cont) {
			search(controller);
			cont = continueLoop();
		}
	}
	
	
	public static void search(Controller controller) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a keyword:");
		if (scanner.hasNextLine()) {
			String response = scanner.nextLine().strip();
			if (response.length() != 0) {
				showRecipes(response, controller.searchRecipes(response));
			} else {
				System.out.print("\nInvalid response: Must provide input. ");
				search(controller);
			}
		} else {
			System.out.print("\nInvalid response: Must provide input. ");
			search(controller);
		}
	}
	
	
	public static void showRecipes(String response, Recipe[] recipes) {
		if (recipes == null) {
			System.out.printf("No recipes found containing keyword: \"%s\"\n", response);
		} else {
			for (int i = 0; i < recipes.length; i++) {
				System.out.print("---------------\n" + recipes[i] + "\n---------------\n");
			}
		}
	}
	
	
	public static boolean continueLoop() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Perform another search? y/n");
		if (scanner.hasNextLine()) {
			String response = scanner.nextLine().strip();
			if (response.length() != 0) {
				if (response.charAt(0) == 'y' || response.charAt(0) == 'Y') {
					return true;
				} if (response.charAt(0) == 'n' || response.charAt(0) == 'N') {;
					return false;
				} else {
					System.out.println("Invalid response: Please type 'y' or 'n'.");
					return continueLoop();
				}
			} else {
				System.out.print("\nInvalid response: Must provide input. ");
				return continueLoop();
			}
		} else {
			System.out.print("\nInvalid response: Must provide input. ");
			return continueLoop();
		}
	}
}
