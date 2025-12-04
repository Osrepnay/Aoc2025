import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

char[][] grid;

void main() throws FileNotFoundException {
    Scanner s = new Scanner(new File("inputs/4"));
    List<char[]> lines = new ArrayList<>();
    while (s.hasNextLine()) {
        lines.add(s.nextLine().toCharArray());
    }
    grid = lines.stream().toArray(char[][]::new);

    int[][] deltas = {
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1},
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1},
    };
    int forkliftable = 0;
    while (true) {
        boolean didChange = false;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] != '@') {
                    continue;
                }

                int neighbors = 0;
                for (int[] delta : deltas) {
                    int newR = r + delta[0];
                    int newC = c + delta[1];
                    if (newR < 0 || newC < 0 || newR >= grid.length || newC >= grid[r].length) {
                        continue;
                    }
                    if (grid[newR][newC] == '@') {
                        neighbors++;
                    }
                }
                if (neighbors < 4) {
                    forkliftable++;
                    grid[r][c] = '.';
                    didChange = true;
                }
            }
        }

        if (!didChange) {
            break;
        }
    }
    System.out.println(forkliftable);
}