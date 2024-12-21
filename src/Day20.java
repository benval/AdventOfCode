import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Day20 {

    private static final Map<StartPosition, Long> costToPosition = new HashMap<>();
    private static final List<StartPosition> queue = new ArrayList<>();

    public static void main(String[] args) {

        String inputData = Day20TestData.inputData;

        char[][] grid = Util.convertStringToCharArray(inputData);

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        StartPosition startPosition = Util.getStartPosition(grid, 'S');
        StartPosition endPosition = Util.getStartPosition(grid, 'E');

        // Allowed to cheat, by skipping 2 consequtive blocks

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

        Long newLength = costToPosition.get(new StartPosition(endPosition.posX(), endPosition.posY()));
        System.out.println("ShortestPath: " + newLength);

        long numberOfCheats = numberOfPotentialCheats2();

//        long numberOfCheats = numberOfPotentialCheats(true);
//        numberOfCheats += numberOfPotentialCheats(false);

        System.out.println("Antall cheats: " + numberOfCheats);

        // calculate paths with cheat

    }

    private static long numberOfPotentialCheats2() {
        AtomicLong numberOfCheats = new AtomicLong();

        List<Map.Entry<StartPosition, Long>> sortedEntries = new ArrayList<>(costToPosition.entrySet());
        List<Map.Entry<StartPosition, Long>> norrmalEntries = new ArrayList<>(costToPosition.entrySet());

        Comparator<Map.Entry<StartPosition, Long>> comparator = Comparator.comparingLong((Map.Entry<StartPosition, Long> entry) -> -entry.getKey().posX())
                .thenComparingInt(entry -> entry.getKey().posY());

        sortedEntries.sort(comparator);
        sortedEntries.forEach(entry -> {

            StartPosition k = entry.getKey();
            long value = entry.getValue();

            for (Map.Entry<StartPosition, Long> norrmalEntry : norrmalEntries) {

                StartPosition key = norrmalEntry.getKey();
                long nValue = norrmalEntry.getValue();

                int xDistance = Math.abs(k.posX() - key.posX());
                int yDistance = Math.abs(k.posY() - key.posY());
                int totalDistance = xDistance + yDistance;

                long savedSeconds = Math.abs(value - nValue);

                if (value > nValue && totalDistance <= 20 && totalDistance > 0 && savedSeconds - totalDistance >= 100) {
                    numberOfCheats.getAndIncrement();
                }
            }
        });

        return numberOfCheats.get();
    }

    private static long numberOfPotentialCheats(boolean sortByRows) {
        AtomicLong numberOfCheats = new AtomicLong();

        AtomicInteger previousRow = new AtomicInteger(-1);
        AtomicInteger previousCol = new AtomicInteger(-1);
        AtomicLong previousScore = new AtomicLong();

        List<Map.Entry<StartPosition, Long>> sortedEntries = new ArrayList<>(costToPosition.entrySet());

        Comparator<Map.Entry<StartPosition, Long>> comparator;

        if (sortByRows) {
            comparator = Comparator.comparingLong((Map.Entry<StartPosition, Long> entry) -> -entry.getKey().posX())
                    .thenComparingInt(entry -> entry.getKey().posY());
        } else {
            comparator = Comparator.comparingLong((Map.Entry<StartPosition, Long> entry) -> -entry.getKey().posY())
                    .thenComparingInt(entry -> entry.getKey().posX());
        }

        sortedEntries.sort(comparator);
        sortedEntries.forEach(entry -> {
            StartPosition k = entry.getKey();
            long value = entry.getValue();

            if (k.posX() == previousRow.get() && Math.abs(k.posY() - previousCol.get()) == 2 && Math.abs(value - previousScore.get()) > 100) {
                numberOfCheats.getAndIncrement();
            } else if (k.posY() == previousCol.get() && Math.abs(k.posX() - previousRow.get()) == 2 && Math.abs(value - previousScore.get()) > 100) {
                numberOfCheats.getAndIncrement();
            }

            previousRow.set(k.posX());
            previousCol.set(k.posY());
            previousScore.set(value);

        });

        return numberOfCheats.get();
    }

    private static long search(char[][] grid, StartPosition startPosition, long score, boolean[][] visited) {
        int row = startPosition.posX();
        int col = startPosition.posY();

        if (grid[row][col] == 'E') {
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
