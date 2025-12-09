import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

record Point(int x, int y) {
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/9"));
    List<Point> points = new ArrayList<>();
    while (s.hasNextLine()) {
        int[] parts = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
        points.add(new Point(parts[0], parts[1]));
    }

    long largest = Long.MIN_VALUE;
    for (int i = 0; i < points.size() - 1; i++) {
        for (int j = i + 1; j < points.size(); j++) {
            Point p1 = points.get(i);
            Point p2 = points.get(j);
            largest = Math.max(largest, (long) (Math.abs(p2.x - p1.x) + 1) * (Math.abs(p2.y - p1.y) + 1));
        }
    }
    System.out.println(largest);
}