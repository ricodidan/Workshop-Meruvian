package org.meruvian.workshop.service;

import android.content.Context;
import android.net.ConnectivityManager;

import org.apache.http.HttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Enrico_Didan on 24/12/2016.
 */

public class ConnectionUtil {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null)
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        else
            return false;
    }

    public static HttpParams getHttpParams(int connectionTimeout,
                                           int socketTimeout) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);
        return params;
    }

    public static String convertEntityToString(HttpEntity entity) {
        InputStream instream;
        StringBuilder total = null;
        try {
            instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    instream));
            total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }


}
