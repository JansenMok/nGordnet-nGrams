package main;

import browser.NgordnetQueryHandler;
import helpers.WordNet;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

        WordNet wordNet = new WordNet(synsetFile, hyponymFile);
        return new HyponymsHandler(wordNet);
    }
}
