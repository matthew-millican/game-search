package functions;

import layouts.BHLayout;
import types.Node;
import layouts.WHLayout;

import java.util.HashSet;


/**
 * This function recursively solves a given puzzle
 * See Solver.java for further method implementations
 */
public class RecursiveSolver extends Solver {
    // Filter used to ignore visited nodes
    private HashSet<BHLayout> filter = new HashSet<>();


    // Stores the start time of the recursive algorithm in nano seconds
    private double startTime;

    private boolean timedOut;

    /**
     * Recursive function for solving Black Hole puzzles.
     * @param node node to visit and expand
     */
    private void recursiveSolve(Node node) {
        if (timedOut) {
            return;
        }
        nodesVisited++;
        // Ignore node if it is already visited
        if (!filter.contains(node.layout())) {
            filter.add(node.layout());
            BHLayout state = node.layout();
            // Check if this state is a solution
            if (isSolution(node)) {
                // Check if this is a complete search so continue recursively descending
                if (complete) {
                    solution = new Node(new BHLayout(node.layout()), node.copyLog());
                    solutions.add(solution);
                } else {
                    // Return out of the function
                    solution = new Node(new BHLayout(node.layout()), node.copyLog());
                    // Take the elapsed time now
                    this.elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
                    return;
                }
            }
            // Expand this node by creating new nodes and recursively visiting them
            int numPiles = state.numPiles();
            int i;
            for (i = 0; i < numPiles; i++) {
                if (findNode(state, i)) {
                    Node newNode = createNewNode(node, i, state.topCard(i), false);
                    if (newNode != null) {
                        recursiveSolve(newNode);
                    }
                    // If we have a found a solution when recursively descending, then exit
                    if ((solution != null && !complete) || timedOut) {
                        return;
                    }
                }
            }
        }
        elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
        timedOut = checkTimeOut();
    }

    /**
     * Function for recursively solving Worm Hole puzzles
     * @param node
     */
    private void recursiveSolveWorm(Node node) {
        if (timedOut) {
            return;
        }
        nodesVisited++;

        // Filter out visited nodes
        if (!filter.contains(node.layout())) {
            WHLayout state = (WHLayout) node.layout();
            filter.add(state);
            if (isSolution(node)) {
                // If we want to complete the search tree then add to the list of solutions
                if (complete) {
                    solution = new Node(new WHLayout(node.layout()), node.copyLog());
                    this.solutions.add(solution);
                }
                else {
                    // Else we take the first solution
                    solution = new Node(new WHLayout(node.layout()), node.copyLog());
                    this.elapsedTime = (System.nanoTime() - startTime) /CONVERSION;
                }
                return;
            }
            // Expand the node
            int numPiles = state.numPiles();
            int i, topCard;
            for (i = 0; i < numPiles; i++) {
                if (findNode(state, i)) {
                    Node newNode = createNewNode(node, i, state.topCard(i), false);
                    if (newNode != null) {
                        recursiveSolveWorm(newNode);
                    }
                    // If we have found a solution (in-complete search) then exit
                    if ((solution != null && !complete) || timedOut) {
                        return;
                    }
                }
            }
            // Expand the node by setting the worm hole to different cards
            if (state.topCard(BHLayout.ERR) == BHLayout.ERR) {
                for (i = 0; i < numPiles; i++) {
                    topCard = state.topCard(i);
                    if (topCard != BHLayout.ERR) {
                        Node newNode = createNewNode(node, i, topCard, true);
                        if (newNode != null) {
                            recursiveSolveWorm(newNode);
                        }
                        if ((solution != null && !complete) || timedOut) {
                            return;
                        }
                    }
                }
            }
            // If we can move the worm hole card to the hole card then visit this node
            else if (findNode(state, BHLayout.ERR)) {

                topCard = state.topCard(BHLayout.ERR);
                Node newNode = createNewNode(node, BHLayout.ERR, topCard, false);
                if (newNode != null) {
                    recursiveSolveWorm(newNode);
                }
            }
        }
        elapsedTime = (System.nanoTime() - startTime) / CONVERSION;
        timedOut = checkTimeOut();
    }

    /**
     * Override the run function in Solver
     * Reroute the execution to the recursive functions
     */
    @Override
    public boolean run() {
        // Only run if we have not yet solved the puzzle
        if (!hasRun) {
            try {
                if (isWorm) {
                    startTime = System.nanoTime();
                    recursiveSolveWorm(pop());
                } else {
                    startTime = System.nanoTime();
                    recursiveSolve(pop());
                }
            }
            catch(StackOverflowError se) {
                // If the recursive calls have reached a certain depth and the stack memory overflows
                System.out.println("Stack overflow error. Exiting ... ");
                System.exit(BHLayout.ERR);
            }
            catch(OutOfMemoryError oe) {
                // If the program runs out of memory to be allocated
                System.out.println("Program out of memory. Exiting ... ");
                System.exit(BHLayout.ERR);
            }
            catch(Exception e) {
                // General exception catch
                // Don't exit just print the output
                System.out.println("Program encountered an error. Printing output ... ");
            }
            hasRun = true;
        }
        print();

        return solution != null;
    }


    public RecursiveSolver(BHLayout layout, boolean isWorm, boolean complete, boolean suppress) {
        super(layout, isWorm, complete, suppress, false, false);
    }

}
