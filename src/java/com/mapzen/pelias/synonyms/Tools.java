package com.mapzen.pelias.synonyms;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Tools {
    public static JSONObject getJSONFromURL(String urlString){
        URL url = null;
        String responseBody = "";
        InputStream inputStream = null;
        try {
            url = new URL(urlString);
            inputStream = url.openStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            String line;
            while((line = dataInputStream.readLine()) != null){
                responseBody += line;
            }
        }
        catch (Exception e) { // For now this works for both IO and MalformedURL exceptions
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return new JSONObject(responseBody);

    }
}
