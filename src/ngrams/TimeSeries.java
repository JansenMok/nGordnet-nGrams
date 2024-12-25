package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int year = startYear; year <= endYear; year++) {
            Double value = ts.get(year);
            if (value != null) {
                this.put(year, value);
            }
        }

        // wait im dumb
        // super();
        // TimeSeries returnTs = new TimeSeries();
        // for (int year = startYear; year <= endYear; year++) {
        //     returnTs.put(year, ts.get(year));
        // }
        // return returnTs;
        // TimeSeries copy = ts.clone();
        // for (int year = MIN_YEAR; year < startYear; year++) {
        //     ts.remove(year);
        // }
        // for (int year = MAX_YEAR; year > endYear; year--) {
        //     ts.remove(year);
        // }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List returnList = new ArrayList();
        for (Integer years : keySet()) {
            returnList.add(years);
        }
        return returnList;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List returnList = new ArrayList();
        for (Integer years : keySet()) {
            returnList.add(get(years));
        }
        return returnList;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries newTS = new TimeSeries();
        for (Integer years : this.keySet()) {
            if (ts.containsKey(years)) {
                newTS.put(years, this.get(years) + ts.get(years));
            } else {
                newTS.put(years, this.get(years));
            }
        }
        for (Integer years : ts.keySet()) {
            if (this.containsKey(years)) {
                newTS.put(years, this.get(years) + ts.get(years));
            } else {
                newTS.put(years, ts.get(years));
            }
        }
        return newTS;

        // att 1
        // TimeSeries newTS = new TimeSeries();
        // if (this.isEmpty() && ts.isEmpty()) {
        //     return newTS;
        // }

        // for (Integer years : keySet()) {
        //     newTS.add(years);
        // }
        // for (Integer years : ts.keySet()) {
        //     if (!newTS.containsKey(years)) {
        //         newTS.put(years, ts.get(years));
        //     } else if (newTS.containsKey(years)) {
        //         valueAdd = newTS.get(years);
        //         newTS.put(years, ts.get(years) + newTS.get(years));
        //     }
        // }
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries newTS = new TimeSeries();
        for (Integer years : this.keySet()) {
            if (ts.containsKey(years)) {
                newTS.put(years, this.get(years) / ts.get(years));
            } else {
                throw new IllegalArgumentException();
            }
        }
        return newTS;
    }

}
