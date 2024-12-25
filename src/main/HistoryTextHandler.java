package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap nGramMap;

    public HistoryTextHandler(NGramMap map) {
        this.nGramMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder response = new StringBuilder();
        for (String word : words) {
            TimeSeries ts = nGramMap.weightHistory(word, startYear, endYear);
            response.append(word).append(": ").append(ts.toString()).append("\n");
        }

        return response.toString();
    }
}

