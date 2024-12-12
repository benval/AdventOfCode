import java.util.*;

public class Day8 {
    public static void mainTask1(String[] args) {
        int numberOfUniqueAntinodes = 0;
        Set<String> uniqueAntinodes = new HashSet<>();

        // find all pairs of antennas
        // create a map that contains a key and the positions of all values for that Key
        char[][] chars = Util.convertStringToCharArray(Day8TestData.inputdata);
        Map<Character, List<String>> digitPositions = findCharPositions(chars);


        // Display the results
        for (Map.Entry<Character, List<String>> entry : digitPositions.entrySet()) {
            if (entry.getValue().size() > 1) {
                // FIXME potensielt: vi antar at en antinode kan være på samme sted som en annen inntil videre


                // TODO anta at man ikke kan ha en antinode på samme sted som en annen inntil videre

                // Generate all combinations of pairs
                for (int i = 0; i < entry.getValue().size(); i++) {
                    for (int j = i + 1; j < entry.getValue().size(); j++) {
                        int[] firstPair = extractCoordinates(entry.getValue().get(i));
                        int[] secondPair = extractCoordinates(entry.getValue().get(j));

                        int x = firstPair[0];
                        int y = firstPair[1];
                        int a = secondPair[0];
                        int b = secondPair[1];

                        int antinode1X = x + (x - a);
                        int antinode1Y = y + (y - b);

                        if (antinode1X < chars.length && antinode1X >= 0 && antinode1Y >= 0 && antinode1Y < chars[0].length && !uniqueAntinodes.contains(antinode1X + "," + antinode1Y)) {
                            uniqueAntinodes.add(antinode1X + "," + antinode1Y);
                            numberOfUniqueAntinodes++;
                        }

                        int antinode2X = a + (a - x);
                        int antinode2Y = b + (b - y);

                        if (antinode2X < chars.length && antinode2X >= 0 && antinode2Y >= 0 && antinode2Y < chars[0].length && !uniqueAntinodes.contains(antinode2X + "," + antinode2Y)) {
                            uniqueAntinodes.add(antinode2X + "," + antinode2Y);
                            numberOfUniqueAntinodes++;
                        }

                    }
                }
            }


            System.out.println("Digit '" + entry.getKey() + "' found at: " + entry.getValue());
        }

        System.out.println("Number of antinodes: " + numberOfUniqueAntinodes);
    }

    public static void main(String[] args) {
        int numberOfUniqueAntinodes = 0;
        Set<String> uniqueAntinodes = new HashSet<>();

        char[][] chars = Util.convertStringToCharArray(Day8TestData.inputdata);
        // find all pairs of antennas
        // create a map that contains a key and the positions of all values for that Key
        Map<Character, List<String>> charPositions = findCharPositions(chars);

        for (Map.Entry<Character, List<String>> entry : charPositions.entrySet()) {
            if (entry.getValue().size() > 1) {
                // Generate all combinations of pairs
                numberOfUniqueAntinodes = numberOfUniqueAntinodes + calculateNumberOfUniqueAntinodes(chars, entry, uniqueAntinodes);
            }

            System.out.println("Digit '" + entry.getKey() + "' found at: " + entry.getValue());
        }

        System.out.println("Number of unique antinodes: " + numberOfUniqueAntinodes);
    }

    private static int calculateNumberOfUniqueAntinodes(char[][] chars, Map.Entry<Character, List<String>> entry, Set<String> uniqueAntinodes) {
        int numberOfUniqueAntinodes = 0;
        for (int i = 0; i < entry.getValue().size(); i++) {
            for (int j = i + 1; j < entry.getValue().size(); j++) {
                int[] firstPair = extractCoordinates(entry.getValue().get(i));
                int[] secondPair = extractCoordinates(entry.getValue().get(j));

                int x = firstPair[0];
                int y = firstPair[1];
                int a = secondPair[0];
                int b = secondPair[1];

                numberOfUniqueAntinodes = numberOfUniqueAntinodes + uniqueAntinodesInOneDirection(chars, uniqueAntinodes, x, y, a, b);
                numberOfUniqueAntinodes = numberOfUniqueAntinodes + uniqueAntinodesInOneDirection(chars, uniqueAntinodes, a, b, x, y);
            }
        }
        return numberOfUniqueAntinodes;
    }

    private static int uniqueAntinodesInOneDirection(char[][] chars, Set<String> uniqueAntinodes, int a, int b, int x, int y) {
        int numberOfUniqueAntinodes = 0;
        int increment = 0;
        int j1 = a;
        int j2 = b;

        while (j1 < chars.length && j2 < chars[0].length && j1 >= 0 && j2 >= 0) {
            if (!uniqueAntinodes.contains(j1 + "," + j2)) {
                uniqueAntinodes.add(j1 + "," + j2);
                numberOfUniqueAntinodes++;
            }
            increment++;
            j1 = a + (a - x) * increment;
            j2 = b + (b - y) * increment;
        }

        return numberOfUniqueAntinodes;
    }

    private static int[] extractCoordinates(String position) {
        String[] parts = position.split(", ");
        int i = Integer.parseInt(parts[0].split("=")[1]);
        int j = Integer.parseInt(parts[1].split("=")[1]);
        return new int[]{i, j};
    }

    public static Map<Character, List<String>> findCharPositions(char[][] array) {
        Map<Character, List<String>> map = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                char currentChar = array[i][j];

                if (Character.isDigit(currentChar) || Character.isAlphabetic(currentChar)) {
                    // Get or create the list for the current digit
                    List<String> positions = map.computeIfAbsent(currentChar, k -> new ArrayList<>());
                    // Add the current position to the list
                    positions.add("i=" + i + ", j=" + j);
                }
            }
        }

        return map;
    }
}
