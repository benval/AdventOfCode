public class Day13 {

    private static int costA = 3;
    private static int costB = 1;

    public static void main(String[] args) {
        String inputString = Day13TestData.inputData;

        long tokens = 0;

        String[] machines = inputString.split("\n\n");

        for (String machine : machines) {

            long fewestTokens = findFewestTokens(machine);
            tokens = tokens + fewestTokens;

            System.out.println("The machine used: " + fewestTokens);

        }

        System.out.println("Fewest number of tokens: " + tokens);
    }

    public static long solveEquation(int a1, int b1, int a2, int b2, long tot1, long tot2) {
        int costA = 3;
        int costB = 1;

        long antallB = ((b1 * tot1) - (a1 * tot2)) / (((long) b1 * a2) - ((long) a1 * b2));

        long antallA = (tot1 - a2 * antallB) / a1;

        if (tot1 < a1 * antallA + a2 * antallB || tot2 < b1 * antallA + b2 * antallB) {
            return 0;
        }
        if (tot1 == a1 * antallA + a2 * antallB && tot2 == b1 * antallA + b2 * antallB) {
            return costA * antallA + costB * antallB;
        }

        return 0;

    }


    public static long findFewestTokens(String machine) {
        // Button A
        String AX = machine.substring(machine.indexOf('X') + 2, machine.indexOf(','));
        String AY = machine.substring(machine.indexOf('Y') + 2, machine.indexOf('\n'));

        // Buttton B
        String tempB = machine.substring(machine.indexOf('\n') + 1);
        String BX = tempB.substring(tempB.indexOf('X') + 2, tempB.indexOf(','));
        String BY = tempB.substring(tempB.indexOf('Y') + 2, tempB.indexOf('\n'));

        // Prize
        String tempP = tempB.substring(tempB.indexOf('\n') + 1);
        String PrizeX = tempP.substring(tempP.indexOf('=') + 1, tempP.indexOf(','));
        String PrizeY = tempP.substring(tempP.indexOf('Y') + 2);

        long prizeX = Long.parseLong(PrizeX) + 10000000000000L;
        long prizeY = Long.parseLong(PrizeY) + 10000000000000L;

        long prizeCost = solveEquation(Integer.parseInt(AX), Integer.parseInt(AY), Integer.parseInt(BX), Integer.parseInt(BY), prizeX, prizeY);

        // FIXME original code from part1
        //        long prizeCost = getPrize(Integer.parseInt(AX), Integer.parseInt(AY), Integer.parseInt(BX), Integer.parseInt(BY), prizeX, prizeY);
//        if (prizeCost == -1) {
//            return 0;
//        } else {
//            return prizeCost;
//        }
        return prizeCost;
    }

    public static long getPrize(long ax, long ay, long bx, long by, long totalScoreX, long totalScoreY) {
        long cost = 0;

        long currentScoreX = 0;
        long currentScoreY = 0;

        // potentially 2 possible solutions for each problem

        long attempts = 0;
        while (currentScoreX <= totalScoreX && currentScoreY <= totalScoreY) {

            long ncurrentScoreX;
            long ncurrentScoreY;

            for (long i = 0; i < 10000000000000L; i++) {
                ncurrentScoreX = currentScoreX + bx * i;
                ncurrentScoreY = currentScoreY + by * i;

                if (ncurrentScoreX > totalScoreX || ncurrentScoreY > totalScoreY) {
                    cost = -1;
                    break;
                }

                if (ncurrentScoreX == totalScoreX && ncurrentScoreY == totalScoreY) {
                    cost = costB * i + attempts * costA;
                    break;
                }
            }

            if (cost == -1) {
                attempts++;
                currentScoreX = ax * attempts;
                currentScoreY = ay * attempts;
            } else {
                return cost;
            }

        }

        return cost;
    }

}
