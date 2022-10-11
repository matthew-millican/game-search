package performance;

import functions.RecursiveSolver;
import functions.Writer;
import layouts.BHLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

/**
 * Runs and stores the results for different searches using a recursive approach
 */
public class RecursivePerformanceTest {

    private static final double suitRatio = 4f/52f;

    private static final double rankRatio = 13f/52f;


    private BHLayout layout;
    private Random rand;

    @BeforeEach
    public void start() {
        rand = new Random();
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
        layout = PerformanceTest.run(rand.nextInt(), numcards, ranks, suits, 10);

        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(ranks, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "recursive-part2", "rank", ranks);
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
        layout = PerformanceTest.run(rand.nextInt(), numcards, ranks, suits, 10);

        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(suits, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "recursive-part2", "suit", suits);
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
        layout = PerformanceTest.run(rand.nextInt(), newNum, ranks, suits, 10);

        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(numcards, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "recursive-part2", "decksize", newNum);

    }

    /**
     * Runs the searching algorithm with different numbers of piles in the puzzle
     * @param piles
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 5, 8, 9, 10, 15, 20, 22, 25, 28, 30, 32, 35, 38, 40, 42, 45, 50, 52})
    public void pileTest(int piles) {
        layout = PerformanceTest.run(rand.nextInt(), 52, 13, 4, piles);

        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(piles, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "recursive-part2", "pile", piles);
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

        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();

        Writer writer = new Writer();
        writer.run(Writer.format(seed, solver.getElapsedTime(), solver.getNodesVisited(), solver.getSolution() != null), "recursive-part2", "seed", seed);
    }
}
