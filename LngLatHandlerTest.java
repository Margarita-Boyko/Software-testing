package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

public class LngLatHandlerTest extends TestCase {

    public void testDistanceTo(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(2,5);
        LngLat endPosition = new LngLat(5,9);
        assertEquals(5.0, lngLatHandler.distanceTo(startPosition, endPosition));
    }

    public void testIsCloseTo(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(2.00002,2.0001);
        LngLat endPosition = new LngLat(2.000001,2.00001);
        assertTrue(lngLatHandler.isCloseTo(startPosition, endPosition));
    }

    public void testIsInRegion(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.185, 55.944);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertTrue(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testIsNotInRegion(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-4.185, 55.944);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertFalse(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testIsInRegionVertex(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.192473, 55.943);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertTrue(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testIsInRegionBoundary(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.192473, 55.943);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertTrue(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testIsInRegionNoVertices(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.192473, 55.943);
        LngLat[] vertices = new LngLat[]{};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertFalse(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testNextPosition0DegEast(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(5, 1);
        LngLat nextPosition = lngLatHandler.nextPosition(startPosition,0.0);
        assertEquals(5.00015, nextPosition.lng() );
        assertEquals(1.0, nextPosition.lat() );
    }

    public void testNextPosition90DegNorth(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(5, 1);
        LngLat nextPosition = lngLatHandler.nextPosition(startPosition,90.0);
        assertEquals(5.0, nextPosition.lng() );
        assertEquals(1.00015, nextPosition.lat() );
    }

    public void testNextPosition180DegWest(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(5, 1);
        LngLat nextPosition = lngLatHandler.nextPosition(startPosition,180.0);
        assertEquals(4.99985, nextPosition.lng() );
        assertEquals(1.0, nextPosition.lat() );
    }

    public void testNextPosition270DegSouth(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(5, 1);
        LngLat nextPosition = lngLatHandler.nextPosition(startPosition,270.0);
        assertEquals(5.0, nextPosition.lng() );
        assertEquals(0.99985, nextPosition.lat() );
    }

    public void testNextPosition999Hover(){
        var lngLatHandler = new LngLatHandler();
        LngLat startPosition = new LngLat(5, 1);
        LngLat nextPosition = lngLatHandler.nextPosition(startPosition,999.0);
        assertEquals(5.0, nextPosition.lng() );
        assertEquals(1.0, nextPosition.lat() );
    }
}
