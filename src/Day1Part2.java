import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/1"));
    int start = 50;
    int count = 0;
    while (s.hasNextLine()) {
        String line = s.nextLine();
        int fac = 1;
        if (line.charAt(0) == 'L') {
            fac = -1;
        }
        int change = Integer.parseInt(line.substring(1));
        for (int i = 0; i < change; i++) {
            start += fac;
            if (start % 100 == 0) {
                count++;
            }
        }
    }
    System.out.println(count);
}