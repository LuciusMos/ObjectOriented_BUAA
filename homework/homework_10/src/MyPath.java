import com.oocourse.specs2.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class MyPath implements Path {
    private ArrayList<Integer> nodeList;
    private HashSet<Integer> nodeSet;
    
    ArrayList<Integer> getNodeList() {
        return nodeList;
    }
    
    public MyPath(int... nodeList) {
        int listLength = nodeList.length;
        this.nodeList = new ArrayList<>(listLength);
        this.nodeSet = new HashSet<>();
        for (int node : nodeList) {
            this.nodeList.add(node);
            this.nodeSet.add(node);
        }
    }
    
    public int size() {
        return nodeList.size();
    }
    
    public int getNode(int index) {
        // TODO: requires index >= 0 && index < size();
        return nodeList.get(index);
    }
    
    public boolean containsNode(int node) {
        return nodeSet.contains(node);
    }
    
    public int getDistinctNodeCount() {
        // TODO: I CAN'T UNDERSTAND JML
        return nodeSet.size();
    }
    
    public boolean isValid() {
        return (nodeList.size() >= 2);
    }
    
    @Override
    public int compareTo(Path var1) {
        MyPath var = (MyPath) var1;
        int i;
        int size1 = size();
        int size2 = var.size();
        int size = Math.min(size1, size2);
        for (i = 0; i < size; i++) {
            if (nodeList.get(i) < var.getNodeList().get(i)) { return -1; }
            else if (nodeList.get(i) > var.getNodeList().get(i)) { return 1; }
        }
        return Integer.compare(size1, size2);
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return nodeList.iterator();
    }
    
    @Override
    public boolean equals(Object ob) {
        if (this == ob) { return true; }
        if (ob == null || !(ob instanceof MyPath)) { return false; }
        MyPath myPath = (MyPath) ob;
        int size = size();
        if (size != myPath.getNodeList().size()) { return false; }
        for (int i = 0; i < size; i++) {
            if (!nodeList.get(i).equals(myPath.getNodeList().get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nodeList);
    }
}