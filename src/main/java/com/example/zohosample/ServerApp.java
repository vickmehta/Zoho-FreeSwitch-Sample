package com.example.zohosample;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ServerApp{

    Properties props = new Properties();

    public ServerApp() throws IOException {
        props.load(Files.newInputStream(Paths.get("src/main/resources/application.properties")));
    }

    public static ServerApp of() throws IOException{
        return new ServerApp();
    }

    public void test()
    {
        System.out.println(props.getProperty("zoho.client.secret"));
    }

    public String getProp(String property){
        return props.getProperty(property);
    }

    // public void accessAndRefreshGrantToken()
	// {
	// 	ZCRMRestClient.initialize(configurations_map);
	// 	ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
	// 	String grantToken = props.getProperty("zoho.client.grant.token"); //“paste_the_self_authorized_grant_token_here”;
	// 	ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
	// 	String accessToken = tokens.getAccessToken();
	// 	String refreshToken = tokens.getRefreshToken();
	// 	System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);

	// 	ZohoOAuthClient client = ZohoOAuthClient.getInstance();
	// 	client.generateAccessTokenFromRefreshToken(refreshToken, userMailId);
	// }
}