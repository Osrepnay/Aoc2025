import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

long bankMaxJolt(List<Integer> bank, int digitsLeft) {
    if (digitsLeft == 0) {
        return 0;
    }

    int tens = Integer.MIN_VALUE;
    int tensIdx = -1;
    for (int i = 0; i < bank.size() - digitsLeft + 1; i++) {
        if (bank.get(i) > tens) {
            tens = bank.get(i);
            tensIdx = i;
        }
    }
    return tens * Math.powExact(10l, digitsLeft - 1)
            + bankMaxJolt(bank.subList(tensIdx + 1, bank.size()), digitsLeft - 1);
}

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/3"));
    long total = 0;
    while (s.hasNextLine()) {
        var bank = Arrays.stream(s.nextLine().split("")).map(Integer::valueOf).toList();
        total += bankMaxJolt(bank, 12);
    }
    System.out.println(total);
}