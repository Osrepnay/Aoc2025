import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

record Point(int x, int y, int z) {
    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2));
    }
}

record Tuple2<A, B>(A a, B b) {
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/8"));
    List<Point> points = new ArrayList<>();
    while (s.hasNextLine()) {
        int[] parts = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
        points.add(new Point(parts[0], parts[1], parts[2]));
    }

    Map<Integer, List<Point>> pointsInCircuit = new HashMap<>();
    for (int i = 0; i < points.size(); i++) {
        pointsInCircuit.put(i, new ArrayList<>(List.of(points.get(i))));
    }
    Map<Point, Integer> partOfCircuit = new HashMap<>();
    for (int i = 0; i < points.size(); i++) {
        partOfCircuit.put(points.get(i), i);
    }
    Set<Tuple2<Integer, Integer>> encountered = new HashSet();
    outer:
    for (int i = 0; i < 1000; i++) {
        int closest1 = -1;
        int closest2 = -1;
        double closestDist = Integer.MAX_VALUE;
        Tuple2<Integer, Integer> idxTuple = null;
        boolean noop = false;
        for (int j = 0; j < points.size() - 1; j++) {
            Point point1 = points.get(j);
            for (int k = j + 1; k < points.size(); k++) {
                Point point2 = points.get(k);
                double dist = point1.distanceTo(point2);
                if (dist < closestDist && !encountered.contains(new Tuple2<>(j, k))) {
                    int point1Circuit = partOfCircuit.get(point1);
                    int point2Circuit = partOfCircuit.get(point2);
                    noop = point1Circuit == point2Circuit;
                    closestDist = dist;
                    closest1 = point1Circuit;
                    closest2 = point2Circuit;
                    idxTuple = new Tuple2<>(j, k);
                }
            }
        }
        encountered.add(idxTuple);
        if (!noop) {
            pointsInCircuit.get(closest1).addAll(pointsInCircuit.get(closest2));
            int finalClosest = closest1;
            pointsInCircuit.get(closest2).forEach(p -> partOfCircuit.put(p, finalClosest));
            pointsInCircuit.remove(closest2);
        }
    }

    long product = pointsInCircuit.entrySet()
            .stream()
            .mapToInt(e -> -e.getValue().size())
            .sorted()
            .limit(3)
            .mapToLong(l -> Math.abs(l))
            .reduce(1, (a, b) -> a * b);
    System.out.println(product);
}