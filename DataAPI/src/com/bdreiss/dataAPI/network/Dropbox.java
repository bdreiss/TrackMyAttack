package com.bdreiss.dataAPI.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxPKCEWebAuth;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.json.JsonReader.FileLoadException;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.exceptions.NetworkException;

/**
 * 
 */
public class Dropbox {

	/**
	 * 
	 */
	public static String message = "Go to the following address and click \"Allow\" (you might have to log in first).\nCopy the authorization code and enter it here.";

	private static String DROPBOX_PATH = "/";

	private static DbxPKCEWebAuth PKCE_WEB_AUTH;

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String getDbxFilePath(DataModel data) {
		return data.getSaveFile().getParent() + "/DropboxAuth";
	};

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getAuthorizationURL(String key) {
		// Run through Dropbox API authorization process without client secret
		DbxRequestConfig requestConfig = new DbxRequestConfig("TrackMyAttack");
		DbxAppInfo appInfoWithoutSecret = new DbxAppInfo(key);
		PKCE_WEB_AUTH = new DbxPKCEWebAuth(requestConfig, appInfoWithoutSecret);

		DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().withNoRedirect()
				.withTokenAccessType(TokenAccessType.OFFLINE).build();

		return PKCE_WEB_AUTH.authorize(webAuthRequest);
	}

	/**
	 * 
	 * @param key
	 * @param authorizationToken
	 * @param data
	 * @throws NetworkException
	 */
	public static void authorize(String key, String authorizationToken, DataModel data) throws NetworkException {

		try {
			if (authorizationToken == null)
				throw new NetworkException(new NullPointerException(
						"No authorization code has been passed to the Dropbox authorization routine."));

			authorizationToken = authorizationToken.trim();

			DbxAuthFinish authorization = PKCE_WEB_AUTH.finishFromCode(authorizationToken);

			DbxCredential credential = new DbxCredential(authorization.getAccessToken(), authorization.getExpiresAt(),
					authorization.getRefreshToken(), key);

			DbxCredential.Writer.writeToFile(credential, getDbxFilePath(data));

		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}

	}

	/**
	 * 
	 * @param data
	 * @throws NetworkException
	 */
	public static void upload(DataModel data) throws NetworkException {

		DbxClientV2 client = getClient(data);

		try {
			InputStream is = new FileInputStream(data.getSaveFile());

			client.files().uploadBuilder(DROPBOX_PATH + data.getSaveFileName()).withMode(WriteMode.OVERWRITE)
					.uploadAndFinish(is);

			is.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}
	}

	/**
	 * 
	 * @param data
	 * @throws NetworkException
	 */
	public static void download(DataModel data) throws NetworkException {
		DbxClientV2 client = getClient(data);

		try {
			OutputStream os = new FileOutputStream(data.getSaveFile());

			client.files().download(DROPBOX_PATH + data.getSaveFileName()).download(os);

			os.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}

		data.load();
	}

	private static DbxClientV2 getClient(DataModel data) throws NetworkException {
		DbxRequestConfig config = new DbxRequestConfig("TrackMyAttack");
		DbxClientV2 client;

		// File authenticationFile = new File(data.getSaveFile().getParent().toString()
		// + "/DropboxAuthentication");
		File authenticationFile = new File(getDbxFilePath(data));
		DbxCredential credential = null;

		try {
			credential = DbxCredential.Reader.readFromFile(authenticationFile);

			if (credential.aboutToExpire()) {
				credential.refresh(config);
				DbxCredential.Writer.writeToFile(credential, authenticationFile);
			}
		} catch (FileLoadException | DbxException | IOException e) {
			throw new NetworkException(e);
		}

		client = new DbxClientV2(config, credential.getAccessToken());
		return client;
	}

}
