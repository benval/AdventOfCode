import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day162 {
    private static final Map<StartPosition, Long> costToPosition = new HashMap<>();
    private static final Map<StartPosition, Integer> directions = new HashMap<>();
    private static final List<StartPosition> queue = new ArrayList<>();

    private static final List<StartPosition> bakwardsQueue = new ArrayList<>();

    // FIXME SHOULD HAVE USED DJIKSTRA?!

    public static void main(String[] args) {
        int startingDirection = 2;

        String inputString = Day16TestData.inputData;

        char[][] grid = Util.convertStringToCharArray(inputString);

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        StartPosition startPosition = Util.getStartPosition(grid, 'S');
        StartPosition endPosition = Util.getStartPosition(grid, 'E');

        queue.add(startPosition);
        costToPosition.put(startPosition, 0L);
        directions.put(startPosition, startingDirection);

        long searchCost = search(grid, queue.get(0), startingDirection, 0, visited);

        while (queue.size() > 1) {
            int size = queue.size();

            StartPosition startPosition1 = queue.get(size - 1);
            Long currentCost = costToPosition.get(startPosition1);
            int direction1 = directions.get(startPosition1);

            searchCost += search(grid, startPosition1, direction1, currentCost, visited);

            if (queue.size() == size) {
                queue.remove(size - 1);
            }
        }

        boolean[][] visited2 = new boolean[grid.length][grid[0].length];
        long tiles = numberOfPotentialTiles(endPosition, visited2, false);
        while (!bakwardsQueue.isEmpty()) {
            StartPosition startPosition1 = bakwardsQueue.remove(bakwardsQueue.size() - 1);
            long l = numberOfPotentialTiles(startPosition1, visited2, true);
            tiles += l;
        }

        tiles++;

        // find all possible ways to get to the end, but everytime the score is higher than the smallest, terminate.

        System.out.println("Lowest score: " + costToPosition.get(endPosition));
        System.out.println("Number of tiles: " + tiles);
    }

    private static long numberOfPotentialTiles(StartPosition endPosition, boolean[][] visited2, boolean special) {
        long numberOfTiles = 0;

        Long costToCurrentPos = costToPosition.get(endPosition);

        int row = endPosition.posX();
        int col = endPosition.posY();

        visited2[row][col] = true;


        for (int d = 0; d < 4; d++) {
            int nextRow = row;
            int nextCol = col;
            if (d == 0) {
                nextCol = col - 1;
            } else if (d == 1) {
                nextRow = row - 1;
            } else if (d == 2) {
                nextCol = col + 1;
            } else {
                nextRow = row + 1;
            }

            if (costToPosition.containsKey(new StartPosition(nextRow, nextCol)) && costToPosition.get(new StartPosition(nextRow, nextCol)) < costToCurrentPos && !visited2[nextRow][nextCol]) {
                numberOfTiles++;
                bakwardsQueue.add(new StartPosition(nextRow, nextCol));
            } else if (special) {
                if (costToPosition.containsKey(new StartPosition(nextRow, nextCol)) && costToPosition.get(new StartPosition(nextRow, nextCol)) == costToCurrentPos + 999 && !visited2[nextRow][nextCol]) {
                    numberOfTiles++;
                    bakwardsQueue.add(new StartPosition(nextRow, nextCol));
                }
            }
        }

        return numberOfTiles;
    }

    private static long search(char[][] grid, StartPosition startPosition, int direction, long score, boolean[][] visited) {
        int row = startPosition.posX();
        int col = startPosition.posY();

        if (row == 10 && col == 1) {
            System.out.println("hei");
        }

        if (grid[row][col] == 'E') {
            visited[row][col] = true;
            return score;
        }

        visited[row][col] = true;

        for (int d = 0; d < 4; d++) {
            long currentScore;
            int nextRow = row;
            int nextCol = col;
            if (d == 0) {
                nextCol = col - 1;
            } else if (d == 1) {
                nextRow = row - 1;
            } else if (d == 2) {
                nextCol = col + 1;
            } else {
                nextRow = row + 1;
            }

            if (!Util.isTileOutOfRange(grid, nextRow, nextCol) && grid[nextRow][nextCol] != '#') {
                if (direction == d) {
                    currentScore = score + 1;
                } else if (direction == 0 && d == 2 || direction == 2 && d == 0 || direction == 1 && d == 3 || direction == 3 && d == 1) {
                    currentScore = score + 2001;
                } else {
                    currentScore = score + 1001;
                }

                if (visited[nextRow][nextCol]) {
                    if (costToPosition.get(new StartPosition(nextRow, nextCol)) > currentScore) {
                        costToPosition.put(new StartPosition(nextRow, nextCol), currentScore);
                        directions.put(new StartPosition(nextRow, nextCol), d);
                        queue.add(new StartPosition(nextRow, nextCol));
                    }

                } else if (!queue.contains(new StartPosition(nextRow, nextCol))) {
                    costToPosition.put(new StartPosition(nextRow, nextCol), currentScore);
                    directions.put(new StartPosition(nextRow, nextCol), d);
                    queue.add(new StartPosition(nextRow, nextCol));
                }
            }
        }

        return score;
    }
}
