package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;
import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap nGramMap;

    public HistoryHandler(NGramMap map) {
        this.nGramMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<TimeSeries> listOfTimeSeries = new ArrayList<>();

        for (String word : words) {
            labels.add(word);
            TimeSeries wordWeightHistory = nGramMap.weightHistory(word, startYear, endYear);
            listOfTimeSeries.add(wordWeightHistory);

            // TimeSeries ts = nGramMap.countHistory(word, startYear, endYear);
            // if (!ts.isEmpty()) {
            //     listOfTimeSeries.add(ts);
            //     labels.add(word);
            // }
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, listOfTimeSeries);
        String encodedImage = Plotter.encodeChartAsString(chart);
        return encodedImage;
    }
}

