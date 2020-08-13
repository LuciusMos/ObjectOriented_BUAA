import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class FloydHashMap {
    private HashMap<Pair<Integer, Integer>, Integer> edge; //edge->count
    private HashMap<Pair<Integer, Integer>, Integer> dis; //path->dis
    private final RepeatableSet allNodeSet;
    
    FloydHashMap(RepeatableSet allNodeSet) {
        edge = new HashMap<>();
        dis = new HashMap<>();
        this.allNodeSet = allNodeSet;
    }
    
    void addAll(ArrayList<Integer> nodes) {
        int length = nodes.size();
        int node1;
        int node2;
        for (int i = 0; i < length - 1; i++) {
            node1 = nodes.get(i);
            node2 = nodes.get(i + 1);
            Pair<Integer, Integer> temp1 = new Pair<>(node1, node2);
            Pair<Integer, Integer> temp2 = new Pair<>(node2, node1);
            hashMapPut(temp1);
            hashMapPut(temp2);
        }
    }
    
    void removeAll(ArrayList<Integer> nodes) {
        int length = nodes.size();
        int node1;
        int node2;
        for (int i = 0; i < length - 1; i++) {
            node1 = nodes.get(i);
            node2 = nodes.get(i + 1);
            Pair<Integer, Integer> temp1 = new Pair<>(node1, node2);
            Pair<Integer, Integer> temp2 = new Pair<>(node2, node1);
            hashMapDelete(temp1);
            hashMapDelete(temp2);
        }
    }
    
    void calShortestPath() {
        dis.clear();
        for (Pair<Integer, Integer> pair : edge.keySet()) {
            dis.put(pair, 1);
        }
        HashSet<Integer> allNodes = allNodeSet.allNodes();
        Pair<Integer, Integer> ii;
        for (int i : allNodes) {
            ii = new Pair<>(i, i);
            dis.put(ii, 0);
        }
        Pair<Integer, Integer> ij;
        Pair<Integer, Integer> ik;
        Pair<Integer, Integer> kj;
        for (int k : allNodes) {
            for (int i : allNodes) {
                for (int j : allNodes) {
                    ik = new Pair<>(i, k);
                    if (!dis.containsKey(ik)) { continue; }
                    kj = new Pair<>(k, j);
                    if (!dis.containsKey(kj)) { continue; }
                    ij = new Pair<>(i, j);
                    int newdis = dis.get(ik) + dis.get(kj);
                    if (!dis.containsKey(ij) || dis.get(ij) > newdis) {
                        dis.put(ij, newdis);
                    }
                }
            }
        }
    }
    
    private void hashMapPut(Pair<Integer, Integer> key) {
        if (edge.containsKey(key)) {
            edge.put(key, edge.get(key) + 1);
        } else {
            edge.put(key, 1);
        }
    }
    
    private void hashMapDelete(Pair<Integer, Integer> key) {
        if (edge.get(key) != 1) {
            edge.put(key, edge.get(key) - 1);
        } else {
            edge.remove(key);
        }
    }
    
    boolean containsEdge(int fromNodeId, int toNodeId) {
        Pair<Integer, Integer> temp = new Pair<>(fromNodeId, toNodeId);
        return edge.containsKey(temp);
    }
    
    boolean isConnected(int fromNodeId, int toNodeId) {
        Pair<Integer, Integer> temp = new Pair<>(fromNodeId, toNodeId);
        return dis.containsKey(temp);
    }
    
    int getShortestPathLength(int fromNodeId, int toNodeId) {
        Pair<Integer, Integer> temp = new Pair<>(fromNodeId, toNodeId);
        return dis.get(temp);
    }
}
