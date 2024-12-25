package main;

import browser.NgordnetServer;
import org.slf4j.LoggerFactory;
import helpers.WordNet;

public class Main {
    static {
        LoggerFactory.getLogger(Main.class).info("\033[1;38mChanging text color to white");
    }
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String synsetFile = "./data/wordnet/synsets.txt";
        // String synsetFile = "./data/wordnet/synsets11.txt";
        // String synsetFile = "./data/wordnet/synsets16.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";
        // String hyponymFile = "./data/wordnet/hyponyms11.txt";
        // String hyponymFile = "./data/wordnet/hyponyms16.txt";

        WordNet wordNet = new WordNet(synsetFile, hyponymFile);

        hns.startUp();
        hns.register("history", new DummyHistoryHandler());
        hns.register("historytext", new DummyHistoryTextHandler());
        hns.register("hyponyms", new HyponymsHandler(wordNet));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
