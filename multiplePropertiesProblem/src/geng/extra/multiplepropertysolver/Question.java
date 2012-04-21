package geng.extra.multiplepropertysolver;

import java.util.Arrays;

import java.util.Iterator;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;

import java.util.Set;

/**
 * This is the major class representing the Question. A Question object includes
 * all information necessary to solve the problem.
 *
 * The process of solving is performed on the Question object itself. Therefore,
 *
 * a) if a this question is solved, the solution is the Question class itself.
 * b) before adding assumed conditions to the Question object, one need to clone
 * an assumed version in case the assumption doesn't make sense.
 *
 * A Question object needs an list of sets of options to construct. The sets in
 * the list are the properties an object can have in the problem. For example,
 * if a person need to decide what hat, pants, and tie to wear, then there are
 * three properties, and therefore there should be three sets in the list. Each
 * set should contain the same number of options to be select from. For example,
 * the options in the set of hat can be "red hat", "blue hat", "yellow hat".
 *
 * Besides a list of set of all Options, the Question consists of a list of
 * entries. These entries are the limitations listed in the problem. For
 * example, if the question states:
 * "the person wearing red hat also wears blue pants but doesn't wear yellow ties."
 * Then this information will be stord in the Entry class, and an Entry will be
 * put in the field "properties" in the Question class.
 *
 * Basically, the Solver compares and reduce Entries in a Question in order to
 * produce certain number of definite Entries which are the final solution.
 *
 * @author Tianyu Geng (tony1)
 * @version Mar 27, 2012 v1.0
 */
public class Question implements Cloneable {

    /**
     * This is the Entry class represent a limiting condition stated in the
     * problem. It has an array of properties, for example, hat, tie, pants.
     *
     * An Entry object can be compared to another Entry object using various
     * methods defined in the class.
     *
     * @author Tianyu Geng (tony1)
     * @version Mar 27, 2012
     */
    public class Entry implements Cloneable {

        private final Property properties[];

        private int id; // this id is not necessary for the algorithm to work.

        private int numOfDefipro;

        /**
         * The constructor will initialize the properties array.
         */
        public Entry() {
            this.id = entryIdCounter++;
            numOfDefipro = 0;

            properties = new Property[numOfProperties];
            for (int i = 0; i < numOfProperties; i++) {
                properties[i] = new Property(i);
            }
        }

        /**
         * Edit a property of the Entry.
         *
         * @param categoryId
         *            to specify which property to edit
         * @param yes
         *            if there is a definite answer to this property, set it to
         *            the definite answer; otherwise set it to null.
         * @param no
         *            set all the options that will be excluded
         * @throws PropertyFailException
         *             indicates an contradiction happens for this property,
         *             normally this means the Question cannot be solved
         */
        public void
                editProperty(int categoryId, Option yes, Option... no)
                        throws PropertyFailException {
            assert categoryId < numOfProperties;
            assert no.length < numOfOptions;
            if (yes != null) {
                numOfDefipro++;
                properties[categoryId].setYesOption(yes);
            }
            else {

                properties[categoryId].addExlusion(no);
            }

        }

        /**
         * If the Entry has properties that are not definite, this method will
         * produce a List of all possible slightly stronger Entries. If the
         * Entry is definite, the List will be empty.
         *
         * @return
         */
        public List<Entry> canBe() {
            ArrayList<Entry> result = new ArrayList<Entry>();
            for (int i = 0; i < numOfProperties; i++) {
                if (!properties[i].isDefinite()) {
                    for (Property pro : properties[i].canBe()) {
                        Entry possibility = this.clone();
                        possibility.properties[i] = pro;
                        result.add(possibility);
                    }
                }
            }
            return result;
        }

        /**
         * Clone this Entry along with all the properties, so this is a deep
         * clone in the sense of Property; but shallow in the sense of Option.
         *
         * @return
         */
        public Entry clone() {
            Entry clone = new Entry();
            clone.id = id;
            clone.numOfDefipro = numOfDefipro;
            for (int i = 0; i < numOfProperties; i++) {
                clone.properties[i] = properties[i].clone();
            }
            return clone;

        }

        /**
         * Determine whether this Entry is contradict to another one.
         *
         *
         * @param that
         *            the Entry to be compared with.
         * @return true if a contradiction happens.
         */
        public boolean contradict(Entry that) {
            if (that == null)
                return false;

            // traverse all the properties and compare them one by one.
            for (int i = 0; i < numOfProperties; i++) {

                // for the ith property, determine whether this cannot be that
                if (this.properties[i].isDefinite()) {
                    if (that.properties[i].isNot(this.properties[i].is())) {
                        return true;
                    }
                }

                // for the ith property, determine whether that cannot be this
                if (that.properties[i].isDefinite()) {
                    if (this.properties[i].isNot(that.properties[i].is())) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Compare two Entries and see whether they are equal or not. This
         * method is not necessary in version 1.0
         *
         * @param obj
         * @return
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry that = (Entry) obj;
            for (int i = 0; i < numOfProperties; i++) {
                if (properties[i].yesOption == properties[i].yesOption
                        || (properties[i].yesOption != null && !properties[i].yesOption
                                .equals(that.properties[i].yesOption))) {
                    return false;
                }
            }
            return true;

        }

        /**
         * Compare this Entry with the other one and see whether these two
         * entries can be merged into a single one.
         *
         * @param that
         *            Entry to be compared with
         * @return true if any properties have the same definite Option;
         *         otherwise false
         */
        public boolean equivalentTo(Entry that) {

            for (int i = 0; i < numOfProperties; i++) {
                if (this.getProperty(i).isDefinite()
                        && this.getProperty(i).is(
                                that.getProperty(i).is())) {
                    return true;
                }
            }

            // for (int i = 0; i < numOfProperties; i++) {
            // if (!this.properties[i].equals(that.properties[i]))
            // return false;
            // }
            //
            // return true;
            return false;
        }

        /**
         * If two Entries are contradicting, this method can be called to
         * elaborate each other by add each other's definite Option in each
         * property to the exclusion set of the other Entry.
         *
         * For example, we have an Entry
         * entry1:"red hat, not blue pants, blue tie", and another
         * entry2:"not red hat, yellow pants". Then
         * entry1.fillContradiction(entry2) will change entry1 and entry2 to the
         * following:
         *
         * entry1:"red hat, not blue pants not yellow pants, not blue tie"
         * entry2:"not red hat, yellow pants, blue tie"
         *
         * @param that
         * @return true if fillContradiction changes anything for these two
         *         entries.
         * @throws PropertyFailException
         */
        public boolean fillContradiction(Entry that)
                throws PropertyFailException {
            boolean result = false;
            boolean added = false;
            for (int i = 0; i < numOfProperties; i++) {
                if (this.properties[i].isDefinite()) {
                    added =
                            that.properties[i]
                                    .addExlusion(this.properties[i].is());

                    result |= added;
                    if (added && that.getProperty(i).is() != null) {
                        that.numOfDefipro++;
                    }
                }
                if (that.properties[i].isDefinite()) {
                    if (this.properties[i].isNot(that.properties[i].is())) {
                        added =
                                this.properties[i]
                                        .addExlusion(that.properties[i]
                                                .is());
                        result |= added;
                        if (added && getProperty(i).is() != null) {
                            numOfDefipro++;
                        }
                    }
                }
            }
            return result;

        }

        /**
         * Get a property by category id.
         *
         * @param categoryId
         * @return
         */
        public Property getProperty(int categoryId) {
            return properties[categoryId];
        }

        /**
         * If all properties all definite, then the entry is definite.
         *
         * @return
         */
        public boolean isDefinite() {
            return numOfDefipro == numOfProperties;
        }

        /**
         * Merge two entries. This means the result will have all the
         * information of the two entries before merging.
         *
         * Merging should only happen when equivalentTo() return true
         *
         * @param that
         * @throws PropertyFailException
         */
        public void mergeWith(Entry that) throws PropertyFailException {
            for (int i = 0; i < numOfProperties; i++) {
                if (this.getProperty(i).addExlusion(
                        that.getProperty(i).isNot())
                        && getProperty(i).is() != null) {
                    numOfDefipro++;
                }

            }
        }

        public String toString() {
            StringBuilder result =
                    new StringBuilder("\nEntry ID: " + id
                            + " # Definite Property: " + numOfDefipro);
            for (Property pro : properties) {
                result.append("\nCategory ID: " + pro.categoryId + " "
                        + pro.toString());
            }
            return result.toString();
        }
        public String[] toCSVTextArray() {
            String[] result = new String[numOfProperties];
            for (int i=0;i<numOfProperties;i++) {
                result[i]=properties[i].toCSVText();
            }
            return result;
        }
        public int hashCode() {
            return Arrays.hashCode(toCSVTextArray());
        }
    }

    /**
     * Represent a property in an Entry.
     *
     * @author Tianyu Geng (tony1)
     * @version Mar 27, 2012
     */
    public class Property implements Cloneable {

        private HashSet<Option> noOptions;
        private Option yesOption;
        private final Set<Option> allOptions;
        public final int categoryId;

        /**
         * An entry has to created with an category ID, even though this
         * categoryId field is not directly involved in the algorithm.
         *
         * @param cateogryId
         */
        public Property(int cateogryId) {
            yesOption = null;
            this.categoryId = cateogryId;
            this.allOptions = listOfAllOptionSets.get(cateogryId);
            noOptions = new HashSet<Option>();
        }

        /**
         * A property can also be created with an definite yesOption. This
         * overloading constructor was primarily used to simplify the assuming
         * process in version 1.0.
         *
         * @param categoryId
         * @param yes
         */
        public Property(int categoryId, Option yes) {
            this(categoryId);
            setYesOption(yes);
        }

        /**
         * Add all Options that are not possible for this property.
         *
         * @param no
         *            the noOptions
         * @return true if any changes are ACTUALLY made to the property after
         *         the invocation of this method.
         * @throws PropertyFailException
         *             When too many no options are added, this indicate a
         *             logical error has happened, indicating the current
         *             Question is not solvable.
         */
        public boolean addExlusion(Option... no)
                throws PropertyFailException {
            boolean result = false;
            for (Option noOption : no) {
                result |= noOptions.add(noOption);
            }

            // When there are enough noOptions, a yesOption will be generated by
            // exclusion. And this property will become definite.
            if (noOptions.size() == numOfOptions - 1) {
                Set<Option> copyAll = new HashSet<Option>(allOptions);
                copyAll.removeAll(noOptions);
                Option[] yes = new Option[1];
                copyAll.toArray(yes);
                yesOption = yes[0];

            }
            if (noOptions.size() >= numOfOptions) {
                throw new PropertyFailException(
                        "A property that cannot be anything is produced.");
            }
            return result;
        }

        /**
         * If the property is not definite, this method return all the
         * possibilities it can be. Return a List of all possible stronger
         * Properties
         *
         * @return
         */
        public List<Property> canBe() {
            Set<Option> copyAll = new HashSet<Option>(allOptions);
            copyAll.removeAll(noOptions);
            Iterator<Option> it = copyAll.iterator();
            ArrayList<Property> result =
                    new ArrayList<Property>(numOfOptions
                            - noOptions.size());
            while (it.hasNext()) {
                result.add(new Property(categoryId, it.next()));
            }
            return result;
        }

        public Property clone() {
            Property clone = new Property(categoryId);
            clone.noOptions = (HashSet<Option>) noOptions.clone();
            clone.yesOption = yesOption;
            return clone;
        }

        // This method is not required for the algorithm in version 1.0.
        public boolean equals(Object obj) {
            return (obj instanceof Property)
                    && (this.noOptions.equals(((Property) obj).noOptions));

        }

        /**
         * Return what this property is if it is definite, null if it's not
         * definite.
         *
         * @return
         */
        public Option is() {
            return yesOption;
        }

        /**
         * Determine whether this property is an Option or not.
         *
         * @param option
         * @return
         */
        public boolean is(Option option) {
            return yesOption == option;
        }

        /**
         * Return whether this property is definite or not.
         *
         * @return
         */
        public boolean isDefinite() {
            return yesOption != null;
        }

        /**
         * Return an Array of Options that this property cannot be.
         *
         * @return
         */
        public Option[] isNot() {
            Option[] result = new Option[noOptions.size()];
            noOptions.toArray(result);
            return result;
        }

        /**
         * Determine whether this property is not something no matter this
         * property is definite or not.
         *
         * @param option
         * @return
         */
        public boolean isNot(Option option) {
            return noOptions.contains(option);
        }

        /**
         * Set the yesOption an automatically update the exclusion set.
         *
         * @param yes
         */
        public void setYesOption(Option yes) {
            yesOption = yes;
            if (yes == null)
                return;

            HashSet<Option> copyAll = new HashSet<Option>(allOptions);
            copyAll.remove(yes);
            noOptions = copyAll;
        }

        public String toString() {
            if (isDefinite()) {
                return yesOption.toString();
            }
            else {
                StringBuilder result = new StringBuilder();
                for (Option noOption : noOptions) {
                    result.append("/" + noOption + " ");
                }
                return result.toString();
            }
        }
        public String toCSVText() {
            return toString();
        }
    }

    /**
     * The Exception indicate a logical error in the Question, could be the
     * logical error in the original problem or a wrong assumption.
     *
     * @author Tianyu Geng (tony1)
     * @version Mar 27, 2012
     */
    public static class PropertyFailException extends Exception {
        public PropertyFailException(String msg) {
            super(msg);
        }

    }

    private static int entryIdCounter = 0;
    public final int numOfProperties;
    public final int numOfOptions;
    public final List<Set<Option>> listOfAllOptionSets;

    String assumption;

    private List<Entry> entries;
    private int numOfEntries;

    /**
     * Constructor of Question, takes a list of sets of all Options
     *
     * @param listOfAllOptionSets
     */
    public Question(List<Set<Option>> listOfAllOptionSets) {
        numOfEntries = 0;
        this.listOfAllOptionSets = listOfAllOptionSets;
        this.numOfProperties = listOfAllOptionSets.size();
        this.numOfOptions = listOfAllOptionSets.get(0).size();
        entries = new ArrayList<Entry>();
        assumption = "";

    }

    /**
     * Add an Entry to this Question, used to input the problem.
     *
     * @return
     */
    public Entry addEntry() {

        return addEntry(new Entry());
    }

    /**
     * Add a certain Entry
     *
     * @param entry
     * @return
     */
    public Entry addEntry(Entry entry) {
        entries.add(entry);
        numOfEntries++;
        return entry;
    }

    public Question clone() {
        Question clone = new Question(listOfAllOptionSets);
        for (Entry entry : entries) {
            if (entry != null) {
                clone.entries.add(entry.clone());
            }
            else {
                clone.entries.add(null);
            }
        }
        clone.numOfEntries = numOfEntries;
        clone.assumption = assumption;
        return clone;

    }

    // public boolean equals(Object obj) {
    // if (!(obj instanceof Question)) {
    // return false;
    // }
    // Question that = (Question) obj;
    // return(this.entries.equals(that.entries));
    //
    // }

    public List<Entry> getEntries() {
        return entries;
    }

    public Entry getEntry(int index) {
        return entries.get(index);
    }

    /**
     * @return number of Entries still existing
     */
    public int getNumOfEntries() {
        return numOfEntries;
    }

    /**
     *
     * @return True if the Question is solved.
     */
    public boolean isSolved() {
        return numOfEntries == numOfOptions;
    }

    /**
     * Remove an Entry because it is not necessary anymore, probably because the
     * information in it has been mergeWith() to another Entry.
     *
     * @param index
     */
    public void removeEntry(int index) {
        entries.set(index, null);
        numOfEntries--;

    }

    public String toString() {
        StringBuilder result =
                new StringBuilder(
                        "\nQuestion | # entry: "
                                + numOfEntries
                                + " =======================\n\nAssumption--------------\n"
                                + assumption
                                + "\n\nAssumption ends--------------\n");
        for (Entry entry : entries) {
            if (entry != null) {
                result.append(entry.toString() + "\n");
            }
        }
        return result.toString();
    }

    public String toTextArray() {
        return null;
    }
}
