import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int visitedPositions = 0;

//        If there is something directly in front of you, turn right 90 degrees.
//                Otherwise, take a step forward.

        String inputTask1 = Day6TestData.inputTask1;

        char[][] charGrid = Util.convertStringToCharArray(inputTask1);

        char startingDirection = findStartingDirection(inputTask1);
        playOutPath(charGrid, startingDirection);
//        playOutPath2(charGrid, startingDirection);
        visitedPositions = countDistinctMoves(charGrid);

        System.out.println("Sum of middle numbers from valid orders: " + visitedPositions);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
    }

    private static char findStartingDirection(String input) {
        Pattern pattern = Pattern.compile("[\\^<>v]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group().charAt(0) : ' ';
    }

    private static char[][] playOutPath2(char[][] grid, char startingDirection) {
        char[][] previousGridWins = new char[grid.length][grid[0].length];

        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == startingDirection) {

                    while (true) {
                        // IF you put down a 0 you will now turn in another direction, if you have already been there then it is a valid 0

                        if (grid[i][j] == '<') {
                            if (j - 1 < 0) {
                                break;
                            }
                            if (grid[i][j - 1] == '|' || grid[i][j - 1] == '+') {
                                if ((grid[i - 1][j - 1] == '+' || grid[i - 1][j - 1] == '|') && previousGridWins[i][j - 2] != '0') {
                                    previousGridWins[i][j - 2] = '0';
                                    grid[i][j] = '-';
                                    grid[i][j - 1] = '+';
                                    grid[i - 1][j - 1] = '^';
                                    i = i - 1;
                                    j = j - 1;
                                } else {
                                    grid[i][j] = '-';
                                    grid[i][j - 1] = '<';
                                    j = j - 1;
                                }
                            }

                            if (grid[i][j - 1] == '.') {
                                grid[i][j - 1] = '<';
                                grid[i][j] = '-';
                                j = j - 1;
                            } else if (grid[i][j - 1] == '#') {
                                grid[i][j] = '+';
                                if (i - 1 < cols && (grid[i - 1][j] != '#')) {
                                    grid[i][i - 1] = '^';
                                    i = i - 1;
                                }
                            }
                        }

                        if (grid[i][j] == '^') {
                            if (grid[i - 1][j] == '-') {
                                if ((grid[i - 1][j + 1] == '+' || grid[i - 1][j + 1] == '-') && previousGridWins[i - 2][j] != '0') {
                                    previousGridWins[i - 2][j] = '0';
                                    grid[i][j] = '|';
                                    grid[i - 1][j] = '+';
                                    grid[i - 1][j + 1] = '>';
                                    i = i - 1;
                                    j = j + 1;
                                } else {
                                    grid[i][j] = '|';
                                    grid[i - 1][j] = '^';
                                    i = i - 1;
                                }
                            }

                            if (grid[i - 1][j] == '.' || grid[i - 1][j] == 'X') {
                                grid[i - 1][j] = '^';
                                grid[i][j] = '|';
                                i = i - 1;
                            } else if (grid[i - 1][j] == '#') {
                                grid[i][j] = '+';
                                if (j + 1 < cols && (grid[i][j + 1] != '#')) {
                                    grid[i][j + 1] = '>';
                                    j = j + 1;
                                }
                            }
                        }

                        if (grid[i][j] == '>') {
                            if (grid[i][j + 1] == '|') {
                                if ((grid[i + 1][j + 1] == '+' || grid[i + 1][j + 1] == '|') && previousGridWins[i][j + 2] != '0') {
                                    previousGridWins[i][j + 2] = '0';
                                    grid[i][j] = '-';
                                    grid[i][j + 1] = '+';
                                    grid[i + 1][j + 1] = 'v';
                                    i = i + 1;
                                    j = j + 1;
                                } else {
                                    grid[i][j] = '-';
                                    grid[i][j + 1] = '>';
                                    j = j + 1;
                                }
                            }

                            if (grid[i][j + 1] == '.' || grid[i][j + 1] == 'X') {
                                grid[i][j + 1] = '>';
                                grid[i][j] = '-';
                                j = j + 1;
                            } else if (grid[i][j + 1] == '#') {
                                grid[i][j] = '+';
                                if (i + 1 < rows && (grid[i + 1][j] != '#')) {
                                    grid[i + 1][j] = 'v';
                                    i = i + 1;
                                }
                            }
                        }

                        if (grid[i][j] == 'v') {
                            if (grid[i + 1][j] == '-') {
                                if ((grid[i + 1][j - 1] == '+' || grid[i + 1][j - 1] == '-') && previousGridWins[i + 2][j] != '0') {
                                    previousGridWins[i + 2][j] = '0';
                                    grid[i][j] = '|';
                                    grid[i][j + 1] = '+';
                                    grid[i - 1][j + 1] = '<';
                                    i = i - 1;
                                    j = j + 1;
                                } else {
                                    grid[i][j] = '|';
                                    grid[i][j + 1] = 'v';
                                    j = j + 1;
                                }
                            }

                            if (grid[i + 1][j] == '.' || grid[i + 1][j] == 'X') {
                                grid[i + 1][j] = 'v';
                                grid[i][j] = '|';
                                i = i + 1;
                            } else if (grid[i + 1][j] == '#') {
                                grid[i][j] = '+';
                                if (j - 1 > 0 && (grid[i][j - 1] != '#')) {
                                    grid[i][j - 1] = '<';
                                    j = j - 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        return grid;
    }


    private static char[][] playOutPath(char[][] grid, char startingDirection) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == startingDirection) {
                    while (true) {
                        if (grid[i][j] == '<') {
                            if (j - 1 < 0) {
                                grid[i][j] = 'X';
                                return grid; // finished
                            }

                            if (grid[i][j - 1] == '.' || grid[i][j - 1] == 'X') {
                                grid[i][j - 1] = '<';
                                grid[i][j] = 'X';
                                j = j - 1;
                            } else if (grid[i][j - 1] == '#') {
                                grid[i][j] = '^';
                            }
                        }

                        if (grid[i][j] == '^') {
                            if (i - 1 < 0) {
                                grid[i][j] = 'X';
                                return grid; // finished
                            }

                            if (grid[i - 1][j] == '.' || grid[i - 1][j] == 'X') {
                                grid[i - 1][j] = '^';
                                grid[i][j] = 'X';
                                i = i - 1;
                            } else if (grid[i - 1][j] == '#') {
                                grid[i][j] = '>';
                            }
                        }

                        if (grid[i][j] == '>') {
                            if (j + 1 >= grid.length) {
                                grid[i][j] = 'X';
                                return grid; // finished
                            }

                            if (grid[i][j + 1] == '.' || grid[i][j + 1] == 'X') {
                                grid[i][j + 1] = '>';
                                grid[i][j] = 'X';
                                j = j + 1;
                            } else if (grid[i][j + 1] == '#') {
                                grid[i][j] = 'v';
                            }
                        }

                        if (grid[i][j] == 'v') {
                            if (i + 1 >= grid.length) {
                                grid[i][j] = 'X';
                                return grid; // finished
                            }

                            if (grid[i + 1][j] == '.' || grid[i + 1][j] == 'X') {
                                grid[i + 1][j] = 'v';
                                grid[i][j] = 'X';
                                i = i + 1;
                            } else if (grid[i + 1][j] == '#') {
                                grid[i][j] = '<';
                            }
                        }
                    }
                }
            }
        }
        return grid;
    }

    private static int countDistinctMoves(char[][] input) {
        int count = 0;
        for (char[] row : input) {
            for (char c : row) {
                if (c == 'X') {
                    count++;
                }
            }
        }
        return count;
    }
}
