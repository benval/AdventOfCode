import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day16 {

    private static final int[] rowDirections = {0, -1, 0, 1};
    private static final int[] colDirections = {1, 0, -1, 0};

    private static Map<StartPosition, String> rettninger = new HashMap<>();

    private static Set<String> uniquePaths = new HashSet<>();

    private static long lowestScore = 10000000000L;

    public static void main(String[] args) {
        char startingDirection = '>';

        String inputString = Day16TestData.inputExample1;

        char[][] grid = Util.convertStringToCharArray(inputString);

        StartPosition startPosition = Util.getStartPosition(grid, 'S');

        // find all possible ways to get to the end, but everytime the score is higher than the smallest, terminate.
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        long dfs = dfs(grid);

        System.out.println("Lowest score: " + lowestScore);
    }

    public static long dfs(char[][] grid) {
        long scores = 0;

        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'S') {
                    boolean[][] visited = new boolean[rows][cols];
                    long temp = dfsHelper(grid, i, j, "", '>', 0, visited);
                    scores += temp;
                    return scores;
                }
            }
        }
        return scores;
    }

    private static long dfsHelper(char[][] grid, int row, int col, String path, char direction, long currentScore, boolean[][] visited) {
        int rotate = 1000;
        int moveForward = 1;

        long tempScore = 0;

        // Check boundaries and whether the cell is already visited
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length || grid[row][col] == '#') {
            return currentScore;
        }

        String currentPath = path + "(" + row + "," + col + ")";
        visited[row][col] = true;

        // Attempting to reach a unique 9 without revisiting the same path
        if (grid[row][col] == 'E') {
            if (!uniquePaths.contains(currentPath)) {
                uniquePaths.add(currentPath);
                System.out.println("Unique path to E found: " + currentPath);

                if (currentScore < lowestScore) {
                    lowestScore = currentScore;
                    System.out.println("New lowest score: " + lowestScore);
                }

                return currentScore;
            }
            return currentScore;
        }

        for (int d = 0; d < 4; d++) {
            int newRow = row + rowDirections[d];
            int newCol = col + colDirections[d];

            char nextDirection;
            if (d == 0) {
                nextDirection = '>';
            } else if (d == 1) {
                nextDirection = '^';
            } else if (d == 2) {
                nextDirection = '<';
            } else {
                nextDirection = 'v';
            }

            if (nextDirection == direction) {
                tempScore += moveForward;
            } else {
                tempScore += rotate;
            }

            String string = rettninger.get(new StartPosition(newRow, newCol));

            if (visited[newRow][newCol]) {
                int startIndex = currentPath.lastIndexOf("(");
                int endIndex = currentPath.lastIndexOf(")");
                StringBuilder sb = new StringBuilder(currentPath);
                sb.delete(startIndex, endIndex + 1); // Use `endIndex + 1` as `delete` doesn't include the end index
                currentPath = sb.toString();
            }

            if (string == null || !string.contains(String.valueOf(nextDirection))) {
                rettninger.compute(new StartPosition(newRow, newCol), (k, v) -> v == null ? nextDirection + "" : v + nextDirection);
                long l = dfsHelper(grid, newRow, newCol, currentPath, nextDirection, tempScore, visited);
                if (l != 0) {
                    currentScore += l;
                }
            }
        }

        return 0;
    }
}
