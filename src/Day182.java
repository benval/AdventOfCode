import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day182 {

    private static Map<StartPosition, Long> costToPosition = new HashMap<>();
    private static List<StartPosition> queue = new ArrayList<>();

    public static void main(String[] args) {

        String inputData = Day18TestData.inputData;

        String[] fallenPos = inputData.split("\n");

        int size = 71;
        int numberOfFallingBytes = 2874;

        char[][] grid = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '.';
            }
        }

        for (int i = 0; i < numberOfFallingBytes; i++) {
            int row = Integer.parseInt(fallenPos[i].substring(0, fallenPos[i].indexOf(',')));
            int col = Integer.parseInt(fallenPos[i].substring(fallenPos[i].indexOf(',') + 1));
            grid[col][row] = '#';
        }

        int row = 0;
        int col = 0;

        for (int i = numberOfFallingBytes; i < fallenPos.length; i++) {
            row = Integer.parseInt(fallenPos[i].substring(0, fallenPos[i].indexOf(',')));
            col = Integer.parseInt(fallenPos[i].substring(fallenPos[i].indexOf(',') + 1));
            grid[col][row] = '#';

            if (row == 58 && col == 1) {
                System.out.println("hei");
            }

            queue = new ArrayList<>();
            costToPosition = new HashMap<>();

            boolean[][] visited = new boolean[size][size];
            StartPosition startPosition = new StartPosition(0, 0);
            queue.add(startPosition);
            costToPosition.put(startPosition, 1L);

            long length = search(grid, startPosition, 0, visited);

            while (queue.size() > 1) {
                int size2 = queue.size();

                StartPosition startPosition1 = queue.get(size2 - 1);
                Long currentCost = costToPosition.get(startPosition1);

                length += search(grid, startPosition1, currentCost, visited);

                if (queue.size() == size2) {
                    queue.remove(size2 - 1);
                }
            }

            if (!visited[size - 1][size - 1]) {
                break;
            }
        }

        System.out.println("Row: " + row + " , Col: " + col);

    }

    private static long search(char[][] grid, StartPosition startPosition, long score, boolean[][] visited) {
        int row = startPosition.posX();
        int col = startPosition.posY();

        if (row == grid.length - 1 && col == grid[0].length - 1) {
            visited[row][col] = true;
            return score;
        }

        visited[row][col] = true;

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

            long currentScore = score + 1;
            if (!Util.isTileOutOfRange(grid, nextRow, nextCol) && grid[nextRow][nextCol] != '#') {
                if (visited[nextRow][nextCol]) {
                    if (costToPosition.get(new StartPosition(nextRow, nextCol)) > currentScore) {
                        costToPosition.put(new StartPosition(nextRow, nextCol), currentScore);
                        queue.add(new StartPosition(nextRow, nextCol));
                    }

                } else if (!queue.contains(new StartPosition(nextRow, nextCol))) {
                    costToPosition.put(new StartPosition(nextRow, nextCol), currentScore);
                    queue.add(new StartPosition(nextRow, nextCol));
                }
            }
        }

        return score;
    }
}
