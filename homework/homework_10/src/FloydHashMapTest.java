import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FloydHashMapTest {
    RepeatableSet repeatableSet;
    FloydHashMap floydHashMap;
    
    @Before
    public void setUp() throws Exception {
        repeatableSet = new RepeatableSet();
        floydHashMap = new FloydHashMap(repeatableSet);
        System.out.println("Before.");
    }
    
    @After
    public void tearDown() throws Exception {
        System.out.println("After.");
    }
    
    private ArrayList<Integer> initArrayList(int... nodes) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int node : nodes) {
            temp.add(node);
        }
        return temp;
    }
    
    @Test
    public void addAll() {
        ArrayList<Integer> nodes = initArrayList(1, 2, 2, 3, 4);
        floydHashMap.addAll(nodes);
        Assert.assertTrue(floydHashMap.containsEdge(1, 2));
        Assert.assertTrue(floydHashMap.containsEdge(2, 2));
        Assert.assertTrue(floydHashMap.containsEdge(2, 3));
        Assert.assertTrue(floydHashMap.containsEdge(3, 4));
        Assert.assertFalse(floydHashMap.containsEdge(1, 4));
    }
    
    @Test
    public void removeAll() {
        MyPath myPath = new MyPath(1, 2, 3);
        floydHashMap.addAll(myPath.getNodeList());
        floydHashMap.removeAll(myPath.getNodeList());
        Assert.assertTrue(floydHashMap.containsEdge(1, 2));
    }
    
    @Test
    public void calShortestPath() {
    }
    
    @Test
    public void containsEdge() {
    }
    
    @Test
    public void isConnected() {
    }
    
    @Test
    public void getShortestPathLength() {
    }
}