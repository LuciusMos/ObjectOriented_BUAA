import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class RepeatableSet {
    private HashMap<Integer, Integer> repeatableSet; //nodeId->times
    
    RepeatableSet() {
        repeatableSet = new HashMap<>();
    }
    
    private void add(Integer node) {
        if (repeatableSet.containsKey(node)) {
            repeatableSet.put(node, repeatableSet.get(node) + 1);
        } else {
            repeatableSet.put(node, 1);
        }
    }
    
    void addAll(ArrayList<Integer> nodes) {
        for (Integer node : nodes) {
            add(node);
        }
    }
    
    private void remove(Integer nodeId) {
        if (repeatableSet.get(nodeId) == 1) {
            repeatableSet.remove(nodeId);
        } else {
            repeatableSet.put(nodeId, repeatableSet.get(nodeId) - 1);
        }
    }
    
    void removeAll(ArrayList<Integer> nodes) {
        for (Integer node : nodes) {
            remove(node);
        }
    }
    
    int size() {
        return repeatableSet.size();
    }
    
    boolean contains(Integer nodeId) {
        return repeatableSet.containsKey(nodeId);
    }
    
    HashSet<Integer> allNodes() {
        return new HashSet<>(repeatableSet.keySet());
    }
}
