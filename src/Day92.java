import java.math.BigInteger;

public class Day92 {
    public static void main(String[] args) {

        // first position is length of file
        // second position is size of file
        // third position is length of next fil
        // etc.

        String exampleInput = "2333133121414131402";
        String testInput = Day9TestData.inputdata;

        String expandString = expandString(testInput);
        int index = expandStringFindIndex(testInput);
        String sortedString = sortString(expandString, index);
        String exampleInput2 = "00992111777.44.333....5555.6666.....8888..";
        BigInteger i = calculateSum(exampleInput2);

        System.out.println("Resulting filesystem checksum " + i);
    }

    private static String expandString(String input) {
        // Fixme what happens if index > 10?

        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int i1 = Integer.parseInt(input.substring(i, i + 1));
            if (i % 2 == 0) {
                stringBuilder.append(String.valueOf(index).repeat(Math.max(0, i1)));
            } else {
                stringBuilder.append(".".repeat(Math.max(0, i1)));
                index++;
            }
        }
        return stringBuilder.toString();
    }

    private static int expandStringFindIndex(String input) {
        // Fixme what happens if index > 10?

        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int i1 = Integer.parseInt(input.substring(i, i + 1));
            if (i % 2 == 0) {
                stringBuilder.append(String.valueOf(index).repeat(Math.max(0, i1)));
            } else {
                stringBuilder.append(".".repeat(Math.max(0, i1)));
                index++;
            }
        }
        return index;
    }

    private static String sortString(String input, int lastIndexNumber) {
        StringBuilder stringBuilder = new StringBuilder();

        int length = input.length();

        // what to do if we have double digits?
        // if stringBuilder.charAt(i) == 9

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(input.charAt(i))) {

                // lastIndexNumber needs to match last digit
                boolean indexOfLastDigit = findSubstringMatchingLatestIndex(input, i, length, lastIndexNumber);
                if (!indexOfLastDigit) {
                    lastIndexNumber--;
                    int extraLength = updateLength(input, i, length, lastIndexNumber);
                    length = length - extraLength;
                    // need to terminate before we get 2 to many 6
                }
                stringBuilder.append(lastIndexNumber);

                // length needs to decrease with 1 extra for each "."

                int length1 = String.valueOf(lastIndexNumber).length();
                length = length - length1;
            } else if (String.valueOf(input.charAt(i)).equals(String.valueOf(lastIndexNumber))) {
                stringBuilder.append(input.charAt(i));
                break;
            } else {
                stringBuilder.append(input.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

    private static boolean findSubstringMatchingLatestIndex(String input, int startIndex, int endIndex, int lastIndexNumber) {
        for (int i = endIndex - 1; i >= startIndex; i--) {
            if (input.substring(i, endIndex).equals(String.valueOf(lastIndexNumber))) {
                return true;
            }
        }
        return false;
    }

    private static int updateLength(String input, int startIndex, int endIndex, int lastIndexNumber) {
        int count = 0;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            if (input.substring(i, endIndex).equals(".")) {
                count++;
            }
            if (input.substring(i, endIndex).equals(String.valueOf(lastIndexNumber))) {
                return count;
            }
        }
        return count;
    }

    private static BigInteger calculateSum(String input) {
        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                continue;
            } else {
                BigInteger number = new BigInteger(String.valueOf(input.charAt(i)));
                BigInteger multiplyValue = number.multiply(BigInteger.valueOf(i));
                sum = sum.add(multiplyValue);
            }
        }
        return sum;
    }

}
