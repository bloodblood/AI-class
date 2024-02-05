package AIp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileToArray {
    public static void main(String[] args) {
    }

    public int[][] mazeInitial(){
        String filePath = "D:\\GWU Learning\\2024spring\\AI\\p1\\P1_Option1_Maze\\P1_Option1_Maze\\Maze.txt";
        List<String> lines = readFileToArray(filePath);
        int[][] maze = new int[81][81];
        // Printing the contents of the array
        int j = 0;
        for (String line : lines) {
            String[] lineToArray = line.split(" ");
            for(int i = 0; i < 81; i++) {
                maze[j][i] = Integer.parseInt(lineToArray[i]);
                //System.out.print(maze[j][i]+" ");
            }
           // System.out.println(" ");
        }
        return maze;
    }
    private static List<String> readFileToArray(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}