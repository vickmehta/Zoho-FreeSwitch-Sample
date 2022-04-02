package com.example.zohosample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.OAuthToken.TokenType;
import com.zoho.api.authenticator.store.FileStore;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.api.logger.Logger;
import com.zoho.api.logger.Logger.Levels;
import com.zoho.crm.api.HeaderMap;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.SDKConfig;
import com.zoho.crm.api.UserSignature;
import com.zoho.crm.api.dc.USDataCenter;
import com.zoho.crm.api.dc.DataCenter.Environment;
import com.zoho.crm.api.exception.SDKException;
import com.zoho.crm.api.record.ActionHandler;
import com.zoho.crm.api.record.ActionResponse;
import com.zoho.crm.api.record.ActionWrapper;
import com.zoho.crm.api.record.BodyWrapper;
import com.zoho.crm.api.record.Field;
import com.zoho.crm.api.record.Record;
import com.zoho.crm.api.record.RecordOperations;
import com.zoho.crm.api.record.ResponseHandler;
import com.zoho.crm.api.record.ResponseWrapper;
import com.zoho.crm.api.record.SuccessResponse;
import com.zoho.crm.api.tags.Tag;
import com.zoho.crm.api.users.User;
import com.zoho.crm.api.util.APIResponse;

public class ZohoSampleApplication {

	public static void main(String[] args) throws Exception {
		// https://github.com/zoho/zohocrm-java-sdk
		// Register client and authenticate the app
		initialize();

		// createLead("Api-fname", "Api-lname");

		getLeads();
	}

	public static void initialize() throws Exception{
		ServerApp x = ServerApp.of();
		// Logger.getInstance(Levels.INFO, filePath)
		Logger logger = Logger.getInstance(Levels.INFO, "output/app_log.log");

		UserSignature user = new UserSignature("christopher-maclead@gmail.com");

		Environment env = USDataCenter.SANDBOX;

		Token token = new OAuthToken(x.getProp("zoho.client.id"), 
			x.getProp("zoho.client.secret"),
			x.getProp("zoho.client.grant.token"), 
			TokenType.GRANT);
		
		TokenStore tokenStore = new FileStore("output/tokenstore.txt");

		SDKConfig sdkconfig = new SDKConfig.Builder()
			.setAutoRefreshFields(false)
			.setPickListValidation(true)
			.build();

		String resourcePath = "output/resource";

		Initializer.initialize(user, env, token, tokenStore, sdkconfig, resourcePath, logger);

		System.out.println("Success " + tokenStore.getTokens());
	}

	public static void getLeads() throws SDKException
	{
		RecordOperations cro = new RecordOperations();
		
		APIResponse<ResponseHandler> response = cro.getRecords("Leads", null, null);

		if(response != null)
		{
			System.out.println("Status Code: " + response.getStatusCode());
            
            if(Arrays.asList(204,304).contains(response.getStatusCode()))
            {
                System.out.println(response.getStatusCode() == 204? "No Content" : "Not Modified");
                return;
            }

			if(response.isExpected())
			{
				//Get the object from response
                ResponseHandler responseHandler = response.getObject();
				if(responseHandler instanceof ResponseWrapper)
				{
					//Get the received ResponseWrapper instance
					ResponseWrapper responseWrapper = (ResponseWrapper) responseHandler;
						
					//Get the obtained Record instances
					List<Record> records = responseWrapper.getData();
					
					for(Record record : records)
					{
						System.out.println("Record id " + record.getId());
						// User createdBy = record.getCreatedBy();
						// if(createdBy != null)
                        // {
                        //     //Get the ID of the createdBy User
                        //     System.out.println("Record Created By User-ID: " + createdBy.getId());
                            
                        //     //Get the name of the createdBy User
                        //     System.out.println("Record Created By User-Name: " + createdBy.getName());
                            
                        //     //Get the Email of the createdBy User
                        //     System.out.println("Record Created By User-Email: " + createdBy.getEmail());
                        // }

						for(Map.Entry<String, Object> entry : record.getKeyValues().entrySet())
                        {
                            String keyName = entry.getKey();
                            // System.out.println("key = " +keyName);
                            Object value = entry.getValue();

							if(value instanceof List)
							{
								List<?> dataList = (List<?>) value;

								if(dataList.size() > 0)
								{
									if(dataList.get(0) instanceof Tag)
                                    {
                                        @SuppressWarnings("unchecked")
                                        List<Tag> tagList = (List<Tag>) value;
                                        
                                        for(Tag tag : tagList)
                                        {
                                            //Get the Name of each Tag
                                            System.out.println("Record Tag Name: " + tag.getName());
                                            
                                            //Get the Id of each Tag
                                            System.out.println("Record Tag ID: " + tag.getId());
                                        }
                                    }else if(dataList.get(0) instanceof com.zoho.crm.api.record.Record)
                                    {
                                        @SuppressWarnings("unchecked")
                                        List<Record> recordList = (List<Record>) dataList;
                                        
                                        for(Record record1 : recordList)
                                        {
                                            //Get the details map
                                            for(Map.Entry<String, Object> entry1 : record1.getKeyValues().entrySet())
                                            {
                                                //Get each value in the map
                                                System.out.println(entry1.getKey() + ": " + entry1.getValue());
                                            }
                                        }
                                    }
								}
							} 
							// else if( value instanceof Record)
                            // {
                            //     Record recordValue = (Record) value;
                                
                            //     System.out.println("Record " + keyName + " ID: " + recordValue.getId());
                                
                            //     System.out.println("Record " + keyName + " Name: " + recordValue.getKeyValue("name"));
                            // }
							else if(keyName.equals("First_Name"))
							{
								System.out.println("Lead first name - " + value);
							}else if(keyName.equals("Last_Name"))
							{
								System.out.println("Lead last name - " + value);
							}
							else{
								// if(value != null)
								// System.out.println(value.getClass());
							}

						}


					}

				}
				
			}
		}

	}

	public static void createLead(String firstname, String lastname) throws SDKException{
		//https://www.zoho.com/crm/developer/docs/java-sdk/v1/record-samples.html
		RecordOperations cro = new RecordOperations();
		
		BodyWrapper bwrapper = new BodyWrapper();

		List<Record> records = new ArrayList<>();

		Record record1 = new Record();

		record1.addFieldValue(Field.Leads.LAST_NAME, lastname);
		record1.addFieldValue(Field.Leads.FIRST_NAME, firstname);
		Tag tag = new Tag();
		tag.setName("api-tag");
		List<Tag> tags = new ArrayList<>();
		tags.add(tag);
		record1.setTag(tags);
		records.add(record1);

		List<String> of = new ArrayList<>();
		of.add("approval");
		of.add("workflow");
		of.add("blueprint");
		bwrapper.setTrigger(of);
		bwrapper.setData(records);

		HeaderMap hmap = new HeaderMap();

		APIResponse<ActionHandler> response = cro.createRecords("Leads", bwrapper);

		if(response != null)
		{
			System.out.println("status code = " + response.getStatusCode());

			if(response.isExpected())
			{
				ActionHandler actionHandler = response.getObject();

				if(actionHandler instanceof ActionWrapper)
				{
					ActionWrapper wrapper = (ActionWrapper) actionHandler;

					List<ActionResponse> data = wrapper.getData();

					System.out.println("ActionWrapper List size = " + data.size());
					for (ActionResponse actionResponse : data) {
						SuccessResponse successResponse = (SuccessResponse) actionResponse;

						System.out.println("Status - " + successResponse.getStatus().getValue());

						for (Map.Entry<String, Object> entry : successResponse.getDetails().entrySet()) {
							System.out.println(entry.getKey() + ": " + entry.getValue());
						}

						System.out.println("Message - " + successResponse.getMessage().getValue());
					}
				}
			}
			else
			{

			}

			
		}
		
	}
}
