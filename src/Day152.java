import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day152 {

    private static int startRow = 0;
    private static int startCol = 0;

    private static HashMap<Coordinate, Character> incrementBoxes = new HashMap<>();

    public static void main(String[] args) {
        int sumOfAllBoxesGPSCoordiates = 0;

        String inputString = Day15TestData.inputData;


        String actionSequence = findActionSequence(inputString);

        String board = findBoard(inputString);
        String newBoard = createNewBoard(board);

        char[][] grid = Util.convertStringToCharArray(newBoard);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '@') {
                    startRow = i;
                    startCol = j;
                }
            }
        }

        for (int i = 0; i < actionSequence.length(); i++) {
            char action = actionSequence.charAt(i);
            performMove(grid, startRow, startCol, action);
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '[') {
                    sumOfAllBoxesGPSCoordiates += (i * 100) + j;
                }
            }
        }

        System.out.println("Safety factor: " + sumOfAllBoxesGPSCoordiates);
    }

    private static String createNewBoard(String board) {
        StringBuilder stringBuilder = new StringBuilder();

        board.chars().forEach(c -> {
            if (c == '#') {
                stringBuilder.append('#');
                stringBuilder.append('#');
            } else if (c == '.') {
                stringBuilder.append('.');
                stringBuilder.append('.');
            } else if (c == '@') {
                stringBuilder.append('@');
                stringBuilder.append('.');
            } else if (c == 'O') {
                stringBuilder.append('[');
                stringBuilder.append(']');
            } else {
                stringBuilder.append(System.lineSeparator());
            }
        });

        return stringBuilder.toString();
    }

    private static void performMove(char[][] grid, int row, int col, char startingDirection) {
        int startPosX = row;
        int startPosY = col;

        if (!canMove(grid, row, col, startingDirection)) {
            return;
        }

        if (startingDirection == '^') {
            // we check up
            row = row - 1;
        } else if (startingDirection == '<') {
            // we check left
            col = col - 1;
        } else if (startingDirection == '>') {
            // we check right
            col = col + 1;
        } else if (startingDirection == 'v') {
            // we check down
            row = row + 1;
        }

        grid[startPosX][startPosY] = '.';

        if (grid[row][col] == '.') {
            grid[row][col] = '@';
        } else {
            if (startingDirection == '<' || startingDirection == '>') {
                performComplexBoxMove(grid, startingDirection);
                grid[row][col] = '@';
            } else {
                if (grid[row][col] == '[') {
                    grid[row][col + 1] = '.';
                } else {
                    grid[row][col - 1] = '.';
                }
                performComplexBoxMove(grid, startingDirection);
                grid[row][col] = '@';
            }
        }

        startRow = row;
        startCol = col;

    }

    private static void performComplexBoxMove(char[][] grid, char startingDirection) {
        // Convert incrementBoxes (which is a HashMap) to a List of Map.Entry
        List<Map.Entry<Coordinate, Character>> sortedEntries = new ArrayList<>(incrementBoxes.entrySet());

        // Sort the entries based on the startingDirection
        Comparator<Map.Entry<Coordinate, Character>> comparator;

        switch (startingDirection) {
            case '^': // Ascending order of row, then ascending order of col
                comparator = Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> entry.getKey().row)
                        .thenComparingInt(entry -> entry.getKey().col);
                break;
            case 'v': // Descending order of row, then ascending order of col
                comparator = Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> -entry.getKey().row)
                        .thenComparingInt(entry -> entry.getKey().col);
                break;
            case '<': // Ascending order of col, then ascending order of row
                comparator = Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> entry.getKey().col)
                        .thenComparingInt(entry -> entry.getKey().row);
                break;
            case '>': // Descending order of col, then ascending order of row
                comparator = Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> -entry.getKey().col)
                        .thenComparingInt(entry -> entry.getKey().row);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + startingDirection);
        }

        sortedEntries.sort(comparator);
        sortedEntries.forEach(entry -> {
            Coordinate k = entry.getKey();
            char v = entry.getValue();
            incrementBoxess(grid, k.row, k.col, startingDirection, v);
        });

        incrementBoxes.clear();
    }

    private static void incrementBoxess(char[][] grid, int row, int col, char startingDirection, char character) {
        grid[row][col] = '.';
        if (startingDirection == '^') {
            row = row - 1;
        } else if (startingDirection == 'v') {
            row = row + 1;
        } else if (startingDirection == '<') {
            col = col - 1;
        } else if (startingDirection == '>') {
            col = col + 1;
        }

        grid[row][col] = character;
    }

    private static boolean canMove(char[][] grid, int row, int col, char startingDirection) {
        if (startingDirection == '^' || startingDirection == 'v') {
            boolean b = canDoComplexMove2(grid, row, col, startingDirection);
            if (!b) {
                incrementBoxes.clear();
            }
            return b;
        } else {
            boolean b = canDoSimpleMove2(grid, row, col, startingDirection);
            if (!b) {
                incrementBoxes.clear();
            }
            return b;
        }
    }

    private static boolean canDoComplexMove2(char[][] grid, int row, int col, char startingDirection) {
        if (startingDirection == '^') {
            row = row - 1;
        } else if (startingDirection == 'v') {
            row = row + 1;
        }

        if (grid[row][col] == '.') {
            return true;
        }

        if (grid[row][col] == '#') {
            return false;
        }

        if (grid[row][col] == '[') {
            char character = grid[row][col];
            incrementBoxes.putIfAbsent(new Coordinate(row, col), character);
            char character2 = grid[row][col + 1];
            incrementBoxes.compute(new Coordinate(row, col + 1), (k, v) -> character2);
            return canDoComplexMove2(grid, row, col, startingDirection) && canDoComplexMove2(grid, row, col + 1, startingDirection);
        } else {
            char character = grid[row][col];
            incrementBoxes.compute(new Coordinate(row, col), (k, v) -> character);
            char character2 = grid[row][col - 1];
            incrementBoxes.compute(new Coordinate(row, col - 1), (k, v) -> character2);
            return canDoComplexMove2(grid, row, col, startingDirection) && canDoComplexMove2(grid, row, col - 1, startingDirection);
        }
    }

    private static boolean canDoSimpleMove2(char[][] grid, int row, int col, char startingDirection) {
        if (startingDirection == '<') {
            col = col - 1;
        } else if (startingDirection == '>') {
            col = col + 1;
        }

        if (grid[row][col] == '.') {
            return true;
        }

        if (grid[row][col] == '#') {
            return false;
        }

        char character = grid[row][col];
        incrementBoxes.putIfAbsent(new Coordinate(row, col), character);
        return canDoSimpleMove2(grid, row, col, startingDirection);

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

class Coordinate {
    int row, col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}