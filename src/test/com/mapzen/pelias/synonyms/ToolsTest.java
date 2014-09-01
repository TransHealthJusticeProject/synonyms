package com.mapzen.pelias.synonyms;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToolsTest {
    @Test
    public void testWikipediaOK(){
        JSONObject wikipediaOK = Tools.getJSONFromURL("http://en.wikipedia.org/w/api.php?format=json");
        assertNotNull("Request being responded", wikipediaOK);
        assertTrue("Wiki API OK", wikipediaOK.has("servedby"));
    }
}
