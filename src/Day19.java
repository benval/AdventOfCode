import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Day19 {

    private static Map<String, Long> numberOfWays = new HashMap<>();

    public static void main(String[] args) {
        int possibleDesigns = 0;
        long numberOfDifferentWaysToMakeDesign = 0;

        String inputData = Day19TestData.inputData;

        String[] availablePatterns = inputData.substring(0, inputData.indexOf('\n')).replace(" ", "").split(",");
        String[] designs = inputData.substring(inputData.indexOf('\n') + 1).trim().split("\n");

        Arrays.sort(availablePatterns, Comparator.comparingInt(String::length));

        Map<String, Boolean> knownDesign = new HashMap<>();
        for (String design : designs) {
            long designPossible2 = isDesignPossible2(availablePatterns, design, knownDesign);
            if (designPossible2 >= 1) {
                possibleDesigns++;
            }
            numberOfDifferentWaysToMakeDesign += designPossible2;
        }

        System.out.println("Number of possible designs: " + possibleDesigns);
        System.out.println("Number of possible ways to make all designs: " + numberOfDifferentWaysToMakeDesign);
    }

    private static long isDesignPossible2(String[] patterns, String design, Map<String, Boolean> knownWorkingDesign) {
        long count = 0;
        if (design.isEmpty()) {
            return 1;
        }

        if (knownWorkingDesign.containsKey(design)) {
            return knownWorkingDesign.get(design) ? numberOfWays.getOrDefault(design, 1L) : 0;
        }

        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                long designPossible2 = isDesignPossible2(patterns, design.substring(pattern.length()), knownWorkingDesign);
                if (designPossible2 >= 1) {
                    knownWorkingDesign.put(design, true);
                }
                count += designPossible2;
            }
        }

        if (knownWorkingDesign.containsKey(design)) {
            if (knownWorkingDesign.get(design)) {
                numberOfWays.put(design, count);
            }
            return count;

        }
        knownWorkingDesign.put(design, false);
        return count;
    }

    private static boolean isDesignPossible(String[] patterns, String design, Map<String, Boolean> knownWorkingDesign) {
        if (design.isEmpty()) {
            return true;
        }

        if (knownWorkingDesign.containsKey(design)) {
            return knownWorkingDesign.get(design);
        }

        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                if (isDesignPossible(patterns, design.substring(pattern.length()), knownWorkingDesign)) {
                    knownWorkingDesign.put(design, true);
                    return true;
                }
            }
        }

        knownWorkingDesign.put(design, false);
        return false;
    }
}
