package AIp1;

import java.util.*;

class Cell {
    int row;
    int col;
    int f; // total cost
    int g; // cost from start to current cell
    int h; // heuristic: estimated cost from current cell to target

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }
}

public class AStarMazeSolver {
    private static final int[][] NEIGHBORS = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}}; // up, left, down, right
    private static final int MAZE_SIZE = 81;

    public static boolean isMazeConnected(int[][] maze, int startX, int startY, int targetX, int targetY) {
        // Validate start and target positions
        if (isValidPosition(startX, startY, maze) || isValidPosition(targetX, targetY, maze))
            return false;

        // Create a 2D array to keep track of visited cells
        boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE];

        // Create a priority queue for open cells sorted by their total cost (f)
        PriorityQueue<Cell> openCells = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));

        // Create a 2D array to store the cost from start to each cell
        int[][] gCost = new int[MAZE_SIZE][MAZE_SIZE];

        // Initialize all costs to a large value
        for (int i = 0; i < MAZE_SIZE; i++) {
            for (int j = 0; j < MAZE_SIZE; j++) {
                gCost[i][j] = Integer.MAX_VALUE;
            }
        }

        // Start cell
        Cell startCell = new Cell(startX, startY);
        startCell.g = 0;
        startCell.h = calculateHeuristic(startX, startY, targetX, targetY);
        startCell.f = startCell.g + startCell.h;

        // Add the start cell to the open set
        openCells.offer(startCell);

        while (!openCells.isEmpty()) {
            Cell currentCell = openCells.poll();

            // Check if target is reached
            if (currentCell.row == targetX && currentCell.col == targetY){
/*                for( boolean[] v: visited){
                    for(boolean vi :v){
                        System.out.print(vi+" ");
                    }
                    System.out.println(" ");
                }*/
                return true;
            }

            visited[currentCell.row][currentCell.col] = true;

            // Explore neighbors
            for (int[] neighbor : NEIGHBORS) {
                int newRow = currentCell.row + neighbor[0];
                int newCol = currentCell.col + neighbor[1];

                // Skip invalid positions and blocked cells
                if (isValidPosition(newRow, newCol, maze) || maze[newRow][newCol] == 1)
                    continue;

                // Calculate the tentative cost from start to neighbor
                int neighborGCost = currentCell.g + 1;

                if (neighborGCost < gCost[newRow][newCol]) {
                    // Update the neighbor's cost and f value
                    gCost[newRow][newCol] = neighborGCost;
                    int neighborHCost = calculateHeuristic(newRow, newCol, targetX, targetY);
                    int neighborFCost = neighborGCost + neighborHCost;

                    Cell neighborCell = new Cell(newRow, newCol);
                    neighborCell.g = neighborGCost;
                    neighborCell.h = neighborHCost;
                    neighborCell.f = neighborFCost;

                    // Add the neighbor to the open set
                    openCells.offer(neighborCell);
                }
            }
        }

        return false; // No path found
    }

    private static boolean isValidPosition(int row, int col, int[][] maze) {
        return row < 0 || col < 0 || row >= MAZE_SIZE || col >= MAZE_SIZE || maze[row][col] != 0;
    }

    private static int calculateHeuristic(int currentX, int currentY, int targetX, int targetY) {
        // Manhattan distance heuristic
        return Math.abs(currentX - targetX) + Math.abs(currentY - targetY);
    }

    public static void main(String[] args) {
        int[][] maze = new ReadFileToArray().mazeInitial();
        // Initialize your maze here with 0s and 1s

        int[][] testArray = {{1,34,15,47},
                {1,2,3,39},{0,0,3,77},{1,75,8,79},{1,75,39,40}};
        boolean isPathExist;
        for (int[] ints : testArray) {
            System.out.println("startX: " + ints[0] + " startY: " + ints[1] + " endX: " + ints[2] + " ENDy: " + ints[3]);
            isPathExist = isMazeConnected(maze, ints[0], ints[1], ints[2], ints[3]);
            if (isPathExist)
                System.out.println("Yes");
            else
                System.out.println("No");
        }



    }
}