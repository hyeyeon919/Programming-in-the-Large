package srg.ports;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {
    @Test
    public void constructorPosition() {
        Position position = new Position(0,0,0);
        assertEquals(position.x,0);
        assertEquals(position.y,0);
        assertEquals(position.z,0);
    }
    @Test
    public void distanceToTest() {
        Position position = new Position(0,0,0);
        assertEquals(position.distanceTo(new Position(3,3,3)),5);
    }
    @Test
    public void toStringTest() {
        Position position = new Position(0,0,0);
        assertEquals(position.toString(),"(0, 0, 0)");
    }
}
