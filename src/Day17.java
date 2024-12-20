public class Day17 {

    private static long registerA;
    private static long registerB;
    private static long registerC;

    public static void main(String[] args) {

        String inputData = Day17TestData.inputData;

        String[] split = inputData.split("\n");

        registerA = Long.parseLong(split[0].substring(split[0].indexOf(":") + 2));
        registerB = Long.parseLong(split[1].substring(split[1].indexOf(":") + 2));
        registerC = Long.parseLong(split[2].substring(split[2].indexOf(":") + 2));

        long initialA = registerA;
        long initialB = registerB;
        long initialC = registerC;

        String part2 = split[4].substring(split[4].indexOf(":") + 2);
        String[] program = part2.split(",");

        StringBuilder output = new StringBuilder();

        long iteration = 0;

        while (!output.toString().equals(part2)) {
            output = new StringBuilder();
            registerB = initialB;
            registerC = initialC;

            iteration++;
            registerA = initialA + iteration;

            runOpcode(program, output);

            output.deleteCharAt(output.length() - 1);

            System.out.println("Attempted output: " + output);

            long finalA2 = initialA + iteration;
            if (finalA2 > 999729324600000L) {
                System.out.println("Missed");
                break;
            }
//            if (output.substring(output.length()-27).equals(part2.substring(part2.length()-27))) {
//                System.out.println("SJEKK OPP I: " + finalA2);
//                break;
//            }

            System.out.println("Current register A iteration : " + finalA2);
        }

        long finalA = initialA + iteration;
        System.out.println("The lowest positive initial value found is: " + finalA);
    }

    // answer is between 9997337967362 and 999729324600000
//    9997337967362
//    47906345178982
//    99973649857688
//    999729324600000
    // that is still 989732031354000 values to calculate

    private static void runOpcode(String[] program, StringBuilder output) {
        int instructionPointer;
        for (instructionPointer = 0; instructionPointer < program.length; instructionPointer++) {
            int opcode = Integer.parseInt(program[instructionPointer]);
            long operand = Long.parseLong(program[instructionPointer + 1]);

            switch (opcode) {
                case 0:
                    registerA = (long) (registerA / Math.pow(2, determineComboOperand(operand)));
                    break;
                case 1:
                    registerB = registerB ^ operand;
                    break;
                case 2:
                    registerB = determineComboOperand(operand) % 8;
                    break;
                case 3:
                    if (registerA != 0) {
                        instructionPointer = Math.toIntExact(operand) - 2;
                    }
                    break;
                case 4:
                    registerB = registerB ^ registerC;
                    break;
                case 5:
                    long ong = determineComboOperand(operand) % 8;
                    output.append(ong).append(",");
                    break;
                case 6:
                    registerB = (long) (registerA / Math.pow(2, determineComboOperand(operand)));
                    break;
                case 7:
                    registerC = (long) (registerA / Math.pow(2, determineComboOperand(operand)));
                    break;
            }

            instructionPointer++;
        }
    }

    private static long determineComboOperand(long value) {
        long result = 0;
        if (value == 0 || value == 1 || value == 2 || value == 3) {
            result = value;
        } else if (value == 4) {
            result = registerA;
        } else if (value == 5) {
            result = registerB;
        } else if (value == 6) {
            result = registerC;
        } else if (value == 7) {
            result = -1;
        }
        return result;
    }

}
