package cisco.demo;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.*;


/**
 * Created by irygiels on 20.04.16.
 */
public class AmazonDB {

  /*  CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider( getApplicationContext(),
            "eu-west-1:d53a6e76-8302-407c-a21f-8a741b5cf6c0", Regions.EU_WEST_1);

    CognitoSyncManager syncClient = new CognitoSyncManager(
            getApplicationContext(),
            Regions.EU_WEST_1,
            credentialsProvider
    );

    Dataset dataset = syncClient.openOrCreateDataset("myDataset");
    dataset.put("myKey", "myValue");
    dataset synchronize(new DefaultSyncCallback){
        @Override
                public void onSuccess(Dataset dataset, List newRecords){}
    }

    AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);*/
}
