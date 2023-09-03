import java.security.InvalidAlgorithmParameterException;
import java.util.*;
import java.io.*;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 * @author Swarajh Mehta
 * @author Anirudh Jain
 */
public class GraphProcessor {

    private Map<String, Point> labMap    = new HashMap<>();
    private Map<Point, Set<Point>> adjMap = new HashMap<>();

    public void initialize(FileInputStream file) throws Exception {

        Scanner uwu = new Scanner(file);

        int Vertices = 0;
        int Edges    = 0;

        if (uwu.hasNextInt()){
            Vertices = uwu.nextInt();
        } else {
            uwu.close();
            throw new Exception("file formatting error");
        }

        if (uwu.hasNextInt()){
            Edges = uwu.nextInt(); 
        } else {
            uwu.close();
            throw new Exception("file formatting error");
        }
        uwu.nextLine();

        Map<Integer, Point> indMap = new HashMap<>();

        for (int i = 0; i < Vertices; i++){
            String[] vertexInfo = uwu.nextLine().split(" ");
            String info = vertexInfo[0];
            Double lat = Double.parseDouble(vertexInfo[1]);
            Double lon = Double.parseDouble(vertexInfo[2]);
            Point vertex = new Point(lat,lon);

            labMap.put(info, vertex);
            adjMap.put(vertex, new HashSet<Point>());
            indMap.put(i, vertex);
        }

        for (int n = 0; n < Edges; n++) {
            String[] oodges = uwu.nextLine().split(" ");
            int beg = Integer.parseInt(oodges[0]);
            int end = Integer.parseInt(oodges[1]);

            adjMap.get(indMap.get(beg)).add(indMap.get(end));
            adjMap.get(indMap.get(end)).add(indMap.get(beg));
        }
        uwu.close();
    }


    public Point nearestPoint(Point p) {
        double ndist = -1;
        Point npoint = null;

        for (Point i : adjMap.keySet()) {
            if (ndist == -1) {
                ndist = p.distance(i);
                npoint = i;
                continue;
            }

            double temp = p.distance(i);
            if (temp < ndist) {
                ndist = temp;
                npoint = i;
            }
        }
        return npoint;
    }

    public double routeDistance(List<Point> route) {
        double total = 0;
        for (int u = 0; u < route.size()-1; u++) {
            total = total + route.get(u).distance(route.get(u+1));
        }
        return total;
    }
    

    public boolean connected(Point p1, Point p2) {
        if (!adjMap.containsKey(p1) || !adjMap.containsKey(p2)) return false;
        return dfs(p1, p2);
    }

    public List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        if (!connected(start, end) || start.equals(end)) {
            throw new InvalidAlgorithmParameterException("No possible route found");
        } 
        List<Point> ans = new LinkedList<>();
        Map<Point,Point> previous = new HashMap<>(dijkstra(start));
        Point current = end;
        ans.add(current);
        while (!current.equals(start)) {
            current = previous.get(current);
            ans.add(0, current);
        }
        return ans;
    }

    //Helper Methods:
    private boolean dfs(Point st, Point fin){
        Set<Point> visited = new HashSet<>();
        Stack<Point> toExplore = new Stack<>();
        Point current = st;

        toExplore.add(current);
        while (!toExplore.isEmpty()) {
            current = toExplore.pop();
            for (Point neighbour : adjMap.get(current)) {
                if (neighbour.equals(fin)){
                    return true;
                }
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    toExplore.push(neighbour);
                }
            }
        }
        return false;
    }

    private Map<Point, Point> dijkstra (Point start) {
        Map<Point, Point> last = new HashMap<>();
        Map<Point, Double> dist = new HashMap<>();

        Comparator<Point> comp = (a, b) -> (int) (dist.get(a) - dist.get(b));
        PriorityQueue<Point> toExplore = new PriorityQueue<>(comp);

        Point current = start;
        dist.put(current, 0.0);
        toExplore.add(current);

        while (!toExplore.isEmpty()){
            current = toExplore.remove();
            for (Point neighbour : adjMap.get(current)) {
                double weight = current.distance(neighbour);
                if (!dist.containsKey(neighbour) || dist.get(neighbour) > dist.get(current) + weight) {
                    dist.put(neighbour, dist.get(current) + weight);
                    last.put(neighbour, current);
                    toExplore.add(neighbour);
                }
            }
        }
        return last;
    }
    
}