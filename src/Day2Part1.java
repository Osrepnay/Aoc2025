import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/2"));
    String all = s.nextLine();
    String[] ranges = all.split(",");
    long total = 0;
    for (String rawRange : ranges) {
        String[] startEndRaw = rawRange.split("\\-");
        long start = Long.parseLong(startEndRaw[0]);
        long end = Long.parseLong(startEndRaw[1]);
        for (long i = start; i <= end; i++) {
            String stringified = String.valueOf(i);
            if (stringified.length() % 2 == 0
                    && stringified.substring(0, stringified.length() / 2)
                    .equals(stringified.substring(stringified.length() / 2))) {
                total += i;
            }
        }
    }
    System.out.println(total);
}