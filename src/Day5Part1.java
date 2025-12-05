import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

record Range(long start, long end) {
    public boolean contains(long x) {
        return start <= x && x <= end;
    }
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/5"));
    List<Range> ranges = new ArrayList<>();
    String line = s.nextLine();
    while (!line.isEmpty()) {
        long[] split = Arrays.stream(line.split("\\-")).mapToLong(Long::parseLong).toArray();
        ranges.add(new Range(split[0], split[1]));
        line = s.nextLine();
    }

    int fresh = 0;
    while (s.hasNextLong()) {
        long id = s.nextLong();
        if (ranges.stream().anyMatch(r -> r.contains(id))) {
            fresh++;
        }
    }
    System.out.println(fresh);
}