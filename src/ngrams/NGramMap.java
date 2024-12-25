package ngrams;

import edu.princeton.cs.algs4.In;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // why are these maps so confusing 😭

    private Map<String, TimeSeries> wordTimeSeriesMap = new HashMap<>();
    // key: word, value: TimeSeries → key: year, value: occurences of that word for key year

    private TimeSeries totalCountsPerYear = new TimeSeries();
    // key: year, value = total word occurrences

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // split this into readWordsFile and readCountsFile methods after passing all tests
        In wordsIn = new In(wordsFilename);
        while (wordsIn.hasNextLine()) {
            String[] wordsFileData = wordsIn.readLine().split("\t");

            String word = wordsFileData[0];
            int wordYear = Integer.parseInt(wordsFileData[1]);
            Double occurrences = Double.parseDouble(wordsFileData[2]);
            // bookoccurrences = wordsFileData[3]; // ignore this data

            if (wordTimeSeriesMap.containsKey(word)) {
                TimeSeries wordFoundTS = wordTimeSeriesMap.get(word);
                wordFoundTS.put(wordYear, occurrences);
                /* use year as key and the # of occurrences as value (stored as double to allow for flexibility in
                division calculations)
                 */
            } else {
                TimeSeries newTimeSeries = new TimeSeries();
                newTimeSeries.put(wordYear, occurrences);
                wordTimeSeriesMap.put(word, newTimeSeries);
            }
        }

        In countsIn = new In(countsFilename);
        while (countsIn.hasNextLine()) {
            String[] countsFileData = countsIn.readLine().split(",");

            int year = Integer.parseInt(countsFileData[0]);
            Double totalWords = Double.parseDouble(countsFileData[1]);
            // totalPages = countsFlieData[2]; // ignoring last two columns
            // totalDistinctSources = countsFlieData[3];

            totalCountsPerYear.put(year, totalWords); // year as key, total count of words as value
        }

        // attempt 1 using FileReadDemo.java as reference but its too confusing
        // In in = new In(SHORT_WORDS_FILE);
        // // while (!in.isEmpty())
        // while (in.hasNextLine()) {
        //     String nextLine = in.readLine();
        //     String[] splitLine = nextLine.split("\t");
        //     if (splitLine.length >= 3) {
        //         String word = splitLine[0];
        //         int year = Integer.parseInt(splitLine[1]);
        //         double count = Double.parseDouble(splitLine[2]);

        //         TimeSeries ts;
        //         if (wordTS.containsKey(word)) {
        //             ts = wordTS.get(word);
        //         } else {
        //             ts = new TimeSeries();
        //             wordTS.put(word, ts);
        //         }
        //     }
        // }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordTimeSeriesMap.containsKey(word)) {
            return new TimeSeries();
        }
        if (startYear > endYear) {
            return new TimeSeries();
        }

        // invalid year rails to nearest valid year
        if (startYear < TimeSeries.MIN_YEAR) {
            startYear = TimeSeries.MIN_YEAR;
        }
        if (endYear > TimeSeries.MAX_YEAR) {
            endYear = TimeSeries.MAX_YEAR;
        }

        return new TimeSeries(wordTimeSeriesMap.get(word), startYear, endYear);

        // attempt 1 fail
        // TimeSeries findWord = wordTS.get(word);
        // if (findWord == null) {
        //     return new TimeSeries();
        // } else {
        //     return new TimeSeries(findWord, startYear, endYear);
        // }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordTimeSeriesMap.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordTimeSeriesMap.get(word), TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCountsPerYear, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (startYear < TimeSeries.MIN_YEAR) { // railing year
            startYear = TimeSeries.MIN_YEAR;
        }
        if (endYear > TimeSeries.MAX_YEAR) {
            endYear = TimeSeries.MAX_YEAR;
        }

        TimeSeries weightHistoryTS = new TimeSeries();
        if (!wordTimeSeriesMap.containsKey(word)) {
            return weightHistoryTS;
        }

        for (int year = startYear; year <= endYear; year++) {
            Double wordOccurrences = wordTimeSeriesMap.get(word).get(year);
            Double totalWords = totalCountsPerYear.get(year);

            if (wordOccurrences != null && totalWords != null && totalWords != 0) {
                weightHistoryTS.put(year, wordOccurrences / totalWords);
            }
        }
        return weightHistoryTS;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries weightHistoryTS = new TimeSeries();
        if (!wordTimeSeriesMap.containsKey(word)) {
            return weightHistoryTS;
        }

        for (int year = TimeSeries.MIN_YEAR; year <= TimeSeries.MAX_YEAR; year++) {
            Double wordOccurrences = wordTimeSeriesMap.get(word).get(year);
            Double totalWords = totalCountsPerYear.get(year);

            if (wordOccurrences != null && totalWords != null && totalWords != 0) {
                weightHistoryTS.put(year, wordOccurrences / totalWords);
            }
        }
        return weightHistoryTS;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        if (startYear < TimeSeries.MIN_YEAR) { // railing year
            startYear = TimeSeries.MIN_YEAR;
        }
        if (endYear > TimeSeries.MAX_YEAR) {
            endYear = TimeSeries.MAX_YEAR;
        }

        TimeSeries summedWeightHistoryTS = new TimeSeries();
        Double summedRelFreq;
        for (int year = startYear; year <= endYear; year++) {
            summedRelFreq = 0.0;
            for (String word : words) {
                if (wordTimeSeriesMap.containsKey(word)) {
                    Double wordOccurrences = wordTimeSeriesMap.get(word).get(year);
                    Double totalWords = totalCountsPerYear.get(year);

                    if (wordOccurrences != null && totalWords != null && totalWords != 0) {
                        summedRelFreq += wordOccurrences / totalWords;
                    }
                    if (wordOccurrences != null) {
                        summedWeightHistoryTS.put(year, summedRelFreq);
                    }
                }
            }
        }
        return new TimeSeries(summedWeightHistoryTS, startYear, endYear);

        // attempt 1: wrong logic
        // for (String word : words) {
        //     if (wordTimeSeriesMap.containsKey(word)) {
        //         // idk fix this
        //         for (int year = startYear; year <= endYear; year++) {
        //             Double wordOccurrences = wordTimeSeriesMap.get(word).get(year);
        //             Double totalWords = totalCountsPerYear.get(year);

        //             if (wordOccurrences != null && totalWords != null && totalWords != 0) {
        //                 weightHistoryTS.put(year, wordOccurrences / totalWords);
        //             }
        //         }
        //     }
        // }
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedWeightHistoryTS = new TimeSeries();
        Double summedRelFreq;

        for (int year = TimeSeries.MIN_YEAR; year <= TimeSeries.MAX_YEAR; year++) {
            summedRelFreq = 0.0;
            for (String word : words) {
                if (wordTimeSeriesMap.containsKey(word)) {
                    Double wordOccurrences = wordTimeSeriesMap.get(word).get(year);
                    Double totalWords = totalCountsPerYear.get(year);

                    if (wordOccurrences != null && totalWords != null && totalWords != 0) {
                        summedRelFreq += wordOccurrences / totalWords;
                    }
                    if (wordOccurrences != null) {
                        summedWeightHistoryTS.put(year, summedRelFreq);
                    }
                }
            }
            // summedWeightHistoryTS.put(year, summedRelFreq);
        }
        return summedWeightHistoryTS;
    }

    // come back to these helper functions one i pass all tests
    // private void readWordsFile(wordsFilename) {
    //     return null;
    // }

    // private void readCountsFile(countsFilename) {
    //     return null;
    // }

}

