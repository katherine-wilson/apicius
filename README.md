# <img src="https://i.imgur.com/Ir7TMiB.png">

Apicius is a Java application designed to help users find and keep track of recipes through its three main features: search, favorites, and pantry. 
The purpose of this project is to simplify the task of finding new recipes to try. Although there are many apps available that provide similar services,
most of them are locked behind subscription fees and other paywalls. The final state of this application would remain free and easy to use.
<br><br>
Apicius was built using MVC architecture and Java. JavaFX, ControlsFX, FXML, and CSS were used additionally to construct the UI. Our recipe data is stored in a DynamoDB table hosted through AWS. On the other hand, user data (ex. favorites list, virtual pantry) is stored locally. This was a school project for a Software Development class and is more of a concept piece than a finished product.

### Contents
- [Setup](#how-to-run-apicius)
- [Feature Guide](#how-to-use-apicius)
  - [Searching for recipes](#search)
  - [Keeping track of recipes](#favorites)
  - [Finding recipes to make with ingredients you already have](#pantry)
- [Limitations](#limitations)
- [Future](#future)
- [References](#references)

# How to Run Apicius
<ol>
  <li>Download the folder from <a href="https://drive.google.com/drive/folders/1YRXt2l9ud0zLGQgg0KiO-dMuUn_wA2XY?usp=sharing">this link</a>.</li>
  <li>Unzip it in a directory of your choice.</li>
  <li>Run the "Apicius.jar" file. The most recent <a href="https://www.oracle.com/java/technologies/downloads/">JDK</a> must be installed on your machine in order for the application to run properly.</li>
  <li>After a brief moment, the application should appear!</li>
  <br><br>
  <p align="center">
  <img src="https://i.imgur.com/4um7Z8A.png" width=75% height=75%>
  </p>
</ol>
<br><br>

# How to Use Apicius
Apicius has three main features: search, favorites, and pantry, which can each be accessed by the buttons on the left side of the application.
## Search
  <p align="center">
  <img src="https://i.imgur.com/0YCdLNN.png" width=75% height=75%>
  </p>
  <br>
  The "Search" menu allows the user to search for recipes from the DynamoDB database using keywords. Search results can also be refined by clicking the "≡" button to the right of the search bar. Results can be filtered by the number of minutes, steps, or ingredients it takes to complete a recipe. They can also be sorted by clicking on the table headers. The white triangle that appears will indicate what column the results are sorted by and whether it is in descending or ascending order.
  <br><br>
  <p align="center">
  <img src="https://i.imgur.com/br12p5c.png" width=35% height=35%>
  </p>
  <p align="center">
  <img src="https://i.imgur.com/V686z0N.png" width=50% height=50%>
  </p>
  <br><br>
  Each recipe result can also be "opened" to view more detail by clicking on them.
  <p align="center">
  <img src="https://i.imgur.com/FJoHqrM.png" width=50% height=50%>
  </p>
  <br><br>
  
## Favorites
  <p align="center">
  <img src="https://i.imgur.com/DhDzS6S.png" width=75% height=75%>
  </p>
  <br><br>
The "Favorites" menu allows users to save recipes they're interested in for later. Recipes can be added to a user's favorites list by clicking the star button in the upper left corner when viewing a recipe, such as in the "Search" and "Pantry" menus. 
<br><br>
  <p align="center">
  <img src="https://i.imgur.com/34R4gvd.png" width=50% height=50%>
  </p>
  <br><br>
When a user selects a recipe from their favorites, a row of buttons appears on top of the list. Here, the user can click the green arrows to move the recipe up and down in the list, the blue "Open" button to view the recipe in more detail (the recipe can also be double-clicked), and the red "Delete" button can be used to remove recipes from this list. 
<br><br>
  <p align="center">
  <img src="https://i.imgur.com/K4HixyC.png" width=75% height=75%>
  </p>
<br><br>

## Pantry
The "Pantry" menu allows users to select ingredients from a list that they already have in their household to receive recipe recommendations based on what they can already make. 
  <p align="center">
  <img src="https://i.imgur.com/KG64143.png" width=75% height=75%>
  </p>
  <br><br>
  Users can type in the search bar to add ingredients, which must match a suggestion from the drop-down list that appears. Ingredients can be added and removed from the virtual pantry at any time.  
  <br><br>
  <p align="center">
  <img src="https://i.imgur.com/dUX7J4q.png" width=35% height=35%>
  </p>
  <br><br>
  After updating their virtual pantry to match their actual one, the user can then view a list of recipes that can be made using their pantry through the "See What You Can Make!" button at the button of the menu. A list of recipes will appear and like the "Search" menu, these results can be filtered and sorted as well.
  <br><br>
  <p align="center">
  <img src="https://i.imgur.com/N7ytmIN.png" width=75% height=75%>
  </p>
  <br><br>

# Limitations
<br>
<ul>
  <li>This project was ultimately most limited by the recipe datasets available for free online since we did not have time to scrape our own. Although we chose the <a href=#references>most optimal one</a> for our academic purposes, it would not be ideal for an actual product. For example, the dataset is missing pictures, serving sizes, ingredient measurements, and most importantly, curation. The source of the dataset is <a href=https://Food.com>Food.com</a>, which is a site that allows anyone to post a recipe. As such, many of the recipes aren't very useful. This, along with the aforementioned lack of information, makes this application's practicality questionable. However, with much better data, this would be a much more promising product!</li><br>
  <li>For the sake of demonstration and financial purposes, the dataset available to users is only a fraction of the size of the full one (180K+ recipes). Because the database is hosted by AWS, a database too big could incur high costs. As a result, the number of recipes in the application is limited to 50. Should this application be scaled to the full dataset, its code would have to be optimized to handle much larger amounts of data.</li>
</ul>
<br>

# Future
Unfortunately, due to time constraints (this was a roughly ~10-week project and each member of the development team was a full-time student), there were many ideas pitched during the planning process that we were unable to add to the final project. If I were to pick up and continue this project, here's what I would add:
<br><br>
<ul>
  <li>User account system that allows users to sign-up and access their data from anywhere using DynamoDB</li>
  <li>Allow for multiple favorites lists to be created and maintained by the user</li>
  <li>Allow users to create their own recipes and store them in their Favorites</li>
  <li>The ability to "broaden" the pantry search by allowing users to specify a "missing-ingredient threshold" that returns a list of recipes they are missing only X ingredients for</li>
  <li>Using recipe tags, create a recommendation menu based on recipes favorited by the user</li>
  <li>Attempt to find a dataset or Recipe API with more comprehensive details per recipe</li>
</ul>
<br>

# References
* Images
    * <a href="https://www.flaticon.com/free-icons/fork" title="fork icons">Fork icons created by Freepik - Flaticon</a>
    * <a href="https://www.flaticon.com/free-icons/spoon" title="spoon icons">Spoon icons created by Roundicons - Flaticon</a>
* Data
    * Shuyang Li. (2019). <i>Food.com Recipes and Interactions</i> [Data set]. Kaggle. https://doi.org/10.34740/KAGGLE/DSV/783630
