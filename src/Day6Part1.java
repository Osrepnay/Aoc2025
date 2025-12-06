import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/6"));
    List<List<BigInteger>> ints = new ArrayList<>();
    while (s.hasNextInt()) {
        String[] parts = s.nextLine().trim().split(" +");
        if (ints.isEmpty()) {
            for (String part : parts) {
                ints.add(new ArrayList<>(List.of(new BigInteger(part))));
            }
        } else {
            for (int i = 0; i < parts.length; i++) {
                ints.get(i).add(new BigInteger(parts[i]));
            }
        }
    }
    List<String> ops = List.of(s.nextLine().split(" +"));
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < ops.size(); i++) {
        BinaryOperator<BigInteger> op = switch (ops.get(i)) {
            case "*" -> (a, b) -> a.multiply(b);
            case "+" -> (a, b) -> a.add(b);
            default -> throw new IllegalStateException("Unexpected value: " + ops.get(i));
        };
        sum = sum.add(ints.get(i).stream().reduce(op).get());
    }
    System.out.println(sum);
}