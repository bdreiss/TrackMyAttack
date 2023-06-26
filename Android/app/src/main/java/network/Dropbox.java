package network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class Dropbox {
    private static final String ACCESS_TOKEN = "sl.BhB5xCeGufAvS7YgQrPdW1SFlPvk9VGcSmCeeb5kB07TpeGVXVMDrhn9XLqQ-KHv690hFPQ0SDqN3KftCv7zsik_Xidp0oA9Lm0_IZFV_MuvoBViFXBM77Uz_QJ5YRkFExVObj8";


    public Dropbox() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("TrackMyAttack").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }

        Log.d("DropboxTest", account.getName().getDisplayName());

    }

}
