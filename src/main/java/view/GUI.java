package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Model;
import utilities.Recipe;
import org.controlsfx.control.RangeSlider;

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
	static boolean inSearch;	// indicates if GUI is displaying search menu
	ObservableList<Recipe> obsResults = FXCollections.observableArrayList();
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
	static boolean inFavorites;	// indicates if GUI is displaying favorites menu
	ObservableList<String> obsFavorites = FXCollections.observableArrayList();
	private HashMap<String, Recipe> nameToRecipe = new HashMap<String, Recipe>(); 
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
	
	// Window Nodes
	static Parent currentMenu;
	static Stage stage;
	static Scene scene;
	
	// MVC classes
	static Controller controller;
	static Model model;
	
	static final String DATA_FILE_NAME = "user_data.dat";	// name of user data file
	
	
	@Override
	public void start(Stage stage) throws Exception {
		// load home page FXML file by default
		currentMenu = FXMLLoader.load(getClass().getResource("Home.fxml"));
		inSearch = false;
		inFavorites = false;
		
		// initialize static fields - shared by every GUI instance created by FXML
		File userData = new File(DATA_FILE_NAME); 
		if (userData.exists()) {	// initializes model with user data (if it exists)
			try {
				model = new Model(DATA_FILE_NAME);
			} catch (ClassNotFoundException | IOException e) {
				model = new Model();
			}
		} else {
			model = new Model();
		}
		GUI.controller = new Controller(model);
		GUI.stage = stage;
		GUI.scene = new Scene(currentMenu, 1250, 750);
		GUI.allRecipes = new ArrayList<Recipe>();
		for (Recipe recipe : controller.searchRecipes("")) {
			allRecipes.add(recipe);
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
		
		// TODO: remove block; debugging
		System.out.println("Loaded Favorites:");
		for (Recipe recipe : model.getFavorites()) {
			if (recipe != null) {
				System.out.println(recipe.getName());
			}
		}
		
		// saves user data on exit
		stage.setOnCloseRequest(e -> { controller.saveUserData(); });
		
	}
	
	// switches to search menu
	public void searchButtonHandler() {
		inSearch = true;
		inFavorites = false;
		try {
			currentMenu = FXMLLoader.load(getClass().getResource("Search.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Search.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(currentMenu);
		stage.show();
	}
	
	// switches to favorites menu
	public void favoritesButtonHandler() {
		inSearch = false;
		inFavorites = true;
		try {
			currentMenu = FXMLLoader.load(getClass().getResource("Favorites.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Favorites.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(currentMenu);
		stage.show();
		for (Recipe recipe : controller.getFavorites()) {
			//System.out.println(recipe.getName());
		}
	}
	
	// switches to pantry menu
	public void pantryButtonHandler() {
		inSearch = false;
		inFavorites = false;
		try {
			currentMenu = FXMLLoader.load(getClass().getResource("Pantry.fxml"));
		} catch (IOException e) {
			System.err.println("ERROR: 'Pantry.fxml' not found.");
			e.printStackTrace();
		}
		scene.setRoot(currentMenu);
		stage.show();
	}
	
	
	// called when enter is pressed or magnifying glass button is clicked
	public void searchForRecipes() throws Exception {
		Recipe[] results = controller.searchRecipes(searchBar.getText());
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
			searchResults.setVisible(true);		// shows table
		} else {					// shows "No Recipes Found" message if unsuccessful search
			noRecipesFound.setVisible(true);
		}
	}
	
	public void hideFilterMenu() {
		filterMenu.setVisible(false);
	}
	
	public void clearFilter() throws Exception {
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
		
		// searches again to remove filters from results
		searchForRecipes();
	}
	
	
	// when enter is pressed while typing in search bar, search is made
	public void searchBarHandler(KeyEvent key) throws Exception {
		if (key.getCode().equals(KeyCode.ENTER)) {
			searchForRecipes();
		}
	}
	
	// toggles filter menu
	public void filterButtonHandler() {
		if (searchResults.isVisible()) {	// can only filter if there are search results present
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
		searchResults.setVisible(false);
		
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

		}
		searchResults.setVisible(true);	// shows table	
	}
	
	public void selectRecipe() {
		Recipe recipe;
		if (inSearch) {	// takes from tableview
			recipe = searchResults.getSelectionModel().getSelectedItem();
			searchResults.getSelectionModel().clearSelection();	// prevents opening of menu when empty cell clicked
		} else if (inFavorites) {	// takes from listview
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
		selectRecipe();
		if (event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2) {
			openRecipeMenu();
		}
	}
	
	
	public void openRecipeMenu() {
		// retrieve recipe that was clicked
		selectRecipe();
		if (currentRecipe != null) {
			if (controller.getFavorites().contains(currentRecipe)) {
				recipeFavoriteButton.setStyle("-fx-background-color: linear-gradient(to left, #F27440, #CB1349); -fx-text-fill: white;");
			} else {
				recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
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
			recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
			controller.removeFromFavorites(currentRecipe);
			obsFavorites.remove(currentRecipe.getName());
			if (inFavorites) {
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
	
	
	@FXML
	// called when an FXML is loaded, initializes necessary nodes
	private void initialize() throws Exception {
		if (inSearch) {	// initializes the TableView for the search menu
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
			
			// sets width and other column properties
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
			searchResults.setPrefHeight(100);
			searchResults.getSortOrder().add(recipeNameCol);	// makes sort arrow visible when table loads
			for (Recipe recipe : allRecipes) {
				obsResults.add(recipe);
			}
			
			// adds tooltip to filter/search buttons
			Tooltip search = new Tooltip("Search");	search.setStyle("-fx-font-size: 12;");
			Tooltip filter = new Tooltip("Filter");	filter.setStyle("-fx-font-size: 12;");
			searchBarButton.setTooltip(search);
			filterButton.setTooltip(filter);
			
			// all recipes are shown to the user by default in the menu
			recipesFound.setText("Showing all recipes");
		} else if (inFavorites) {	// initializes the ListView for the favorites menu
			for (Recipe recipe : controller.getFavorites()) { // adds each favorite to listview's observable items
				obsFavorites.add(recipe.getName());
				nameToRecipe.put(recipe.getName(), recipe);
			}
			
			favoritesList.setItems(obsFavorites);
		}
	}
}
