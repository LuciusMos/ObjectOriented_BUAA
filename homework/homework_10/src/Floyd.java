import com.oocourse.specs2.models.NodeIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

class Floyd {
    private HashMap<Integer, Integer> nodeIdAsKey; //int->250
    private int[][] edge; // 存储边的个数
    private int[][] dis;
    private ArrayList<Integer> unusedIndex;
    private ArrayList<Integer> usedIndex;
    private final RepeatableSet allNodeSet;
    private final int notConnected = 1000;
    
    Floyd(RepeatableSet allNodeSet) {
        nodeIdAsKey = new HashMap<>();
        edge = new int[250][250];
        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 250; j++) {
                edge[i][j] = 0;
            }
        }
        dis = new int[250][250];
        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 250; j++) {
                if (i == j) {
                    dis[i][j] = 0; //存在长为0的路径（自圈）
                } else {
                    dis[i][j] = notConnected; //不存在路径
                }
            }
        }
        usedIndex = new ArrayList<>();
        unusedIndex = new ArrayList<>();
        for (int i = 249; i >= 0; i--) {
            unusedIndex.add(i);
        }
        this.allNodeSet = allNodeSet;
    }
    
    void addAll(ArrayList<Integer> nodes) {
        for (int nodeId : nodes) {
            allocAndInsert(nodeId);
        }
        int length = nodes.size();
        int node1;
        int node2;
        for (int i = 0; i < length - 1; i++) {
            try {
                node1 = nodes.get(i);
                node1 = lookup(node1);
                node2 = nodes.get(i + 1);
                node2 = lookup(node2);
                if (node1 != node2) {
                    edge[node1][node2]++;
                    edge[node2][node1]++;
                } else {
                    edge[node1][node1]++;
                }
            } catch (NodeIdNotFoundException ex) { return; }
        }
    }
    
    void removeAll(ArrayList<Integer> nodes) {
        int length = nodes.size();
        int node1;
        int node2;
        for (int i = 0; i < length - 1; i++) {
            try {
                node1 = nodes.get(i);
                node1 = lookup(node1);
                node2 = nodes.get(i + 1);
                node2 = lookup(node2);
                if (node1 != node2) {
                    edge[node1][node2]--;
                    edge[node2][node1]--;
                } else {
                    edge[node1][node1]--;
                }
            } catch (NodeIdNotFoundException ex) { return; }
        }
        for (int nodeId : nodes) {
            free(nodeId);
        }
    }
    
    void calShortestPath() {
        for (int i : usedIndex) {
            for (int j : usedIndex) {
                if (i == j) { continue; }
                if (edge[i][j] > 0) {
                    dis[i][j] = 1;
                } else {
                    dis[i][j] = notConnected;
                }
                
            }
        }
        for (int k : usedIndex) {
            for (int i : usedIndex) {
                for (int j : usedIndex) {
                    if (dis[i][k] != notConnected && dis[k][j] != notConnected
                            && dis[i][k] + dis[k][j] < dis[i][j]) {
                        dis[i][j] = dis[i][k] + dis[k][j];
                    }
                }
            }
        }
    }
    
    private void allocAndInsert(Integer nodeId) {
        if (nodeIdAsKey.containsKey(nodeId)) {
            return;
        }
        int index = unusedIndex.remove(unusedIndex.size() - 1);
        usedIndex.add(index);
        nodeIdAsKey.put(nodeId, index);
    }
    
    private void free(Integer nodeId) {
        if (!nodeIdAsKey.containsKey(nodeId)) { return; } //已经free过（重复）
        if (allNodeSet.contains(nodeId)) { return; } //不需要释放
        int index = nodeIdAsKey.get(nodeId);
        nodeIdAsKey.remove(nodeId, index);
        unusedIndex.add(index);
        usedIndex.remove(Integer.valueOf(index));
    }
    
    int lookup(Integer nodeId) throws NodeIdNotFoundException {
        if (!nodeIdAsKey.containsKey(nodeId)) {
            throw new NodeIdNotFoundException(nodeId);
        } else {
            return nodeIdAsKey.get(nodeId);
        }
    }
    
    boolean containsEdge(int fromNodeIndex, int toNodeIndex) {
        return (edge[fromNodeIndex][toNodeIndex] > 0);
    }
    
    boolean isConnected(int fromNodeIndex, int toNodeIndex) {
        return (dis[fromNodeIndex][toNodeIndex] < notConnected);
    }
    
    int getShortestPathLength(int fromNodeIndex, int toNodeIndex) {
        return dis[fromNodeIndex][toNodeIndex];
    }
}
