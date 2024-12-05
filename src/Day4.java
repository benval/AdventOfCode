public class Day4 {

    private static final int[] ROW_DIRS = {0, 1, 0, -1, 1, 1, -1, -1}; // right, down, left, up, and diagonals
    private static final int[] COL_DIRS = {1, 0, -1, 0, 1, -1, 1, -1};

    public static void main(String[] args) {
        String word = "MAS";

        Day4TestData data = new Day4TestData();
        char[][] charGrid = convertToCharArray(data.task2);

        int antallX = searchWord2(charGrid, word);
        System.out.println("Number of words: " + antallX);
    }

    public static char[][] convertToCharArray(String multilineString) {
        String[] lines = multilineString.split("\n");
        int numberOfRows = lines.length;
        int numberOfCols = lines[0].length(); // Assumes all lines are of equal length

        char[][] charArray = new char[numberOfRows][numberOfCols];

        for (int i = 0; i < numberOfRows; i++) {
            charArray[i] = lines[i].toCharArray();
        }

        return charArray;
    }

    public static int searchWord2(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;

        int count = 0;

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (grid[i][j] == 'A') {
                    int innerCount = 0;

                    if (grid[i-1][j-1] == word.charAt(0) && grid[i+1][j+1] == word.charAt(2)) {
                        innerCount++;
                    }

                    if (grid[i-1][j-1] == word.charAt(2) && grid[i+1][j+1] == word.charAt(0)) {
                        innerCount++;
                    }

                    if (grid[i+1][j-1] == word.charAt(0) && grid[i-1][j+1] == word.charAt(2)) {
                        innerCount++;
                    }

                    if (grid[i+1][j-1] == word.charAt(2) && grid[i-1][j+1] == word.charAt(0)) {
                        innerCount++;
                    }

                    if (innerCount == 2) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static int searchWord(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;

        int count = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int dir = 0; dir < ROW_DIRS.length; dir++) {
                    if (dfs(grid, word, i, j, 0, ROW_DIRS[dir], COL_DIRS[dir])) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static boolean dfs(char[][] grid, String word, int x, int y, int index, int dx, int dy) {
        if (index == word.length()) {
            return true;
        }

        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || grid[x][y] != word.charAt(index)) {
            return false;
        }

        char temp = grid[x][y];
        grid[x][y] = '#'; // Mark the cell as visited to prevent revisiting

        boolean found = dfs(grid, word, x + dx, y + dy, index + 1, dx, dy);

        grid[x][y] = temp; // Unmark the cell

        return found;
    }

}