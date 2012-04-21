package geng.extra.predictor;

import java.util.TreeMap;

import java.util.Collection;

/**
 *
 * @author Tianyu Geng (tony1)
 * @version Apr 1, 2012
 * @param <Type>
 */
public class StringablePredictor<Type extends Stringable> {
    public static class NoStringableMatchesException extends Exception {

    }

    private TreeMap<String, Type> map;

    public StringablePredictor(Collection<Type> types) {

        map = new TreeMap<String, Type>(String.CASE_INSENSITIVE_ORDER);
        for (Type type : types) {
            map.put(type.toStringKey(), type);
        }
    }

    public Type get(String abr) throws NoStringableMatchesException {
        if (abr == null || abr.isEmpty())
            return null;
        Type temp;
        if ((temp=map.get(abr))!=null) {
            return temp;
        }
        else {
            temp = map.higherEntry(abr).getValue();
            if(!temp.toStringKey().startsWith(abr)) {
                throw new NoStringableMatchesException();
            }
            return temp;
        }
    }
}
