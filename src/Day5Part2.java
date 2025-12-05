import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

record Range(long start, long end) {
    public boolean contains(long x) {
        return start <= x && x <= end;
    }

    public Optional<Range> merge(Range other) {
        if (other.start < this.start) {
            return other.merge(this);
        }
        if (this.end < other.start) {
            return Optional.empty();
        } else {
            return Optional.of(new Range(this.start, Math.max(this.end, other.end)));
        }
    }

    public long span() {
        return this.end - this.start + 1;
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

    ranges.sort(Comparator.comparing(r -> r.start));

    List<Range> completedRanges = new ArrayList<>();
    while (ranges.size() >= 2) {
        Optional<Range> optMerged = ranges.getFirst().merge(ranges.get(1));
        if (optMerged.isPresent()) {
            ranges.remove(1);
            ranges.set(0, optMerged.get());
        } else {
            completedRanges.add(ranges.removeFirst());
        }
    }
    completedRanges.addAll(ranges);

    System.out.println(completedRanges.stream().mapToLong(Range::span).sum());
}