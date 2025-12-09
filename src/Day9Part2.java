import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

private static int coolMod(int x, int m) {
    int res = x % m;
    if (res < 0) {
        return res + m;
    } else {
        return res;
    }
}

public enum Turn {
    LEFT, RIGHT
}

private enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction makeTurn(Turn t) {
        int delta = switch (t) {
            case LEFT -> -1;
            case RIGHT -> 1;
        };
        return Direction.values()[coolMod(this.ordinal() + delta, Direction.values().length)];
    }
}

private record Point(int x, int y) {
    public Direction directionTo(Point other) {
        if (other.x > x) {
            return Direction.EAST;
        } else if (other.x < x) {
            return Direction.WEST;
        } else if (other.y > y) {
            return Direction.SOUTH;
        } else {
            return Direction.NORTH;
        }
    }

    public Point step(Direction d) {
        return switch (d) {
            case NORTH -> new Point(x, y - 1);
            case EAST -> new Point(x + 1, y);
            case SOUTH -> new Point(x, y + 1);
            case WEST -> new Point(x - 1, y);
        };
    }
}

private static boolean betweenInclusive(int a, int b, int x) {
    if (a > b) {
        return betweenInclusive(b, a, x);
    } else {
        return a <= x && x <= b;
    }
}

private record Box(Point corner1, Point corner2) {
    // exclusive
    public boolean contains(Point p) {
        return betweenInclusive(corner1.x, corner2.x, p.x) && betweenInclusive(corner1.y, corner2.y, p.y);
    }

    public boolean intersects(Box b) {
        return List.of(
                b.corner1,
                b.corner2,
                new Point(b.corner1.x, b.corner2.y),
                new Point(b.corner2.x, b.corner1.y)
        ).stream().anyMatch(p -> this.contains(p));
    }
}

private record Corner(Point start, Point corner, Point end) {
    public Turn getTurn() {
        return switch (coolMod(corner.directionTo(end).ordinal() - start.directionTo(corner).ordinal(),
                Direction.values().length)) {
            case -1 -> Turn.LEFT;
            case 3 -> Turn.LEFT;
            case 1 -> Turn.RIGHT;
            case -3 -> Turn.RIGHT;
            default -> throw new IllegalStateException("bad turn");
        };
    }

    private int intSignum(int x) {
        return x < 0 ? -1 : x > 0 ? 1 : 0;
    }

    public List<Point> outline() {
        Point offsetStart = new Point(
                start.x + intSignum(end.x - corner.x),
                start.y + intSignum(end.y - corner.y)
        );
        Point offsetEnd = new Point(
                end.x + intSignum(start.x - corner.x),
                end.y + intSignum(start.y - corner.y)
        );
        Point offsetCorner = new Point(
                corner.x + intSignum(start.x - corner.x) + intSignum(end.x - corner.x),
                corner.y + intSignum(start.y - corner.y) + intSignum(end.y - corner.y)
        );
        Set<Point> outline = new LinkedHashSet<>();
        for (; !offsetStart.equals(offsetCorner);
             offsetStart = offsetStart.step(offsetStart.directionTo(offsetCorner))) {
            outline.add(offsetStart);
        }
        for (; !offsetEnd.equals(offsetCorner); offsetEnd = offsetEnd.step(offsetEnd.directionTo(offsetCorner))) {
            outline.add(offsetEnd);
        }
        return outline.stream().toList();
    }

    private static boolean segmentHasPoint(Point a, Point b, Point p) {
        return a.x == b.x && p.x == a.x && betweenInclusive(a.y, b.y, p.y)
                || a.y == b.y && p.y == a.y && betweenInclusive(a.x, b.x, p.x);
    }

    public boolean hasPoint(Point p) {
        return segmentHasPoint(start, corner, p) || segmentHasPoint(corner, end, p);
    }
}

record Tuple2<A, B>(A a, B b) {
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/9"));
    List<Point> points = new ArrayList<>();
    while (s.hasNextLine()) {
        int[] parts = Arrays.stream(s.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
        points.add(new Point(parts[0], parts[1]));
    }

    // there are no intersections!

    List<Corner> corners = new ArrayList<>();
    int turnCounter = 0;
    for (int i = 0; i < points.size(); i++) {
        Point prevPoint = points.get(i);
        Point centerPoint = points.get((i + 1) % points.size());
        Point nextPoint = points.get((i + 2) % points.size());
        Direction prevDir = prevPoint.directionTo(centerPoint);
        Direction nextDir = centerPoint.directionTo(nextPoint);
        // clear weird double segments just in case
        // this should make every group of 3 points a "corner"
        if (prevDir.equals(nextDir)) {
            points.remove(i);
            i--;
        } else {
            Corner newCorner = new Corner(prevPoint, centerPoint, nextPoint);
            corners.add(newCorner);
            turnCounter += switch (newCorner.getTurn()) {
                case LEFT -> -1;
                case RIGHT -> 1;
            };
        }
    }
    Turn inside = turnCounter > 0 ? Turn.RIGHT : Turn.LEFT;
    Set<Point> badPoints = new HashSet<>();
    for (int i = 0; i < corners.size(); i++) {
        if (corners.get(i).getTurn() != inside) {
            badPoints.addAll(corners.get(i).outline().stream()
                    .filter(p -> corners.stream().noneMatch(c -> c.hasPoint(p)))
                    .toList()
            );
        }
    }

    List<Tuple2<Long, Box>> candidates = new ArrayList<>();
    for (int i = 0; i < points.size() - 1; i++) {
        for (int j = i + 1; j < points.size(); j++) {
            Point p1 = points.get(i);
            Point p2 = points.get(j);
            long size = (long) (Math.abs(p2.x - p1.x) + 1) * (Math.abs(p2.y - p1.y) + 1);
            candidates.add(new Tuple2<>(size, new Box(p1, p2)));
        }
    }
    candidates.sort(Comparator.comparing(Tuple2<Long, Box>::a).reversed());

    for (Tuple2<Long, Box> candidate : candidates) {
        if (badPoints.stream().noneMatch(p -> candidate.b.contains(p))) {
            System.out.println(candidate.a);
            return;
        }
    }
}