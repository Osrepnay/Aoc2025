import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/6"));
    List<String> lines = new ArrayList<>();
    while (s.hasNextInt()) {
        lines.add(s.nextLine());
    }
    int maxLineWidth = lines.stream().mapToInt(String::length).max().getAsInt();
    for (int i = 0; i < lines.size(); i++) {
        lines.set(i, String.format("%-" + maxLineWidth + "s", lines.get(i)));
    }

    List<List<BigInteger>> ints = new ArrayList<>();
    ints.add(new ArrayList<>());
    for (int c = lines.getFirst().length() - 1; c >= 0; c--) {
        StringBuilder accum = new StringBuilder();
        for (int r = 0; r < lines.size(); r++) {
            accum.append(lines.get(r).charAt(c));
        }
        if (accum.toString().isBlank()) {
            ints.add(new ArrayList<>());
        } else {
            ints.getLast().add(new BigInteger(accum.toString().trim()));
        }
    }
    ints = ints.reversed();

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