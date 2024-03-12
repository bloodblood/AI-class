package AIp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

 class Queen {
     int col;
     int row;
     List<Integer> domain;

    public Queen(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void createDomain(int n) {
        domain = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            domain.add(i) ;
        }
    }

    public boolean isConflict(Queen q2, int c1, int c2) {
        int lineFit = (c1 - c2) / (row - q2.row);
        return lineFit == -1 || lineFit == 0 || lineFit == 1;
    }
}
 class Arc {
    int r1;
    int r2;

    public Arc(int r1, int r2) {
        this.r1 = r1;
        this.r2 = r2;
    }
}



class CSP {
    private static final int PRINT_CUTOFF = 201;
    private static int MAX_STEPS ;
    private static int N;
    private static int[][] board;
    private static int[] queenCols;
    private static Random random = new Random ();
    private static boolean smartStart = true;
    List<Queen> queens;
    Map<Arc, List<Arc>> conMap;


    public boolean isInConstraintMap(Queen q1, Queen q2, int x1, int x2) {
        if (q2.col < q1.col) {
            // Reverse queen order
            // Constraint map is generated in one "direction"
            return isInConstraintMap(q2, q1, x2, x1);
        }
        List<Arc> constraints = conMap.get(new Arc(q1.col, q2.col));

        // Iterate through all pairs of valid row assignments
        for (Arc conPair : constraints) {
            if (conPair.r1 == x1 && conPair.r2 == x2) {
                return true;
            }
        }
        return false;
    }
    //intelligently initializes a board with N queens
    public static void smartInitializeBoard(){

        //initializes the board with all 0's
        for (int row = 0; row<N; row++){
            for (int col = 0; col<N; col++){
                board[row][col]=0;
            }
        }

        //places each queen in the col with the fewest number of conflicts
        for (int row = 0; row < N; row++){
            int minCol = findMinConColumn(row, board);
            System.out.println("row: "+row+" minCol: "+minCol);
            board[row][minCol] = 1;
            queenCols[row] = minCol;
            Queen tempQueen = new Queen(minCol,row);
            tempQueen.createDomain(N);
        }

    }
    public static int findMinConColumn(int row, int[][] currBoard){

        int minConflicts=Integer.MAX_VALUE;
        //  int minConflicts = -1;
        int minConCol=0;
        int[] conflictArray = new int[N];
        List<Integer> minConflictCols = new ArrayList<Integer>();

        //sets the queen in the row to zero to not overcount conflicts
        int queenCol = queenCols[row];
        currBoard[row][queenCol] = 0;

        //finds the number of conflicts for each column and saves the mininum
        for (int c=0; c<N; c++){
            int conflicts = findConflicts(row, c, currBoard);
            conflictArray[c] = conflicts;
            if (conflicts < minConflicts){
                minConflicts = conflicts;
                minConCol = c;
            }
        }

        //adds all of the cols with the same number of conflicts to an ArrayList
        for (int i=0; i < N; i++){
            if (conflictArray[i] == minConflicts){
                minConflictCols.add(i);
            }
        }

        int size = minConflictCols.size();

        //if there is more than one row with the same number of conflicts, choose one randomly
        if (size != 1){
            int randIndex = random.nextInt(size);
            minConCol = minConflictCols.get(randIndex);
        }

        if (!smartStart){
            currBoard[row][queenCol] = 1;
        }

        return minConCol;
    }

    public static int findConflicts(int row, int col, int[][] currBoard){
        int conflicts = 0;

        //check for other queens in that row
        for (int i=0; i<N; i++){
            if (currBoard[row][i]==1 && i!=col){
                conflicts++;
            }
        }

        //check for other queens in that column
        for (int i=0; i<N; i++){
            if (currBoard[i][col]==1 && i!=row){
                conflicts++;
            }
        }

        //check for other queens in the SE diagonal
        for (int i=0; (row+i)<N && (col+i)<N; i++){
            if (currBoard[row+i][col+i] == 1 && i!=0){
                conflicts++;
            }
        }

        //check for other queens in the SW diagonal
        for (int i=0; (row+i)<N && (col-i) >= 0; i++){
            if (currBoard[row+i][col-i] == 1 && i!=0){
                conflicts++;
            }
        }

        //check for other queens in the NW diagonal
        for (int i=0; (row-i) >= 0 && (col-i) >= 0; i++){
            if (currBoard[row-i][col-i] == 1 && i!=0){
                conflicts++;
            }
        }

        //check for other queens in the NE diagonal
        for (int i=0; (row-i) >= 0 && (col+i) < N; i++){
            if (currBoard[row-i][col+i] == 1 && i!=0){
                conflicts++;
            }
        }

        for(int i=1; (row-i) >= 0 && (col+i) < N; i++){
            Queen q1 = new Queen(col,row);
            Queen q2 = new Queen(col-i,row-i);
            if(q1.isConflict(q2,col,row-i))
                continue;
        }

        return conflicts;

    }
    public static void printBoard(int[][] boardToPrint){
        for (int row = 0; row<N; row++){
            for (int col = 0; col<N; col++){
                int n = boardToPrint[row][col];
                if (n == 1){
                    System.out.print(n + " ");
                }
                else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void fileRead(int N, String filePath) throws IOException {
        String fileName = filePath;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int row = 0;
            int[] queenPositions = new int[N]; // Assuming there are 10 rows (adjust as needed)

            while ((line = reader.readLine()) != null) {
                // Skip comment lines (lines starting with a colon)
                if (line.startsWith("#")) {
                    continue;
                }

                // Parse the queen position (1-indexed)
                int queenColumn = Integer.parseInt(line.trim());
                queenPositions[row] = queenColumn;
                row++;
            }
            reader.close();
            //initializes the board with all 0's
            for (int i = 0; i<N; i++){
                for (int j = 0; j<N; j++){
                    board[i][j]=0;
                }
            }

            //Place and  print the queen positions
            for (int i = 0; i < N; i++) {
                board[i][queenPositions[i]-1] = 1;
                System.out.println("Row " + (i + 1) + ": Queen in column " + queenPositions[i]);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
    public static void startGame(int size, int maxSteps, int source, String filePath) throws IOException {

        N = size;
        MAX_STEPS = maxSteps;

        board = new int[N][N];
        queenCols = new int[N];
        if(source == 0)
            smartInitializeBoard();
        else
            fileRead(N,filePath);
        long startTime = System.currentTimeMillis();


        if (N < PRINT_CUTOFF) {
            System.out.println("--------------THE BOARD--------------");
            printBoard(board);
        }


        smartStart = false;

        System.out.println("solving with csp search..");
        minConflictsCsp(board, MAX_STEPS);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time in milliseconds: " + (endTime - startTime));
    }

    //solves the board by placing queens into least constraining value
    public static int[][] minConflictsCsp(int[][] currBoard, int maxSteps){

        //maxes out after a certain number of steps
        for (int i=0; i<maxSteps; i++){

            int minConCol = 0;
            //if the board is already solved, stop
            if (validBoard(currBoard)){
                System.out.println("Solved with CSP approach");
                if (N < PRINT_CUTOFF) {
                    printBoard(currBoard);
                }
                return currBoard;
            }

            //ArrayList will hold all conflicted queens
            List<Integer> conflictRows = new ArrayList<Integer>();
            for (int r = 0; r<N; r++){
                int queen = queenCols[r];
                if (findConflicts(r, queen, currBoard) != 0){
                    conflictRows.add(r);
                }
            }

            //choose a random queen from the conflicted ones
            int size = conflictRows.size();
            int randIndex = random.nextInt(size);
            int randomQueenRow = conflictRows.get(randIndex);
            int randomQueenCol = queenCols[randomQueenRow];

            //find the column with the fewest conflicts and move the queen there
            minConCol = findMinConColumn(randomQueenRow, currBoard);
            //System.out.println("row: "+randomQueenRow+" col0: "+randomQueenCol+" col1: "+minConCol);
            //change the board to reflect the new placement
            currBoard[randomQueenRow][randomQueenCol]=0;
            currBoard[randomQueenRow][minConCol]=1;
            queenCols[randomQueenRow] = minConCol;

        }
        System.out.println("CSP approach maxed out at " + maxSteps + " steps.");
        return currBoard;

    }

    public static boolean validBoard(int[][] currBoard){
        for (int row=0; row<N; row++){
            for (int col=0; col<N; col++){
                if (currBoard[row][col]==1){
                    if(findConflicts(row, col, currBoard) != 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public static void main(String[] args) throws IOException {
        //set the board size
        int size = 5;
        int maxSteps = 1000000000;
        // Change this to your local file name
        String filePath = "D:\\GWU Learning\\2024spring\\AI\\p2\\queens.txt";
        // if source == 0 , initialize the board with smart random start; if source = 1, read the input file
        int source = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the size of the board:(input an integer from 10 to 200):");
        size = input.nextInt();
        if(!(size<1000&& size>9)){
            System.out.println("False input! Please retry");return;
        }
        System.out.println("choose to auto-generate a start board or read the input file");
        System.out.println("0(auto generated) or 1(read the file):");
        source = input.nextInt();
        if(!(source == 0 || source == 1)){
            System.out.println("False input! Please retry");return;
        }
        startGame(size, maxSteps, source , filePath);
        new AC3Solver().ac3(new CSP());
        //new Test1(size, maxSteps, 1, tweak);
    }
}

class AC3Solver {
    static Deque<Arc> arcs  = new LinkedList<Arc>();
    public static boolean revise(CSP c, int row1, int row2) {
        boolean revised = false;
        Queen q1 = c.queens.get(row1);
        Queen q2 = c.queens.get(row2);
        List<Integer> tmpDomain = new ArrayList<>();

        for (int x1 : q1.domain) {
            for (int x2 : q2.domain) {
                if (c.isInConstraintMap(q1, q2, x1, x2)) {
                    tmpDomain.add(x1);
                    break;
                }
            }
        }

        if (tmpDomain.size() != q1.domain.size()) {
            q1.domain = tmpDomain;
            revised = true;
        }
        return revised;
    }

    public static boolean ac3(CSP c) {
        while (!arcs.isEmpty()) {
            Arc aqp = arcs.pop();
            int row1 = aqp.r1;
            int row2 = aqp.r2;

            if (revise(c, row1, row2)){
                if (c.queens.get(row1 - 1).domain.isEmpty()) {
                    return false;
                }
                for (Queen qneighbor : c.queens) {
                    if (qneighbor.row == row1 || qneighbor.row == row2) {
                        continue;
                    }
                    arcs.add(new Arc(row1, qneighbor.row));
                }
            }
        }
        return true;
    }
}