import java.math.BigInteger;

public class Day7 {
    public static void main(String[] args) {
        BigInteger validSum = BigInteger.ZERO;

        String[] lines = convertToStringArray(Day7TestData.inputTask1);

        for (String line : lines) {
            if (checkIfLineIsValid(line)) {
                validSum = validSum.add(BigInteger.valueOf(Long.parseLong(line.substring(0, line.indexOf(":")))));
            }
        }

        System.out.println("Sum of middle numbers from valid orders: " + validSum);
    }

    public static String[] convertToStringArray(String multilineString) {
        return multilineString.split("\n");
    }

    private static boolean checkIfLineIsValid(String line) {
        int parseIndex = line.indexOf(":");

        BigInteger sum = BigInteger.valueOf(Long.parseLong(line.substring(0, parseIndex)));

        String leftOverString = line.substring(parseIndex + 2);
        String[] numbers = leftOverString.split(" ");

        // vi har en sum og en liste med tall

        return calculate(sum, numbers);

    }

    private static boolean calculate(BigInteger totalSum, String[] numbers) {
        int numOperators = numbers.length - 1;
        int combinations = (int) Math.pow(3, numOperators);

        for (int i = 0; i < combinations; i++) {
            if (evaluateExpression(numbers, i).equals(totalSum)) {
                return true;
            }
        }
        return false;
    }


    public static BigInteger evaluateExpression(String[] numbers, int opCombination) {
        BigInteger result = new BigInteger(numbers[0]);

        for (int i = 0; i < numbers.length - 1; i++) {

            int operator = (opCombination / (int) Math.pow(3, i)) % 3;
            switch (operator) {
                case 0: // Addition
                    result = result.add(new BigInteger(numbers[i + 1]));
                    break;
                case 1: // Multiplication
                    result = result.multiply(new BigInteger(numbers[i + 1]));
                    break;
                case 2: // Division
                    String concatenated = result + numbers[i + 1];
                    result = new BigInteger(concatenated);
                    break;
            }
        }

        return result;
    }

}
