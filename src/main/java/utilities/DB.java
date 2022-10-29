package utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

/**
 * This class searches the DynamoDB table and prints the results
 */
public class DB {
    static AmazonDynamoDB dynamoDB;
    String sTableName;
    
    public DB() 
    {
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIA27QC4C7OKQMFHHCH", 
    			"kc0NPpvx+qu9iTS9rZF1darGGMeaU6oaKy86BbcM");

    	dynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_2)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
    	
        sTableName = "436Project";
    }

    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (/Users/kyle/.aws/credentials).
         */
        System.out.println("Init");
    }

    public Recipe[] Search(String sSearchStr) throws Exception
    {
    	try 
    	{
    		
    		Map<String,String> expressionAttributesNames = new HashMap<>();
    	    expressionAttributesNames.put("#recipe","recipe");
    	 
    	    Map<String,AttributeValue> expressionAttributeValues = new HashMap<>();
    	    expressionAttributeValues.put(":recipeValue",new AttributeValue().withS(sSearchStr));
    	    
    	    ScanRequest scanRequest = new ScanRequest()
    	    	    .withTableName(sTableName)
    	    	    .withFilterExpression("contains(recipe, :recipeValue)")
    	    	    .withExpressionAttributeValues(expressionAttributeValues);
    	    
    	    ScanResult result = dynamoDB.scan(scanRequest);
    	    List<Map<String,AttributeValue>> items = result.getItems();
    	    Recipe[] recipesFound = new Recipe[items.size()];
    	    for (int i = 0; i < items.size(); i++) 
    	    {
    	    	Map<String, AttributeValue> curPair = items.get(i);
    	    	String recipeName = curPair.get("recipe").getS();
    	    	String ingredientsStr = curPair.get("ingredients").getS();
    	    	ingredientsStr = ingredientsStr.replace("'", "");
    	    	ingredientsStr = ingredientsStr.replace("[", "");
    	    	ingredientsStr = ingredientsStr.replace("]", "");
    	    	String [] split = ingredientsStr.split("\\s*,\\s*");
    	    	List<String> ingredients = Arrays.asList(split);
    	    	Recipe curRecipe = new Recipe(recipeName);
    	    	for (int j = 0; j < ingredients.size(); j++) 
    	    	{
    	    		curRecipe.addIngredient(ingredients.get(j));
    	    	}
    	    	recipesFound[i] = curRecipe;
    	    }
    	    PantryQuery(null);
    	    return recipesFound;

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) 
    	{
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
		return null;
    }
    
    // Finds recipes that match the user pantry of ingredients
    public List<Recipe> PantryQuery(List<String> pantry) throws Exception
    {
    	try 
    	{
    		List<String> testPantry = new ArrayList<String>();
    		testPantry.add("eggs");
    		testPantry.add("flour");
    		testPantry.add("milk");
    		testPantry.add("chicken");
    		
     	   ScanRequest scanRequest = new ScanRequest()
   	    	    .withTableName(sTableName);
     	    
     	   ScanResult result = dynamoDB.scan(scanRequest);
     	   List<Map<String,AttributeValue>> items = result.getItems();
     	   List<Recipe> availableRecipes = new ArrayList<Recipe>();
     	   for (int i = 0; i < items.size(); i++) 
     	   {
   	    	Map<String, AttributeValue> curPair = items.get(i);
   	    	String recipeName = curPair.get("recipe").getS();
   	    	String ingredientsStr = curPair.get("ingredients").getS();
	    	ingredientsStr = ingredientsStr.replace("'", "");
	    	ingredientsStr = ingredientsStr.replace("[", "");
	    	ingredientsStr = ingredientsStr.replace("]", "");
	    	String [] split = ingredientsStr.split("\\s*,\\s*");
	    	List<String> ingredients = Arrays.asList(split);
   	    	Recipe curRecipe = new Recipe(recipeName.substring(2));
   	    	
   	    	for (int j = 0; j < ingredients.size(); j++) 
   	    	{
   	    		
   	    		curRecipe.addIngredient(ingredients.get(j));
   	    	}
   	    	if (testPantry.containsAll(curRecipe.getIngredients())) 
   	    	{
   	    		availableRecipes.add(curRecipe);
   	    	}
   	    }
     	  for (int i = 0; i < availableRecipes.size(); i++) 
     	  {
				System.out.print("---------------\n" + availableRecipes.get(i) +
						" - ingredients: " + availableRecipes.get(i).getIngredientString() + "\n---------------\n");
     	  }
     	  return availableRecipes;

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) 
    	{
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
		return null;
    }
    
    

}
