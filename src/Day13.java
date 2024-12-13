public class Day13 {

    private static int costA = 3;
    private static int costB = 1;

    public static void main(String[] args) {
        String inputString = Day13TestData.inputExample1;

        long tokens = 0;

        String[] machines = inputString.split("\n\n");

        for (String machine : machines) {

            long fewestTokens = findFewestTokens(machine);
            tokens = tokens + fewestTokens;

            System.out.println("The machine used: " + fewestTokens);

        }

        System.out.println("Fewest number of tokens: " + tokens);
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

        long prizeCost = getPrize(Integer.parseInt(AX), Integer.parseInt(AY), Integer.parseInt(BX), Integer.parseInt(BY), prizeX, prizeY, true);

        if (prizeCost == -1) {
            return 0;
        } else {
            return prizeCost;
        }

    }

    public static long getPrize(long ax, long ay, long bx, long by, long totalScoreX, long totalScoreY, boolean isAFirst) {
        long cost = 0;

        long currentScoreX = 0;
        long currentScoreY = 0;

        // potentially 2 possible solutions for each problem

        long attempts = 100000000000L;
        while (currentScoreX <= totalScoreX && currentScoreY <= totalScoreY) {

            long ncurrentScoreX;
            long ncurrentScoreY;

            for (long i = 100000000000L; i < 10000000000000L; i++) {
                ncurrentScoreX = currentScoreX + bx * i;
                ncurrentScoreY = currentScoreY + by * i;

                if (ncurrentScoreX > totalScoreX || ncurrentScoreY > totalScoreY) {
                    cost = -1;
                    break;
                }

                if (ncurrentScoreX == totalScoreX && ncurrentScoreY == totalScoreY) {
                    if (isAFirst) {
                        cost = costB * i + attempts * costA;
                    } else {
                        cost = costA * i + attempts * costB;
                    }
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
