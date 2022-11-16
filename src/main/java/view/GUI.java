package view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import controller.Controller;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;
import utilities.Recipe;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class GUI extends Application {

	// Menu Buttons
	@FXML
	private Button searchButton;
	@FXML
	private Button favoritesButton;
	@FXML
	private Button pantryButton;
	
	// Search Menu Fields
	static ArrayList<Recipe> allRecipes;
	ObservableList<Recipe> obsResults = FXCollections.observableArrayList();
	private boolean filtersActive;
	@FXML
	private TableView<Recipe> searchResults;
	@FXML
	private TableColumn<Recipe, String> recipeNameCol;
	@FXML
	private TableColumn<Recipe, Integer> lengthCol;
	@FXML
	private TableColumn<Recipe, Integer> stepsCol;
	@FXML
	private TableColumn<Recipe, Integer> numIngredientsCol;
	@FXML
	private TableColumn<Recipe, String> ingredientsCol;
	@FXML
	private HBox searchToolbar;
	@FXML
	private BorderPane searchInfo;
	@FXML
	private TextField searchBar;
	@FXML
	private Button searchBarButton;
	@FXML
	private Button filterButton;
	@FXML
	private StackPane filterMenu;
	@FXML
	private CheckBox lengthCheckBox;
	@FXML
	private CheckBox stepsCheckBox;
	@FXML
	private CheckBox ingredientsCheckBox;
	@FXML
	private Button confirmFilterButton;
	@FXML
	private Button clearFilterButton;
	@FXML
	private RangeSlider lengthSlider;
	@FXML
	private RangeSlider stepsSlider;
	@FXML
	private RangeSlider ingredientsSlider;
	@FXML
	private Label recipesFound;
	@FXML
	private Label filtersApplied;
	@FXML
	private Label noRecipesFound;
	
	// Recipe Menu Fields
	@FXML
	private AnchorPane recipeMenu;
	@FXML
	private VBox ingredientsBox;
	@FXML
	private VBox directionsBox;
	@FXML
	private Button recipeMenuButton;
	@FXML
	private Label recipeTitle;
	@FXML
	private Label recipeLength;
	@FXML
	private Label recipeSteps;
	@FXML
	private Label recipeIngredientCount;
	@FXML
	private Button recipeFavoriteButton;
	private Recipe currentRecipe;
	
	// Favorites Menu Fields
	ObservableList<String> obsFavorites = FXCollections.observableArrayList();
	private HashMap<String, Recipe> nameToRecipe = new HashMap<String, Recipe>(); 
	private List<String> allIngredients;
	@FXML
	private ListView<String> favoritesList;
	@FXML
	private Button openFavoriteButton;
	@FXML
	private Button deleteFavoriteButton;
	@FXML
	private HBox favoritesButtons;
	@FXML
	private Button upArrow;
	@FXML
	private Button downArrow;	
	
	// Pantry Menu Fields
	ObservableList<String> obsIngredients = FXCollections.observableArrayList();
	private String currentIngredient;
	@FXML
	private AnchorPane pantrySearchMenu;
	@FXML
	private AnchorPane pantryIngredientsMenu;
	@FXML
	private ListView<String> ingredientsList;
	@FXML
	private TextField ingredientSearchBar;
	@FXML
	private AutoCompletionBinding<String> autoIngredient;
	@FXML
	private Button deleteIngredientButton;
	@FXML
	private Button pantrySearchButton;
	@FXML
	private Button backButton;
	@FXML
	private Label menuTitle;
	
	
	// Window Nodes
	static Parent menu;
	static Stage stage;
	static Scene scene;
	
	// MVC classes
	static Controller controller;
	static Model model;
	
	static final String RECIPE_SUBSET_FILE = "recipe_subset.dat"; // name of recipe subset file
	
	static char currentMenu; // 'h' = home, 's' = search, 'f' = favorites, 'p' = pantry
	
	
	@Override
	public void start(Stage stage) throws Exception {
		// load home page FXML file by default
		menu = FXMLLoader.load(getClass().getResource("Home.fxml"));
		currentMenu = 'h';
		
		// initialize static fields - shared by every GUI instance created by FXML
		model = new Model();
		GUI.controller = new Controller(model);
		GUI.stage = stage;
		GUI.scene = new Scene(menu, 1250, 750);
		if (allRecipes == null) {
			GUI.allRecipes = new ArrayList<Recipe>();
			
			ObjectInputStream input;
			try {
				input = new ObjectInputStream(new FileInputStream(RECIPE_SUBSET_FILE));
				ArrayList<Recipe> subset = (ArrayList<Recipe>) input.readObject();	// reads subset from file
				if (subset != null) {
					GUI.allRecipes = (ArrayList<Recipe>) subset.clone();
				}
				input.close();
			} catch (IOException | ClassNotFoundException e) { }
			
			for (Recipe recipe : controller.searchRecipes("")) {
				allRecipes.add(recipe);
			}
		}
		
		// prepares stage + launches GUI
		stage.setTitle("Apicius");
		stage.setScene(scene);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_16.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_32.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_64.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_128.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_256.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Apicius-icon_512.png")));
		stage.setMinWidth(956);
		stage.setMinHeight(595);
		stage.show();
		
		// saves user data on exit
		stage.setOnCloseRequest(e -> { controller.saveUserData(); });
	}
	
	// switches to search menu
	public void searchButtonHandler() {
		currentMenu = 's';
		try {
			menu = FXMLLoader.load(getClass().getResource("Search.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Search.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(menu);
		stage.show();
	}
	
	// switches to favorites menu
	public void favoritesButtonHandler() {
		currentMenu = 'f';
		try {
			menu = FXMLLoader.load(getClass().getResource("Favorites.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Favorites.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(menu);
		stage.show();
	}
	
	// switches to pantry menu
	public void pantryButtonHandler() {
		currentMenu = 'p';
		try {
			menu = FXMLLoader.load(getClass().getResource("Pantry.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Pantry.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(menu);
		stage.show();
	}
	
	
	// called when enter is pressed or magnifying glass button is clicked
	public void searchForRecipes() throws Exception {
		Recipe[] results = controller.searchRecipes(searchBar.getText().strip());
		obsResults.clear(); 				// clears table of previous search results
		searchResults.setVisible(false);	// hides table
		recipesFound.setText("");			// removes "X recipes found" message
		filtersApplied.setText("");			// removes "X filters active" message

		if (results.length != 0) {	// shows table of results if successful search
			noRecipesFound.setVisible(false);
			for (Recipe recipe : results) {		// adds recipes from results to observable list
				obsResults.add(recipe);
			}
			recipesFound.setText(obsResults.size() + " recipes found");
			filterResults();
			if (obsResults.size() != 0) {
				searchResults.setVisible(true);		// shows table
			} else {
				searchResults.setVisible(false);
			}
		} else {					// shows "No Recipes Found" message if unsuccessful search
			noRecipesFound.setVisible(true);
		}
	}
	
	public void hideFilterMenu() {
		filterMenu.setVisible(false);
	}
	
	public void clearFilter() throws Exception {
		filtersActive = false;
		if (currentMenu == 's') {
			resetFilter();
			searchForRecipes(); // searches again to remove filters from results
		} else if (currentMenu == 'p') {
			resetFilter();
			makePantrySearch(false);	// resets recommendations
			filterMenu.setVisible(false);
		}
	}
	
	public void resetFilter() {
		// removes selection from text boxes
		lengthCheckBox.setSelected(false);
		stepsCheckBox.setSelected(false);
		ingredientsCheckBox.setSelected(false);
		
		// resets slider thumbs and disables them
		lengthSlider.setLowValue(0);
		lengthSlider.setHighValue(0);
		lengthSlider.setDisable(true);
		stepsSlider.setLowValue(0);
		stepsSlider.setHighValue(0);
		stepsSlider.setDisable(true);
		ingredientsSlider.setLowValue(0);
		ingredientsSlider.setHighValue(0);
		ingredientsSlider.setDisable(true);
	}
	
	
	// when enter is pressed while typing in search bar, search is made
	public void searchBarHandler(KeyEvent key) throws Exception {
		if (key.getCode().equals(KeyCode.ENTER)) {
			searchForRecipes();
			searchBar.getParent().requestFocus();
		}
	}
	
	// toggles filter menu
	public void filterButtonHandler() {
		if (searchResults.isVisible() || filtersActive) {	// can only filter if there are search results/filters present
			if (filterMenu.isVisible()) {	// toggles filter menu
				filterMenu.setVisible(false);
			} else {
				filterMenu.setVisible(true);
			}
		}
	}
	
	// handlers length checkbox
	public void toggleLengthSlider() {
		if (lengthCheckBox.isSelected()) {
			lengthSlider.setDisable(false);
		} else {
			lengthSlider.setDisable(true);
		}
	}
	
	public void toggleStepsSlider() {
		if (stepsCheckBox.isSelected()) {
			stepsSlider.setDisable(false);
		} else {
			stepsSlider.setDisable(true);
		}
	}
	
	public void toggleIngredientsSlider() {
		if (ingredientsCheckBox.isSelected()) {
			ingredientsSlider.setDisable(false);
		} else {
			ingredientsSlider.setDisable(true);
		}
	}
	
	private int getRanges(int[][] ranges) {
		int filters = 0;
		if (lengthCheckBox.isSelected()) {
			ranges[0][0] = (int) lengthSlider.getLowValue();	// sets minimum
			ranges[0][1] = (int) lengthSlider.getHighValue();	// sets maximum
			filters++;
		}
		if (stepsCheckBox.isSelected()) {
			ranges[1][0] = (int) stepsSlider.getLowValue();
			ranges[1][1] = (int) stepsSlider.getHighValue();
			filters++;
		}
		if (ingredientsCheckBox.isSelected()) {
			ranges[2][0] = (int) ingredientsSlider.getLowValue();
			ranges[2][1] = (int) ingredientsSlider.getHighValue();
			filters++;
		}
		
		return filters;
	}
	
	
	// for each filter, checks if it's enabled, then decides if recipe needs to be
	// filtered based on criteria
	private boolean filterRecipe(Recipe recipe, int[][] ranges) {
		if (lengthCheckBox.isSelected()) {	// filters based on length
			if (recipe.getLength() < ranges[0][0] ||
					recipe.getLength() > ranges[0][1]) {
				return true;
			}
		}
		if (stepsCheckBox.isSelected()) {	// filters based on # of steps
			if (recipe.getSteps() < ranges[1][0] ||
					recipe.getSteps() > ranges[1][1]) {
				return true;
			}
		}
		if (ingredientsCheckBox.isSelected()) {	// filters based on # of ingredients
			if (recipe.getNumIngredients() < ranges[2][0] ||
					recipe.getNumIngredients() > ranges[2][1]) {
				return true;
			}
		}
		
		return false;
	}
	
	// applies user's search filters to the current results
	public void filterResults() throws Exception {
		filterMenu.setVisible(false);
		
		int[][] ranges = new int[3][2];	// inclusive min/max for each value
		int filters = getRanges(ranges);
		
		ArrayList<Recipe> filtered = new ArrayList<Recipe>();	// list of recipes to be removed
		
		// iterates over each recipe result, adds to filter list
		for (Recipe recipe : obsResults) {
			if (filterRecipe(recipe, ranges)) {
				filtered.add(recipe);
			}
		}
		
		// removes filtered recipes (separate loop needed to avoid concurrent modification)
		for (Recipe recipe : filtered) {
			obsResults.remove(recipe);
		}
		
		// updates labels above table
		if (filters > 0) {
			filtersActive = true;
			if (filters == 1) {
				filtersApplied.setText(filters + " filter active");
			} else {
				filtersApplied.setText(filters + " filters active");
			}
			if (obsResults.size() == 1) {
				recipesFound.setText(obsResults.size() + " recipe found");
			} else {
				recipesFound.setText(obsResults.size() + " recipes found");
			}
		} else {
			filtersActive = false;
		}
		
		if (obsResults.size() == 0) {
			noRecipesFound.setVisible(true);
			searchResults.setVisible(false);
		} else {
			noRecipesFound.setVisible(false);
		}
	}
	
	public void selectRecipe() {
		Recipe recipe;
		if (currentMenu == 's' || currentMenu == 'p') {	// takes from tableview
			recipe = searchResults.getSelectionModel().getSelectedItem();
			searchResults.getSelectionModel().clearSelection();	// prevents opening of menu when empty cell clicked
		} else if (currentMenu == 'f') {	// takes from listview
			recipe = nameToRecipe.get(favoritesList.getSelectionModel().getSelectedItem());
			if (recipe == null) {	// returns if selected cell is empty
				return;
			} else {
				favoritesButtons.setVisible(true);	// show favorites button
			}
		} else {	// shouldn't run, but needed to compile
			return;
		}
		currentRecipe = recipe;
	}
	
	
	public void favoriteCellClicked(MouseEvent event) {
		Recipe oldCurrent = currentRecipe;
		selectRecipe();
		if (event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2) {
			openRecipeMenu();
		} else if (oldCurrent != null && oldCurrent == currentRecipe) { // deselects recipe if previously clicked
			favoritesList.getSelectionModel().clearSelection();
			favoritesButtons.setVisible(false);
			currentRecipe = null;
		}
	}
	
	
	public void openRecipeMenu() {
		selectRecipe(); // retrieves recipe that was clicked
		if (currentRecipe != null) {
			if (recipeFavoriteButton != null) {	// only executes if recipe menu has a favorites button
				if (controller.getFavorites().contains(currentRecipe)) {
					recipeFavoriteButton.setStyle("-fx-background-color: linear-gradient(to left, #F27440, #CB1349); -fx-text-fill: white;");
				} else {
					recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
				}
			}
			
			// Set text fields for recipe menu
			recipeTitle.setText(currentRecipe.getName());
			recipeLength.setText(currentRecipe.getLength() + " minutes");
			recipeSteps.setText(currentRecipe.getSteps() + " steps");
			recipeIngredientCount.setText(currentRecipe.getNumIngredients() + " ingredients");
			
			
			// clears all but first element (VBox label)
			ingredientsBox.getChildren().subList(1, ingredientsBox.getChildren().size()).clear();
			directionsBox.getChildren().subList(1, directionsBox.getChildren().size()).clear();
			
			// adds ingredients to VBox as labels
			List<String> ingredients = currentRecipe.getIngredients();
			for (String ingredient : ingredients) {
				Label ingLabel = new Label("-    " + ingredient);
				ingLabel.setFont(Font.font("Nirmala UI", 16));
				ingLabel.setPadding(new Insets(0, 0, 0, 10));
				ingredientsBox.getChildren().add(ingLabel);
			}
			
			// adds directions to VBox as labels
			List<String> directions = currentRecipe.getDirections();
			for (int i = 1; i <= directions.size(); i++) {
				Label dirLabel = new Label(Integer.toString(i) + ". " + directions.get(i-1));
				dirLabel.setFont(Font.font("Nirmala UI", 16));
				dirLabel.setPadding(new Insets(0, 0, 0, 20));
				directionsBox.getChildren().add(dirLabel);
			}
			
			recipeMenu.setVisible(true);
		}
	}
		
	public void closeRecipeMenu() {
		recipeMenu.setVisible(false);
	}
	

	public void hoverOverStar() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// star changes color if not in favorites
			recipeFavoriteButton.setStyle("-fx-text-fill: linear-gradient(to left, #F27440, #CB1349);");
		}
	}
	
	public void exitStar() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// star changes color if not in favorites
			recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
		}
	}
	
	public void toggleFavorite() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// adds to favorites
			recipeFavoriteButton.setStyle("-fx-background-color: linear-gradient(to left, #F27440, #CB1349); -fx-text-fill: white;");
			controller.addToFavorites(currentRecipe);
		} else {													// removes from favorites
			if (recipeFavoriteButton != null) {
				recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
			}
			controller.removeFromFavorites(currentRecipe);
			obsFavorites.remove(currentRecipe.getName());
			if (currentMenu == 'f') {
				if (controller.getFavorites().size() != 0) {		// if some favorites left
					Recipe recipe = nameToRecipe.get(favoritesList.getSelectionModel().getSelectedItem());
					if (recipe == null) {
						return;
					}
					currentRecipe = recipe;		// move current recipe marker to next one selected
				} else {		// if no more favorites left
					currentRecipe = null;		// reset currenRecipe marker
					favoritesButtons.setVisible(false);	// hide open/delete buttons
				}
			}
		}
	}
	
	public void moveFavoriteUp() {
		int index = favoritesList.getSelectionModel().getSelectedIndex();
		if (index > 0) {
			favoritesList.getSelectionModel().clearSelection();
			obsFavorites.remove(index);
			obsFavorites.add(index-1, currentRecipe.getName());
			favoritesList.getSelectionModel().select(index-1);
			controller.moveFavoriteUp(index, currentRecipe);
		}
	}
	
	public void moveFavoriteDown() {
		int index = favoritesList.getSelectionModel().getSelectedIndex();
		if (index < obsFavorites.size() - 1) {
			favoritesList.getSelectionModel().clearSelection();
			obsFavorites.remove(index);
			obsFavorites.add(index+1, currentRecipe.getName());
			favoritesList.getSelectionModel().select(index+1);
			controller.moveFavoriteDown(index, currentRecipe);
		}
	}
	
	// when enter is pressed while typing in ingredient search bar, make search
	public void ingSearchBarHandler(KeyEvent key) throws Exception {
		if (key.getCode().equals(KeyCode.ENTER)) {
			String ingredient = titleCase(ingredientSearchBar.getText().strip());
			if (controller.addPantryItem(ingredient)) {
				obsIngredients.add(ingredient);
				Collections.sort(obsIngredients, String.CASE_INSENSITIVE_ORDER);
			} else {
				quickShake(ingredientSearchBar);
				if (obsIngredients.contains(ingredient)) {
					ingredientSearchBar.setPromptText("Already in your pantry!");
				} else {
					ingredientSearchBar.setPromptText("Invalid ingredient");
				}
				ingredientSearchBar.getParent().requestFocus();
			}
			ingredientSearchBar.clear();
		}
	}
	
	// handles event in which user clicks on an ingredient in their pantry
	public void ingredientCellClicked(MouseEvent event) {
		String ingredient = ingredientsList.getSelectionModel().getSelectedItem();
		if (ingredient != null) {
			if (currentIngredient != ingredient) {	// if ingredient clicked wasn't already selected
				currentIngredient = ingredient;
				deleteIngredientButton.setVisible(true);
			} else {	// removes selection if ingredient clicked when selected
				ingredientsList.getSelectionModel().clearSelection();
				deleteIngredientButton.setVisible(false);
				currentIngredient = "";
			}
		}
	}
	
	public void deleteIngredient() {
		controller.removePantryItem(currentIngredient);
		obsIngredients.remove(currentIngredient);
		if (obsIngredients.size() <= 0) {
			deleteIngredientButton.setVisible(false);
		} else {
			String next = ingredientsList.getSelectionModel().getSelectedItem();
			if (next == null) {
				return;
			}
			currentIngredient = next;  // move current ingredient marker to next one 
		}
	}
	
	public void pantrySearchButtonHandler() throws Exception {
		makePantrySearch(true);
	}
	
	private void makePantrySearch(boolean slideIn) throws Exception {
		obsResults.clear();
		pantrySearchMenu.setVisible(true);
		if (slideIn) {
			slideLeft(pantrySearchMenu);
		}
		List<Recipe> recommendations = controller.searchWithPantry();
		for (Recipe recipe : recommendations) {
			obsResults.add(recipe);
		}
		if (obsResults.size() == 0) {
			searchResults.setVisible(false);
			noRecipesFound.setText("No Recipes Found...\nTime to go grocery shopping?");
			noRecipesFound.setVisible(true);
			filterButton.setVisible(false);
		} else {
			searchResults.setVisible(true);
			noRecipesFound.setVisible(false);
			filterButton.setVisible(true);
			if (recommendations.size() == 1) {
				recipesFound.setText("1 recipe found");	// removes "X recipes found" message
			} else {
				recipesFound.setText(recommendations.size() + " recipes found");
			}
		}
		filtersApplied.setText("");	// removes "X filters active" message
	}
	
	public void returnToPantry() {
		pantryIngredientsMenu.setVisible(true);
		slideRight(pantrySearchMenu);
		resetFilter();
		filterMenu.setVisible(false);
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
	
	private void quickShake(Node toShake) {
		TranslateTransition shake = new TranslateTransition();
		shake.setDuration(new Duration(100));
		shake.setNode(toShake);
		shake.setByX(10);
		shake.setToX(10);
		shake.setCycleCount(4);
		shake.setAutoReverse(true);
		shake.play(); shake.stop(); shake.play(); // allows animation to be played more than once
	}
	
	private void slideDown(Node toSlide, boolean bounce) {
		toSlide.setVisible(true);
		toSlide.setTranslateY(-100);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(500));
		slide.setNode(toSlide);
		slide.setToY(0);
		slide.setByY(0);
		slide.play();
		if (bounce) {
			slide.setOnFinished(e -> bounce(toSlide, true));
		}
	}
	
	private void slideUp(Node toSlide) {
		toSlide.setTranslateY(1000);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(800));
		slide.setNode(toSlide);
		slide.setToY(0);
		slide.setByY(0);
		if (currentMenu == 's') {
			searchToolbar.setVisible(false); // hides toolbar/info bar at first
			searchInfo.setVisible(false);
			slide.setOnFinished(e ->	// slides other elements into view
			{ bounce(toSlide, false); slideDown(searchToolbar, false); 
			  slideLeft(searchInfo); });
		} else if (currentMenu == 'f') {
			menuTitle.setVisible(false);
			slide.setOnFinished(e -> slideDown(menuTitle, true));
		}
		slide.play();
	}
	
	private void bounce(Node toBounce, boolean up) {
		int direction = 1;
		if (!up) {
			direction = -1;
		}
		TranslateTransition bounce = new TranslateTransition();
		bounce.setDuration(new Duration(175));
		bounce.setNode(toBounce);
		bounce.setByY(-9 * direction);
		bounce.setToY(-9 * direction);
		bounce.setCycleCount(2);
		bounce.setAutoReverse(true);
		bounce.play(); bounce.stop(); bounce.play();
	}
	
	private void slideLeft(Node toSlide) {
		toSlide.setVisible(true);
		toSlide.setTranslateX(1920);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(700));
		slide.setNode(toSlide);
		slide.setToX(0);
		slide.setByX(0);
		slide.play();
		slide.setOnFinished(e -> bounceSlideIn(toSlide));
	}
	
	private void bounceSlideIn(Node toBounce) {
		TranslateTransition bounce = new TranslateTransition();
		bounce.setDuration(new Duration(150));
		bounce.setNode(toBounce);
		bounce.setByX(30);
		bounce.setToX(30);
		bounce.setCycleCount(2);
		bounce.setAutoReverse(true);
		bounce.play(); bounce.stop(); bounce.play();
		bounce.setOnFinished(e -> secondBounce(toBounce));
	}
	
	private void secondBounce(Node toBounce) {		
		TranslateTransition bounce = new TranslateTransition();
		bounce.setDuration(new Duration(150));
		bounce.setNode(toBounce);
		bounce.setByX(5);
		bounce.setToX(5);
		bounce.setCycleCount(2);
		bounce.setAutoReverse(true);
		bounce.play(); bounce.stop(); bounce.play();
		if (currentMenu == 'p') {
			bounce.setOnFinished(e -> pantryIngredientsMenu.setVisible(false));
		}
	}
	
	private void slideRight(Node toSlide) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(900));
		slide.setNode(toSlide);
		slide.setToX(2000);
		slide.setByX(2000);
		slide.play();
		slide.setOnFinished(e -> toSlide.setVisible(false));
	}
	
	@FXML
	// called when an FXML is loaded, initializes necessary nodes
	private void initialize() throws Exception {
		if (currentMenu == 's' || currentMenu == 'p') { // initializes the TableView for the pantry/search menu
			// sets up cell factories for each row
			recipeNameCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
			recipeNameCol.setSortType(TableColumn.SortType.DESCENDING);	// sorts table by name by default
			recipeNameCol.setStyle("-fx-font-family: 'Dubai Medium'; -fx-font-size: 20; -fx-alignment: 'CENTER_LEFT'; -fx-padding: 0 0 0 20;");
			lengthCol.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("length"));
			lengthCol.setStyle("-fx-font-size: 20");
			stepsCol.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("steps"));
			stepsCol.setStyle("-fx-font-size: 20");
			numIngredientsCol.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("numIngredients"));
			numIngredientsCol.setStyle("-fx-font-size: 20");
			ingredientsCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("ingredientString"));
			ingredientsCol.setStyle("-fx-font-size: 15; -fx-alignment: 'CENTER_RIGHT'; -fx-text-alignment: right; -fx-padding: 0 50 0 0;");
			
			// sets up table dimensions
			recipeNameCol.prefWidthProperty().bind(searchResults.widthProperty().multiply(0.33));		
			recipeNameCol.setResizable(false);		recipeNameCol.setReorderable(false);
			lengthCol.prefWidthProperty().bind(searchResults.widthProperty().multiply(0.1));		
			lengthCol.setResizable(false);			lengthCol.setReorderable(false);
			stepsCol.prefWidthProperty().bind(searchResults.widthProperty().multiply(0.08));		
			stepsCol.setResizable(false);			stepsCol.setReorderable(false);
			numIngredientsCol.prefWidthProperty().bind(searchResults.widthProperty().multiply(0.18));		
			numIngredientsCol.setResizable(false);	numIngredientsCol.setReorderable(false);
			ingredientsCol.prefWidthProperty().bind(searchResults.widthProperty().multiply(0.28));
			ingredientsCol.setResizable(false);		ingredientsCol.setReorderable(false);
			
			// sets up tableview
			searchResults.setItems(obsResults);
			searchResults.getSortOrder().add(recipeNameCol); // makes sort arrow visible when table loads
			
			// adds tooltip to filter button
			Tooltip filter = new Tooltip("Filter");	filter.setStyle("-fx-font-size: 12;");
			filterButton.setTooltip(filter);
		}
		
		if (currentMenu == 's') {	// initializes nodes used in search menu
			for (Recipe recipe : allRecipes) {	// initializes recipes in table
				obsResults.add(recipe);
			}
			
			searchResults.setPrefHeight(100);   // adjusts table height 
			
			// adds tooltip to filter/search buttons
			Tooltip search = new Tooltip("Search");	search.setStyle("-fx-font-size: 12;");
			searchBarButton.setTooltip(search);
			
			// all recipes are shown to the user by default in the menu
			recipesFound.setText("Showing 50 recipes");
			
			slideUp(searchResults);
		} else if (currentMenu == 'f') {	// initializes the ListView for the favorites menu
			for (Recipe recipe : controller.getFavorites()) { // adds each favorite to listview's observable items
				if (recipe != null) {
					obsFavorites.add(recipe.getName());
					nameToRecipe.put(recipe.getName(), recipe);
				}
			}
			favoritesList.setItems(obsFavorites);
			slideUp(favoritesList);
		} else if (currentMenu == 'p') {	// initializes ListView for pantry
			// loads pantry into menu
			for (String ingredient : controller.getPantry()) {
				obsIngredients.add(ingredient);
			}
			ingredientsList.setItems(obsIngredients);
			
			// sets up auto-complete for adding ingredients
			allIngredients = controller.getAllIngredients();
			TextFields.bindAutoCompletion(ingredientSearchBar, allIngredients.toArray());
			
			slideDown(menuTitle, true);
		}
	}
}