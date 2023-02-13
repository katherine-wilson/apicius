# <img src="https://i.imgur.com/Ir7TMiB.png">

Apicius is a Java application designed to help users find and keep track of recipes through its three main features: search, favorites, and pantry. 
The purpose of this project is to simplify the task of finding new recipes to try. Although there are many apps available that provide similar services,
most of them are locked behind subscription fees and other paywalls. This final state of this application would remain free and easy to use.
<br><br>
Apicius was built using MVC architecture and Java. JavaFX, ControlsFX, FXML, and CSS were used additionally to construct the UI. Our recipe data is stored in a DynamoDB table hosted through AWS. On the other hand, user data (ex. favorites list, virtual pantry) is stored locally. This was a school project for a Software Development class and is more of a concept piece than a finished product.

#### Contents
- [Setup](#how-to-run-apicius)
- [Feature Guide](#how-to-use-apicius)
  - [Searching for recipes](#search)
  - [Keeping track of recipes](#favorites)
  - [Finding recipes to make with ingredients you already have](#pantry)
- [Limitations](#limitations)
- [Future](#future)
- [Packages](#packages)
- [References](#references)

# How to Run Apicius
<ol>
  <li>Download the folder from <a href="https://drive.google.com/drive/folders/1YRXt2l9ud0zLGQgg0KiO-dMuUn_wA2XY?usp=sharing">this link</a>.</li>
  <li>Unzip it in a directory of your choice.</li>
  <li>Run the "Apicius.jar" file. The most recent <a href="https://www.oracle.com/java/technologies/downloads/">JDK</a> must be installed on your machine in order for the application to run properly.</li>
  <li>After a brief moment, the application should appear!</li>
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
  The "Search" menu allows the user to search for recipes from the DynamoDB database using keywords. Search results can also be refined by clicking the "≡" button to the right of the search bar. Results can be filtered by number of minutes, steps, or ingredients it takes to complete a recipe. They can also be sorted by clicking on the table headers. The white triangle that appears will indicate what column the results are sorted by and whether it is in descending or ascending order.
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
When a user selects a recipe from their favorites, a row of buttons appear on top of the list. Here, the user use the green arrows to move the recipe up and down in the list, the blue "Open" button to view the recipe in more detail (the recipe can also be double-clicked), and the red "Delete" button can be used to remove recipes from this list. 
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
<br><br>
# Future
<br><br>
# Packages
<br><br>
# References
<br><br>
