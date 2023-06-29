package com.bdreiss.dataAPI.network;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxPKCEWebAuth;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;

import com.bdreiss.dataAPI.exceptions.NetworkException;

import com.dropbox.core.DbxAppInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PkceAuthorize {
    public static DbxAuthFinish authorize(String key, DbxUserAuthCode dbxUserAuthCode) throws IOException, NetworkException {
        // Run through Dropbox API authorization process without client secret
        DbxRequestConfig requestConfig = new DbxRequestConfig("TrackMyAttack");
        DbxAppInfo appInfoWithoutSecret = new DbxAppInfo(key);
        DbxPKCEWebAuth pkceWebAuth = new DbxPKCEWebAuth(requestConfig, appInfoWithoutSecret);

        DbxWebAuth.Request webAuthRequest =  DbxWebAuth.newRequestBuilder()
            .withNoRedirect()
            .withTokenAccessType(TokenAccessType.OFFLINE)
            .build();

        String authorizeUrl = pkceWebAuth.authorize(webAuthRequest);
        
        String message = "Go to the following address and click \"Allow\" (you might have to log in first).\nCopy the authorization code and enter it here.";
        
        String code = dbxUserAuthCode.getAuthorization(new URL(authorizeUrl), message);
        if (code != null)
        	code =code.trim();

        try {
            return pkceWebAuth.finishFromCode(code);
        } catch (DbxException e) {
        	throw new NetworkException(e);
        }
    }
}