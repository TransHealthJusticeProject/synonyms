package com.mapzen.pelias.synonyms;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.lucene.all.AllEntries;
import org.elasticsearch.common.lucene.all.AllTokenStream;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.*;

public class SynonymsTest {
    private AnalysisService analysisService;

    @Before
    public void callElasticsearch(){
        Settings settings = settingsBuilder().loadFromClasspath("com/mapzen/pelias/synonyms/synonyms.json").build();
        Index index = new Index("pelias");

        Injector parentInjector = new ModulesBuilder().add(
                new SettingsModule(settings),
                new EnvironmentModule(new Environment(settings)),
                new IndicesAnalysisModule())
                .createInjector();

        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, settings),
                new IndexNameModule(index),
                new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class)))
                .createChildInjector(parentInjector);

        analysisService = injector.getInstance(AnalysisService.class);
    }

    @Test
    public void capsMatch(){
        match("pelias", "CAPS DONT MAtTER", "caps dont matter");
        match("pelias", "THiS MatteRS EveN LeSS", "this matters even less");
    }

    @Test
    public void basicSynonymMatch(){
        match("pelias", "Tottenham Ct Rd", "tottenham court road");
    }

    @Test
    public void synonymOverlap(){
        match("pelias", "Oxford St", "oxford street saint");
    }

    private void match(String analyzerName, String source, String target) {
        try {
            Analyzer analyzer = analysisService.analyzer(analyzerName).analyzer();

            AllEntries allEntries = new AllEntries();
            allEntries.addText("field", source, 1.0f);
            allEntries.reset();

            TokenStream stream = AllTokenStream.allTokenStream("_all", allEntries, analyzer);
            stream.reset();
            CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

            StringBuilder sb = new StringBuilder();
            while (stream.incrementToken()) {
                sb.append(termAtt.toString()).append(" ");
            }

            assertEquals(target, sb.toString().trim());
            stream.close();
        }
        catch(IOException e){
            System.out.println("There was a problem writing the token stream");
            e.printStackTrace();
        }
    }

}