package test;

import common.*;

import functions.RecursiveSolver;
import functions.Solver;
import functions.Checker;
import layouts.BHLayout;

import layouts.WHLayout;
import org.junit.jupiter.api.Test;
import types.Node;
import types.Tuple;


import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Solver and Recursive Solver classes
 *
 */
public class SolverTest extends SuperTest {

    /**
     * Tests that a node is pushed to the stack on initialisation
     */
    @Test
    public void pushNodeTest() {
        layout = new BHLayout(BHMain.readIntArray(bhStandard));
        Solver solver = new Solver(layout, false, true, true);
        assertEquals(BHLayout.ONE, solver.getNodes().size());

    }

    /**
     * Tests that we can correctly create a new node after a step in the patience game
     */
    @Test
    public void createNewNodeTest() {
        layout = new BHLayout(BHMain.readIntArray(bhStandard));
        Node startNode = new Node(layout, new ArrayList<>());
        Solver solver = new Solver(layout, false, true, true);
        ArrayList<Tuple> newLog = new ArrayList<>();
        Tuple tuple = new Tuple(0, 26);
        newLog.add(tuple);
        BHLayout newLayout = new BHLayout(BHMain.readIntArray(bhStandardNode));
        Node correctNode = new Node(newLayout, newLog);

        Node newNode = solver.createNewNode(startNode, 0, 26, false);

        assertTrue(correctNode.equals(newNode));
    }

    /**
     * Tests that we can correctly create a new worm hole node after a step in the patience game
     */
    @Test
    public void createNewWormHoleNodeTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandard)));

        Node startNode = new Node(whLayout, new ArrayList<>());
        Solver solver = new Solver(whLayout, true, true, true);
        ArrayList<Tuple> newLog = new ArrayList<>();
        Tuple tuple = new Tuple(0, -26);
        newLog.add(tuple);
        BHLayout newLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        Node correctNode = new Node(newLayout, newLog);
        Node newNode = solver.createNewNode(startNode, 0, 26, true);
        assertEquals(newNode, correctNode);
    }

    /**
     * Tests findNodes cannot find any new ones when the piles are empty
     */
    @Test
    public void findNodeTest() {
        layout = new BHLayout(BHMain.readIntArray(bhTrivial));
        for (int i = 0; i < layout.numPiles(); i++) {
            assertFalse(Solver.findNode(layout, i));
        }
    }

    /**
     * Tests that we can correctly filter out already seen layouts using a HashSet
     */
    @Test
    public void filterTest() {
        HashSet<BHLayout> filter = new HashSet<>();
        filter.add(layout);
        BHLayout duplicate = new BHLayout();
        assertTrue(filter.contains(duplicate));
        BHLayout newLayout = new BHLayout(BHMain.readIntArray(bhStandard));
        assertFalse(filter.contains(newLayout));
        filter.add(newLayout);
        BHLayout newDuplicate = new BHLayout(BHMain.readIntArray(bhStandard));
        assertTrue(filter.contains(newDuplicate));

    }

    /**
     * Does the same as above but with a HashSet of worm hole layouts
     */
    @Test
    public void filterWormHoleTest() {
        HashSet<WHLayout> filter = new HashSet<>();
        filter.add(whLayout);
        WHLayout duplicate = new WHLayout(new BHLayout());
        assertTrue(filter.contains(duplicate));
        WHLayout newWhLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandard)));
        assertFalse(filter.contains(newWhLayout));
    }

    /**
     * Solves a worm hole puzzle completely, checking all solutions found with the checker
     */
    @Test
    public void completeSolverWormHoleTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whComplete)));
        Solver solver = new Solver(new WHLayout(whLayout), true, true, true, false, false);
        solver.run();
        ArrayList<Node> solutions = solver.getSolutions();
        Checker checker;
        for (int i = 0; i < solutions.size(); i++) {
            ArrayList<Integer> log = Checker.unwrapLog(solutions.get(i).log());
            checker = new Checker(new WHLayout(whLayout), log, true, true);
            assertTrue(checker.run());
        }
    }

    /**
     * Solves a black hole puzzle completely checking all solutions found with the checker
     */
    @Test
    public void completeSolverBlackHoleTest() {
        layout = new BHLayout(BHMain.readIntArray(bhComplete));
        Solver solver = new Solver(new BHLayout(layout), false, true, true);
        solver.run();
        ArrayList<Node> solutions = solver.getSolutions();
        Checker checker;
        for (int i = 0; i < solutions.size(); i++) {
            ArrayList<Integer> log = Checker.unwrapLog(solutions.get(i).log());
            checker = new Checker(new BHLayout(layout), log, false, true);
            assertTrue(checker.run());
        }
    }

    /**
     * Solves a worm hole puzzle completely using recursion
     */
    @Test
    public void completeSolverWormHoleRecursiveTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whComplete)));
        RecursiveSolver solver = new RecursiveSolver(new WHLayout(whLayout), true, true, true);
        solver.run();
        ArrayList<Node> solutions = solver.getSolutions();
        Checker checker;
        for (int i = 0; i < solutions.size(); i++) {
            ArrayList<Integer> log = Checker.unwrapLog(solutions.get(i).log());
            checker = new Checker(new WHLayout(whLayout), log, true, true);
            assertTrue(checker.run());
        }
    }

    /**
     * Solves a black hole puzzle completely using recursion
     */
    @Test
    public void completeSolverBlackHoleRecursiveTest() {
        layout = new BHLayout(BHMain.readIntArray(bhComplete));
        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, true, true);
        solver.run();
        ArrayList<Node> solutions = solver.getSolutions();
        Checker checker;
        for (int i = 0; i < solutions.size(); i++) {
            ArrayList<Integer> log = Checker.unwrapLog(solutions.get(i).log());
            checker = new Checker(new BHLayout(layout), log, false, true);
            assertTrue(checker.run());
        }
    }

    /**
     * Runs the recursive solver with a worm hole puzzle layout
     * The solver should not be able to retrieve a solution for this case
     */
    @Test
    public void ImpossibleRecursiveTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(whComplete));
        RecursiveSolver solver = new RecursiveSolver(new BHLayout(layout), false, false, true);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout including a card larger than the deck size
     */
    @Test
    public void ImpossibleTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs));
        Solver solver = new Solver(new BHLayout(layout), false, false, true);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout including a negatively ranked card
     */
    @Test
    public void ImpossibleTest2() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs2));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout including a negatively ranked card
     */
    @Test
    public void ImpossibleTest3() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs3));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout which has a negative number of piles so it defaults to 17
     */
    @Test
    public void ImpossibleTest4() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs4));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout which has a negative number of suits
     */
    @Test
    public void ImpossibleTest5() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs5));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout which has a negative number of ranks
     */
    @Test
    public void ImpossibleTest6() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhImposs6));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertFalse(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout with no piles present which is another trivial situation
     */
    @Test
    public void SolutionTest() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhPoss1));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertTrue(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout that has no piles and no pile number - trivial
     */
    @Test
    public void SolutionTest2() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhPoss2));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertTrue(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout that has no piles, pile number or suit value - trivial
     */
    @Test
    public void SolutionTest3() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhPoss3));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertTrue(solver.run());
    }

    /**
     * Runs the solver with a puzzle layout that has no piles, pile number, suit value or rank - trivial
     */
    @Test
    public void SolutionTest4() {
        BHLayout layout = new BHLayout(BHMain.readIntArray(bhPoss4));
        Solver solver = new Solver(new BHLayout(layout), false, false, true, false, false);
        assertTrue(solver.run());
    }


}
