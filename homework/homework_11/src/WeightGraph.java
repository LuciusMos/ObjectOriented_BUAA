import java.util.ArrayList;
import java.util.LinkedList;

class WeightGraph {
    private int edgeWeight; //普通边权重
    private int transferWeight; //换乘边权重
    private boolean unpleasant; //是否是unpleasant问题
    private int[] nodeIndexAsKey;
    private MyRailwaySystem myRailwaySystem;
    private ArrayList<ArrayList<Integer>> weight;
    private ArrayList<ArrayList<Integer>> edge;
    //private HashMap<Pair, Integer> weight;
    //private int[][] weight;
    private int[][] cache;
    private boolean[] isCached;
    private int[] dist;
    private boolean[] visited;
    private static final int notConnected = Integer.MAX_VALUE / 3;
    private int[] unpleasantEdgeWeight;
    //private Pair pair;
    
    WeightGraph(boolean unpleasant, int transferWeight, int edgeWeight,
                MyRailwaySystem myRailwaySystem) {
        this.edgeWeight = edgeWeight;
        this.transferWeight = transferWeight;
        this.unpleasant = unpleasant;
        this.myRailwaySystem = myRailwaySystem;
        this.visited = new boolean[4500];
        for (int i = 0; i < 4500; i++) {
            this.visited[i] = false;
        }
        //this.weight = new int[4500][4500];
        //weight = new HashMap<>();
        weight = new ArrayList<>();
        for (int i = 0; i < 240; i++) {
            weight.add(new ArrayList<>());
        }
        this.edge = myRailwaySystem.getEdge();
        this.cache = new int[120][4500];
        this.isCached = new boolean[120];
        this.dist = new int[4500];
        this.nodeIndexAsKey = myRailwaySystem.getNodeIndexAsKey();
        this.unpleasantEdgeWeight = new int[]{1, 4, 16, 64, 256};
        //pair = new Pair();
    }
    
    void normalEdgeWeight(int nodeIndex1, int nodeIndex2) {
        /*if (unpleasant) {
            int nodeId1 = nodeIndexAsKey[nodeIndex1];
            int nodeId2 = nodeIndexAsKey[nodeIndex2];
            int un = Math.max((nodeId1 % 5 + 5) % 5, (nodeId2 % 5 + 5) % 5);
            edgeWeight = unpleasantEdgeWeight[un];
        }*/
        /*pair = new Pair(nodeIndex1, nodeIndex2);
        weight.put(pair, edgeWeight);
        pair = new Pair(nodeIndex2, nodeIndex1);
        weight.put(pair, edgeWeight);*/
        /*weight[nodeIndex1][nodeIndex2] = edgeWeight;
        weight[nodeIndex2][nodeIndex1] = edgeWeight;*/
    }
    
    void startendEdgeWeight(int index, int start, int end) {
        /*pair = new Pair(index, end);
        weight.put(pair, 0);
        pair = new Pair(end, start);
        weight.put(pair, transferWeight);
        pair = new Pair(start, index);
        weight.put(pair, 0);*/
        /*weight[index][end] = 0;
        weight[end][start] = transferWeight;
        weight[start][index] = 0;*/
    }
    
    void refresh() {
        weight.clear();
        for (int i = 0; i < 240; i++) {
            weight.add(new ArrayList<>());
        }
        for (int i = 0; i < 120; i++) {
            isCached[i] = false;
        }
    }
    
    int calDist(int fromIndex, int toIndex) {
        if (isCached[fromIndex]) {
            return cache[fromIndex][toIndex];
        }
        //dij(fromIndex, toIndex);
        spfa(fromIndex, toIndex);
        isCached[fromIndex] = true;
        ArrayList<Integer> usedIndex = myRailwaySystem.getUsedIndex();
        for (int i : usedIndex) {
            cache[fromIndex][i] = dist[i];
        }
        return dist[toIndex];
    }
    
    /*private void dij(int fromIndex, int toIndex) {
        ArrayList<Integer> usedIndex = myRailwaySystem.getUsedIndex();
        for (int i : usedIndex) {
            visited[i] = false;
            pair = new Pair(fromIndex, i);
            dist[i] = weight.get(pair);
        }
        dist[fromIndex] = 0;
        visited[fromIndex] = true;
        int count = usedIndex.size() - 1;
        while (count > 0) {
            int min = notConnected;
            int index = 1; //may have bugs
            for (int i : usedIndex) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    index = i;
                }
            }
            count--;
            visited[index] = true;
            for (int i : usedIndex) {
                pair = new Pair(index, i);
                if (!visited[i] &&
                        (dist[index] + weight.get(pair) < dist[i])) {
                    dist[i] = dist[index] + weight.get(pair);
                }
            }
        }
    }*/
    
    private void spfa(int fromIndex, int toIndex) {
        ArrayList<Integer> usedIndex = myRailwaySystem.getUsedIndex();
        for (int i : usedIndex) {
            dist[i] = notConnected;
            visited[i] = false;
        }
        dist[fromIndex] = 0;
        visited[fromIndex] = true;
        //Queue<Integer> queue = new LinkedList<Integer>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(fromIndex);
        while (!queue.isEmpty()) {
            int u = queue.removeFirst();
            visited[u] = false;
            for (int i = 0; i < edge.get(u).size(); i++) {
                int v = edge.get(u).get(i);
                int uv = weight.get(u).get(i);
                if (dist[u] + uv < dist[v]) {
                    dist[v] = dist[u] + uv;
                    if (!visited[v]) {
                        queue.add(v);
                        visited[v] = true;
                    }
                }
            }
        }
    }
    
    void startendWeightWrite(int index, int start, int end, boolean firstTime) {
        weight.add(new ArrayList<>());
        weight.get(index).add(0);
        weight.get(start).add(0);
        if (firstTime) {
            weight.get(end).add(transferWeight);
        }
    }
    
    void normalWeightWrite(int nodeIndex1, int nodeIndex2) {
        if (unpleasant) {
            int nodeId1 = nodeIndexAsKey[nodeIndex1];
            int nodeId2 = nodeIndexAsKey[nodeIndex2];
            int un = Math.max((nodeId1 % 5 + 5) % 5, (nodeId2 % 5 + 5) % 5);
            edgeWeight = unpleasantEdgeWeight[un];
        }
        weight.get(nodeIndex1).add(edgeWeight);
        weight.get(nodeIndex2).add(edgeWeight);
    }
}
