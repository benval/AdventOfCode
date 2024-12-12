import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static char[][] convertStringToCharArray(String multilineString) {
        String[] lines = multilineString.split("\n");
        int numberOfRows = lines.length;
        int numberOfCols = lines[0].length(); // Assumes all lines are of equal length

        char[][] charArray = new char[numberOfRows][numberOfCols];

        for (int i = 0; i < numberOfRows; i++) {
            charArray[i] = lines[i].toCharArray();
        }

        return charArray;
    }

    public static int[][] convertStringToIntArray(String multilineString) {
        String[] lines = multilineString.split("\n");
        int numberOfRows = lines.length;
        int numberOfCols = lines[0].length(); // Assumes all lines are of equal length

        int[][] intArray = new int[numberOfRows][numberOfCols];

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfCols; j++) {
                // Convert each character to an integer
                char c = lines[i].charAt(j);
                if (Character.isDigit(c)) {
                    intArray[i][j] = Character.getNumericValue(c);
                } else {
                    throw new IllegalArgumentException("Input string must contain only digits.");
                }
            }
        }

        return intArray;
    }

    public static StartPosition getStartPosition(char[][] charArray, char identifier) {
        int rows = charArray.length;
        int cols = charArray[0].length;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (charArray[x][y] == identifier) {
                    return new StartPosition(x, y);
                }
            }
        }
        return null;
    }

    public static int countOccurrencesOfChar(char[][] grid, char inputChar) {
        int count = 0;
        for (char[] row : grid) {
            for (char c : row) {
                if (c == inputChar) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isTileOutOfRange(char[][] grid, int row, int col) {
        return row < 0 || row >= grid.length || col < 0 || col >= grid[0].length;
    }

    public static char matchRegExToStringInput(String regEx, String input) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group().charAt(0) : ' ';
    }

}

record StartPosition(int posX, int posY) {
}