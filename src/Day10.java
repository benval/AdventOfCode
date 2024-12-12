import java.util.HashSet;
import java.util.Set;

public class Day10 {
    private static Set<String> uniquePaths = new HashSet<>();

    private static final int[] rowDirections = {-1, 0, 1, 0};
    private static final int[] colDirections = {0, -1, 0, 1};

    public static void main(String[] args) {
        // finn alle 0-ere og så hvor mange 9-ere den kan nå. Dette er lik antall trailheads
        // hver trailhead kan ha sin egen score, og får 1 i score for hver 9-er den når.

        int[][] grid = Util.convertStringToIntArray(Day10TestData.data);

        int dfsScore = dfs(grid, true);
        int dfsScore2 = dfs(grid, false);

        System.out.println("Sum of the scores of all trailheads from part1: " + dfsScore);
        System.out.println("Sum of the scores of all trailheads from part2: " + dfsScore2);
    }

    public static int dfs(int[][] grid, boolean part1) {
        int scores = 0;
        int startValue = 0;

        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    if (part1) {
                        visited = new boolean[rows][cols];
                        int temp = dfsHelper(grid, i, j, visited, startValue);
                        scores += temp;
                    } else {
                        int temp = dfsHelper(grid, i, j, startValue, "");
                        scores += temp;
                    }

                }
            }
        }
        return scores;
    }

    private static int dfsHelper(int[][] grid, int row, int col, boolean[][] visited, int trailScore) {
        int tempScore = 0;

        // Check boundaries and whether the cell is already visited
        if (row < 0
                || row >= grid.length
                || col < 0
                || col >= grid[0].length
                || visited[row][col]
                || grid[row][col] != trailScore) {
            return 0;
        }

        // må finne unike 9-ere for denne 0-eren
        if (grid[row][col] == 9) {
            visited[row][col] = true;
            System.out.println("Visiting: (" + row + ", " + col + ") = " + grid[row][col]);
            return 1;
        }

        // Mark the cell as visited
        visited[row][col] = true;
        System.out.println("Visiting: (" + row + ", " + col + ") = " + grid[row][col]);

        trailScore++;
        // Explore neighbors in the order: up, left, down, right
        for (int d = 0; d < 4; d++) {
            int newRow = row + rowDirections[d];
            int newCol = col + colDirections[d];

            tempScore = tempScore + dfsHelper(grid, newRow, newCol, visited, trailScore);
        }
        return tempScore;
    }

    private static int dfsHelper(int[][] grid, int row, int col, int trailScore, String path) {
        int tempScore = 0;

        // Check boundaries and whether the cell is already visited
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length || grid[row][col] != trailScore) {
            return 0;
        }

        String currentPath = path + "(" + row + "," + col + ")";

        // Attempting to reach a unique 9 without revisiting the same path
        if (grid[row][col] == 9) {
            if (!uniquePaths.contains(currentPath)) {
                uniquePaths.add(currentPath);
                System.out.println("Unique path to 9 found: " + currentPath);
                return 1;
            }
            return 0;
        }

//        System.out.println("Visiting: (" + row + ", " + col + ") = " + grid[row][col]);

        trailScore++;
        // Explore neighbors in the order: up, left, down, right
        for (int d = 0; d < 4; d++) {
            int newRow = row + rowDirections[d];
            int newCol = col + colDirections[d];

            tempScore = tempScore + dfsHelper(grid, newRow, newCol, trailScore, currentPath);
        }
        return tempScore;
    }


}
