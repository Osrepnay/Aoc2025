import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

int bankMaxJolt(int[] bank) {
    int tens = Integer.MIN_VALUE;
    int tensIdx = -1;
    for (int i = 0; i < bank.length - 1; i++) {
        if (bank[i] > tens) {
            tens = bank[i];
            tensIdx = i;
        }
    }

    int ones = Integer.MIN_VALUE;
    for (int j = tensIdx + 1; j < bank.length; j++) {
        if (bank[j] > ones) {
            ones = bank[j];
        }
    }
    return tens * 10 + ones;
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/3"));
    int total = 0;
    while (s.hasNextLine()) {
        int[] bank = Arrays.stream(s.nextLine().split("")).mapToInt(Integer::parseInt).toArray();
        total += bankMaxJolt(bank);
    }
    System.out.println(total);
}