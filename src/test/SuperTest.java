package test;

import layouts.BHLayout;


import org.junit.jupiter.api.BeforeEach;
import layouts.WHLayout;

import java.io.File;
import java.util.Random;


/**
 * Abstract class used during testing including key attributes for the subclasses
 *
 *
 * Coverage Report:
 *
 * Checker -> 88% lines
 *
 * Recursive Solver -> 85% lines
 *
 * Solver -> 82% lines
 *
 * BHLayout -> 92% lines
 *
 * WHLayout -> 100% lines
 *
 * Node -> 96% lines
 *
 * Tuple -> 90% lines
 */
public abstract class SuperTest {

    // Puzzle layouts
    protected BHLayout layout;

    protected WHLayout whLayout;

    // Random object used for generating seeds in randomising puzzle layouts
    protected Random rand;


    // File separator
    static final String s = File.separator;

    // File paths for reading game puzzles
    static final String bhTrivial = "."+s+"files"+s+"BlackHole"+s+"standard.trivial.txt";

    static final String bhStandard = "."+s+"files"+s+"BlackHole"+s+"standard.1.txt";

    static final String whStandard = "."+s+"files"+s+"WormHole"+s+"standard.1.txt";

    static final String whStandardNode = "."+s+"files"+s+"WormHole"+s+"standard.1.node.txt";

    static final String bhStandardNode = "."+s+"files"+s+"BlackHole"+s+"standard.1.node.txt";

    static final String whComplete = "."+s+"files"+s+"WormHole"+s+"51-26-9-3-6.txt";

    static final String bhComplete = "."+s+"files"+s+"BlackHole"+s+"951-26-9-3-10.txt";

    static final String bhCardRank = "."+s+"files"+s+"BlackHole"+s+"1-39-10-4-8.txt";

    static final String bhImposs = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.txt";

    static final String bhImposs2 = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.2.txt";

    static final String bhImposs3 = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.3.txt";

    static final String bhImposs4 = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.4.txt";

    static final String bhImposs5 = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.5.txt";

    static final String bhImposs6 = "."+s+"files"+s+"BlackHole"+s+"standard.imposs.6.txt";

    static final String bhPoss1 = "."+s+"files"+s+"BlackHole"+s+"standard.poss.4.txt";

    static final String bhPoss2 = "."+s+"files"+s+"BlackHole"+s+"standard.poss.5.txt";

    static final String bhPoss3 = "."+s+"files"+s+"BlackHole"+s+"standard.poss.6.txt";

    static final String bhPoss4 = "."+s+"files"+s+"BlackHole"+s+"standard.poss.7.txt";


    /**
     * Before each test, initialise the layouts and random object
     */
    @BeforeEach
    protected void start() {
        layout = new BHLayout();
        whLayout = new WHLayout(layout);
        rand = new Random();
    }
}
