# Read Me First

## Zoho Sample Application
The Zoho CRM main method is located here: com.example.zohosample.ZohoSampleApplication.main()

Assumption - you have an account with Zoho CRM

This application creates a lead and retrieves all the leads in the SANDBOX CRM env

Create a sandbox env in Zoho CRM here:
> (Settings icon in top right) > Under Data Administration > "Sandbox"

> Follow prompts to create your sandbox env

Once sandbox is created, create a client to interact with Zoho CRM here

https://api-console.zoho.com/

From the selection, choose:
> Self-Client

In application.properties, add your client information, add the client_id and client_secret

Generate code with these scopes:

```
ZohoCRM.settings.fields.ALL,ZohoCRM.modules.ALL,ZohoCRM.modules.ALL,ZohoCRM.settings.fields.ALL,ZohoCRM.settings.related_lists.ALL
```

Add the code in application.properties

Now, run
```
ZohoSampleApplication.main()
```





