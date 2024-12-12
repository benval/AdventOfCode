import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Day9 {
    public static void main(String[] args) {

        // first position is length of file
        // second position is size of file
        // third position is length of next fil
        // etc.

        String exampleInput = "2333133121414131402";
        String testInput = Day9TestData.inputdata;

        Result expandString = expandStrings(exampleInput);

        List<String> sortedString = sortString(expandString.getResultList(), expandString.getIndex());
        BigInteger i = calculateSum(sortedString);

        System.out.println("Resulting filesystem checksum " + i);
    }

    private static Result expandStrings(String input) {
        // Initialize a list to dynamically store results
        List<String> resultList = new ArrayList<>();

        int index = 0;
        for (int i = 0; i < input.length(); i++) {
            int i1 = Integer.parseInt(input.substring(i, i + 1));

            if (i % 2 == 0) {
                for (int j = 0; j < i1; j++) {
                    resultList.add(String.valueOf(index));
                }
            } else {
                for (int j = 0; j < i1; j++) {
                    resultList.add(".");
                }
                index++;
            }
        }

        return new Result(resultList, index);
    }

    private static List<String> sortString(List<String> input, int indexOfLatestNumber) {
        int index;

        int length = input.size();

        while (indexOfLatestNumber > 0) {
            index = input.indexOf(".");
            if (input.get(index).equals(".")) {
                // find numberOfConsecutiveDotsNeeded for the latest index
                CountAndEndLength res = numberOfConsecutiveDotsNeededForLatestIndex(input, indexOfLatestNumber, index, length);
                if (res == null) {
                    break;
                }
                length = res.getEndLength();

                // find available spot
                int start = findXConvecutiveDots(input, index, length, res.getCount());

                // find something to move
                if (start != -1) {
                    for (int j = start; j < start + res.getCount(); j++) {
                        input.set(j, String.valueOf(indexOfLatestNumber));
                        input.set(length - 1, ".");
                        length--;
                    }
                    indexOfLatestNumber--;
                } else {
                    indexOfLatestNumber--;
                    length = length - res.getCount() - 1;
                }
            }
        }
        return input;
    }

    private static int findXConvecutiveDots(List<String> input, int startSearchPos, int endLength, int x) {
        int numberOfDotsFound = 0;

        for (int i = startSearchPos; i < endLength; i++) {
            if (input.get(i).equals(".")) {
                numberOfDotsFound++;
            } else {
                numberOfDotsFound = 0;
            }

            if (numberOfDotsFound == x) {
                return i - x + 1;
            }
        }

        return -1;
    }

    private static CountAndEndLength numberOfConsecutiveDotsNeededForLatestIndex(List<String> input, int indexOfLatestNumber, int startSearchPos, int endLength) {
        int count = 0;

        for (int i = endLength - 1; i >= startSearchPos; i--) {
            if (input.get(i).equals(".") || Integer.parseInt(String.valueOf(input.get(i))) > indexOfLatestNumber) {
//                endLength--;
            } else if (Integer.parseInt(String.valueOf(input.get(i))) == indexOfLatestNumber) {
                count++;
            } else {
                return new CountAndEndLength(count, endLength);
            }
        }

        return null;

    }

    private static BigInteger calculateSum(List<String> input) {
        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < input.size(); i++) {
            try {
                BigInteger number = new BigInteger(input.get(i));
                BigInteger multiplyValue = number.multiply(BigInteger.valueOf(i));
                sum = sum.add(multiplyValue);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return sum;
    }

}

class Result {
    private final List<String> resultList;
    private final int index;

    public Result(List<String> resultList, int index) {
        this.resultList = resultList;
        this.index = index;
    }

    public List<String> getResultList() {
        return resultList;
    }

    public int getIndex() {
        return index;
    }
}

class CountAndEndLength {
    private final int count;
    private final int endLength;

    public CountAndEndLength(int count, int endLength) {
        this.count = count;
        this.endLength = endLength;
    }

    public int getCount() {
        return count;
    }

    public int getEndLength() {
        return endLength;
    }
}