import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {

    private static int startRow = 0;
    private static int startCol = 0;

    public static void main(String[] args) {
        int sumOfAllBoxesGPSCoordiates = 0;

        String inputString = Day15TestData.inputData;


        String actionSequence = findActionSequence(inputString);

        String board = findBoard(inputString);
        char[][] grid = Util.convertStringToCharArray(board);

        Coordinate startCoordinate = Day152.findStartCoordinate(grid);
        assert startCoordinate != null;
        startCol = startCoordinate.col;
        startRow = startCoordinate.row;

        for (int i = 0; i < actionSequence.length(); i++) {
            char action = actionSequence.charAt(i);
            performMove(grid, startRow, startCol, action);
        }

        // 100 * distance from top edge of map + distance from left edge of map

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'O') {
                    sumOfAllBoxesGPSCoordiates += (i * 100) + j;
                }
            }
        }

        System.out.println("Safety factor: " + sumOfAllBoxesGPSCoordiates);
    }

    private static void performMove(char[][] grid, int row, int col, char startingDirection) {
        int startPosX = row;
        int startPosY = col;

        if (!canMove(grid, row, col, startingDirection)) {
            return;
        }

        int[] updated = Util.updateGrid(row, col, startingDirection);
        row = updated[0];
        col = updated[1];

        grid[startPosX][startPosY] = '.';

        if (grid[row][col] == '.') {
            grid[row][col] = '@';
        } else if (grid[row][col] == 'O') {
            grid[row][col] = '@';
            performOMove(grid, row, col, startingDirection);
        }

        startRow = row;
        startCol = col;

    }

    private static void performOMove(char[][] grid, int row, int col, char startingDirection) {
        int[] updated = Util.updateGrid(row, col, startingDirection);
        row = updated[0];
        col = updated[1];

        if (grid[row][col] == '.') {
            grid[row][col] = 'O';
        } else if (grid[row][col] == 'O') {
            performOMove(grid, row, col, startingDirection);
        }
    }

    private static boolean canMove(char[][] grid, int row, int col, char startingDirection) {
        int[] updated = Util.updateGrid(row, col, startingDirection);
        row = updated[0];
        col = updated[1];

        if (grid[row][col] == '#') {
            return false;
        } else if (grid[row][col] == '.') {
            return true;
        } else if (grid[row][col] == 'O') {
            return canMove(grid, row, col, startingDirection);
        } else {
            return false;
        }
    }

    private static String findBoard(String input) {
        Pattern pattern = Pattern.compile("[\\^<>v]");
        Matcher matcher = pattern.matcher(input);

        return matcher.find() ? input.substring(0, input.indexOf(matcher.group())) : "";
    }

    private static String findActionSequence(String input) {
        Pattern pattern = Pattern.compile("[\\^<>v]");
        Matcher matcher = pattern.matcher(input);

        String string = matcher.find() ? input.substring(input.indexOf(matcher.group())) : "";

        return string.replace("\n", "");
    }

}
