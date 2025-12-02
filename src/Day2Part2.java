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
            String iStr = String.valueOf(i);
            int iLength = iStr.length();
            boolean valid = true;
            divLoop:
            for (int divisions = 2; divisions <= iLength; divisions++) {
                if (iLength % divisions != 0) {
                    continue;
                }

                int divisionSize = iLength / divisions;
                for (int j = 0; j < divisionSize; j++) {
                    char targetChar = 0;
                    for (int k = 0; k < divisions; k++) {
                        char thisChar = iStr.charAt(k * divisionSize + j);
                        if (targetChar == 0) {
                            targetChar = thisChar;
                        } else if (targetChar != thisChar) {
                            continue divLoop;
                        }
                    }
                }
                total += i;
                break;
            }
        }
    }
    System.out.println(total);
}