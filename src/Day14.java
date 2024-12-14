import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Day14 {

    public static void main(String[] args) {
//        int seconds = 5;
        int seconds = 100;

        int width = 101;
//        int width = 11;
        int height = 103;
//        int height = 7;

        String inputString = Day14TestData.inputData;
        String[] robots = inputString.split("\n");
        Map<Integer, Integer> integerMap = calculateRobotPosition(robots, seconds, width, height);

        int safetyFactor = calculateSafetyFactor(integerMap);

        System.out.println("Safety factor: " + safetyFactor);


        // TODO
        //  Part 2 determine if it becomes a christmas tree.... after n-seconds

        // introduser grafikk?

        int timestamp = 9000;
        int safety = 215476074;
        for (int i = 6285; i < 9000; i++) {
            Map<Integer, Integer> integerMap2 = calculateRobotPosition(robots, i, width, height);

            int safetyFactor2 = calculateSafetyFactor(integerMap2);

            if (safetyFactor2 < safety) {
                safety = safetyFactor2;
                timestamp = i;
                System.out.println("timestamp: " + timestamp);
            }

//            char[][] chars = printMap(robots, i, width, height);
//            visualizeGrid(chars, i);
        }
    }

    public static void visualizeGrid(char[][] grid, int secdonds) {
        System.out.println("\nCurrent Grid after " + secdonds + " seconds: ");
        for (char[] chars : grid) {
            for (char aChar : chars) {
                if (aChar != 'x') {
                    System.out.print(" ");
                } else {
                    System.out.print(aChar);
                }
            }
            System.out.println();
        }
        System.out.println("_____________________________________________________________________");
    }

    private static char[][] printMap(String[] robots, int seconds, int width, int height) {
        char[][] grid = new char[height][width];

        for (String robot : robots) {
            String startX = robot.substring(robot.indexOf('p') + 2, robot.indexOf(','));
            String startY = robot.substring(robot.indexOf(',') + 1, robot.indexOf(' '));

            String temp = robot.substring(robot.indexOf(' ') + 1);

            String startXVelocity = temp.substring(temp.indexOf('v') + 2, temp.indexOf(','));
            String startYVelocity = temp.substring(temp.indexOf(',') + 1);

            int x = Integer.parseInt(startX);
            int y = Integer.parseInt(startY);
            int xVelocity = Integer.parseInt(startXVelocity);
            int yVelocity = Integer.parseInt(startYVelocity);

            x = (((x + xVelocity * seconds) % width) + width) % width;
            y = (((y + yVelocity * seconds) % height) + height) % height;

            // we know the robot's position after x-seconds
            grid[y][x] = 'x';
        }

        return grid;
    }


    private static int calculateSafetyFactor(Map<Integer, Integer> integerMap) {
        AtomicInteger safetyFactor = new AtomicInteger(1);

        // find quadrants
        integerMap.forEach((integer, integer2) -> safetyFactor.updateAndGet(v -> v * integer2));

        return safetyFactor.get();
    }

    private static Map<Integer, Integer> calculateRobotPosition(String[] robots, int seconds, int width, int height) {
        Map<Integer, Integer> positions = new HashMap<>();
        int ignoreRow = height / 2;
        int ignoreCol = width / 2;

        for (String robot : robots) {
            String startX = robot.substring(robot.indexOf('p') + 2, robot.indexOf(','));
            String startY = robot.substring(robot.indexOf(',') + 1, robot.indexOf(' '));

            String temp = robot.substring(robot.indexOf(' ') + 1);

            String startXVelocity = temp.substring(temp.indexOf('v') + 2, temp.indexOf(','));
            String startYVelocity = temp.substring(temp.indexOf(',') + 1);

            int x = Integer.parseInt(startX);
            int y = Integer.parseInt(startY);
            int xVelocity = Integer.parseInt(startXVelocity);
            int yVelocity = Integer.parseInt(startYVelocity);

            x = (((x + xVelocity * seconds) % width) + width) % width;
            y = (((y + yVelocity * seconds) % height) + height) % height;

            // we know the robot's position after x-seconds
            if (x != ignoreCol && y != ignoreRow) {
                // add it to correct quadrant
                if (x < ignoreCol && y < ignoreRow) {
                    positions.compute(1, (k, v) -> v == null ? 1 : v + 1);
                } else if (x > ignoreCol && y < ignoreRow) {
                    positions.compute(2, (k, v) -> v == null ? 1 : v + 1);
                } else if (x < ignoreCol) {
                    positions.compute(3, (k, v) -> v == null ? 1 : v + 1);
                } else {
                    positions.compute(4, (k, v) -> v == null ? 1 : v + 1);
                }
            }
        }

        return positions;
    }

}
