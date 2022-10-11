package test;

import common.*;
import functions.Checker;
import functions.RecursiveSolver;
import functions.Solver;
import layouts.BHLayout;
import layouts.WHLayout;
import org.junit.jupiter.api.Test;
import types.Tuple;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testing suite for the Checker function
 *
 *
 */
public class CheckerTest extends SuperTest {

    /**
     * Tests that the Checker class correctly converts a list of tuples to integers
     */
    @Test
    public void unwrapLogTest() {
        ArrayList<Tuple> tupleList = new ArrayList<Tuple>();
        tupleList.add(new Tuple(BHLayout.ZERO, BHLayout.ONE));
        tupleList.add(new Tuple(BHLayout.ONE, BHLayout.ZERO));
        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(BHLayout.ZERO);
        integerList.add(BHLayout.ONE);
        integerList.add(BHLayout.ONE);
        integerList.add(BHLayout.ZERO);
        assertEquals(integerList, Checker.unwrapLog(tupleList));
    }

    /**
     * Checks that it correctly calculates to see if card ranks are adjacent to one another
     */
    @Test
    public void checkCardRankTest() {
        assertFalse(Checker.checkCardRank(new BHLayout(), 3));
        assertFalse(Checker.checkCardRank(new BHLayout(), 10));
        assertTrue(Checker.checkCardRank(new BHLayout(), 2));
        assertTrue(Checker.checkCardRank(new BHLayout(), 13));
        assertFalse(Checker.checkCardRank(new BHLayout(), 1));
    }

    /**
     * Checks for a given layout, if it correctly identifies when the piles are empty
     */
    @Test
    public void checkPilesEmptyTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhStandard));
        assertFalse(Checker.checkPilesEmpty(layout));
        layout = new BHLayout(BHMain.readIntArray(bhTrivial));
        assertTrue(Checker.checkPilesEmpty(layout));
    }

    /**
     * Checks to see if the Checker correctly verifies the top card of a pile
     */
    @Test
    public void checkTopCardTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhStandard));
        Checker checker = new Checker(layout, new ArrayList<Integer>(), false, true);
        assertFalse(checker.checkTopCard(layout, 0, 21));
        assertFalse(checker.checkTopCard(layout, 0, 47));
        assertTrue(checker.checkTopCard(layout, 0, 26));
        assertTrue(checker.checkTopCard(layout, 4, 21));

        WHLayout whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandard)));
        checker = new Checker(whLayout, new ArrayList<Integer>(), true, true);
        assertFalse(checker.checkTopCard(whLayout, 0, 21));
        assertFalse(checker.checkTopCard(whLayout, 0, 47));
        assertTrue(checker.checkTopCard(whLayout, 0, 26));
        assertFalse(checker.checkTopCard(whLayout, -1, -1));
        assertFalse(checker.checkTopCard(layout, 0, 26));
    }

    /**
     * Runs the checker alongside the solver
     */
    @Test
    public void checkRunTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhStandard));
        Solver solver = new Solver(new BHLayout(layout), false, false, true);
        solver.run();
        ArrayList<Integer> log = Checker.unwrapLog(solver.getSolution().log());
        Checker checker = new Checker(new BHLayout(layout), log, false, true);
        assertTrue(checker.run());

        WHLayout whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandard)));
        solver = new Solver(new WHLayout(whLayout), true, false, true);
        solver.run();
        ArrayList<Integer> newLog = Checker.unwrapLog(solver.getSolution().log());
        checker = new Checker(whLayout, newLog, true, true);
        assertTrue(checker.run());

    }

    /**
     * Runs the checker alongside the recursive solver
     */
    @Test
    public void checkRunRecursiveTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhComplete));
        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        solver.run();
        ArrayList<Integer> log = Checker.unwrapLog(solver.getSolution().log());
        Checker checker = new Checker(new BHLayout(layout), log, false, true);
        assertTrue(checker.run());

        WHLayout whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whComplete)));
        solver = new RecursiveSolver(new WHLayout(whLayout), true, false, true);
        solver.run();
        ArrayList<Integer> newLog = Checker.unwrapLog(solver.getSolution().log());
        checker = new Checker(whLayout, newLog, true, true);
        assertTrue(checker.run());
    }

}
