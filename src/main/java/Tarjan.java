import java.util.*;

public class Tarjan {
    private int time;
    private List<Integer>[] graph;
    private int[] discoveryTime;
    private int[] lowTime;
    private boolean[] visited;
    private boolean[] isArticulationPoint;

    public boolean isBiconnected(List<Integer>[] adjList) {
        int n = adjList.length;
        graph = adjList;
        discoveryTime = new int[n];
        lowTime = new int[n];
        visited = new boolean[n];
        isArticulationPoint = new boolean[n];
        time = 0;

        // Initialize arrays
        for (int i = 0; i < n; i++) {
            discoveryTime[i] = -1;
            lowTime[i] = -1;
            visited[i] = false;
            isArticulationPoint[i] = false;
        }

        // Start DFS from vertex 0
        dfs(0, -1);

        // Check if any unvisited vertices exist
        for (int k = 0; k < n; k++) {
            boolean v = visited[k];
            if (!v) {
                System.out.println("The vertex "+k+" is unvisited");
                System.out.println("The graph is not connected");
                return false; // Graph is not connected
            }
        }

        // Check if any articulation points exist
        for (boolean v : isArticulationPoint) {
            if (v) {
                System.out.println("The graph is not biconnected");
                List<Integer> articulationPoints = new Tarjan().findArticulationPoints(adjList);
                for (int point : articulationPoints) {
                    System.out.println(point);
                }
                return false; // Graph is not biconnected
            }
        }
        System.out.println("The graph is biconnected");
        return true; // Graph is biconnected
    }

    // find all articulationPoints
    public List<Integer> findArticulationPoints(List<Integer>[] adjList) {
        int n = adjList.length;
        graph = adjList;
        discoveryTime = new int[n];
        lowTime = new int[n];
        visited = new boolean[n];
        isArticulationPoint = new boolean[n];
        time = 0;

        // Initialize arrays
        for (int i = 0; i < n; i++) {
            discoveryTime[i] = -1;
            lowTime[i] = -1;
            visited[i] = false;
            isArticulationPoint[i] = false;
        }

        // Start DFS from vertex 0
        dfs(0, -1);

        List<Integer> articulationPoints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (isArticulationPoint[i]) {
                articulationPoints.add(i);
            }
        }
        return articulationPoints;
    }

    private void dfs(int current, int parent) {
        visited[current] = true;
        discoveryTime[current] = lowTime[current] = ++time;

        int childCount = 0;
        boolean isArticulation = false;

        for (int v : graph[current]) {
            if (v == parent) {
                continue;
            }
            if (!visited[v]) {
                childCount++;
                dfs(v, current);
                lowTime[current] = Math.min(lowTime[current], lowTime[v]);

                if (discoveryTime[current] <= lowTime[v]) {
                    isArticulation = true;
                }
            } else {
                lowTime[current] = Math.min(lowTime[current], discoveryTime[v]);
            }
        }

        if ((parent == -1 && childCount > 1) || (parent != -1 && isArticulation)) {
            isArticulationPoint[current] = true;
        }
    }



    // ...

    public static void main(String[] args) {
        int n = 1400000; // Number of vertices
        int e = 80000; // Number of edges

        List<Integer>[] adjList = generateRandomGraph(n, e);
        long starttime = System.currentTimeMillis();
        Tarjan tarjan = new Tarjan();
        tarjan.isBiconnected(adjList);
        long endtime = System.currentTimeMillis();
        System.out.println("usedTime: ");
        System.out.println(endtime-starttime);
    }

    private static List<Integer>[] generateRandomGraph(int n, int e) {
        Random random = new Random();
        List<Integer>[] adjList = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }

        for (int i = 0; i < e; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
         // System.out.println(u+"  ,  "+v);
            adjList[u].add(v);
            adjList[v].add(u);
        }

        return adjList;
    }
}