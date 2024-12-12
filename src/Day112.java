import java.util.HashMap;
import java.util.Map;

public class Day112 {

    public static void main(String[] args) {
        String input = "0 4 4979 24 4356119 914 85734 698829";
        long res = splitStones(input, 75);
        System.out.println(res);
    }

    public static long splitStones(String input, int blinks) {
        String[] initialStones = input.trim().split(" ");
        Map<Long, Long> stonesMap = new HashMap<>();

        for (String stoneStr : initialStones) {
            long stone = Long.parseLong(stoneStr);
            stonesMap.put(stone, stonesMap.getOrDefault(stone, 0L) + 1);
        }

        for (int blink = 0; blink < blinks; blink++) {
            Map<Long, Long> newStoneCounts = new HashMap<>();

            for (Map.Entry<Long, Long> entry : stonesMap.entrySet()) {
                Long stone = entry.getKey();
                Long count = entry.getValue();

                if (stone == 0) {
                    newStoneCounts.put(1L, newStoneCounts.getOrDefault(1L, 0L) + count);
                } else if (Long.toString(stone).length() % 2 == 0) {
                    String digits = Long.toString(stone);
                    int mid = digits.length() / 2;
                    long left = Long.parseLong(digits.substring(0, mid));
                    long right = Long.parseLong(digits.substring(mid));

                    newStoneCounts.put(left, newStoneCounts.getOrDefault(left, 0L) + count);
                    newStoneCounts.put(right, newStoneCounts.getOrDefault(right, 0L) + count);
                } else {
                    long newStone = stone * 2024;
                    newStoneCounts.put(newStone, newStoneCounts.getOrDefault(newStone, 0L) + count);
                }
            }

            stonesMap = newStoneCounts;
        }

        long totalStones = 0;
        for (long count : stonesMap.values()) {
            totalStones += count;
        }

        return totalStones;
    }
}