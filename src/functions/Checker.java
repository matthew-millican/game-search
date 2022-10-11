package functions;

import layouts.*;
import types.Tuple;

import java.util.ArrayList;


/**
 * Function class for checking if a given solution to a black hole or worm hole patience game is valid
 */
public class Checker {

    /**
     * Defines whether the puzzle is the worm hole or black hole variation
     */
    private boolean isWorm;


    /**
     * Suppress output made in print if this is true
     */
    private boolean suppress;

    /**
     * Stores the card layout
     */
    private BHLayout layout;

    /**
     * Stores the list of integers which give pairs of moves in the patience solution
     */
    private ArrayList<Integer> pairs;

    /**
     * Function which checks to see if the top card of a given pile can be retrieved
     * and if so, is equal to the card we expect to see there
     * @param layout
     * @param pile
     * @param card
     * @return true if the top card is valid, false if not
     */
    public boolean checkTopCard(BHLayout layout, int pile, int card)  {
        int top;
        if (!isWorm) {
            return !((top = layout.topCard(pile)) == BHLayout.ERR || top != card);
        }
        else {
            // Cast as a worm hole layout
            try {
                WHLayout wh = (WHLayout) layout;
                top = wh.topCard(pile);
                return !(top == BHLayout.ERR || top != card);
            }
            catch(ClassCastException ce) {
                return false;
            }
        }
    }

    /**
     * This function is used to help the JUnit tests.
     * It takes the solution list of tuples and creates a list of integers to be used during checking a solution.
     * @param log
     * @return
     */
    public static ArrayList<Integer> unwrapLog(ArrayList<Tuple> log) {
        ArrayList<Integer> solutionList = new ArrayList<>();
            for (int i = 0; i < log.size(); i++) {
                solutionList.add(log.get(i).getPile());
                solutionList.add(log.get(i).getCard());
            }
        return solutionList;
    }

    /**
     * Checks to see if a given card has a rank that is adjacent to the hole card rank
     * @param layout
     * @param card
     * @return true or false
     */
    public static boolean checkCardRank(BHLayout layout, int card) {
        // Retrieve the ranks
        int cardRank = layout.getCardRank(card);
        int holeRank = layout.getCardRank(layout.holeCard());

        // Get the minimum and max ranks for this deck of cards
        // Only the maximum changes
        int min = BHLayout.ONE;
        int max = layout.numRanks();

        // Check to see if the cards are circular before comparing their adjacency
        if (!(holeRank == min && cardRank == max) && !(cardRank == min && holeRank == max)) {
            // They are not adjacent if true
            if (Math.abs(cardRank - holeRank) != BHLayout.ONE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if the piles of a layout are empty by trying to retrieve it's top card
     * @param layout
     * @return true or false
     */
    public static boolean checkPilesEmpty(BHLayout layout) {
        for (int i = BHLayout.ZERO; i < layout.numPiles(); i++) {
            if (!(layout.topCard(i) == BHLayout.ERR)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Runs through the solution list and checks to see if the solution given is valid for the puzzles
     * @return
     */
    private boolean checkMoves() {
        BHLayout bhLayout = new BHLayout(this.layout);
        // Loop while we still have two integers in the list
        while (pairs.size() >= BHLayout.PAIR) {
            // Retrieve the first two elements
            int pile = pairs.get(BHLayout.ZERO);
            int card = pairs.get(BHLayout.ONE);
            // Check to see if this is the corresponding top card for the pile
            if (!checkTopCard(bhLayout, pile, card)) {
                return false;
            }
            // Check to see if this can be placed onto the hole card
            if (!checkCardRank(bhLayout, card)) {
                return false;
            }
            // If so, remove the top card and set the new black hole
            bhLayout.removeTopCard(pile);
            bhLayout.setHole(card);
            // Remove these two values
            pairs.remove(BHLayout.ZERO);
            pairs.remove(BHLayout.ZERO);
        }
        // Return if the piles are empty and we have successfully moved every card
        return checkPilesEmpty(bhLayout);
    }

    /**
     * Checks to see if the solution to a worm hole game is valid
     * @return true or false
     */
    private boolean checkWormMoves() {
        // New (Worm hole) WH layout
        WHLayout whLayout = (WHLayout) layout;
        // Loop until we no longer have a pair of numbers
        while (pairs.size() >= BHLayout.PAIR) {
            // Get the first two cards
            int pile = pairs.get(BHLayout.ZERO);
            int card = pairs.get(BHLayout.ONE);
            // Check to see if this is the corresponding top card
            if (!checkTopCard(whLayout, pile, Math.abs(card))) {
                return false;
            }
            // Check to see if the card given is negative ( i.e. we move it to the worm hole )
            if (pile >= BHLayout.ZERO && card < BHLayout.ZERO) {
                // Check if we can set the worm hole (i.e. it is not already occupied )
                if (whLayout.setWormHole(Math.abs(card)) == BHLayout.ERR) {
                    return false;
                }
                // Remove the top card
                whLayout.removeTopCard(pile);
            }
            else {
                // Check to see if we can move the card to the black hole
                if (!checkCardRank(whLayout, card)) {
                    return false;
                }
                // Remove the top card and set the new black hole
                whLayout.removeTopCard(pile);
                whLayout.setHole(card);
            }
            // Remove this pair
            pairs.remove(BHLayout.ZERO);
            pairs.remove(BHLayout.ZERO);
        }
        // return if this is a valid solution
        return checkPilesEmpty(whLayout);
    }


    /**
     * Runs the corresponding checker function
     */
    public boolean run() {
        boolean success = isWorm ? checkWormMoves() : checkMoves();
        // Output corresponding message if the output is not to be suppressed
        if (!suppress) {
            if (success) {
                System.out.println("true\n");
            } else {
                System.out.println("false\n");
            }
        }
        return success;
    }

    /**
     * @param layout puzzle layout
     * @param workingList provided solution ( which might not be valid )
     * @param isWorm are we solving a worm hole variation ?
     * @param suppress suppress stdout output
     */
    public Checker(BHLayout layout, ArrayList<Integer> workingList, boolean isWorm, boolean suppress) {
        this.layout = layout;
        this.suppress = suppress;
        this.pairs = workingList;
        this.isWorm = isWorm;
    }




 }
