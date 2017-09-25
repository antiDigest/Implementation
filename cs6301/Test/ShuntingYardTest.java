package cs6301.Test;

import cs6301.g1025.ShuntingYard;
import junit.framework.TestCase;

public class ShuntingYardTest extends TestCase {
    public void testCheckPostfix() throws Exception {
        assertEquals(true, ShuntingYard.checkPostfix("y1*"));
        assertEquals(false, ShuntingYard.checkPostfix("y*1"));
    }

}