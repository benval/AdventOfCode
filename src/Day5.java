import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {

    private static String sampleData = "47|53\n" +
            "97|13\n" +
            "97|61\n" +
            "97|47\n" +
            "75|29\n" +
            "61|13\n" +
            "75|53\n" +
            "29|13\n" +
            "97|29\n" +
            "53|29\n" +
            "61|53\n" +
            "97|53\n" +
            "61|29\n" +
            "47|13\n" +
            "75|47\n" +
            "97|75\n" +
            "47|61\n" +
            "75|61\n" +
            "47|29\n" +
            "75|13\n" +
            "53|13\n" +
            "\n" +
            "75,47,61,53,29\n" +
            "97,61,53,29,13\n" +
            "75,29,13\n" +
            "75,97,47,61,53\n" +
            "61,13,29\n" +
            "97,13,75,29,47";


    public static void main(String[] args) {
        int sumOfMiddleNumbers = 0;
        int sumOfNewlyOrderedMiddleNumbers = 0;

        // determine the order of things pased on rules
        // check if an order is valid by comparing it to the order of rules
        // extract the middle number value and summarize it


        Map<String, String> orderOfRules = parsePageOrderingRules(Day5TestData.inputTask2);
        String[] ordersToBeChecked = parseUpdatedPages(Day5TestData.inputTask2);

        for (String orderToBeChecked : ordersToBeChecked) {
            String[] oneLineOfNumbers = orderToBeChecked.split(",");
            if (isValidOrder(oneLineOfNumbers, orderOfRules)) {
                sumOfMiddleNumbers += findMiddleNumber(oneLineOfNumbers);
            } else {
                String[] fixed = fixOrder(oneLineOfNumbers, orderOfRules);
                while (!isValidOrder(fixed, orderOfRules)) {
                    fixed = fixOrder(fixed, orderOfRules);
                }

                sumOfNewlyOrderedMiddleNumbers += findMiddleNumber(fixed);
            }
        }

        System.out.println("Sum of middle numbers from valid orders: " + sumOfMiddleNumbers);
        System.out.println("Sum of middle numbers from valid orders: " + sumOfNewlyOrderedMiddleNumbers);
    }

    private static Map<String, String> parsePageOrderingRules(String input) {
        String regex = "\\d\\d\\|\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Map<String, String> dictionary = new HashMap<>();

        while (matcher.find()) {
            String firstNumber = (matcher.group().substring(0, 2));
            String secondNumber = (matcher.group().substring(3, 5));

            dictionary.merge(firstNumber, secondNumber, (a, b) -> a + "," + b);
        }

        return dictionary;
    }

    private static String[] parseUpdatedPages(String input) {
        String substring = input.substring(input.indexOf(",") - 2);
        return substring.split("\n");
    }

    private static boolean isValidOrder(String[] orderToBeChecked, Map<String, String> orderOfRules) {
        // ["75","47","61","53","29"]

        for (int i = 0; i < orderToBeChecked.length; i++) {
            if (orderOfRules.get(orderToBeChecked[i]) == null) {
                continue;
            } else {
                String string = orderOfRules.get(orderToBeChecked[i]);
                String[] numbers = string.split(",");

                for (String number : numbers) {
                    int searchIndex = i;
                    while (searchIndex >= 0) {
                        if (number.equals(orderToBeChecked[searchIndex])) {
                            return false;
                        } else {
                            searchIndex--;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static String[] fixOrder(String[] orderToBeFixed, Map<String, String> orderOfRules) {
        // ["75","47","61","53","29"]
        String[] newOrder = new String[orderToBeFixed.length];

        for (int i = 0; i < orderToBeFixed.length; i++) {
            if (orderOfRules.get(orderToBeFixed[i]) == null) {
                newOrder[i] = orderToBeFixed[i];
            } else {
                String string = orderOfRules.get(orderToBeFixed[i]);

                int newPlacement = getNewPlacement(orderToBeFixed, string, i);

                String temp = newOrder[newPlacement];
                newOrder[i] = temp;
                newOrder[newPlacement] = orderToBeFixed[i];
            }
        }
        return newOrder;
    }

    private static int getNewPlacement(String[] orderToBeFixed, String string, int i) {
        String[] numbers = string.split(",");

        int newPlacement = i;
        for (String number : numbers) {
            int searchIndex = i;
            while (searchIndex >= 0) {
                if (number.equals(orderToBeFixed[searchIndex])) {
                    if (newPlacement > searchIndex) {
                        newPlacement = searchIndex;
                    }
                }
                searchIndex--;
            }
        }
        return newPlacement;
    }

    private static int findMiddleNumber(String[] input) {
        return Integer.parseInt(input[input.length / 2]);
    }
}
