import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/7"));
    int start = -1;
    List<List<Integer>> splitterLocs = new ArrayList<>();
    while (s.hasNextLine()) {
        String line = s.nextLine();
        if (line.contains("S")) {
            start = line.indexOf('S');
        }
        List<Integer> thisLocs = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '^') {
                thisLocs.add(i);
            }
        }
        splitterLocs.add(thisLocs);
    }

    Set<Integer> currentBeams = new HashSet<>();
    currentBeams.add(start);
    int splitCount = 0;
    for (List<Integer> splitters : splitterLocs) {
        Set<Integer> toAdd = new HashSet<>();
        for (int splitterX : splitters) {
            if (currentBeams.contains(splitterX)) {
                currentBeams.remove(splitterX);
                toAdd.add(splitterX - 1);
                toAdd.add(splitterX + 1);
                splitCount++;
            }
        }
        currentBeams.addAll(toAdd);
    }
    System.out.println(splitCount);
}