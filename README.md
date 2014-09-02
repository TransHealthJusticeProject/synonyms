##Pelias-Synonyms

Mapping synonyms of permanent address terms (e.g. "Street", "Lane", "Bridge") is useful to improve the flexibility of the geocoder. We should assume that not all the users will write addresses or places the same way, like using abbreviations (Lexington Av/Lexington Avenue), similar words (Interstate 80 / I-80 Highway), or just getting it slightly wrong (if the user searches within London for Oxford Avenue, the geocoder should probably include [Oxford Street](http://en.wikipedia.org/wiki/Oxford_Street)).

Synonym treatment is not a trivial problem, since excessive synonym mapping will reduce the relevance of results <sup>(example needed)</sup>, and no mappings will significantly reduce flexibility, making the search frustrating for the user. Additionally, different languages imply different address terms, different ways of abbreviating those, and, crucially, different term placements. It's pretty trivial to map ```Street``` to ```St```, but a German user will probably expect the same search results when searching for *Kaiser St*, *Kaiserstraße*, *Kaiserstrasse*, and *Kaiserstr.*

###Synonyms roadmap 

1. English abbreviations mapping.
2. Similar word equivalences.
3. Mistake tolerance.
4. Term concatenating.
5. Multilingual support (all the above with other languages).

###What this repo does

For now this is a Java command line application that generates an ElasticSearch synonyms.txt attached to a token filter. The tool should expand quickly in functionality, but the immediate expectation is for it to be able to hook the token filter to the index settings and, critically, implement a series of tests that ensure that we're not losing on the grounds of relevance nor flexibility.

###Build

This is very much a work in progress, build steps will follow soon. 

###Reference

* [Synonym treatment in ElasticSearch](http://www.elasticsearch.org/guide/en/elasticsearch/guide/current/using-synonyms.html)
* [Abbreviations in Nominatim](http://wiki.openstreetmap.org/wiki/Name_finder:Abbreviations)
* [ElasticSearch synonym unit testing](https://github.com/elasticsearch/elasticsearch/blob/1816951b6b0320e7a011436c7c7519ec2bfabc6e/src/test/java/org/elasticsearch/index/analysis/synonyms/SynonymsAnalysisTest.java) 
