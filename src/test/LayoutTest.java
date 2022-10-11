package test;

import common.BHMain;
import layouts.BHLayout;
import layouts.WHLayout;
import org.junit.jupiter.api.Test;
import types.Node;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Performs a series of tests on the BHLayout and WHLayout classes
 *
 */
public class LayoutTest extends SuperTest {

    /**
     * Tests for a simple not null on initialisation
     */
    @Test
    public void notNull() {
        assertNotNull(whLayout);
        assertNotNull(layout);
    }

    /**
     * Tests that it returns the correct max rank for a layout
     */
    @Test
    public void maxRankTest() {
        assertEquals(layout.numRanks(), 13);
        layout = new BHLayout(0, 0, 0);
        assertEquals(BHLayout.ZERO, layout.numRanks());
    }

    /**
     * Tests that we can correctly update the hole card of a layout
     */
    @Test
    public void setHoleTest() {
        assertEquals(BHLayout.ONE, layout.setHole(27));
        assertEquals(layout.holeCard(),27);
        assertEquals(BHLayout.ERR, layout.setHole(-10));
        assertEquals(layout.holeCard(), 27);
    }

    /**
     * Tests that we retrieve the correct rank when calling getCardRank()
     */
    @Test
    public void getCardRankTest() {
        assertEquals(BHLayout.ONE, layout.getCardRank(27));
        assertEquals(BHLayout.ERR, layout.getCardRank(BHLayout.ERR));
        assertEquals(12, layout.getCardRank(38));
        layout = new BHLayout(BHMain.readIntArray(bhCardRank));
        assertEquals(7, layout.getCardRank(27));
    }

    /**
     * Tests that we can correctly remove the top card from a given pile
     * and when we have removed all cards from a pile
     */
    @Test
    public void removeTopCardTest() {
        layout.randomise(rand.nextInt());
        for (int i = 0; i < layout.numPiles(); i++) {
            int size = layout.pileSize(i);
            assertEquals(BHLayout.ONE, layout.removeTopCard(i));
            assertEquals(layout.pileSize(i), size - 1);
        }
        int size = layout.pileSize(0);
        while (size > 0) {
            layout.removeTopCard(0);
            size = layout.pileSize(0);
        }
        assertEquals(BHLayout.ERR, layout.removeTopCard(0));
    }

    /**
     * Tests the overridden equality behaviour of the BHLayout class
     */
    @Test
    public void equalsTest() {
        assertTrue(layout.equals(new BHLayout()));
        BHLayout newLayout = new BHLayout();
        newLayout.randomise(rand.nextInt());
        assertFalse(layout.equals(newLayout));
        layout = new BHLayout(BHMain.readIntArray(bhCardRank));
        newLayout = new BHLayout(BHMain.readIntArray(bhCardRank));

        assertTrue(newLayout.equals(layout));
    }
    /**
     * Tests the overridden equality behaviour of the WHLayout class
     */
    @Test
    public void equalsTest2() {
        assertTrue(whLayout.equals(new WHLayout(new BHLayout())));
        WHLayout unequal = new WHLayout(new BHLayout(), 2);
        assertFalse(whLayout.equals(unequal));
        WHLayout newLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        assertFalse(newLayout.equals(unequal));
        unequal = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        assertTrue(unequal.equals(newLayout));

    }

    /**
     * Tests that we cannot apply our equals behaviour to two different types of layouts
     * However we can when we treat WHLayout as an object of its parent
     */
    @Test
    public void equalsTest3() {
        BHLayout layout = new BHLayout();
        WHLayout whLayout = new WHLayout(new BHLayout());
        assertFalse(whLayout.equals(layout));
        assertTrue(layout.equals(whLayout));
    }

    /**
     * Tests that we can correctly retrieve the top card from a given pile
     */
    @Test
    public void topCardTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        assertEquals(26, whLayout.topCard(BHLayout.ERR));
        assertEquals(47, whLayout.topCard(BHLayout.ZERO));

    }

    /**
     * Tests that we can update the worm hole card of a worm hole layout
     */
    @Test
    public void setWormHoleTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandard)));
        assertEquals(BHLayout.ONE, whLayout.setWormHole(26));
        assertEquals(26, whLayout.wormHole());
        assertEquals(BHLayout.ERR, whLayout.setWormHole(1));
        assertEquals(26, whLayout.wormHole());

    }

    /**
     * Tests that we can remove the top card of the worm hole in a worm hole layout
     */
    @Test
    public void removeTopCardWormHoleTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        assertEquals(BHLayout.ONE, whLayout.removeTopCard(BHLayout.ERR));
        assertEquals(BHLayout.ERR, whLayout.wormHole());
        assertEquals(BHLayout.ERR, whLayout.wormHole());


    }

    @Test
    public void nodeEqualsTest() {
        whLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 26);
        WHLayout newWhLayout = new WHLayout(new BHLayout(BHMain.readIntArray(whStandardNode)), 1);
        Node node = new Node(whLayout);
        Node newNode = new Node(newWhLayout);
        assertFalse(node.equals(newNode));


    }

}
