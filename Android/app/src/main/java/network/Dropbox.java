package network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Dropbox {
    private static final String ACCESS_TOKEN = "sl.BhEOvOBToejq_jgasJTWZZnAHJJ36yBpKKCwN2KcV8oQ522pJK9KskPoIyEwf3A0P6F5AaQxPdEPrm1i6BvbT7vP1efpXIB9HfJ8H0kxahkFaI8jO8Tv1cGYm2A_EY-wQcHcCmc";
    private static final String DROPBOX_PATH = "/";

    public Dropbox(String DEVICE_PATH, String FILE_NAME) {

        DbxRequestConfig config = DbxRequestConfig.newBuilder("TrackMyAttack").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = new FileInputStream(DEVICE_PATH + "DataModel");

            client.files().uploadBuilder(DROPBOX_PATH + "DataModel").withMode(WriteMode.OVERWRITE).uploadAndFinish(is);
            is.close();
        } catch (IOException e){
            e.printStackTrace();
            Log.d("Dropbpox", e.toString());
        } catch (UploadErrorException e) {
            e.printStackTrace();
            Log.d("Dropbpox", e.toString());

        } catch (DbxException e) {
            Log.d("Dropbpox", e.toString());

            e.printStackTrace();
        }

        Log.d("Dropbox", DEVICE_PATH + FILE_NAME);
        Log.d("Dropbox", DROPBOX_PATH + FILE_NAME);

        try {
            OutputStream os = new FileOutputStream(DEVICE_PATH + FILE_NAME);

//            Metadata pathMetaData = client.files().getMetadata(DROPBOX_PATH + FILE_NAME);
//            client.files().download(pathMetaData.getPathLower()).download(os);

            //client.files().downloadBuilder(DROPBOX_PATH + FILE_NAME).download(os);
            client.files().download(DROPBOX_PATH + FILE_NAME).download(os);
            //client.files().download("output.txt").download(os);

            os.close();
        }catch (IOException e){
            e.printStackTrace();
            Log.d("Dropbox", "1: " + e.toString());

        } catch (DownloadErrorException e) {
            e.printStackTrace();
            Log.d("Dropbox", "2: " + e.toString());

        } catch (DbxException e) {
            e.printStackTrace();
            Log.d("Dropbox", "3: " + e.toString());

        }


    }

}
