package at.spengergasse;

import java.util.*;

public class GraphAnalyzer {

    private final boolean[][] adj;
    private final int n;

    public GraphAnalyzer(boolean[][] adj) {
        this.adj = adj;
        this.n = adj.length;
    }

    public int[][] computeDistanceMatrix() {
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            bfs(i, dist[i]);
        }

        return dist;
    }

    private void bfs(int start, int[] distRow) {
        Queue<Integer> queue = new LinkedList<>();

        distRow[start] = 0;
        queue.add(start);

        while (!queue.isEmpty()) {
            int node = queue.poll();

            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (adj[node][neighbor] && distRow[neighbor] == Integer.MAX_VALUE) {
                    distRow[neighbor] = distRow[node] + 1;
                    queue.add(neighbor);
                }
            }
        }
    }

    public int[] computeEccentricities(int[][] dist) {
        int[] ecc = new int[n];

        for (int i = 0; i < n; i++) {
            int max = 0;

            for (int j = 0; j < n; j++) {
                if (dist[i][j] != Integer.MAX_VALUE) {
                    max = Math.max(max, dist[i][j]);
                }
            }

            ecc[i] = max;
        }

        return ecc;
    }

    public int computeRadius(int[] ecc) {
        int radius = Integer.MAX_VALUE;

        for (int e : ecc) {
            radius = Math.min(radius, e);
        }

        return radius;
    }

    public int computeDiameter(int[] ecc) {
        int diameter = 0;

        for (int e : ecc) {
            diameter = Math.max(diameter, e);
        }

        return diameter;
    }

    public List<Integer> computeCenter(int[] ecc, int radius) {
        List<Integer> center = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (ecc[i] == radius) {
                center.add(i);
            }
        }

        return center;
    }

    public List<List<Integer>> computeComponents() {
        boolean[] visited = new boolean[n];
        List<List<Integer>> components = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> component = new ArrayList<>();
                dfsComponent(i, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private void dfsComponent(int node, boolean[] visited, List<Integer> component) {
        visited[node] = true;
        component.add(node);

        for (int neighbor = 0; neighbor < n; neighbor++) {
            if (adj[node][neighbor] && !visited[neighbor]) {
                dfsComponent(neighbor, visited, component);
            }
        }
    }

    public int countComponents() {
        return computeComponents().size();
    }

    public List<String> computeBridges() {
        List<String> bridges = new ArrayList<>();

        int originalComponents = countComponents();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                if (adj[i][j]) {
                    adj[i][j] = false;
                    adj[j][i] = false;

                    int newComponents = countComponents();

                    if (newComponents > originalComponents) {
                        bridges.add(i + " - " + j);
                    }

                    adj[i][j] = true;
                    adj[j][i] = true;
                }
            }
        }

        return bridges;
    }

    public List<Integer> computeArticulations() {
        List<Integer> articulations = new ArrayList<>();

        int originalComponents = countComponents();

        for (int node = 0; node < n; node++) {
            boolean[] saved = adj[node].clone();

            for (int i = 0; i < n; i++) {
                adj[node][i] = false;
                adj[i][node] = false;
            }

            int newComponents = countComponents();

            if (newComponents > originalComponents) {
                articulations.add(node);
            }

            for (int i = 0; i < n; i++) {
                adj[node][i] = saved[i];
                adj[i][node] = saved[i];
            }
        }

        return articulations;
    }
}