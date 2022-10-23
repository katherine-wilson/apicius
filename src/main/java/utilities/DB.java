package main.java.utilities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

/**
 * This class searches the DynamoDB table and prints the results
 */
public class DB {
    static AmazonDynamoDB dynamoDB;
    String sTableName;
    
    public DB() 
    {
    	ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/kyle/.aws/credentials), and is in valid format.",
                    e);
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
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
    	 
    	    QueryRequest queryRequest = new QueryRequest()
    	            .withTableName(sTableName)
    	            .withKeyConditionExpression("#recipe = :recipeValue")
    	            .withExpressionAttributeNames(expressionAttributesNames)
    	            .withExpressionAttributeValues(expressionAttributeValues);
    	    QueryResult response = dynamoDB.query(queryRequest);
    	    List<Map<String,AttributeValue>> items = response.getItems();
    	    System.out.println(items.size());
    	    // Set to 10 for testing
    	    Recipe[] recipesFound = new Recipe[items.size()];
    	    for (int i = 0; i < items.size(); i++) 
    	    {
    	    	//System.out.println(items.get(0));
    	    	Map<String, AttributeValue> curPair = items.get(i);
    	    	String val = curPair.get("recipe").getS();
    	    	Recipe curRecipe = new Recipe(val);
    	    	recipesFound[i] = curRecipe;
    	    	//System.out.println(val);
    	    }
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
}
