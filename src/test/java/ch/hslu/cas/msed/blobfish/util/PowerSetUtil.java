package ch.hslu.cas.msed.blobfish.util;

import java.util.ArrayList;
import java.util.List;


/**
 * <a href="https://www.baeldung.com/java-power-set-of-a-set">Copied of</a>
 */
public class PowerSetUtil {

    private PowerSetUtil() {
        // is util class
    }

    public static <T> List<List<T>> getPowerSet(List<T> list) {
        List<List<T>> result = new ArrayList<>();

        // add base
        if (list.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }

        T first = list.get(0);

        // get sublists
        List<T> rest = list.subList(1, list.size());

        // recursive call
        List<List<T>> subsetsWithoutFirst = getPowerSet(rest);

        result.addAll(subsetsWithoutFirst);
        for (List<T> subset : subsetsWithoutFirst) {
            List<T> newSubset = new ArrayList<>();
            newSubset.add(first);
            newSubset.addAll(subset);
            result.add(newSubset);
        }

        return result;
    }
}
