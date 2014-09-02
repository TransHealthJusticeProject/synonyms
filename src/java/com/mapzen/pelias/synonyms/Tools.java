package com.mapzen.pelias.synonyms;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public static JSONObject getJSONFromURL(String urlString) {
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

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static HashMap<String, ArrayList<String>> parseTerms (String path) throws IOException {
        HashMap<String, ArrayList<String>> synMap = new HashMap<>();
        JSONArray rootJSONArray = new JSONArray(readFile(path));
        for (int i = 0; i<rootJSONArray.length(); i++){
            JSONObject termJSON = rootJSONArray.getJSONObject(i);
            Term term = new Term(termJSON);
            if(!synMap.containsKey(term.fullWord)){
                ArrayList<String> thisList = new ArrayList<String>();
                thisList.add(term.abbreviation);
                synMap.put(term.fullWord, thisList);
            }
            else{
                synMap.get(term.fullWord).add(term.abbreviation);
            }
        }
        return synMap;
    }
}
