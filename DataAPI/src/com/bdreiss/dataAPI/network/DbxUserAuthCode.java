package com.bdreiss.dataAPI.network;

import java.net.URL;

import com.bdreiss.dataAPI.exceptions.NetworkException;

public interface DbxUserAuthCode {
	
	String getAuthorization(URL authenticationURL, String message) throws NetworkException;
	
}
