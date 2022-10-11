package types;

import java.util.Objects;

/**
 * This class stores a data type Tuple which stores an integer pair that may be stored in a solution list
 */
public class Tuple {

        private int pile;
        private int card;


        //Getters
        public int getPile() {
            return this.pile;
        }
        public int getCard() {
            return this.card;
        }


    /**
     * Overrides equals behaviour for efficient comparing of tuples
     * @param o
     * @return boolean value
     */
    @Override
        public boolean equals(Object o) {
            if (!(o instanceof Tuple)) {
                return false;
            }
            // Check properties of both tuples
            Tuple t = (Tuple) o;
            return this.pile == t.getPile() && this.card == t.getCard();
        }

    /**
     * Overridden hash code function
     * @return hash code
     */
    @Override
        public int hashCode() {
            return Objects.hash(this.pile, this.card);
        }


        // Constructor
        public Tuple(int pile, int card) {
            this.pile = pile;
            this.card = card;
        }
}
