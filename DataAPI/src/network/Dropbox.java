package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.exceptions.NetworkException;

public class Dropbox {

	private static final String ACCESS_TOKEN = "";

	private static String DROPBOX_PATH = "/";
	
	private static String KEY = "z8mbl51ksw78vfz";
	private static String SECRET = "rwh3pdbnq7bbln5";
	
	public static void upload(DataModel data) throws NetworkException {
		
		DbxClientV2 client = getClient(data);
	

		try {
			InputStream is = new FileInputStream(data.getSaveFile());
		
			Metadata response = client.files().uploadBuilder(DROPBOX_PATH + data.getSaveFileName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(is);
		
			is.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}
	}
	
	public static void download(DataModel data) throws NetworkException {
		DbxClientV2 client = getClient(data);
		
		try {
			OutputStream os = new FileOutputStream(data.getSaveFile());
		
			Metadata response = client.files().download(DROPBOX_PATH + data.getSaveFileName()).download(os);
			
			os.close();
		} catch (IOException | DbxException e) {
			throw new NetworkException(e);
		}
		
		data.load();
	}
	
	private static DbxClientV2 getClient(DataModel data) throws NetworkException {
		DbxRequestConfig config = new DbxRequestConfig("TrackMyAttack");
		DbxClientV2 client;
		
		
		try {
			client = new DbxClientV2(config,ACCESS_TOKEN);
			return client;
		} catch (IOException e) {
			throw new NetworkException(e);
		}
	}
	
}
