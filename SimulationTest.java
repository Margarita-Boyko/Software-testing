package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import uk.ac.ed.inf.ilp.data.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationTest extends TestCase {

    public static void main(String[] args) {
        // Define the base URL
        String baseUrl = "https://ilp-rest-2024.azurewebsites.net";

        // Get today's date and format it
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        // Loop through the date range
        LocalDate currentDate = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!currentDate.isAfter(endDate)) {
            String date = currentDate.format(formatter);
            System.out.println("Running App with date: " + date);

            // Measure start time
            long startTime = System.nanoTime();

            // Call App.main with the current date and base URL
            try {
                App.main(new String[]{date, baseUrl});
            } catch (Exception e) {
                System.err.println("Error running App with date: " + date);
                e.printStackTrace();
            }

            // Measure end time and calculate duration
            long endTime = System.nanoTime();
            long durationInMillis = (endTime - startTime) / 1_000_000; // Convert to milliseconds

            // Print execution time
            System.out.println("Execution time for " + date + ": " + durationInMillis + " ms");

            // Move to the next date
            currentDate = currentDate.plusDays(1);
        }
    }





    public void testPathfindingWithStandardNoFlyZones() throws IOException {
        Orders[] orders = new Orders[]{new Orders("1234",
                LocalDate.now(),
                OrdersStatus.VALID,
                OrderValidationCodes.UNDEFINED,
                110,
                new Pizza[]{new Pizza("Margarita", 10)},
                null)};
        // Create the no-fly zones as NamedRegion objects
        NamedRegion[] noFlyZones = new NamedRegion[]{
                new NamedRegion("George Square Area", new LngLat[]{
                        new LngLat(-3.190578818321228, 55.94402412577528),
                        new LngLat(-3.1899887323379517, 55.94284650540911),
                        new LngLat(-3.187097311019897, 55.94328811724263),
                        new LngLat(-3.187682032585144, 55.944477740393744),
                        new LngLat(-3.190578818321228, 55.94402412577528)
                }),
                new NamedRegion("Dr Elsie Inglis Quadrangle", new LngLat[]{
                        new LngLat(-3.1907182931900024, 55.94519570234043),
                        new LngLat(-3.1906163692474365, 55.94498241796357),
                        new LngLat(-3.1900262832641597, 55.94507554227258),
                        new LngLat(-3.190133571624756, 55.94529783810495),
                        new LngLat(-3.1907182931900024, 55.94519570234043)
                }),
                new NamedRegion("Bristo Square Open Area", new LngLat[]{
                        new LngLat(-3.189543485641479, 55.94552313663306),
                        new LngLat(-3.189382553100586, 55.94553214854692),
                        new LngLat(-3.189259171485901, 55.94544803726933),
                        new LngLat(-3.1892001628875732, 55.94533688994374),
                        new LngLat(-3.189194798469543, 55.94519570234043),
                        new LngLat(-3.189135789871216, 55.94511759833873),
                        new LngLat(-3.188138008117676, 55.9452738061846),
                        new LngLat(-3.1885510683059692, 55.946105902745614),
                        new LngLat(-3.1895381212234497, 55.94555918427592),
                        new LngLat(-3.189543485641479, 55.94552313663306)
                }),
                new NamedRegion("Bayes Central Area", new LngLat[]{
                        new LngLat(-3.1876927614212036, 55.94520696732767),
                        new LngLat(-3.187555968761444, 55.9449621408666),
                        new LngLat(-3.186981976032257, 55.94505676722831),
                        new LngLat(-3.1872327625751495, 55.94536993377657),
                        new LngLat(-3.1874459981918335, 55.9453361389472),
                        new LngLat(-3.1873735785484314, 55.94519344934259),
                        new LngLat(-3.1875935196876526, 55.94515665035927),
                        new LngLat(-3.187624365091324, 55.94521973430925),
                        new LngLat(-3.1876927614212036, 55.94520696732767)
                })
        };

        // Create a NamedRegion for the central area
        NamedRegion centralArea = new NamedRegion("central", new LngLat[]{
                new LngLat(-3.192, 55.942),
                new LngLat(-3.192, 55.947),
                new LngLat(-3.185, 55.947),
                new LngLat(-3.185, 55.942),
                new LngLat(-3.192, 55.942)
        });

        // Create the pathfinding solver
        AStarFlightPathSolver solver = new AStarFlightPathSolver();

        long startTime = System.nanoTime();
        // Set up start and end points for the delivery
        //CoordinatePoint startPoint = new CoordinatePoint(-3.186874, 55.944494); // Appleton Tower
        CoordinatePoint endPoint = new CoordinatePoint(-3.1838572025299072, 55.94449876875712); // Dominos pizza

        // Perform the pathfinding
        var path = solver.findShortestPath(endPoint, noFlyZones, centralArea);

        // Assert that the path is valid and avoids no-fly zones
        assertNotNull(path);
        assertTrue(path.size() > 0);
        for (CoordinatePoint point : path) {
            for (NamedRegion noFlyZone : noFlyZones) {
                assertFalse(new LngLatHandler().isInRegion(point.lngLat, noFlyZone));
            }
        }

        JsonFileCreator fileCreator = new JsonFileCreator();
        List<FlightPath> flightPaths = new ArrayList<>();
        for (Orders order: orders) {
            flightPaths.add(new FlightPath(order, path));
        }
        fileCreator.createFiles("2025-01-16", orders, flightPaths);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        // Log the runtime
        System.out.println("Overall runtime: " + durationInMillis + " ms");

    }

    public void testPathfindingWithNoNoFlyZones() throws IOException {
        Orders[] orders = new Orders[]{new Orders("1234",
                LocalDate.now(),
                OrdersStatus.VALID,
                OrderValidationCodes.UNDEFINED,
                110,
                new Pizza[]{new Pizza("Margarita", 10)},
                null)};
        // Create the no-fly zones as NamedRegion objects
        NamedRegion[] noFlyZones = new NamedRegion[0];

        // Create a NamedRegion for the central area
        NamedRegion centralArea = new NamedRegion("central", new LngLat[]{
                new LngLat(-3.192, 55.942),
                new LngLat(-3.192, 55.947),
                new LngLat(-3.185, 55.947),
                new LngLat(-3.185, 55.942),
                new LngLat(-3.192, 55.942)
        });

        // Create the pathfinding solver
        AStarFlightPathSolver solver = new AStarFlightPathSolver();

        // Set up start and end points for the delivery
        //CoordinatePoint startPoint = new CoordinatePoint(-3.186874, 55.944494); // Appleton Tower
        CoordinatePoint endPoint = new CoordinatePoint(-3.1838572025299072, 55.94449876875712); // Near Bayes Central Area

        // Perform the pathfinding
        var path = solver.findShortestPath(endPoint, noFlyZones, centralArea);

        // Assert that the path is valid and avoids no-fly zones
        assertNotNull(path);
        assertTrue(path.size() > 0);
        for (CoordinatePoint point : path) {
            for (NamedRegion noFlyZone : noFlyZones) {
                assertFalse(new LngLatHandler().isInRegion(point.lngLat, noFlyZone));
            }
        }

        JsonFileCreator fileCreator = new JsonFileCreator();
        List<FlightPath> flightPaths = new ArrayList<>();
        for (Orders order: orders) {
            flightPaths.add(new FlightPath(order, path));
        }
        fileCreator.createFiles("2025-01-13", orders, flightPaths);

    }

    public void testPathfindingWithLargeNoFlyZones() throws IOException {
        Orders[] orders = new Orders[]{new Orders("1234",
                LocalDate.now(),
                OrdersStatus.VALID,
                OrderValidationCodes.UNDEFINED,
                110,
                new Pizza[]{new Pizza("Margarita", 10)},
                null)};
        // Generate a large number of no-fly zones
        NamedRegion[] noFlyZones = generateLargeNoFlyZones(0);

        // Save no-fly zones as a GeoJSON file
        saveNoFlyZonesAsGeoJson("2025-01-12-no-fly-zones.geojson", noFlyZones);

        // Define the central area
        NamedRegion centralArea = new NamedRegion("central", new LngLat[]{
                new LngLat(-3.192, 55.942),
                new LngLat(-3.192, 55.947),
                new LngLat(-3.185, 55.947),
                new LngLat(-3.185, 55.942),
                new LngLat(-3.192, 55.942)
        });

        // Create the pathfinding solver
        AStarFlightPathSolver solver = new AStarFlightPathSolver();

        // Set up start and end points
        CoordinatePoint endPoint = new CoordinatePoint(-3.1838572025299072, 55.94449876875712); // Near Bayes Central Area

        long startTime = System.nanoTime();

        // Perform the pathfinding
        List<CoordinatePoint> path = solver.findShortestPath(endPoint, noFlyZones, centralArea);

        // Assertions
        assertNotNull("Path should not be null", path);

        JsonFileCreator fileCreator = new JsonFileCreator();
        List<FlightPath> flightPaths = new ArrayList<>();
        for (Orders order: orders) {
            flightPaths.add(new FlightPath(order, path));
        }
        fileCreator.createFiles("2025-01-12", orders, flightPaths);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        // Log the runtime
        System.out.println("Overall runtime: " + durationInMillis + " ms");
    }

    /**
     * Generates a large number of random no-fly zones for testing.
     * @param count the number of no-fly zones to generate
     * @return an array of NamedRegion objects representing no-fly zones
     */
    private NamedRegion[] generateLargeNoFlyZones(int count) {
        NamedRegion[] noFlyZones = new NamedRegion[count];
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            // Generate a random central point for the no-fly zone within an adjusted bounding box
            double centerLng = -3.19 + (random.nextDouble() * 0.01); // Adjusted longitude range to be more to the left
            double centerLat = 55.943 + (random.nextDouble() * 0.01); // Adjusted latitude range to be lower

            // Generate a polygon with vertices larger and properly aligned to form a square
            LngLat[] vertices = new LngLat[5];
            double size = 0.002; // Increased size for larger zones

            // Define vertices around the central point
            vertices[0] = new LngLat(centerLng - size / 2, centerLat - size / 2); // Bottom-left
            vertices[1] = new LngLat(centerLng + size / 2, centerLat - size / 2); // Bottom-right
            vertices[2] = new LngLat(centerLng + size / 2, centerLat + size / 2); // Top-right
            vertices[3] = new LngLat(centerLng - size / 2, centerLat + size / 2); // Top-left
            vertices[4] = vertices[0]; // Close the polygon by repeating the first vertex

            // Create the NamedRegion
            noFlyZones[i] = new NamedRegion("NoFlyZone-" + i, vertices);
        }
        return noFlyZones;
    }


    /**
     * Saves the no-fly zones as a GeoJSON file for visualization.
     * @param fileName the name of the output GeoJSON file
     * @param noFlyZones the array of NamedRegion objects representing no-fly zones
     * @throws IOException If an I/O error occurs while writing the GeoJSON file
     */
    private void saveNoFlyZonesAsGeoJson(String fileName, NamedRegion[] noFlyZones) throws IOException {
        GeoJSONFeatureCollection featureCollection = new GeoJSONFeatureCollection();
        featureCollection.setType("FeatureCollection");

        List<GeoJSONFeature> features = new ArrayList<>();
        for (NamedRegion noFlyZone : noFlyZones) {
            GeoJSONFeature feature = new GeoJSONFeature();
            feature.setType("Feature");

            GeoJSONGeometry geometry = new GeoJSONGeometry();
            geometry.setType("Polygon");

            // Correctly initialize and build the coordinates structure
            List<List<List<Double>>> coordinates = new ArrayList<>(); // Outer array for polygon rings
            List<List<Double>> polygon = new ArrayList<>(); // Single ring

            for (LngLat vertex : noFlyZone.vertices()) {
                List<Double> point = new ArrayList<>();
                point.add(vertex.lng());
                point.add(vertex.lat());
                polygon.add(point);
//                polygon.add(point);
            }
            coordinates.add(polygon); // Wrap the polygon (ring) into the outer array
//            geometry.setCoordinates(coordinates); // Pass the outer array


            geometry.setCoordinates(coordinates); // GeoJSON Polygon expects a List<List<List<Double>>>

            feature.setGeometry(geometry);

            // Add the name of the no-fly zone as a property (if required)
            feature.setProperties(Map.of("name", noFlyZone.name()));

            features.add(feature);
        }

        featureCollection.setFeatures(features);

        ObjectMapper mapper = new ObjectMapper();
        String geoJson = mapper.writeValueAsString(featureCollection);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(geoJson);
        }
    }


}
