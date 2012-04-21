package geng.extra.multiplepropertysolver;

import geng.extra.predictor.Stringable;

/**
 * This is the class to represent possible options for any properties of an
 * object in a problem. Example: for hat property in a problem about what to
 * wear, "red hat" or "blue hat" can be one of such options. Option class is a
 * delegation of String class, but with more customization possibility.
 *
 * @author Tianyu Geng (tony1)
 * @version Mar 27, 2012 v1.0
 */
public class Option implements Comparable<Option>, Stringable{
    private String data;

    // This is crucial
    /**
     * Takes a String to construct an option.
     * @param for example, "red hat","rubber boots".
     */
    public Option(String data) {
        this.data = data;
    }

    // The method below is useless for now.
    // public boolean equalLoose(Object obj) {
    // return obj instanceof Option
    // && data.substring(0, data.length() - 3).equals(
    // ((Option) obj).data.substring(0,
    // ((Option) obj).data.length()));
    // }

    // Not necessary in version 1.0
    public int compareTo(Option obj) {

        return data.compareTo(obj.data);
    }

    // This is crucial
    public String toString() {
        return data;
    }

    // This is crucial.
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toStringKey() {
        // TODO Auto-generated method stub
        return toString();
    }

}
