package org.tianara.helloworld.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.tianara.helloworld.MainActivity;
import org.tianara.helloworld.generic.NoConnectivityException;
import org.tianara.helloworld.settings.Settings;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;


/**
 * The single purpose of the class is to fetch the HTML file from the given string.
 */
class WebClient {
    private WebClient() {}

    static String get(URL url, String encoding) throws NoConnectivityException {
        // Creating variables
        StringBuilder builder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Main algorithm
        try {
            // Opening connection
            InputStream in = null;
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());

            }
            catch (UnknownHostException ex) {
                throw new NoConnectivityException();
            }
            catch (IOException ex ) {
                ex.printStackTrace();
                return null;
            }
            // Reading
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(in, encoding);
            }
            catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            BufferedReader r = new BufferedReader(reader);
            String line;
            try {
                // For each line...
                while ((line = r.readLine()) != null) {
                    line = StringEscapeUtils.unescapeHtml4(line);
                    //line = line.replace("<br><br>", "\n");
                    line = line.replace("<br>", "\n").replace("<br />", "\n").replace("<br/>", "\n");
                    if (Settings.fromId(Settings.id.LANGUAGE).getIndex() == 1) {
                        builder.append(line).append('\n');
                    }
                    else {
                        builder.append(line);
                    }
                }
            } catch (IOException ex) { urlConnection.disconnect(); ex.printStackTrace(); }
            // Stop the connection
        } finally {
            urlConnection.disconnect();
        }
        return builder.toString();
    }
}
