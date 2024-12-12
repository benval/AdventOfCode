public class Day62 {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int visitedPositions;

        String inputTask1 = Day6TestData.inputTask1;
        String identifyStartingDirectionRegex = "[\\^<>v]";

        char[][] chars = Util.convertStringToCharArray(inputTask1);

        // find starting direction
        char startingDirection = Util.matchRegExToStringInput(identifyStartingDirectionRegex, inputTask1);

        // find starting position
        StartPosition startPosition = Util.getStartPosition(chars, startingDirection);

        // find valid movement
        performMove(chars, startPosition.posX(), startPosition.posY(), startingDirection);
        visitedPositions = Util.countOccurrencesOfChar(chars, 'X');

        System.out.println("Sum of middle numbers from valid orders: " + visitedPositions);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
    }

    private static void performMove2(char[][] grid, int row, int col, char startingDirection) {
        char[][] possibleObstructions = new char[grid.length][grid[0].length];

        // Kan man avgjøre dette uten å måtte sjekke alle mulige steder å putte ned en 0
        // Type test å sette en 0 på 0,0 og se om du havner i loop -> nei!
        // Type test å sette en 0 på 1,0 og se om du havner i loop -> nei!, etc?
        // Må være en bedre måte å avgjøre om man kan opprette en loop?

        while (true) {
            int startPosX = row;
            int startPosY = col;

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

            grid[startPosX][startPosY] = 'X';
            if (Util.isTileOutOfRange(grid, row, col)) {
                break;
            }

            if (isTileValid(grid, row, col)) {
                grid[row][col] = startingDirection;
            } else {
                // change direction
                startingDirection = startingDirection == '^' ? '>' : startingDirection == '>' ? 'v' : startingDirection == 'v' ? '<' : '^';
                grid[startPosX][startPosY] = startingDirection;
                row = startPosX;
                col = startPosY;
            }
        }
    }


    private static void performMove(char[][] grid, int row, int col, char startingDirection) {
        while (true) {
            int startPosX = row;
            int startPosY = col;

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

            grid[startPosX][startPosY] = 'X';
            if (Util.isTileOutOfRange(grid, row, col)) {
                break;
            }

            if (isTileValid(grid, row, col)) {
                grid[row][col] = startingDirection;
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
