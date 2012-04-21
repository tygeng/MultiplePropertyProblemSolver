package geng.extra.multiplepropertysolver;

import java.util.Collection;

import java.util.List;

import java.util.ArrayList;

public class Solver {
    private Collection<Question> solutions;
    private Question q;

    public Solver(Question q) {
        this.q = q;
        solutions = new ArrayList<Question>();
    }

    public Collection<Question> getSolutions() {
        return solutions;
    }

    /**
     * The switch of this solver machine.
     */
    public void solve() {
        List<Question> qs = new ArrayList<Question>(1);
        qs.add(q);
        solve(qs);
    }

    /**
     * A recursive solving method. It will first deduce() then assume(), until
     * all assumed Questions are solved.
     *
     * @param list
     */
    private void solve(List<Question> list) {

        for (Question q : list) {
            try {
                deduce(q);
            }
            catch (Question.PropertyFailException e) {
                // TODO Auto-generated catch block
                continue;
            }
            if (q.isSolved()) {
                solutions.add(q);
            }
            else {
                solve(assume(q));
            }
        }
    }

    /**
     * Generate a List of Questions with slightly stronger limiting conditions
     * than the inputing Question.
     *
     * @param q
     *            the inputing Question
     * @return a List of assumed Question
     */
    private List<Question> assume(Question q) {
        Question.Entry undefinite = null;
        for (Question.Entry entry : q.getEntries()) {
            if (entry != null && !entry.isDefinite()) {
                undefinite = entry;
                break;
            }
        }

        ArrayList<Question> result = new ArrayList<Question>();
        for (Question.Entry entry : undefinite.canBe()) {
            Question assumed = q.clone();
            assumed.addEntry(entry);

            assumed.assumption += entry.toString();
            result.add(assumed);
        }
        return result;
    }

    /**
     * Deduce the Question without assuming anything, if the conditions are
     * complete, this will produce a final solution or the most reduced version
     * of the inputing Question.
     *
     * @param q
     * @throws Question.PropertyFailException
     */
    private void deduce(Question q) throws Question.PropertyFailException {
        boolean changeHappened = true;
        int round = 0;
        while (changeHappened) {

            // System.out.println("\n***************** round: " + round++);

            changeHappened = false;

            for (int i = 0; i < q.getEntries().size(); i++) {
                if (q.getEntry(i) != null) {
                    for (int j = i + 1; j < q.getEntries().size(); j++) {
                        if (q.getEntry(j) != null) {

                            // if (i == 2 && j == 3) {
                            // System.out.print("\n---------\ni=" + i
                            // + " j=" + j
                            // + "\nBEFORE OPERATIONS\n"
                            // + q.getEntry(i).toString()
                            // + q.getEntry(j).toString());
                            // }

                            if (q.getEntry(i).equivalentTo(q.getEntry(j))) {
                                q.getEntry(i).mergeWith(q.getEntry(j));
                                q.removeEntry(j);
                                changeHappened = true;

                            }
                            if (q.getEntry(i).contradict(q.getEntry(j))) {
                                changeHappened |=
                                        q.getEntry(i).fillContradiction(
                                                q.getEntry(j));
                                changeHappened |=
                                        q.getEntry(j).fillContradiction(
                                                q.getEntry(i));
                            }

                            // if (i==2) {
                            // System.out.println("\n\nAFTER OPERATIONS\n"
                            // + q.getEntry(i).toString()
                            // + q.getEntry(j));
                            // }
                        }
                    }
                }
            }
            // System.out.println("\n***************** round: " + round
            // + " end");
        }
    }
}
