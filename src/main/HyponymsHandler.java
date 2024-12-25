package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import helpers.WordNet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wordNet;
    public HyponymsHandler(WordNet wordNet) {
        this.wordNet = wordNet;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        Set<String> commonHyponyms = wordNet.getCommonHyponyms(words.toArray(new String[0]));
        String result = commonHyponyms.stream().sorted().collect(Collectors.joining(", ", "[", "]"));

        return result;
    }
}
