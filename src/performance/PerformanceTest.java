package performance;
import functions.Solver;
import functions.Writer;
import layouts.BHLayout;
import layouts.WHLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;


/**
 * This class runs a multitude of tests and stores the results of the searching algorithms so we can analyse complexities
 */
public class PerformanceTest {

    // Standard suit and rank ratio for a 52 deck of cards
    private static final double suitRatio = 4f/52f;

    private static final double rankRatio = 13f/52f;


    private BHLayout layout;
    private Random rand;

    @BeforeEach
    public void start() {
        rand = new Random();
    }


    /**
     * Used to generate a random layout with given input parameters
     * @param seed
     * @param numcards
     * @param ranks
     * @param suits
     * @param piles
     * @return a new randomised layout
     */
    public static BHLayout run(int seed, int numcards, int ranks, int suits, int piles) {
            BHLayout layout = new BHLayout(ranks, suits, piles);
            layout.randomise(seed, numcards);
            return layout;
    }

    /**
     * Runs the searching algorithm with different numbers of ranks in the deck
     * @param ranks
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 13, 26, 52})
    public void rankTest(int ranks) {
        int numcards = 52;
        int suits = numcards /ranks;
        layout = run(rand.nextInt(), numcards, ranks, suits, 10);

        Solver solver = new Solver(new BHLayout(layout), false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(ranks, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part2", "rank", ranks);
    }

    /**
     * Runs the searching algorithm with different numbers of suits in the deck
     * @param suits
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 13, 26, 52})
    public void suitTest(int suits) {
        int numcards = 52;
        int ranks = numcards /suits;
        layout = run(rand.nextInt(), numcards, ranks, suits, 10);

        Solver solver = new Solver(new BHLayout(layout), false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(suits, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part2", "suit", suits);
    }

    /**
     * Runs the searching algorithm with increasing number of cards in the deck
     * @param numcards
     */
    @ParameterizedTest
    @ValueSource(ints = {20, 30, 40, 52, 60, 70, 80, 90, 100, 200, 400, 600, 800, 900, 1000, 1500, 2000, 2500, 3000, 4000, 5000})
    public void deckSizeTest(int numcards) {
        int suits = (int)(numcards * suitRatio);
        int ranks = (int) (numcards * rankRatio);
        int newNum = suits * ranks;
        layout = run(rand.nextInt(), newNum, ranks, suits, 10);

        Solver solver = new Solver(new BHLayout(layout), false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(numcards, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part2", "decksize", newNum);

    }

    /**
     * Runs the searching algorithm with different numbers of piles in the puzzle
     * @param piles
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 5, 8, 9, 10, 15, 20, 22, 25, 28, 30, 32, 35, 38, 40, 42, 45, 50, 52})
    public void pileTest(int piles) {
        layout = run(rand.nextInt(), 52, 13, 4, piles);

        Solver solver = new Solver(new BHLayout(layout), false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(piles, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part2", "pile", piles);
    }

    /**
     * Runs the searching algorithm with different seeds for layout generation
     * @param seed
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80})
    public void seedTest(int seed) {
        layout = new BHLayout();
        layout.randomise(seed);

        Solver solver = new Solver(new BHLayout(layout), false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(seed, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part2", "seed", seed);
    }

    /**
     * Worm hole search test with different ranks
     * @param ranks
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 13, 26, 52})
    public void rankWhTest(int ranks) {
        int numcards = 52;
        int suits = numcards /ranks;
        layout = run(rand.nextInt(), numcards, ranks, suits, 10);

        Solver solver = new Solver(new WHLayout(layout), true, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(ranks, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part3", "rank", ranks);
    }

    /**
     * Worm hole search test with different suits
     * @param suits
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 13, 26, 52})
    public void suitWhTest(int suits) {
        int numcards = 52;
        int ranks = numcards /suits;
        layout = run(rand.nextInt(), numcards, ranks, suits, 10);

        Solver solver = new Solver(new WHLayout(layout), true, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(suits, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part3", "suit", suits);
    }

    /**
     * Worm hole search test with increasing deck size
     * @param numcards
     */
    @ParameterizedTest
    @ValueSource(ints = {20, 30, 40, 52, 60, 70, 80, 90, 100, 200, 400, 600, 800, 900, 1000, 1500, 2000, 2500, 3000, 4000, 5000})
    public void deckSizeWhTest(int numcards) {
        int suits = (int)(numcards * suitRatio);
        int ranks = (int) (numcards * rankRatio);
        int newNum = suits * ranks;
        layout = run(rand.nextInt(), newNum, ranks, suits, 10);

        Solver solver = new Solver(new WHLayout(layout), true, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(numcards, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part3", "decksize", newNum);

    }

    /**
     * Worm hole search test with increasing number of piles
     * @param piles
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 5, 8, 9, 10, 15, 20, 22, 25, 28, 30, 32, 35, 38, 40, 42, 45, 50, 52})
    public void pileWhTest(int piles) {
        layout = run(rand.nextInt(), 52, 13, 4, piles);

        Solver solver = new Solver(new WHLayout(layout), true, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(piles, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part3", "pile", piles);
    }

    /**
     * Worm hole search test with different seeds for layout generation
     * @param seed
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80})
    public void seedWhTest(int seed) {
        layout = new BHLayout();
        layout.randomise(seed);

        Solver solver = new Solver(new WHLayout(layout), true, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(seed, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "part3", "seed", seed);
    }











}
