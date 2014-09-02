package com.mapzen.pelias.synonyms;

import org.json.JSONObject;

public class Term {
    public final String fullWord;
    public final String abbreviation;
    public final boolean concatenated;

    public Term (JSONObject json){
        fullWord = json.getString("fullword");
        abbreviation = json.getString("abbreviation");
        concatenated = json.getBoolean("concatenated");
    }
}
