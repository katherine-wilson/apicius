package utilities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.stepfunctions.builder.states.Iterator;

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
