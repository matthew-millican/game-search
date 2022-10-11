package functions;
import types.Node;
import types.Tuple;
import layouts.BHLayout;
import layouts.WHLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Function to solve given Black hole or Wormhole variations of Patience using a stack-based Depth-first search algorithm.
 *
 */
public class Solver {


    /**
     * Conversion value for converting nanoseconds to milliseconds
     */
    protected static final double CONVERSION = 1000000;

    /**
     * Time out time for when we want to record the time taken during execution
     *
     *  A time out of 30 seconds was permitted to make sure tests still ran reasonably quickly and didn't hang
     */
    protected static final double TIMEOUT = 60000;

    /**
     * Elapsed time of execution in milliseconds
     */
    protected double elapsedTime;

    /**
     * Number of nodes visited during execution
     */
    protected int nodesVisited = 0;



    /**
     * Determines if we print the time taken to execute after finishing
     */
    protected boolean time;

    /**
     * Determines if we print the number of nodes visited after execution
     */
    protected boolean displayNodeCount;

    /**
     * Stores a list of nodes which represent different states in the abstracted search tree
     */
    protected ArrayList<Node> list = new ArrayList<>();

    /**
     * Defines whether the puzzle is the worm hole variation or not.
     */
    protected boolean isWorm;


    /**
     * Suppresses stdout output if true
     */
    protected boolean suppress;

    /**
     * Visits every node if true, in-complete algorithm if false
     */
    protected boolean complete;

    /**
     * Set to true on first solve of the puzzle
     */
    protected boolean hasRun = false;

    /**
     * Possible solution to the puzzle. Starts as null. May be updated.
     */
    protected Node solution = null;


    /**
     * List of solutions populated if we want a complete search
     */
    protected ArrayList<Node> solutions = new ArrayList<>();

    public Node getSolution() {
        if (solution != null) {
            return new Node(new BHLayout(solution.layout()), solution.copyLog());
        }
        else return null;
    }

    /**
     * Returns a copy of the solutions found when solving
     * @return new list
     */
    public ArrayList<Node> getSolutions() {
        ArrayList<Node> solutionCopy = new ArrayList<>();
        for (int i = 0; i < solutions.size(); i++) {
            Node n = solutions.get(i);
            solutionCopy.add(new Node(new BHLayout(n.layout()), n.copyLog()));
        }

        return solutionCopy;

    }

    public double getElapsedTime() {
        return this.elapsedTime;
    }

    public int getNodesVisited() {
        return this.nodesVisited;
    }


    /**
     * Returns a copy of the nodes in the abstract search tree list
     * @return copy of nodes
     */
    public ArrayList<Node> getNodes() {
        ArrayList<Node> copy = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            copy.add(this.list.get(i));
        }
        return copy;
    }


    /**
     * Pushes a node onto the abstract stack implemenation
     * i.e. add to the front of the ArrayList
     * @param node
     */
    protected void push(Node node) {
        list.add(BHLayout.ZERO, node);
    }


    /**
     * Pops an item from the 'stack'.
     * i.e. removes the front node from the list.
     * @return popped node or null on failure
     */
    protected Node pop() {
        if (list.size() >= BHLayout.ONE) {
            Node popped = list.get(BHLayout.ZERO);
            list.remove(BHLayout.ZERO);
            return popped;
        }
        else return null;

    }



    /**
     * Prints the required output once solved.
     * Prints out 1 or 0 depending on whether the solution was solved.
     * If solved, then the log of moves made is printed in the required format.
     */
    protected void print() {
        // Only if we want to be verbose
        if (!suppress) {
            if (solution == null) {
                System.out.println(BHLayout.ZERO);
            } else {
                System.out.print(BHLayout.ONE + " ");
                ArrayList<Tuple> log = solution.log();
                for (int i = 0; i < log.size(); i++) {
                    Tuple tuple = log.get(i);
                    System.out.print(tuple.getPile() + " " + tuple.getCard() + " ");
                }

            }
        }

         // Display the time taken to run for this function
        if (time) {
            if (elapsedTime == TIMEOUT) {
                System.out.print("TIMEOUT >60s\t");
            }
            else {
                // Round to 2 decimal place using the BigDecimal type
                BigDecimal elapse = new BigDecimal(elapsedTime).setScale(2, RoundingMode.CEILING);
                System.out.print(elapse.toString() + "ms  \t");
            }
        }
        // Display the number of nodes visited during execution
        if (this.displayNodeCount) {
            System.out.print(nodesVisited + "\t\t");
        }
    }

    /**
     * Creates a new node in the tree by:
     * updating the new log
     * setting the hole card or worm hole card
     * @param node
     * @param pile
     * @param card
     * @param setWormHole
     * @return a new node for the search space or null on failure
     */
    public Node createNewNode(Node node, int pile, int card, boolean setWormHole) {

        // Create new log
        ArrayList<Tuple> log;
        Tuple tuple = setWormHole ? new Tuple(pile, -card) : new Tuple(pile, card);
        log = node.copyLog();
        log.add(tuple);
        // Create new layout state for a worm hole game
        if (isWorm) {
            WHLayout state = (WHLayout) node.layout();
            WHLayout copy = new WHLayout(state, state.wormHole());
            if (setWormHole) {

                //Check return values of functions to avoid any unexpected errors
                if (copy.setWormHole(card) == BHLayout.ERR) {
                    return null;
                }
            }
            else {
                if (copy.setHole(card) == BHLayout.ERR) {
                    return null;
                }
            }

            if (copy.removeTopCard(pile) == BHLayout.ERR) {
                return null;
            }
            return new Node(copy, log);
        }
        else {
            // Create a new layout state for a black hole game (setting a new hole card)
            BHLayout copy = new BHLayout(node.layout());
            if (copy.setHole(card) == BHLayout.ERR || copy.removeTopCard(pile) == BHLayout.ERR) {
                return null;
            }
            return new Node(copy, log);
        }

    }

    /**
     * Checks if a solution is found with a given node
     * If the puzzle is a worm hole variation then check if the piles are empty and the wormhole is empty
     * Else just check if the piles are empty
     * @param node
     * @return has it found a solution?
     */
    protected boolean isSolution(Node node) {
        BHLayout layout = node.layout();
        if (isWorm) {
            WHLayout whLayout = (WHLayout) layout;
            return Checker.checkPilesEmpty(whLayout) && whLayout.wormHole() == BHLayout.ERR;
        }
        else return Checker.checkPilesEmpty(layout);
    }

    /**
     * Checks if a new search state can be made with a given pile
     * @param layout
     * @param pile
     * @return can a new search state be made?
     */
    public static boolean findNode(BHLayout layout, int pile) {
        int topCard = layout.topCard(pile);
        if (topCard == BHLayout.ERR) {
            return false;
        }
        else return Checker.checkCardRank(layout, topCard);
    }

    /**
     * Checks if the currently running solver has timed out
     * @return
     */
    protected boolean checkTimeOut() {
        // If the updated elapsed time is greater than 30 seconds
        if (elapsedTime > TIMEOUT) {
            elapsedTime = TIMEOUT;
            return true;
        }
        else return false;
    }


    /**
     * Runs the search algorithm for solving a worm hole patience game
     */
    private void solveWorm() {
        double startTime = System.nanoTime();
        // Filter set for filtering visited nodes
        HashSet<WHLayout> filter = new HashSet<>();
        while (list.size() != BHLayout.ZERO) {
            Node node = pop();
            nodesVisited++;
            WHLayout state = (WHLayout) node.layout();
            if (!filter.contains(state)) {
                filter.add(state);
            } else continue;

            // Check if this state is a solution
            if (isSolution(node)) {
                // If not complete, return first solution
                if (!complete) {
                    solution = new Node(new WHLayout(node.layout()), node.copyLog());
                    solutions.add(solution);
                    break;
                }
                // Add to list of solutions
                else {
                    solutions.add(new Node(new WHLayout(node.layout()), node.copyLog()));
                    continue;
                }
            }

            // Expand this node
            int numPiles = state.numPiles();
            int i, topCard;
            for (i = 0; i < numPiles; i++) {
                if (findNode(state, i)) {
                    Node newNode = createNewNode(node, i, state.topCard(i), false);
                    if (newNode != null) {
                        push(newNode);
                    }
                }
            }
            // Expand this node by creating new nodes with an updated worm hole card
            if (state.topCard(BHLayout.ERR) == BHLayout.ERR) {
                for (i = 0; i < numPiles; i++) {
                    topCard = state.topCard(i);
                    if (topCard != BHLayout.ERR) {
                        Node newNode = createNewNode(node, i, topCard, true);
                        if (newNode != null) {
                            push(newNode);
                        }
                    }
                }
            }
            // Retrieve a new state by attempting to move the worm hole card to the hole card
            else if (findNode(state, BHLayout.ERR)) {
                topCard = state.topCard(BHLayout.ERR);
                Node newNode = createNewNode(node, BHLayout.ERR, topCard, false);
                if (newNode != null) {
                    push(newNode);
                }
            }
            elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
            if (checkTimeOut()) {

                // Stop solving
                return;
            }
        }

        elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
        // Take the first solution if running a complete search
        if (complete) {
            solution = solutions.size() > BHLayout.ZERO ? solutions.get(BHLayout.ZERO) : null;
        }
        hasRun = true;
    }

    /**
     * Runs the search algorithm for solving a black hole patience game.
     *
     * See here for the basic algorithm structure: https://studres.cs.st-andrews.ac.uk/CS3105/Lectures/CS3105-L02-Search-1.pdf
     */
    private void solve() {
        double startTime = System.nanoTime();
        HashSet<BHLayout> filter = new HashSet<>();
        // Loop until we no longer have any nodes in the list
        while (list.size() != BHLayout.ZERO) {
            Node node = pop();
            nodesVisited++;
            // See if we can filter this node
            if (!filter.contains(node.layout())) {
                filter.add(node.layout());
            } else {
                continue;
            }

            // Check if we have found the solution
            BHLayout state = node.layout();
            if (isSolution(node)) {
                if (complete) {
                    solution = new Node(new BHLayout(node.layout()), node.copyLog());
                    solutions.add(solution);
                }
                else {
                    solution = new Node(new BHLayout(node.layout()), node.copyLog());
                    break;
                }
            }

            // Expand the node by pushing new nodes to the front of the list
            int numPiles = state.numPiles();
            int i;
            for (i = 0; i < numPiles; i++) {
                if (findNode(state, i)) {
                    Node newNode = createNewNode(node, i, state.topCard(i), false);
                    if (newNode != null) {
                        push(newNode);
                    }
                }
            }
            elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
            if (checkTimeOut()) {

                // Stop solving
                return;
            }
        }
        // Calculate the final time in milliseconds
        elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
        hasRun = true;
    }

    /**
     * Runs the solver dependent on the patience variation.
     */
    public boolean run() {
        if (!hasRun) {
            try {
                if (isWorm) {
                    solveWorm();
                } else {
                    solve();
                }
            }
            // Space complexity has lead to the list being so large we run out of memory
            catch(OutOfMemoryError oe) {
                System.err.println("Program out of memory. Exiting ... ");
                System.exit(BHLayout.ERR);
            }
            // General exception catcher which doesn't exit
            catch(Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Exiting ... ");
                System.exit(BHLayout.ERR);
            }
        }
        print();

        return solution != null;

    }

    public Solver(BHLayout layout, boolean isWorm, boolean complete, boolean suppress, boolean time, boolean nodes){
        this.isWorm = isWorm;
        this.complete = complete;
        this.suppress = suppress;
        this.time = time;
        this.displayNodeCount = nodes;
        Node node = new Node(layout);
        // Add to the start of the list
        push(node);
    }

    public Solver(BHLayout layout, boolean isWorm, boolean suppress) {
        this(layout, isWorm, false, suppress, false, false);
    }

    public Solver(BHLayout layout, boolean isWorm, boolean complete, boolean suppress) {
        this(layout, isWorm, complete, suppress, false, false);
    }



}
