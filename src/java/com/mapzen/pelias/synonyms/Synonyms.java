package com.mapzen.pelias.synonyms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Synonyms {

    public static void main(String[] arguments){
        HashMap<String, ArrayList<String>> synMappings = new HashMap<>();
        try {
            synMappings = Tools.parseTerms("data/eng.json");
        } catch (IOException e) {
            System.out.println("There was a problem reading the file");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            PrintWriter writer = new PrintWriter("synonyms.txt", "UTF-8");
            for (Map.Entry pair : synMappings.entrySet()) {
                @SuppressWarnings("unchecked")
                ArrayList<String> abbreviations = (ArrayList<String>) pair.getValue();
                for (String abv : abbreviations) {
                    writer.print(abv.toLowerCase());
                    if (!(abbreviations.get(abbreviations.size() - 1).equals(abv))) {
                        writer.print(", ");
                    }
                }
                String lowercased = pair.getKey().toString().toLowerCase();
                writer.print( " => " + lowercased);
                writer.print("\n");
            }
            writer.close();
            System.out.println("Cool! synonyms.txt has been generated in "+System.getProperty("user.dir"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Wrong encoding");
            e.printStackTrace();
        }
    }
}
