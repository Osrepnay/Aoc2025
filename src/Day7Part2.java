import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

record Point(int y, int x) {
}

class Splitter {
    public long universes = 1;
    public List<Splitter> leftParent = new ArrayList<>();
    public List<Splitter> rightParent = new ArrayList<>();

    public List<Splitter> getParents() {
        return Stream.of(leftParent, rightParent)
                .filter(x -> x != null)
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/7"));
    int start = -1;
    List<Set<Integer>> splitterLocs = new ArrayList<>();
    int maxX = 0;
    while (s.hasNextLine()) {
        String line = s.nextLine();
        maxX = line.length();
        if (line.contains("S")) {
            start = line.indexOf('S');
        }
        Set<Integer> thisLocs = new HashSet<>();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '^') {
                thisLocs.add(i);
            }
        }
        if (!thisLocs.isEmpty()) {
            splitterLocs.add(thisLocs);
        }
    }

    Map<Point, Splitter> splitterMap = new HashMap<>();
    Map<Integer, List<Splitter>> splitterLayers = new HashMap<>();
    Splitter root = new Splitter();
    splitterMap.put(new Point(0, splitterLocs.getFirst().stream().findFirst().get()), root);
    splitterLocs = splitterLocs.subList(1, splitterLocs.size());
    int idx = 1;
    for (Set<Integer> splitters : splitterLocs) {
        List<Splitter> layer = new ArrayList<>();
        for (int splitterX : splitters) {
            Splitter thisSplitter = new Splitter();
            layer.add(thisSplitter);
            Point thisPoint = new Point(idx, splitterX);
            for (int y = idx - 1; y >= 0; y--) {
                if (splitterMap.containsKey(new Point(y, splitterX))) {
                    break;
                }
                Point leftParent = new Point(y, splitterX - 1);
                if (splitterMap.containsKey(leftParent)) {
                    thisSplitter.leftParent.add(splitterMap.get(leftParent));
                }
                Point rightParent = new Point(y, splitterX + 1);
                if (splitterMap.containsKey(rightParent)) {
                    thisSplitter.rightParent.add(splitterMap.get(rightParent));
                }
            }
            splitterMap.put(thisPoint, thisSplitter);
        }
        splitterLayers.put(idx, layer);

        idx++;
    }
    int finalIdx = idx - 1;
    for (int y = 1; y <= finalIdx; y++) {
        for (Splitter splitter : splitterLayers.get(y)) {
            splitter.universes = splitter.getParents().stream().mapToLong(p -> p.universes).sum();
        }
    }
    long totalUniverses = 0;
    for (int x = 0; x <= maxX; x++) {
        for (int y = finalIdx; y >= 0; y--) {
            if (splitterMap.containsKey(new Point(y, x))) {
                break;
            }
            for (Point p : List.of(new Point(y, x - 1), new Point(y, x + 1))) {
                if (splitterMap.containsKey(p)) {
                    totalUniverses += splitterMap.get(p).universes;
                }
            }
        }
    }
    System.out.println(totalUniverses);
}