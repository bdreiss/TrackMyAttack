package com.bdreiss.dataAPI.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.json.JsonReader.FileLoadException;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadError;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.exceptions.NetworkException;

public class Dropbox {

	private static String DROPBOX_PATH = "/";
		
	public static void upload(DataModel data, String key, DbxUserAuthCode dbxUserAuthCode) throws NetworkException {
		
		DbxClientV2 client = getClient(data, key, dbxUserAuthCode);
	

		try {
			InputStream is = new FileInputStream(data.getSaveFile());
		
			Metadata response = client.files().uploadBuilder(DROPBOX_PATH + data.getSaveFileName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(is);
		
			is.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}
	}
	

	public static void download(DataModel data, String key, DbxUserAuthCode dbxUserAuthCode) throws NetworkException {
		DbxClientV2 client = getClient(data, key, dbxUserAuthCode);
		
		try {
			OutputStream os = new FileOutputStream(data.getSaveFile());
		
			Metadata response = client.files().download(DROPBOX_PATH + data.getSaveFileName()).download(os);
			
			os.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}
		
		data.load();
	}
	
	private static DbxClientV2 getClient(DataModel data, String key, DbxUserAuthCode dbxUserAuthCode) throws NetworkException {
		DbxRequestConfig config = new DbxRequestConfig("TrackMyAttack");
		DbxClientV2 client;
		
		 //File authenticationFile = new File(data.getSaveFile().getParent().toString() + "/DropboxAuthentication");
		File authenticationFile = new File(data.getSaveFile().getParent()+"/DropboxAuth");
		DbxCredential credential = null;

		
		if (!authenticationFile.exists()) {
			try {
				DbxAuthFinish authorization = PkceAuthorize.authorize(key, dbxUserAuthCode);
				
				credential = new DbxCredential(authorization.getAccessToken(),authorization.getExpiresAt(), authorization.getRefreshToken(),key);
				
				DbxCredential.Writer.writeToFile(credential, authenticationFile);
				
			} catch (IOException e) {
				throw new NetworkException(e);
			}
		} else {
			try {
				credential = DbxCredential.Reader.readFromFile(authenticationFile);
				
				if (credential.aboutToExpire()) {
					credential.refresh(config);
					DbxCredential.Writer.writeToFile(credential, authenticationFile);
				}
			} catch (FileLoadException | DbxException | IOException e) {
				throw new NetworkException(e);
			} 
		}
			
		
		client = new DbxClientV2(config,credential.getAccessToken());
		return client;
	}
	
}
