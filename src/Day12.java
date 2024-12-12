public class Day12 {
    private static final int[] rowDirections = {-1, 0, 1, 0}; // up, left, down, right
    private static final int[] colDirections = {0, -1, 0, 1};

    public static void main(String[] args) {
        String inputString = Day12TestData.inputData;

        char[][] chars = Util.convertStringToCharArray(inputString);

        long[] price = dfs(chars);

        System.out.println("The price in task 1: " + price[0]);
        System.out.println("The price in task 2: " + price[1]);
    }


    public static long[] dfs(char[][] grid) {
        long[] price = new long[2];

        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node node = dfsHelper(grid, i, j, visited, grid[i][j]);
                price[0] += (long) node.perimeter * node.area; // Part 1
                price[1] += (long) node.sides * node.area; // part 2
            }
        }
        return price;
    }

    private static int isCorner(char[][] grid, int row, int col) {
        int total = 0;

        char currentChar = grid[row][col];

        char left = col - 1 >= 0 ? grid[row][col - 1] : ' ';
        char right = col + 1 < grid[0].length ? grid[row][col + 1] : ' ';
        char up = row - 1 >= 0 ? grid[row - 1][col] : ' ';
        char down = row + 1 < grid.length ? grid[row + 1][col] : ' ';

        char upLeft = row - 1 >= 0 && col - 1 >= 0 ? grid[row - 1][col - 1] : ' ';
        char upRight = row - 1 >= 0 && col + 1 < grid[0].length ? grid[row - 1][col + 1] : ' ';
        char downLeft = row + 1 < grid.length && col - 1 >= 0 ? grid[row + 1][col - 1] : ' ';
        char downRight = row + 1 < grid.length && col + 1 < grid[0].length ? grid[row + 1][col + 1] : ' ';

        if (left != currentChar && up != currentChar
                || upLeft != currentChar && left == currentChar && up == currentChar) {
            total++;
        }
        if (right != currentChar && up != currentChar
                || upRight != currentChar && right == currentChar && up == currentChar) {
            total++;
        }
        if (left != currentChar && down != currentChar) {
            total++;
        } else if (downLeft != currentChar && left == currentChar && down == currentChar) {
            total++;
        }
        if (right != currentChar && down != currentChar
                || downRight != currentChar && right == currentChar && down == currentChar) {
            total++;
        }

        return total;
    }

    private static Node dfsHelper(char[][] grid, int row, int col, boolean[][] visited, char lookingFor) {
        Node node = new Node(0, 0, 0);

        if (row < 0
                || row >= grid.length
                || col < 0
                || col >= grid[0].length) {

            node.setPerimeter(1);
            return node;
        }

        if (grid[row][col] != lookingFor) {
            node.setPerimeter(1);
            return node;
        }

        if (visited[row][col]) {
            return node;
        }

        if (grid[row][col] == lookingFor) {
            node.setArea(node.getArea() + 1);
        }

        int sides = isCorner(grid, row, col);
        node.setSides(sides);

        // Mark the cell as visited
        visited[row][col] = true;
        System.out.println("Visiting: (" + row + ", " + col + ") = " + grid[row][col]);

        // Explore neighbors in the order: up, left, down, right
        for (int d = 0; d < 4; d++) {
            int newRow = row + rowDirections[d];
            int newCol = col + colDirections[d];

            Node node1 = dfsHelper(grid, newRow, newCol, visited, lookingFor);
            node.setPerimeter(node.getPerimeter() + node1.getPerimeter());
            node.setSides(node.getSides() + node1.getSides());
            node.setArea(node.getArea() + node1.getArea());
        }

        return node;
    }

    static class Node {
        int perimeter;
        int area;
        int sides;

        public Node(int perimeter, int area, int sides) {
            this.perimeter = perimeter;
            this.area = area;
            this.sides = sides;
        }

        public int getSides() {
            return sides;
        }

        public void setSides(int sides) {
            this.sides = sides;
        }

        public void setPerimeter(int perimeter) {
            this.perimeter = perimeter;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public int getPerimeter() {
            return perimeter;
        }

        public int getArea() {
            return area;
        }
    }

}
