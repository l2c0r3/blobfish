package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.EvalWrapper;

import java.util.ArrayList;
import java.util.List;

public class PowerSetUtil {

    private PowerSetUtil() {
        // is util class
    }

    public static <T> List<List<T>> getPermutation(T[] objectList) {

        var resultList = new ArrayList<List<T>>();

        var tmpList = new ArrayList<T>();

        for (int i = 0; i < objectList.length; i++) {
            var object = objectList[i];

            tmpList = new ArrayList<>();
            tmpList.add(object);
            resultList.add(new ArrayList<>(tmpList));

            for (int j = i + 1; j < objectList.length; j++) {
                var nextObject = objectList[j];
                tmpList.add(nextObject);
                resultList.add(new ArrayList<>(tmpList));
            }
        }

        return resultList;
    }
}
