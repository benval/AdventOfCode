import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day152 {

    private static int startRow = 0;
    private static int startCol = 0;

    private static final HashMap<Coordinate, Character> incrementBoxes = new HashMap<>();

    public static void main(String[] args) {
        int sumOfAllBoxesGPSCoordiates;

        String inputString = Day15TestData.inputData;

        String actionSequence = findActionSequence(inputString);

        String board = findBoard(inputString);
        String newBoard = createNewBoard(board);

        char[][] grid = Util.convertStringToCharArray(newBoard);

        Coordinate startCoordinate = findStartCoordinate(grid);
        assert startCoordinate != null;
        startCol = startCoordinate.col;
        startRow = startCoordinate.row;

        performActions(grid, actionSequence);
        sumOfAllBoxesGPSCoordiates = calculateSumOfAllBoxesGPSCoordiates(grid);

        System.out.println("Safety factor: " + sumOfAllBoxesGPSCoordiates);
    }

    private static int calculateSumOfAllBoxesGPSCoordiates(char[][] grid) {
        int sumOfAllBoxesGPSCoordiates = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '[') {
                    sumOfAllBoxesGPSCoordiates += (i * 100) + j;
                }
            }
        }
        return sumOfAllBoxesGPSCoordiates;
    }

    private static void performActions(char[][] grid, String actionSequence) {
        for (int i = 0; i < actionSequence.length(); i++) {
            char action = actionSequence.charAt(i);
            performMove(grid, startRow, startCol, action);
        }
    }

    static Coordinate findStartCoordinate(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '@') {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
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
        if (!canMove(grid, row, col, startingDirection)) {
            return;
        }

        grid[row][col] = '.';

        if (startingDirection == '^') {
            row = row - 1;
        } else if (startingDirection == '<') {
            col = col - 1;
        } else if (startingDirection == '>') {
            col = col + 1;
        } else if (startingDirection == 'v') {
            row = row + 1;
        }

        if (grid[row][col] != '.') {
            if (startingDirection == '^' || startingDirection == 'v') {
                if (grid[row][col] == '[') {
                    grid[row][col + 1] = '.';
                } else {
                    grid[row][col - 1] = '.';
                }
            }
            performComplexBoxMove(grid, startingDirection);
        }

        grid[row][col] = '@';
        startRow = row;
        startCol = col;
    }

    private static void performComplexBoxMove(char[][] grid, char startingDirection) {
        // Convert incrementBoxes (which is a HashMap) to a List of Map.Entry
        List<Map.Entry<Coordinate, Character>> sortedEntries = new ArrayList<>(incrementBoxes.entrySet());

        // Sort the entries based on the startingDirection
        Comparator<Map.Entry<Coordinate, Character>> comparator = switch (startingDirection) {
            case '^' -> // Ascending order of row, then ascending order of col
                    Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> entry.getKey().row)
                            .thenComparingInt(entry -> entry.getKey().col);
            case 'v' -> // Descending order of row, then ascending order of col
                    Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> -entry.getKey().row)
                            .thenComparingInt(entry -> entry.getKey().col);
            case '<' -> // Ascending order of col, then ascending order of row
                    Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> entry.getKey().col)
                            .thenComparingInt(entry -> entry.getKey().row);
            case '>' -> // Descending order of col, then ascending order of row
                    Comparator.comparingInt((Map.Entry<Coordinate, Character> entry) -> -entry.getKey().col)
                            .thenComparingInt(entry -> entry.getKey().row);
            default -> throw new IllegalArgumentException("Invalid direction: " + startingDirection);
        };

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
        boolean canMove;

        if (startingDirection == '^' || startingDirection == 'v') {
            canMove = canDoARowMove(grid, row, col, startingDirection);
        } else {
            canMove = canDoAColumnMove(grid, row, col, startingDirection);
        }
        if (!canMove) {
            incrementBoxes.clear();
        }
        return canMove;
    }

    private static boolean canDoARowMove(char[][] grid, int row, int col, char startingDirection) {
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

        int nextCol;
        if (grid[row][col] == '[') {
            nextCol = col + 1;
        } else {
            nextCol = col - 1;
        }
        char character = grid[row][col];
        char character2 = grid[row][nextCol];
        incrementBoxes.putIfAbsent(new Coordinate(row, col), character);
        incrementBoxes.putIfAbsent(new Coordinate(row, nextCol), character2);
        return canDoARowMove(grid, row, col, startingDirection) && canDoARowMove(grid, row, nextCol, startingDirection);
    }

    private static boolean canDoAColumnMove(char[][] grid, int row, int col, char startingDirection) {
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
        return canDoAColumnMove(grid, row, col, startingDirection);

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