package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.controlsfx.control.textfield.TextFields;

/**
 * This class contains everything necessary for the GUI of the <i>Apicius</i>
 * application. The application starts on the home screen, and the user can
 * use the buttons on the sidebar to switch between the different menus. 
 * <br>
 * This application contains four different menus: <code>Home</code>,
 * <code>Search</code>, <code>Favorites</code>, and <code>Pantry</code>.
 * <ul>
 * <li>The <code>Home</code> menu simply acts as a landing for the user to decide
 * what other menu they would like to navigate to. </li>
 * <li>The <code>Search</code> menu allows the user to search for recipes using
 * keywords and refine that search through filters and sorting.</li>
 * <li>The <code>Favorites</code> menu allows the user to scroll through the
 * recipes they have added to their favorites in the past. They may edit this list 
 * by removing or rearranging entries or they can tap the recipes to open them.</li>
 * <li>The <code>Pantry</code> menu allows the user to add ingredients from a 
 * predetermined list to their "virtual pantry", remove items from this list, or
 * conduct a recipe search that yields a list of recipes that they can make based
 * on what's in their pantry.</li>
 * </ul><br>
 * 
 * <br><i>
 * This is a 
 * <code><a href="https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html">
 * JavaFX</a></code> application.</i>
 * 
 * @see <code><a href="../model/Model.html">Model</a></code>
 * @see <code><a href="../controller/Controller.html">Controller</a></code>
 * @see <code><a href="../utilities/Recipe.html">Recipe</a></code>
 * 
 * @author Katherine Wilson
 */
public class GUI extends Application {
	// -------------------------------------------[  FIELDS  ]------------------------------------------------
	// --------- Menu Buttons --------- 
	/**
	 * Button on sidebar that switches the scene to the <code>Search</code>
	 * menu.
	 */
	@FXML
	private Button searchButton;
	/**
	 * Button on sidebar that switches the scene to the <code>Favorites</code>
	 * menu.
	 */
	@FXML
	private Button favoritesButton;
	/**
	 * Button on sidebar that switches the scene to the <code>Pantry</code>
	 * menu.
	 */
	@FXML
	private Button pantryButton;
	
	// --------- Search Menu Fields --------- 
	/**
	 * Default list of recipes to show when the user navigates to the search
	 * menu. This gives them something to scroll through before they perform
	 * a search.
	 */
	private static ArrayList<Recipe> recipeSubset;
	/**
	 * <code>Observable</code> list of recipes linked to the {@link #searchResults}
	 * <code>TableView</code>. This list's contents provide the rows for the table.
	 */
	private ObservableList<Recipe> obsResults = FXCollections.observableArrayList();
	/**
	 * Flag for search filters. If any filters are active, this flag will be 
	 * <code>true</code>, if not, then this flag will be <code>false</code>.
	 */
	private boolean filtersActive;
	/**
	 * Recipe that is currently open or otherwise selected across
	 * the various menus.
	 */
	private Recipe currentRecipe;
	/**
	 * Table of search results either from the <code>Pantry</code> or
	 * <code>Search</code> features. It contains 5 columns: {@link #recipeNameCol},
	 * {@link #lengthCol}, {@link #stepsCol}, {@link #numIngredientsCol}, and
	 * {@link #ingredientsCol}.
	 */
	@FXML
	private TableView<Recipe> searchResults;
	/**
	 * Column in the {@link #searchResults} table that stores the 
	 * names of each recipe.
	 */
	@FXML
	private TableColumn<Recipe, String> recipeNameCol;
	/**
	 * Column in the {@link #searchResults} table that stores the 
	 * time it takes to make each recipe (in minutes).
	 */
	@FXML
	private TableColumn<Recipe, Integer> lengthCol;
	/**
	 * Column in the {@link #searchResults} table that stores the 
	 * number of steps it takes to make each recipe.
	 */
	@FXML
	private TableColumn<Recipe, Integer> stepsCol;
	/**
	 * Column in the {@link #searchResults} table that stores the 
	 * number of ingredients it takes to make each recipe.
	 */
	@FXML
	private TableColumn<Recipe, Integer> numIngredientsCol;
	/**
	 * Column in the {@link #searchResults} table that stores the 
	 * ingredients needed to make each recipe.
	 */
	@FXML
	private TableColumn<Recipe, String> ingredientsCol;
	/**
	 * Brown bar that contains the search bar, search button, and
	 * filter button above the recipe table. A drop-down animation
	 * on this node plays when the user first navigates to the
	 * <code>Search</code> menu.
	 */
	@FXML
	private HBox searchToolbar;
	/**
	 * <code>BorderPane</code> that contains two <code>Labels</code>:
	 * {@link #recipesFound} and {@link #filtersApplied}. This is
	 * located between the brown search toolbar and the recipe table.
	 */
	@FXML
	private BorderPane searchInfo;
	/**
	 * Area where user can type a keyword to search the database
	 * for recipes. If this node is currently focused and <code>ENTER</code>
	 * is pressed, a search will be conducted and the results will
	 * appear in the table (if any).
	 */
	@FXML
	private TextField searchBar;
	/**
	 * Magnifying glass button to the right of the search bar 
	 * in the search menu. Clicking this button performs a search
	 * based on what has been typed in the {@link #searchBar}.
	 */
	@FXML
	private Button searchBarButton;
	/**
	 * Button with an image of three bars on it. Clicking this
	 * button toggles the {@link #filterMenu} <code>StackPane</code>.
	 */
	@FXML
	private Button filterButton;
	/**
	 * Menu that displays three sliders and their respective
	 * check boxes that can be used by the user to refine their
	 * search ({@link #lengthSlider}, {@link #stepsSlider}, 
	 * {@link #ingredientsSlider}). Also contains the 
	 * {@link #confirmFilterButton} and {@link #clearFilterButton}.
	 */
	@FXML
	private StackPane filterMenu;
	/**
	 * Checkbox in the {@link #hideFilterMenu()} that enables
	 * the {@link #lengthSlider}.
	 */
	@FXML
	private CheckBox lengthCheckBox;
	/**
	 * Checkbox in the {@link #hideFilterMenu()} that enables
	 * the {@link #stepsSlider}.
	 */
	@FXML
	private CheckBox stepsCheckBox;
	/**
	 * Checkbox in the {@link #hideFilterMenu()} that enables
	 * the {@link #ingredientsSlider}.
	 */
	@FXML
	private CheckBox ingredientsCheckBox;
	/**
	 * Applies filters to the current recipe search based on
	 * the three sliders ({@link #lengthSlider}, 
	 * {@link #stepsSlider}, {@link #ingredientsSlider}).
	 */
	@FXML
	private Button confirmFilterButton;
	/**
	 * Removes the filters currently applied to the recipe
	 * search. Clicking this button will redo the search process.
	 */
	@FXML
	private Button clearFilterButton;
	/**
	 * Slider that allows the user to filter recipes based on
	 * a minimum and maximum number of minutes. This can only
	 * be used if {@link #lengthCheckBox} has been selected.
	 */
	@FXML
	private RangeSlider lengthSlider;
	/**
	 * Slider that allows the user to filter recipes based on
	 * a minimum and maximum number of steps. This can only
	 * be used if {@link #stepsCheckBox} has been selected.
	 */
	@FXML
	private RangeSlider stepsSlider;
	/**
	 * Slider that allows the user to filter recipes based on
	 * a minimum and maximum number of ingredients. This can only
	 * be used if {@link #ingredientsCheckBox} has been selected.
	 */
	@FXML
	private RangeSlider ingredientsSlider;
	/**
	 * Message that appears in the {@link #searchInfo} node that
	 * tells the user how many recipes are in the recipe table.
	 */
	@FXML
	private Label recipesFound;
	/**
	 * Message that appears in the {@link #searchInfo} node that
	 * tells the user how many filters are applied to the current
	 * search.
	 */
	@FXML
	private Label filtersApplied;
	/**
	 * Message that is displayed when no recipes were found matching
	 * the user's search term or after they filtered their search.
	 */
	@FXML
	private Label noRecipesFound;
	
	// --------- Recipe Menu Fields --------- 
	/**
	 * "Window" that pops up when a user clicks on a recipe. This
	 * displays all information in the database about the recipe.
	 * The user can scroll around this window or use the buttons
	 * at the top to add the recipe to their favorites or close it.
	 */
	@FXML
	private AnchorPane recipeMenu;
	/**
	 * List of ingredients shown on the left side of the {@link #recipeMenu}.
	 */
	@FXML
	private VBox ingredientsBox;
	/**
	 * List of directions shown on the right side of the {@link #recipeMenu}.
	 */
	@FXML
	private VBox directionsBox;
	/**
	 * Red "X" button in the top right corner of the recipe window.
	 * This is used to close the recipe window ({@link #recipeMenu}).
	 */
	@FXML
	private Button recipeMenuButton;
	/**
	 * Title of the recipe shown in a bold font at the top of the 
	 * recipe window.
	 */
	@FXML
	private Label recipeTitle;
	/**
	 * Displays the number of minutes it takes to make the recipe
	 * under the title in the recipe window.
	 */
	@FXML
	private Label recipeLength;
	/**
	 * Displays the number of steps it takes to make the recipe
	 * under the title in the recipe window.
	 */
	@FXML
	private Label recipeSteps;
	/**
	 * Displays the number of ingredients it takes to make the
	 * recipe under the title in the recipe window.
	 */
	@FXML
	private Label recipeIngredientCount;
	/**
	 * Star button in the top left corner of the recipe window.
	 * Clicking this button adds or removes the recipe from the
	 * user's favorites list.
	 */
	@FXML
	private Button recipeFavoriteButton;
	
	// --------- Favorites Menu Fields ---------
	/**
	 * <code>Observable</code> list of recipes linked to the rows of the 
	 * {@link #favoritesList} <code>ListView</code>. 
	 */
	ObservableList<String> obsFavorites = FXCollections.observableArrayList();
	/**
	 * Maps the name of a recipe to its corresponding
	 * <code><a href="../utilities/Recipe.html">Recipe</a></code> object.
	 * This is used to "translate" the data in a <code>ListCell</code> (just
	 * a recipe name) into a full <code>Recipe</code> object.
	 */
	private HashMap<String, Recipe> nameToRecipe = new HashMap<String, Recipe>();
	/**
	 * Displays the name of each recipe in the user's favorites list
	 * using rows. 
	 */
	@FXML
	private ListView<String> favoritesList;
	/**
	 * Blue "Open" button that appears when a user clicks on a recipe
	 * in their favorites list. Clicking this button will open a 
	 * {@link #recipeMenu} window that shows more information about the
	 * recipe.
	 */
	@FXML
	private Button openFavoriteButton;
	/**
	 * Red "Delete" button that appears when a user clicks on a recipe
	 * in their favorites list. Clicking this button will delete the
	 * recipe from their favorites and update the list visually.
	 */
	@FXML
	private Button deleteFavoriteButton;
	/**
	 * Green up-arrow button that appears when a user clicks on a recipe
	 * in their favorites list. Clicking this button will move the 
	 * highlighted recipe up by one row in the list. 
	 */
	@FXML
	private Button upArrow;
	/**
	 * Green down-arrow button that appears when a user clicks on a
	 * recipe in their favorites list. Clicking this button will move
	 * the highlighted recipe down by one row in the list. 
	 */
	@FXML
	private Button downArrow;	
	/**
	 * Section between the title and the favorites list where
	 * the {@link #openFavoriteButton}, {@link #deleteFavoriteButton},
	 * {@link #upArrow}, and {@link #downArrow} buttons appear.
	 */
	@FXML
	private HBox favoritesButtons;
	
	// ---------  Pantry Menu Fields ---------
	/**
	 * <code>Observable</code> list of ingredients linked to the rows of the 
	 * {@link #ingredientsList} <code>ListView</code>. 
	 */
	ObservableList<String> obsIngredients = FXCollections.observableArrayList();
	/**
	 * <code>String</code> representing the ingredient currently selected by
	 * the user. This is used to determine which <code>String</code> to
	 * delete from the pantry list.
	 */
	private String currentIngredient;
	/**
	 * A list of all ingredients in the database.
	 */
	private List<String> allIngredients;
	/**
	 * <code>true</code> if changes were made to the pantry while in the
	 * {@link #pantrySearchMenu} or<br><code>false</code> if no changes
	 * were made. This is used to determine if the search should be redone
	 * to reflect pantry changes.
	 */
	private boolean pantryChanged = false;
	/**
	 * Menu first shown to the user when they click on the 
	 * {@link #pantryButton}. It displays the user's "virtual pantry"
	 * (the list of ingredients they have created).
	 */
	@FXML
	private AnchorPane pantryIngredientsMenu;
	/**
	 * Menu that is shown when the user makes a "pantry
	 * search". This menu includes a back arrow button
	 * to return to the {@link #pantryIngredientsMenu},
	 * a table of search results, and a button for
	 * filtering the results.
	 */
	@FXML
	private AnchorPane pantrySearchMenu;
	/**
	 * List of ingredients the user has added to their
	 * "virtual pantry". Contains one ingredient per
	 * row.
	 */
	@FXML
	private ListView<String> ingredientsList;
	/**
	 * <code>TextField</code> that user can use to add
	 * ingredients to their "virtual pantry". This field
	 * uses auto-complete functionality that is bound to
	 * the {@link #allIngredients} list.
	 */
	@FXML
	private TextField ingredientSearchBar;
	/**
	 * Red "delete" button used to remove an ingredient
	 * from the user's virtual pantry. This will visually
	 * update the {@link #ingredientsList}.
	 */
	@FXML
	private Button deleteIngredientButton;
	/**
	 * "See what you can make!" button at the button
	 * of the {@link #pantryIngredientsMenu}. Clicking
	 * this button starts a pantry search and switches
	 * to the {@link #pantrySearchMenu}.
	 */
	@FXML
	private Button pantrySearchButton;
	/**
	 * Button in the top left corner of the {@link #pantrySearchMenu}
	 * that brings the user back to the {@link #pantryIngredientsMenu}.
	 */
	@FXML
	private Button backButton;
	/**
	 * Title shown at the top of a menu. This is used by the
	 * <code>Favorites</code> and <code>Pantry</code> menus.
	 */
	@FXML
	private Label menuTitle;
	
	// --------- Window Nodes ---------
	/**
	 * Menu displayed to the user (<code>Home, Search, Favorites,
	 * or Pantry</code>). This is provided by an <i>FXML</i> file.
	 */
	static Parent menu;
	/**
	 * Used to display {@link #scene}.
	 */
	static Stage stage;
	/**
	 * Scene to be displayed by the {@link #stage}.
	 */
	static Scene scene;
	
	// --------- MVC classes ---------
	/**
	 * <code>Controller</code> through which the GUI interacts
	 * with the <code>Model</code>. This field is static because
	 * it is shared between all instances of this class.
	 */
	static Controller controller;
	/**
	 * <code>Model</code> that stores user data and manages 
	 * any connections between this application and the recipe
	 * database. This field is static because it is shared between
	 * all instances of this class.
	 */
	static Model model;
	/**
	 * Name of a file that contains a list of 50 recipes. This database
	 * subset is used to show some recipes to the user by default when
	 * they navigate to the search menu, so it doesn't look so empty.
	 */
	static final String RECIPE_SUBSET_FILE = "recipe_subset.dat";
	/**
	 * Represents the menu currently being shown to the user. This
	 * takes on one of <i>four</i> values:
	 * <ul>
	 * <li>'h' = Home menu</li>
	 * <li>'s' = Search menu</li>
	 * <li>'f' = Favorites menu</li>
	 * <li>'p' = Pantry menu</li>
	 * </ul>
	 */
	static char currentMenu;
	
	// -------------------------------------------[  INITIALIZATION METHODS  ]-------------------------------------------------
	/**
	 * Starts the GUI for the <i>Apicius</i> application. Initializes everything
	 * needed for the stage and shows it to the user. This includes the user data,
	 * which is loaded in by the {@link #model} as well as a list of all ingredients
	 * in the database located in a text file. This function is called only once on 
	 * startup and will initialize the {@link #model} and {@link #controller} for the
	 * application. 
	 * 
	 * @param stage <code>Stage</code> to be displayed to the user
	 * @throws IOException if the following files are not present in the project's
	 * 						directory: <i>Home.fxml</i>, <i>recipe_subset.dat</i>
	 */
	@Override
	public void start(Stage stage) throws IOException {
		// load home page FXML file by default
		menu = FXMLLoader.load(getClass().getResource("Home.fxml"));
		currentMenu = 'h';
		
		// initialize static fields - shared by every GUI instance created by FXML
		model = new Model();
		GUI.controller = new Controller(model);
		GUI.stage = stage;
		GUI.scene = new Scene(menu, 1250, 750);
		
		// TODO: remove
		try {
			ObjectOutputStream output =  new ObjectOutputStream(new FileOutputStream(RECIPE_SUBSET_FILE));
			Recipe[] recipesArray = controller.searchRecipes("");
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			for (Recipe r : recipesArray) {
				if (!recipes.contains(r)) {
					recipes.add(r);
				}
			}
			output.writeObject(recipes);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (recipeSubset == null) {	// loads subset of recipe database for search menu
			GUI.recipeSubset = new ArrayList<Recipe>();
			ObjectInputStream input;
			try {	// reads in subset from file
				input = new ObjectInputStream(new FileInputStream(RECIPE_SUBSET_FILE));
				ArrayList<Recipe> subset = (ArrayList<Recipe>) input.readObject();
				if (subset != null) {
					GUI.recipeSubset = (ArrayList<Recipe>) subset.clone();
				}
				input.close();
			} catch (ClassNotFoundException e) { }
			
			for (Recipe recipe : controller.searchRecipes("")) {
				recipeSubset.add(recipe);
			}
		}
		
		// prepares stage + adds app icons + launches GUI
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
	
	/**
	 * Called when an FXML is loaded to initialize necessary <code>Nodes</code> for
	 * the menu that was loaded in.
	 * <br>
	 * This function will create a new <code>GUI</code> instance, which is why some
	 * fields (Ex. {@link #model} and {@link #controller}) must be static. 
	 */
	@FXML
	private void initialize() {
		if (currentMenu == 's' || currentMenu == 'p') { // initializes the TableView for the pantry/search menu
			// sets up cell factories for each row
			recipeNameCol.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
			recipeNameCol.setSortType(TableColumn.SortType.ASCENDING);	// sorts table by name by default
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
			obsResults.clear();
			for (Recipe recipe : recipeSubset) { // initializes recipes in table
				if (!obsResults.contains(recipe)) {
					obsResults.add(recipe);
				}
			}
			Collections.sort(obsResults);
			searchResults.setPrefHeight(100);   // adjusts table height 
			
			// adds tooltip to filter/search buttons
			Tooltip search = new Tooltip("Search");	search.setStyle("-fx-font-size: 12;");
			searchBarButton.setTooltip(search);
			
			// all recipes are shown to the user by default in the menu
			recipesFound.setText("Showing 50 recipes");
			
			slideUp(searchResults);
		} else if (currentMenu == 'f') {	// initializes the ListView for the favorites menu
			obsFavorites.clear();
			for (Recipe recipe : controller.getFavorites()) { // adds each favorite to listview's observable items
				if (recipe != null) {
					obsFavorites.add(recipe.getName());
					nameToRecipe.put(recipe.getName(), recipe);
				}
			}
			favoritesList.setItems(obsFavorites);
			slideUp(favoritesList);
		} else if (currentMenu == 'p') {	// initializes ListView for pantry
			obsIngredients.clear();
			obsResults.clear();
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
	
	/**
	 * Switches to the <code>Search</code> menu. This is loaded in from
	 * the "Search.fxml" file. This file <b>must</b> be present in order
	 * for this function to work properly. Also updates the {@link #currentMenu}
	 * global variable.
	 */
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
	
	/**
	 * Switches to the <code>Favorites</code> menu. This is loaded in from
	 * the "Favorites.fxml" file. This file <b>must</b> be present in order
	 * for this function to work properly. Also updates the {@link #currentMenu}
	 * global variable.
	 */
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
	
	/**
	 * Switches to the <code>Pantry</code> menu. This is loaded in from
	 * the "Pantry.fxml" file. This file <b>must</b> be present in order
	 * for this function to work properly. Also updates the {@link #currentMenu}
	 * global variable.
	 */
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
	
	// -------------------------------------------[  SEARCH METHODS  ]-------------------------------------------------
	/**
	 * Searches for recipes in the database based on what has been typed
	 * into the {@link #searchBar} <code>TextField</code>. This is called
	 * when <code>ENTER</code> is typed into the {@link #searchBar} or 
	 * the {@link #searchBarButton} is clicked. After retrieving a list
	 * of <code>Recipe</code> results based on the search query, this
	 * function will also manage any visual updates necessary when making
	 * the search.
	 */
	public void searchForRecipes() {
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
			Collections.sort(obsResults);
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
	
	/**
	 * Makes the {@link #filterMenu} invisible. 
	 * Called when the {@link #searchBar} is clicked.
	 */
	public void hideFilterMenu() {
		filterMenu.setVisible(false);
	}
	
	/**
	 * Clears the filter(s) applied by the user to their
	 * recipe search. Makes another standard or pantry
	 * search after the filters have been cleared to show
	 * the previous, unfiltered results.
	 */
	public void clearFilter() {
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
	
	/**
	 * Deselects all filter <code>CheckBoxes</code> and resets
	 * the filter <code>sliders</code> and their selected values
	 * to their default states.
	 */
	public void resetFilter() {
		// removes selection from text boxes
		lengthCheckBox.setSelected(false);
		stepsCheckBox.setSelected(false);
		ingredientsCheckBox.setSelected(false);
		
		// resets slider thumbs and disables them
		lengthSlider.setLowValue(0);
		lengthSlider.setHighValue(lengthSlider.getMax());
		lengthSlider.setDisable(true);
		stepsSlider.setLowValue(0);
		stepsSlider.setHighValue(stepsSlider.getMax());
		stepsSlider.setDisable(true);
		ingredientsSlider.setLowValue(0);
		ingredientsSlider.setHighValue(ingredientsSlider.getMax());
		ingredientsSlider.setDisable(true);
	}
	
	/**
	 * Makes a search when the user types <code>ENTER</code>
	 * into the {@link #searchBar}.
	 * 
	 * @see #searchForRecipes()
	 * 
	 * @param key <code>KeyEvent</code> triggered by the user.
	 * 				If its <code>KeyCode</code> is <code>ENTER</CODE>,
	 * 				then a search will be conducted.
	 */
	public void searchBarHandler(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			searchForRecipes();
			searchBar.getParent().requestFocus();
		}
	}
	
	/**
	 * Calculates what the highest value possible for the 
	 * <code>RangeSliders</code> should be. This is based on the 
	 * maximum values found for each filterable column in the
	 * current search results.
	 * 
	 * @param ranges Stores the end-points for each of the three sliders.
	 * 				 This should be a 3-item array. The end-points are stored
	 * 				 in the following order: length, steps, ingredients. This
	 * 				 array should be initialized with all 0 values.
	 */
	private void getSliderMaximums(int[] ranges) {
		Recipe first = obsResults.get(0);
		ranges[0] = 5;
		ranges[1] = first.getSteps();
		ranges[2] = first.getNumIngredients();
		
		for (Recipe r : obsResults) {
			if (r.getSteps() > ranges[1]) {
				ranges[1] = r.getSteps();
			}
			if (r.getNumIngredients() > ranges[2]) {
				ranges[2] = r.getNumIngredients();
			}
		}
	}
	
	/**
	 * Formats the given <code>RangeSlider</code> to fit with the
	 * {@link #filterMenu}.
	 * 
	 * @param slider <code>RangeSlider</code> in the {@link #filterMenu}.
	 * @param max Maximum value of slider.
	 */
	private void formatSlider(RangeSlider slider, int max) {
		if (max == 0) { max = 1; }	// max must be 1 by default
		if (max < 5) {
			max = 5;
		} else {
			slider.setMax(max + max%5-1);
		}
		slider.setMin(0);
		slider.setMajorTickUnit(5);
		slider.setMinorTickCount(4);
		slider.setShowTickMarks(true);
		slider.setLowValue(0);
		slider.setHighValue(slider.getMax());
		slider.setOnMouseReleased(e -> updateSliderLabels());
		slider.setOnKeyReleased(e -> updateSliderLabels());
		if (slider == lengthSlider) {
			slider.setShowTickLabels(false);
		}
	}
	
	/**
	 * Updates the "label" for the three <code>RangeSliders</code> in the
	 * {@link #filterMenu} to match the current high and low values of 
	 * each slider. The label is technically a part of the slider's
	 * corresponding <code>CheckBox</code>.
	 */
	private void updateSliderLabels() {
		int lengthMin = (int) lengthSlider.getLowValue();
		int lengthMax = (int) lengthSlider.getHighValue();
		if (lengthMin == 0 && lengthMax == 0) {
			lengthCheckBox.setText("Length (5 minutes and under)");
		} else if (lengthMin == 0 && lengthMax == 4) {
			lengthCheckBox.setText("Length (Any Time)");
		} else if (lengthMin == 4 && lengthMax == 4) {
			lengthCheckBox.setText("Length (Over an hour)");
		} else {
			String min = "";
			String max = "";
			if (lengthMin == 0) {
				min = "0";
			} else if (lengthMin == 1) {
				min = "15";
			} else if (lengthMin == 2) {
				min = "30";
			} else if (lengthMin == 3) {
				min = "1";
			}
			
			if (lengthMax == 1) {
				max = "15 minutes";
			} else if (lengthMax == 2) {
				max = "30 minutes";
			} else if (lengthMax == 3) {
				max = "1 hour";
			} else if (lengthMax == 4) {
				max = "Over an hour";
			}
			
			if (lengthMin == lengthMax) {	// equal min/max
				lengthCheckBox.setText("Length (" + max + ")");
			} else if (lengthMin == 0) {	// no min
				lengthCheckBox.setText("Length (Up to " + max + ")");
			} else if (lengthMax == 4) {	// no max
				if (lengthMin <= 2) {
					min += " minutes";
				} else {
					min += " hour";
				}
				lengthCheckBox.setText("Length (At least " + min + ")");
			} else if (lengthMax >= 3 && lengthMin <= 2) {	// different units (minutes/hours)
				min += " minutes";
				lengthCheckBox.setText("Length (" + min + "-" + max + ")");
			} else {
				lengthCheckBox.setText("Length (" + min + "-" + max + ")");
			}
		}
		
		int stepsMin = (int) stepsSlider.getLowValue();
		int stepsMax = (int) stepsSlider.getHighValue();
		if (stepsMin == stepsMax) {
			if (stepsMin == 1) {	// equal slider values, singular noun
				stepsCheckBox.setText("Steps (" + stepsMin + " direction)");
			} else {				// equal slider values, plural noun
				stepsCheckBox.setText("Steps (" + stepsMin + " directions)");
			}
		} else {
			if (stepsMax == 1) {	// unequal slider values, singular noun
				stepsCheckBox.setText("Steps (" + stepsMin + "-" + stepsMax + " direction)");
			} else {				// unequal slider values, plural noun
				stepsCheckBox.setText("Steps (" + stepsMin + "-" + stepsMax + " directions)");
			}
		}
		
		int ingMin = (int) ingredientsSlider.getLowValue();
		int ingMax = (int) ingredientsSlider.getHighValue();
		if (ingMin == ingMax) {
			if (ingMin == 1) {
				ingredientsCheckBox.setText("Ingredients (" + ingMin + " item)");
			} else {
				ingredientsCheckBox.setText("Ingredients (" + ingMin + " items)");
			}
		} else {
			if (ingMax == 1) {
				ingredientsCheckBox.setText("Ingredients (" + ingMin + "-" + ingMax + " item)");
			} else {
				ingredientsCheckBox.setText("Ingredients (" + ingMin + "-" + ingMax + " items)");
			}
		}
	}
	
	/**
	 * Toggles the {@link #filterMenu}. This cannot
	 * be done if there are no search results to be
	 * filtered.
	 */
	public void filterButtonHandler() {
		if (searchResults.isVisible() || filtersActive) { // can only filter if there are search results/filters present
			if (filterMenu.isVisible()) {	// closes filter menu
				filterMenu.setVisible(false);
			} else {	// opens filter menu
				filterMenu.setVisible(true);
				if (!filtersActive) {	// does not change slider values if filters are applied
					int[] max = new int[3];
					getSliderMaximums(max);
					formatSlider(lengthSlider, max[0]);
					formatSlider(stepsSlider, max[1]);
					formatSlider(ingredientsSlider, max[2]);
					updateSliderLabels();
				}
			}
		}
	}
	
	/**
	 * Toggles the {@link #lengthSlider}. This is called when 
	 * the user selects or deselects the {@link #lengthCheckBox}.
	 */
	public void toggleLengthSlider() {
		if (lengthCheckBox.isSelected()) {
			lengthSlider.setDisable(false);
		} else {
			lengthSlider.setDisable(true);
		}
	}
	
	/**
	 * Toggles the {@link #stepsSlider}s. This is called when 
	 * the user selects or deselects the {@link #stepsCheckBox}.
	 */
	public void toggleStepsSlider() {
		if (stepsCheckBox.isSelected()) {
			stepsSlider.setDisable(false);
		} else {
			stepsSlider.setDisable(true);
		}
	}
	
	/**
	 * Toggles the {@link #ingredientsSlider}. This is called when 
	 * the user selects or deselects the {@link #ingredientsCheckBox}.
	 */
	public void toggleIngredientsSlider() {
		if (ingredientsCheckBox.isSelected()) {
			ingredientsSlider.setDisable(false);
		} else {
			ingredientsSlider.setDisable(true);
		}
	}
	
	/**
	 * Gets the ranges from the length, steps, and ingredients
	 * sliders of the {@link #filterMenu}. These are used to filter
	 * recipe search results.
	 * 
	 * @param ranges out-parameter; stores the minimum and maximum of
	 * 				 each of the three sliders. This should be a [3][2] array.
	 * 				 The ranges are stored in the following order: length, steps,
	 * 				 ingredients.
	 * @return an <code>int</code> representing the number of filters that 
	 * 		   have been activated by the user based on their respective 
	 * 		   <code>CheckBoxes</code>. 
	 */
	private int getRanges(int[][] ranges) {
		int filters = 0;
		if (lengthCheckBox.isSelected()) {
			int lengthLow = (int) lengthSlider.getLowValue();
			int lengthHigh = (int) lengthSlider.getHighValue();
			if (lengthLow == 0) {	// determines minimum value
				ranges[0][0] = 0;
			} else if (lengthLow == 1) {
				ranges[0][0] = 15;
			} else if (lengthLow == 2) {
				ranges[0][0] = 30;
			} else if (lengthLow == 3) {
				ranges[0][0] = 60;
			} else if (lengthLow == 4) {
				ranges[0][0] = 61;	// must be over an hour
			}
			if (lengthHigh == 0) {	// determines maximum value
				ranges[0][1] = 5;
			} else if (lengthHigh == 1) {
				ranges[0][1] = 15;
			} else if (lengthHigh == 2) {
				ranges[0][1] = 30;
			} else if (lengthHigh == 3) {
				ranges[0][1] = 60;
			} else if (lengthHigh == 4) {
				ranges[0][1] = 999999999;	// no "max"
			}
			
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
	
	/**
	 * Determines if the given recipe should remain in the search results based on 
	 * the filters added by the user. For each filter, the function will check if
	 * it's enabled and then decide if the recipe should be filtered based on the
	 * criteria set by it.
	 * 
	 * @param recipe <code>Recipe</code> to be filtered or kept.
	 * @param ranges Stores the minimum and maximum values of each of the three sliders.
	 * 				 This should be a [3][2] array. The ranges should be stored in the
	 * 				 following order: length, steps, ingredients.
	 * @return <code>true</code> if the <code>Recipe</code> passes the filters or <br>
	 * 			<code>false</code> if the <code>Recipe</code> has been filtered
	 */
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
	
	/**
	 * Applies the user's search filters to the current {@link #searchResults}.
	 * This function will also handle any necessary visual updates to the following
	 * nodes: {@link #filterMenu}, {@link #filtersApplied}, {@link #recipesFound},
	 * {@link #noRecipesFound}, and {@link #searchResults}. This function updates
	 * {@link #obsResults} and {@link #filtersActive}. <br>This is used by both
	 * the <code>Search</code> and <code>Pantry</code> menus.
	 */
	public void filterResults() {
		if (currentMenu == 'p') {	// resets pantry results
			makePantrySearch(false);
		}
		
		filterMenu.setVisible(false);
		
		int[][] ranges = new int[3][2];	// inclusive min/max for each value
		int filters = getRanges(ranges);
		
		ArrayList<Recipe> filtered = new ArrayList<Recipe>(); // list of recipes to be removed
		
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
		
		// adds "No Recipes Found" message if no results pass the filters
		if (obsResults.size() == 0) {
			noRecipesFound.setVisible(true);
			searchResults.setVisible(false);
		} else {
			noRecipesFound.setVisible(false);
		}
	}
	
	/**
	 * Determines what <code>Recipe</code> to set the {@link #currentRecipe}
	 * pointer to. This is generally called by <code>TableView</code> and
	 * <code>ListView</code> nodes when one of their items has been clicked.
	 * This function will set the {@link #currentRecipe} equal to that item,
	 * so that other functions know what recipe is currently selected by the
	 * user. <br>
	 * If the {@link #currentMenu} is the <code>Favorites</code> menu,
	 * then this will make the {@link #favoritesButtons} bar visible.
	 */
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
	
	// -------------------------------------------[  FAVORITES METHODS  ]-------------------------------------------------
	/**
	 * Handles the event in which the user clicks on a recipe in their {@link #favoritesList}.
	 * <ul>
	 * <li>If the user clicks only once, then the recipe will be selected and the 
	 * user can choose to open, delete, or change its position in the favorites list.</li>
	 * <li>If the user clicks twice, then the recipe will be selected and opened.</li>
	 * <li>If the user clicks once on an already selected recipe, then the recipe 
	 * will be deselected.</li>
	 * </ul>
	 * 
	 * @see #selectRecipe()
	 * 
	 * @param event <code>MouseEvent</code> triggered by the user on the {@link #favoritesList}.
	 */
	public void favoriteCellClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			Recipe oldCurrent = currentRecipe;
			selectRecipe();
			if (event.getClickCount() == 2) {
				openRecipeMenu();
			} else if (oldCurrent != null && oldCurrent == currentRecipe) { // deselects recipe if previously clicked
				favoritesList.getSelectionModel().clearSelection();
				favoritesButtons.setVisible(false);
				currentRecipe = null;
			}
		}
	}
	
	/**
	 * Makes the {@link #recipeMenu} visible for the {@link #currentRecipe}.
	 * Populates the {@link #recipeMenu}'s nodes with data such as the 
	 * recipe's name, number of steps/ingredients/minutes to make, list of
	 * ingredients, and list of steps.
	 */
	public void openRecipeMenu() {
		if (currentMenu != 'f') {
			hideFilterMenu();
		}
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
				String wrapped = wrapText(ingredient, 20);
				CheckBox ingBox = new CheckBox(wrapped);
				ingBox.setFont(Font.font("Nirmala UI", 16));
				ingBox.setPadding(new Insets(10, 0, 0, 10));
				ingBox.setOnMouseClicked(e -> toggleIngredient(ingBox.isSelected(), ingredient));
				if (controller.getPantry().contains(ingredient)) {	// if ingredient in user's pantry
					ingBox.setSelected(true); 	// make sure box is checked
				}
				ingredientsBox.getChildren().add(ingBox);
			}
			
			// adds directions to VBox as labels
			List<String> directions = currentRecipe.getDirections();
			for (int i = 1; i <= directions.size(); i++) {
				Label dirLabel = new Label(Integer.toString(i) + ". " + directions.get(i-1));
				dirLabel.setFont(Font.font("Nirmala UI", 16));
				dirLabel.setPadding(new Insets(0, 0, 0, 20));
				dirLabel.setWrapText(true);
				directionsBox.getChildren().add(dirLabel);
			}
			
			recipeMenu.setVisible(true);
		}
	}
	
	/**
	 * Closes the {@link #recipeMenu}. If changes were made in
	 * to the pantry in the {@link #pantrySearchMenu}, then its
	 * search results will be updated to match the new pantry.
	 */
	public void closeRecipeMenu() {
		recipeMenu.setVisible(false);
		if (currentMenu == 'p' && pantryChanged) {
			makePantrySearch(false); // updates pantry search results 
		}
	}

	/**
	 * Changes the color of the star-shaped {@link #recipeFavoriteButton} 
	 * if the user hovers over it on an unfavorited-recipe.
	 * 
	 * @see #hoverOverStar()
	 */
	public void hoverOverStar() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// star changes color if not in favorites
			recipeFavoriteButton.setStyle("-fx-text-fill: linear-gradient(to left, #F27440, #CB1349);");
		}
	}
	
	/**
	 * Returns the color of the star-shaped {@link #recipeFavoriteButton}
	 * back to the original when the user's cursor leaves it.
	 * 
	 * @see #exitStar()
	 */
	public void exitStar() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// star changes color if not in favorites
			recipeFavoriteButton.setStyle("-fx-text-fill: #402020;");
		}
	}
	
	/**
	 * Adds or removes a recipe from the user's {@link #favoritesList}.
	 * Updates the {@link #obsFavorites} list and the look of the
	 * {@link #recipeFavoriteButton} depending on if the recipe was added
	 * or removed.
	 */
	public void toggleFavorite() {
		if (!controller.getFavorites().contains(currentRecipe)) {	// adds to favorites
			if (currentMenu != 'f') {
				recipeFavoriteButton.setStyle("-fx-background-color: linear-gradient(to left, #F27440, #CB1349); -fx-text-fill: white;");
			}
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
	
	/**
	 * Moves a <code>Recipe</code> up in the user's {@link #favoritesList}.
	 */
	public void moveFavoriteUp() {
		int index = favoritesList.getSelectionModel().getSelectedIndex();
		if (index > 0) {
			favoritesList.getSelectionModel().clearSelection();
			obsFavorites.remove(index);
			obsFavorites.add(index-1, currentRecipe.getName());
			favoritesList.getSelectionModel().select(index-1);
			controller.moveFavoriteUp(currentRecipe);
		}
	}
	
	/**
	 * Moves a <code>Recipe</code> down in the user's {@link #favoritesList}.
	 */
	public void moveFavoriteDown() {
		int index = favoritesList.getSelectionModel().getSelectedIndex();
		if (index < obsFavorites.size() - 1) {
			favoritesList.getSelectionModel().clearSelection();
			obsFavorites.remove(index);
			obsFavorites.add(index+1, currentRecipe.getName());
			favoritesList.getSelectionModel().select(index+1);
			controller.moveFavoriteDown(currentRecipe);
		}
	}
	
	// -------------------------------------------[  PANTRY METHODS  ]-------------------------------------------------
	/**
	 * Adds the ingredient typed by the user to the their {@link #ingredientsList} 
	 * when they press <code>ENTER</code>.
	 * 
	 * @param key <code>KeyEvent</code> triggered by the user. Ignores all inputs
	 * 			  except for <code>KeyCode ENTER</code>.
	 */
	public void ingSearchBarHandler(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			addIngredient();
		}
	}
	
	/**
	 * Adds the ingredient typed by the user into the {@link #ingredientSearchBar}.
	 * If the ingredient is not in the {@link #allIngredients} list (or it has already
	 * been added by the user), then the addition will be rejected and the
	 * {@link #ingredientSearchBar} will shake and display an error message.
	 */
	public void addIngredient() {
		String ingredient = titleCase(ingredientSearchBar.getText().strip());
		if (controller.addPantryItem(ingredient)) {	// add succeeds
			obsIngredients.add(ingredient);	// update observables
			Collections.sort(obsIngredients, String.CASE_INSENSITIVE_ORDER); // sort list
		} else {	// failed to add ingredient to pantry
			quickShake(ingredientSearchBar);	// shake searchBar
			if (obsIngredients.contains(ingredient)) {	// pantry already has ingredient...
				ingredientSearchBar.setPromptText("Already in your pantry!");
			} else {	// ingredient given that isn't in ingredient list
				ingredientSearchBar.setPromptText("Invalid ingredient");
			}
			ingredientSearchBar.getParent().requestFocus();	// defocuses search bar to show prompt
		}
		ingredientSearchBar.clear();	// clears user's text from search bar
	}
	
	/**
	 * Shows {@link #deleteIngredientButton} when the user clicks on an ingredient
	 * in their pantry ({@link #ingredientsList}). Deselects the ingredient if it
	 * was previously selected when it was clicked.
	 * 
	 * @param event <code>MouseEvent</code> triggered by the user on the {@link #ingredientsList}.
	 */
	public void ingredientCellClicked(MouseEvent event) {
		String ingredient = ingredientsList.getSelectionModel().getSelectedItem();
		if (ingredient != null) {
			if (currentIngredient != ingredient) { // if ingredient clicked wasn't already selected
				currentIngredient = ingredient;
				deleteIngredientButton.setVisible(true);
			} else {	// removes selection if ingredient clicked when selected
				ingredientsList.getSelectionModel().clearSelection();
				deleteIngredientButton.setVisible(false);
				currentIngredient = "";
			}
		}
	}
	
	/**
	 * Removes the currently selected ingredient ({@link #currentIngredient})
	 * from the user's "virtual pantry" ({@link #ingredientsList}).
	 */
	public void deleteIngredient() {
		controller.removePantryItem(currentIngredient);
		obsIngredients.remove(currentIngredient);
		if (obsIngredients.size() <= 0) {	// hides delete button if no more ingredients left
			deleteIngredientButton.setVisible(false);
		} else {	// moves selection to next ingredient
			String next = ingredientsList.getSelectionModel().getSelectedItem();
			if (next == null) {
				return;
			}
			currentIngredient = next;
		}
	}
	
	/**
	 * Makes a pantry search when the {@link #pantrySearchButton} is clicked.
	 * 
	 * @see #makePantrySearch(boolean)
	 */
	public void pantrySearchButtonHandler() {
		makePantrySearch(true);
	}
	
	/**
	 * Makes a pantry search based on all of the ingredients in the
	 * user's pantry. If this is called from the {@link #pantryIngredientsMenu},
	 * then the {@link #pantrySearchMenu} will slide into view. If this is 
	 * called from {@link #clearFilter()}, then no animation will play.
	 * This function handles all visual changes necessary to making this search.
	 * 
	 * @param slideIn <code>true</code> if the {@link #pantrySearchMenu} should
	 * 				  slide into view or<br><code>false</code> if no animation
	 * 				  should play.
	 */
	private void makePantrySearch(boolean slideIn) {
		obsResults.clear();	// resets previous pantry search results
		pantrySearchMenu.setVisible(true);	// shows pantry search menu
		if (slideIn) {	// slides menu into view if argument is true
			slideLeft(pantrySearchMenu);
		}
		List<Recipe> recommendations = controller.searchWithPantry();	// conducts search
		for (Recipe recipe : recommendations) {
			obsResults.add(recipe);		// adds results to observable list
		}
		if (obsResults.size() == 0) {	// no results found -- show message
			searchResults.setVisible(false);
			noRecipesFound.setText("No Recipes Found...\nTime to go grocery shopping?");
			noRecipesFound.setVisible(true);
			filterButton.setVisible(false);
		} else {						// results found -- show results and filter button
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
	
	/**
	 * Switches from the {@link #pantrySearchMenu} to the 
	 * {@link #pantryIngredientsMenu} when the {@link #backButton} is clicked.
	 * This hides the {@link #filterMenu} and resets any filters.
	 */
	public void returnToPantry() {
		pantryIngredientsMenu.setVisible(true);
		slideRight(pantrySearchMenu);
		resetFilter();
		filterMenu.setVisible(false);
	}
	
	/**
	 * Adds or removes an ingredient from the user's pantry based 
	 * on its <code>CheckBox</code> in the {@link #recipeMenu}. If
	 * this event occurs in the {@link #pantrySearchMenu}, then the
	 * search will be redone.
	 * 
	 * @param add <code>true</code> if the given ingredient should be added or<br>
	 * 				<code>false</code> if it should be removed.
	 * @param ingredient Ingredient <code>String</code> to add/remove from the
	 * 					user's pantry.
	 */
	public void toggleIngredient(boolean add, String ingredient) {
		pantryChanged = true;
		if (add) {
			controller.addPantryItem(ingredient); // adds to model
			obsIngredients.add(ingredient);	// adds to observable list
			Collections.sort(obsIngredients, String.CASE_INSENSITIVE_ORDER); // sort list
		} else {
			controller.removePantryItem(ingredient); // removes from model
			obsIngredients.remove(ingredient); // removes from observable list
		}
	}
	
	/**
	 * If the escape key is pressed while the {@link #recipeMenu} is open,
	 * then the menu will be closed.
	 * 
	 * @param key <code>KeyEvent</code>; if its <code>KeyCode</code> is
	 * <code>ENTER</code>, then the {@link #recipeMenu} will be closed.
	 */
	public void escapeMenu(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ESCAPE)) {
			closeRecipeMenu();
		}
	}
	
	// -------------------------------------------[  ANIMATIONS  ]-------------------------------------------------
	/**
	 * Quickly shakes a <code>Node</code> back and forth 4 times.
	 * 
	 * @param toShake <code>Node</code> to quickly shake.
	 */
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
	
	/**
	 * Slides a <code>Node</code> down from off-screen into view.
	 * Will conditionally play a "bounce" animation at the end
	 * of the slide depending on the arguments given.
	 * 
	 * @see #bounce(Node, boolean)
	 * 
	 * @param toSlide <code>Node</code> to slide down.
	 * @param bounce <code>true</code> if the <code>Node</code> should
	 * 				 bounce at the end of the slide or<br><code>false</code>
	 * 				 if the <code>Node</code> should not bounce.
	 */
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
	
	/**
	 * Slides a <code>Node</code> up from off-screen into view.<br>
	 * If this function is called from the <code>Search</code> menu, then
	 * the given <code>Node</code> will "bounce" at the end of the slide and 
	 * the {@link #searchToolbar} and {@link #searchInfo} will also be given
	 * animations to play.<br>If this function is called from the 
	 * <code>Favorites</code> menu, then the {@link #menuTitle} will slide
	 * down once this animation completes.
	 * 
	 * @see #bounce(Node, boolean)
	 *
	 * @param toSlide <code>Node</code> to slide up.
	 */
	private void slideUp(Node toSlide) {
		toSlide.setTranslateY(1000);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(600));
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
			slide.setDuration(Duration.millis(700));
			menuTitle.setVisible(false);
			slide.setOnFinished(e -> slideDown(menuTitle, true));
		}
		slide.play();
	}
	
	/**
	 * Bounces a node in the direction indicated by the arguments.
	 * 
	 * @param toBounce <code>Node</code> to bounce.
	 * @param up <code>true</code> if the <code>Node</code> should bounce
	 * 			 upwards or<br><code>false</code> if the <code>Node</code>
	 * 			 should bounce downwards.
	 */
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
	
	/**
	 * Slides a <code>Node</code> left from off-screen into view.<br>
	 * At the end of the sliding animation, the <code>Node</code> will
	 * bounce twice.
	 * 
	 * @see #slideBounce(Node)
	 * @see #secondBounce(Node)
	 *
	 * @param toSlide <code>Node</code> to slide left.
	 */
	private void slideLeft(Node toSlide) {
		toSlide.setVisible(true);
		toSlide.setTranslateX(1920);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(700));
		slide.setNode(toSlide);
		slide.setToX(0);
		slide.setByX(0);
		slide.play();
		slide.setOnFinished(e -> slideBounce(toSlide));
	}
	
	/**
	 * Bounces a slide to the right.<br>
	 * At the end of the animation, a second bounce will play. 
	 * 
	 * @see #secondBounce(Node)
	 * 
	 * @param toBounce <code>Node</code> to bounce right.
	 */
	private void slideBounce(Node toBounce) {
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
	
	/**
	 * Second bounce that plays after the animation in
	 * {@link #slideBounce(Node)} finishes. This is much smaller
	 * than the first bounce to emulate "gravity".<br>
	 * If this function is called by the <code>Pantry</code> menu,
	 * then the {@link #pantryIngredientsMenu} will be shown.
	 * 
	 * @see #slideBounce(Node)
	 * 
	 * @param toBounce <code>Node</code> to bounce a second time.
	 */
	private void secondBounce(Node toBounce) {		
		TranslateTransition bounce = new TranslateTransition();
		bounce.setDuration(new Duration(150));
		bounce.setNode(toBounce);
		bounce.setByX(5);
		bounce.setToX(5);
		bounce.setCycleCount(2);
		bounce.setAutoReverse(true);
		bounce.play(); bounce.stop(); bounce.play();
		if (currentMenu == 'p' && pantryIngredientsMenu != null) {
			bounce.setOnFinished(e -> pantryIngredientsMenu.setVisible(false));
		}
	}
	
	/**
	 * Slides a <code>Node</code> off-screen to the right.<br>
	 *
	 * @param toSlide <code>Node</code> to slide right.
	 */
	private void slideRight(Node toSlide) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(900));
		slide.setNode(toSlide);
		slide.setToX(2000);
		slide.setByX(2000);
		slide.play();
		slide.setOnFinished(e -> toSlide.setVisible(false));
	}
	
	// -------------------------------------------[  OTHER  ]-------------------------------------------------
	/**
	 * Converts the given <code>String</code> to title case. This function
	 * will ignore quotation marks at the beginning of the <code>String</code>
	 * and capitalize the first letter it finds.
	 * 
	 * @param str <code>String</code> to be converted to title case.
	 * @return the given <code>String</code> in title case.
	 */
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
		return str.toUpperCase();
	}
	
	/**
	 * Creates a string with line breaks in it to create the
	 * illusion of wrapped text.
	 * 
	 * @param str <code>String</code> to convert into wrapped text.
	 * @param rowLen Maximum length of each "row" in the new <code>String</code>.
	 * @return a wrapped-text version of the given <code>String</code>.
	 */
	private String wrapText(String str, int rowLen) {
		if (str.length() > rowLen) {
			String[] words = str.split(" ");
			int curLen = 0;
			String newStr = "";
			for (String word : words) {	// iterates over each word, adds to new string
				if (curLen + word.length() + 1 > 20) {	// +1 for " "
					curLen = 0;
					newStr += "\n";	// adds newline when next word to add exceeds row length
				}
				newStr += word + " ";
				curLen += word.length() + 1;
			}
			return newStr.strip();
		} else {
			return str;
		}
	}
}