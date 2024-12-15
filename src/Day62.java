public class Day62 {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int visitedPositions;
        int numberOfLoopsToReachZero = 0;

        String inputTask1 = Day6TestData.inputTask1;
        String identifyStartingDirectionRegex = "[\\^<>v]";

        char[][] chars = Util.convertStringToCharArray(inputTask1);
        char[][] copyChars = createCharCopy(chars);


        // find starting direction
        char startingDirection = Util.matchRegExToStringInput(identifyStartingDirectionRegex, inputTask1);

        // find starting position
        StartPosition startPosition = Util.getStartPosition(chars, startingDirection);

        // find valid movement
        int numberOfMoves = performMove(chars, startPosition.posX(), startPosition.posY(), startingDirection);

        // this list is the options for where to place a 0 ?
        visitedPositions = Util.countOccurrencesOfChar(chars, 'X');

        System.out.println("Number of visited positions: " + visitedPositions);


        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                if (chars[i][j] == 'X' && copyChars[i][j] != startingDirection) {
                    char[][] copyOfCopy = createCharCopy(copyChars);
                    copyOfCopy[i][j] = '#';
                    if (performMove2(copyOfCopy, startPosition.posX(), startPosition.posY(), startingDirection, numberOfMoves * 2)) {
                        numberOfLoopsToReachZero++;
                    }
                }
            }
        }

        System.out.println("Number of different positions that creates a loop: " + numberOfLoopsToReachZero);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
    }

    private static char[][] createCharCopy(char[][] original) {
        char[][] copyChars = new char[original.length][original[0].length];

        for (int i = 0; i < original.length; i++) {
            // Copy each inner array
            copyChars[i] = new char[original[i].length];
            System.arraycopy(original[i], 0, copyChars[i], 0, original[i].length);
        }

        return copyChars;
    }

    private static boolean performMove2(char[][] grid, int row, int col, char startingDirection, int moves) {
        while (moves > 0) {
            int startPosX = row;
            int startPosY = col;

            int[] updated = Util.updateGrid(row, col, startingDirection);
            row = updated[0];
            col = updated[1];

            grid[startPosX][startPosY] = 'X';
            if (Util.isTileOutOfRange(grid, row, col)) {
                return false;
            }

            if (isTileValid(grid, row, col)) {
                grid[row][col] = startingDirection;
                moves--;
            } else {
                // change direction
                startingDirection = startingDirection == '^' ? '>' : startingDirection == '>' ? 'v' : startingDirection == 'v' ? '<' : '^';
                grid[startPosX][startPosY] = startingDirection;
                row = startPosX;
                col = startPosY;
            }
        }
        return true;
    }


    private static int performMove(char[][] grid, int row, int col, char startingDirection) {
        int moves = 0;

        while (true) {
            int startPosX = row;
            int startPosY = col;

            int[] updated = Util.updateGrid(row, col, startingDirection);
            row = updated[0];
            col = updated[1];

            grid[startPosX][startPosY] = 'X';
            if (Util.isTileOutOfRange(grid, row, col)) {
                return moves;
            }

            if (isTileValid(grid, row, col)) {
                grid[row][col] = startingDirection;
                moves++;
            } else {
                // change direction
                startingDirection = startingDirection == '^' ? '>' : startingDirection == '>' ? 'v' : startingDirection == 'v' ? '<' : '^';
                grid[startPosX][startPosY] = startingDirection;
                row = startPosX;
                col = startPosY;
            }
        }
    }

    private static boolean isTileValid(char[][] grid, int row, int col) {
        return grid[row][col] != '#';
    }

}
