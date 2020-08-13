import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RepeatableSet {
    private HashMap<Integer, Integer> set;
    
    RepeatableSet() {
        set = new HashMap<>();
    }
    
    private void add(Integer node) {
        if (set.containsKey(node)) {
            set.put(node, set.get(node) + 1);
        } else {
            set.put(node, 1);
        }
    }
    
    void addAll(ArrayList<Integer> nodes) {
        for (Integer node : nodes) {
            add(node);
        }
    }
    
    private void remove(Integer node) {
        if (set.get(node) == 1) {
            set.remove(node);
        } else {
            set.put(node, set.get(node) - 1);
        }
    }
    
    void removeAll(ArrayList<Integer> nodes) {
        for (Integer node : nodes) {
            remove(node);
        }
    }
    
    int size() {
        Set<Integer> keys = set.keySet();
        return keys.size();
    }
}
