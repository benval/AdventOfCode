import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {

    public static void main(String[] args) {
        int numberOfStones;

        int[] exampleData = {125, 17};
        int[]inputData = {0, 4, 4979, 24, 4356119, 914, 85734, 698829};

        long startTime = System.nanoTime();
        int numberOfBlinks = 25;

        List<Long> integers = splitStones(inputData, numberOfBlinks);
        numberOfStones = integers.size();

//        If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
//        If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
//        If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.

        System.out.println("Number of stones after " + numberOfBlinks + " blinks: " + numberOfStones);

        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
    }

    private static List<Long> splitStones(int[] listOfStones, int numberOfBlinks) {
        List<Long> newStones = Arrays.stream(listOfStones).asLongStream().boxed().collect(Collectors.toList());

        for (int i = 0; i < numberOfBlinks; i++) {
            newStones = splitStones2(newStones);
        }

        return newStones;
    }

    private static List<Long> splitStones2(List<Long> listOfStones) {
        List<Long> newStones2 = new ArrayList<>();

        for (Long stone : listOfStones) {
            if (stone == 0) {
                newStones2.add(1L);
            } else if (String.valueOf(stone).length() % 2 == 0) {
                String firstHalf = String.valueOf(stone).substring(0, String.valueOf(stone).length() / 2);
                String secondHalf = String.valueOf(stone).substring(String.valueOf(stone).length() / 2);
                newStones2.add(Long.parseLong(firstHalf));
                newStones2.add(Long.parseLong(secondHalf));
            } else {
                newStones2.add(stone * 2024);
            }
        }
        return newStones2;
    }


}
