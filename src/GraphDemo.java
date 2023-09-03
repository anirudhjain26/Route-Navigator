/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * To do: Add your name(s) as authors
 * @author Anirudh Jain
 * @author Swarajh Mehta
 */ 

import java.io.FileInputStream;
import java.util.*;

public class GraphDemo {
    public static void main(String[] args) throws Exception {
        
        Scanner scanner = new Scanner(System.in);
        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream("data/usa.graph"));
        
        System.out.println("Enter your first city and state abbreviation(e.g., Durham NC):");
        String firstcity = scanner.nextLine();
        double[] d = findInCSV(new FileInputStream("data/uscities.csv"),firstcity);
        Point firstCity = new Point(d[0],d[1]);
        
        System.out.println("\nEnter your second city and state abbreviation(e.g., Durham NC):");
        String secondcity = scanner.nextLine();
        
        d = findInCSV(new FileInputStream("data/uscities.csv"),secondcity);
        Point secondCity = new Point(d[0],d[1]);
        long startTime = System.nanoTime();
        
        Point closestToFirstCity = gp.nearestPoint(firstCity);
        Point closestToSecondCity = gp.nearestPoint(secondCity);
        
        List<Point> route = gp.route(closestToFirstCity,closestToSecondCity);
        double rd = gp.routeDistance(route);
        long endTime = System.nanoTime();
        
        System.out.printf("Nearest point to %s is (%.6f,%.6f)%n",firstcity,closestToFirstCity.getLat(),closestToFirstCity.getLon());
        System.out.printf("Nearest point to %s is (%.6f,%.6f)%n",secondcity,closestToSecondCity.getLat(),closestToSecondCity.getLon());
        System.out.printf("Route between (%.6f,%.6f) and (%.6f,%.6f) is %.2fmiles%n",closestToFirstCity.getLat(),closestToFirstCity.getLon(),closestToSecondCity.getLat(),closestToSecondCity.getLon(),rd);
        System.out.printf("Total time to get nearest points, route, and get distance: %.2fms %n", (endTime - startTime)/1e6);
        
        Visualize visualize = new Visualize("data/usa.vis","images/usa.png");
        
        visualize.drawPoint(firstCity);
        visualize.drawPoint(secondCity);
        visualize.drawRoute(route);
    }
    public static double[] findInCSV(FileInputStream file, String city) throws Exception{
        double[] d = new double[2];
        Scanner s = new Scanner(file);
        
        while (s.hasNextLine()) {
            String[] line = s.nextLine().split(",");
            if (line[0].toLowerCase().equals(city.split(" ")[0].toLowerCase()) && line[1].toLowerCase().equals(city.split(" ")[1].toLowerCase())) {
                d[0] = Double.parseDouble(line[2]);
                d[1] = Double.parseDouble(line[3]);
                return d;
            }
        }
        throw new Exception("City not found");
    }

}