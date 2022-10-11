package types;

import layouts.BHLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class defines a data type which represents a node in the abstract search tree
 */
public class Node {

    // Current puzzle layout ( may be at any point in the puzzle )
    private BHLayout layout;

    // This stores a list of tuples representing the solution log (i.e. the steps in the solution )
    private ArrayList<Tuple> log = new ArrayList<>();

    // Getters
    public BHLayout layout() {
        return this.layout;
    }

    public ArrayList<Tuple> log() {
        return this.log;
    }

    /**
     * Deep copies the log to be used when creating new nodes without changing the original log
     * @return a new array list
     */
    public ArrayList<Tuple> copyLog() {
        Iterator<Tuple> iterator = this.log.iterator();
        ArrayList<Tuple> copy = new ArrayList<>();
        while (iterator.hasNext()) {
            Tuple tuple = iterator.next();
            copy.add(new Tuple(tuple.getPile(), tuple.getCard()));
        }

        return copy;
    }



    // Constructors
    public Node(BHLayout layout) {
        this.layout = layout;
    }

    public Node(BHLayout layout, ArrayList<Tuple> log) {
        this.layout = layout;
        this.log = log;
        this.log = copyLog();
    }

    /**
     * Overrides equals behaviour for this class to compare nodes in the program
     * @param o
     * @return boolean value
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        // Compare layouts
        Node object = (Node) o;
        boolean equals = (this.layout.equals(object.layout()));
        if (!equals) {
            return false;
        }
        // Compare the log (steps made so far)
        if (log.size() != object.log().size()) {
            return false;
        }
        for (int i = 0; i < log.size(); i++) {
            ArrayList<Tuple> oLog = object.log();
            equals = equals && log.get(i).getCard() == oLog.get(i).getCard();
            equals = equals && log.get(i).getPile() == oLog.get(i).getPile();
        }

        return equals;
    }

    /**
     * Overrides hash code function for this class
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.layout.hashCode(), this.log().size());
    }


}
