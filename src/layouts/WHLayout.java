package layouts;


/**
 * This class represents the worm hole patience layout with the addition of the 'worm hole'
 * Inherits behaviour of BHLayout
 */
public class WHLayout extends BHLayout {

    // New worm hole card
    private int wormhole;

    // Getter
    public int wormHole() {
        return this.wormhole;
    }

    /**
     * Setter for the worm hole
     * Will return an error if the worm hole is already taken
     * @param card
     * @return
     */
    public int setWormHole(int card) {
        if (this.wormhole == BHLayout.ERR) {
            this.wormhole = Math.abs(card);
            return BHLayout.ONE;
        }
        else return BHLayout.ERR;
    }

    /**
     * topCard function accommodates for the worm hole by checking to see if the pile is the worm hole pile
     * @param pile
     * @return top card of worm hole
     */
    @Override
    public int topCard(int pile) {
        if (pile == BHLayout.ERR) {
            return wormHole();
        }
        return super.topCard(pile);
    }

    /**
     * removeTopCard accommodates for the worm hole
     * Sets the worm hole to -1 if it is removed
     * @param pile
     * @return
     */
    @Override
    public int removeTopCard(int pile) {
        if (pile == BHLayout.ERR) {
            this.wormhole = BHLayout.ERR;
            return BHLayout.ONE;
        }
        else return super.removeTopCard(pile);
    }

    /**
     * Overrides the equals behaviour for this class
     * We now need to account for the worm hole card
     * @param o
     * @return equal ?
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WHLayout)) {
            return false;
        }


        // Check if the entire layout is identical
        WHLayout layout = (WHLayout) o;
        boolean equals;
        equals = layout.numRanks() == this.numranks;
        equals = equals && layout.numSuits() == this.numsuits;
        equals = equals && layout.holeCard() == this.holecard;
        equals = equals && layout.numPiles() == this.numpiles;
        equals = equals && layout.wormHole() == this.wormhole;
        // We assume here that for both objects, the cards under the top cards are the same
        // Since Solver.java is only interested in one game
        if (equals) {
            for (int i = 0; i < this.numpiles; i++) {
                equals = equals && this.topCard(i) == layout.topCard(i);
            }
        }
        return equals;
    }

    /**
     * hashCode function which includes the worm hole
     * @return hash code
     */
    @Override
    public int hashCode() {
        return this.numsuits + this.numpiles + this.numranks + this.holecard + this.wormhole;
    }

    // Constructor for bringing a black hole layout to a worm hole layout
    public WHLayout(BHLayout layout) {
        this.holecard = layout.holeCard();
        this.layout = layout.copyLayout();
        this.numsuits = layout.numSuits();
        this.numpiles = layout.numPiles();
        this.numranks = layout.numRanks();
        this.wormhole = BHLayout.ERR;
    }
    // Constructor if we want to have the worm hole initialised with a given value
    public WHLayout(BHLayout layout, int wormhole) {
        this(layout);
        this.wormhole = wormhole;
    }
}