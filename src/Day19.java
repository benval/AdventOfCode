import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) {
        int possibleDesigns = 0;

        String inputData = Day19TestData.inputData;

        String[] availablePatterns = inputData.substring(0, inputData.indexOf('\n')).replace(" ", "").split(",");
        String[] designs = inputData.substring(inputData.indexOf('\n') + 1).trim().split("\n");

        Arrays.sort(availablePatterns, Comparator.comparingInt(String::length).reversed());

        Map<String, Boolean> knownDesign = new HashMap<>();
        for (String design : designs) {
            if (isDesignPossible(availablePatterns, design, knownDesign)) {
                possibleDesigns++;
            }
        }

        System.out.println("Number of possible designs: " + possibleDesigns);
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
