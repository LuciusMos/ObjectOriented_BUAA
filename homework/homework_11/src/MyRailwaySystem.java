import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyRailwaySystem implements RailwaySystem {
    private HashMap<Integer, MyPath> idAsKey;
    private HashMap<MyPath, Integer> pathAsKey;
    private RepeatableSet allNodeSet;
    private int id;
    private ArrayList<Integer> unusedIndex;
    private ArrayList<Integer> usedIndex;
    private ArrayList<Integer> startIndex;
    private HashMap<Integer, Integer> startNode; //int->120
    private ArrayList<ArrayList<Integer>> edge;
    private int[] nodeIndexAsKey;
    //containsEdge问题
    private HashSet<Pair> simpleEdge; //Pair<nodeId, nodeId>
    //四个最短路径问题
    private WeightGraph shortestPathLength;
    private WeightGraph leastTicketPirce;
    private WeightGraph leastTransferCount;
    private WeightGraph leastUnpleasantValue;
    //连通性问题
    private int connectedBlockCount;
    private HashMap<Integer, Integer> father; //nodeId->father
    
    public MyRailwaySystem() {
        idAsKey = new HashMap<>();
        pathAsKey = new HashMap<>();
        allNodeSet = new RepeatableSet();
        id = 0;
        unusedIndex = new ArrayList<>();
        for (int i = 5000; i >= 240; i--) {
            unusedIndex.add(i);
        }
        usedIndex = new ArrayList<>();
        startIndex = new ArrayList<>();
        for (int i = 119; i >= 0; i--) {
            startIndex.add(i);
        }
        startNode = new HashMap<>();
        edge = new ArrayList<>();
        for (int i = 0; i < 240; i++) {
            edge.add(new ArrayList<>());
        }
        nodeIndexAsKey = new int[4500];
        simpleEdge = new HashSet<>();
        shortestPathLength = new WeightGraph(false,
                0, 1, this);
        leastTicketPirce = new WeightGraph(false,
                2, 1, this);
        leastTransferCount = new WeightGraph(false,
                1, 0, this);
        leastUnpleasantValue = new WeightGraph(true,
                32, 0, this);
        connectedBlockCount = 0;
        father = new HashMap<>();
    }
    
    private int allocAndInsert(Integer nodeId) {
        int index;
        int start;
        int end;
        if (!startNode.containsKey(nodeId)) { //新出现的点
            start = startIndex.remove(startIndex.size() - 1);
            startNode.put(nodeId, start);
            end = start + 120;
            index = unusedIndex.remove(unusedIndex.size() - 1);
            nodeIndexAsKey[index] = nodeId;
            edge.add(new ArrayList<>());
            edge.get(index).add(end);
            edge.get(end).add(start);
            edge.get(start).add(index);
            shortestPathLength.startendWeightWrite(index, start, end,
                    true);
            leastTicketPirce.startendWeightWrite(index, start, end,
                    true);
            leastTransferCount.startendWeightWrite(index, start, end,
                    true);
            leastUnpleasantValue.startendWeightWrite(index, start, end,
                    true);
        } else { //换乘点
            index = unusedIndex.remove(unusedIndex.size() - 1);
            nodeIndexAsKey[index] = nodeId;
            edge.add(new ArrayList<>());
            start = startNode.get(nodeId);
            end = start + 120;
            edge.get(index).add(end);
            edge.get(start).add(index);
            shortestPathLength.startendWeightWrite(index, start, end,
                    false);
            leastTicketPirce.startendWeightWrite(index, start, end,
                    false);
            leastTransferCount.startendWeightWrite(index, start, end,
                    false);
            leastUnpleasantValue.startendWeightWrite(index, start, end,
                    false);
        }
        usedIndex.add(index);
        usedIndex.add(start);
        usedIndex.add(end);
        /*shortestPathLength.startendEdgeWeight(index, start, end);
        leastTicketPirce.startendEdgeWeight(index, start, end);
        leastTransferCount.startendEdgeWeight(index, start, end);
        leastUnpleasantValue.startendEdgeWeight(index, start, end);*/
        return index;
    }
    
    private void clear() {
        unusedIndex.clear();
        for (int i = 5000; i >= 240; i--) {
            unusedIndex.add(i);
        }
        usedIndex.clear();
        startIndex.clear();
        for (int i = 119; i >= 0; i--) {
            startIndex.add(i);
        }
        startNode.clear();
        edge.clear();
        for (int i = 0; i < 240; i++) {
            edge.add(new ArrayList<>());
        }
        simpleEdge.clear();
        shortestPathLength.refresh();
        leastTicketPirce.refresh();
        leastTransferCount.refresh();
        leastUnpleasantValue.refresh();
        connectedBlockCount = 0;
        father.clear();
    }
    
    private void graphRefresh() {
        clear();
        HashSet<MyPath> myPaths = new HashSet<>(pathAsKey.keySet());
        ArrayList<Integer> nodes;
        HashMap<Integer, Integer> nodeIdAsKey = new HashMap<>(); //每个Path独立
        for (MyPath myPath : myPaths) {
            nodeIdAsKey.clear();
            nodes = myPath.getNodeList();
            for (int nodeId : nodes) {
                if (!nodeIdAsKey.containsKey(nodeId)) {
                    int index = allocAndInsert(nodeId);
                    nodeIdAsKey.put(nodeId, index);
                    if (!father.containsKey(nodeId)) {
                        father.put(nodeId, nodeId);
                    }
                }
            }
            int length = nodes.size();
            int nodeId1;
            int nodeId2;
            int nodeIndex1;
            int nodeIndex2;
            for (int i = 0; i < length - 1; i++) {
                //continasEdge需求
                nodeId1 = nodes.get(i);
                nodeId2 = nodes.get(i + 1);
                Pair pair = new Pair(nodeId1, nodeId2);
                simpleEdge.add(pair);
                pair = new Pair(nodeId2, nodeId1);
                simpleEdge.add(pair);
                join(nodeId1, nodeId2);
                //重建四个图
                nodeIndex1 = nodeIdAsKey.get(nodeId1);
                nodeIndex2 = nodeIdAsKey.get(nodeId2);
                if (nodeIndex1 != nodeIndex2) {
                    edge.get(nodeIndex1).add(nodeIndex2);
                    edge.get(nodeIndex2).add(nodeIndex1);
                    shortestPathLength.normalWeightWrite(
                            nodeIndex1, nodeIndex2);
                    leastTicketPirce.normalWeightWrite(
                            nodeIndex1, nodeIndex2);
                    leastTransferCount.normalWeightWrite(
                            nodeIndex1, nodeIndex2);
                    leastUnpleasantValue.normalWeightWrite(
                            nodeIndex1, nodeIndex2);
                    /*shortestPathLength.normalEdgeWeight(nodeIndex1,
                            nodeIndex2);
                    leastTicketPirce.normalEdgeWeight(nodeIndex1, nodeIndex2);
                    leastTransferCount.normalEdgeWeight(nodeIndex1, nodeIndex2);
                    leastUnpleasantValue.normalEdgeWeight(
                            nodeIndex1, nodeIndex2);*/
                }
            }
        }
        HashSet<Integer> allNodes = allNodeSet.allNodes();
        for (Integer nodeId : allNodes) { find(nodeId); }
        HashSet<Integer> roots = new HashSet<>();
        for (int nodeId : allNodes) {
            roots.add(father.get(nodeId));
        }
        connectedBlockCount = roots.size();
    }
    
    private void join(int u, int v) {
        int fu = find(u);
        int fv = find(v);
        if (fu != fv) {
            father.put(fu, fv);
        }
    }
    
    private int find(int x) {
        int root = x;
        while (!father.get(root).equals(root)) { //求根节点r
            root = father.get(root);
        }
        int iter = x;
        int temp;
        while (iter != root) { //路径压缩
            temp = father.get(iter);
            father.put(iter, root);
            iter = temp;
        }
        return root;
    }
    
    public int addPath(Path path) {
        MyPath myPath = (MyPath) path;
        if (myPath == null || !myPath.isValid()) {
            return 0;
        } else {
            if (containsPath(myPath)) {
                try {
                    return getPathId(myPath);
                } catch (PathNotFoundException ex) {
                    return 0;
                }
            } else {
                id++;
                idAsKey.put(id, myPath);
                pathAsKey.put(myPath, id);
                allNodeSet.addAll(myPath.getNodeList());
                graphRefresh();
                return id;
            }
        }
    }
    
    public int removePath(Path path) throws PathNotFoundException {
        MyPath myPath = (MyPath) path;
        if (myPath == null || !myPath.isValid() || !containsPath(myPath)) {
            throw new PathNotFoundException(myPath);
        } else {
            int pathId = getPathId(myPath);
            removePathWithPathAndId(myPath, pathId);
            return pathId;
        }
    }
    
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            MyPath myPath = (MyPath) getPathById(pathId);
            removePathWithPathAndId(myPath, pathId);
        }
    }
    
    private void removePathWithPathAndId(MyPath myPath, Integer pathId) {
        idAsKey.remove(pathId);
        pathAsKey.remove(myPath);
        allNodeSet.removeAll(myPath.getNodeList());
        graphRefresh();
    }
    
    public boolean isConnected(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException {
        if (!startNode.containsKey(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!startNode.containsKey(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            return (father.get(fromNodeId).equals(father.get(toNodeId)));
        }
    }
    
    public int getConnectedBlockCount() {
        return connectedBlockCount;
    }
    
    public int getShortestPathLength(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        return shortestDistanceProblem(fromNodeId, toNodeId,
                shortestPathLength);
        
    }
    
    public int getLeastTicketPrice(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        return shortestDistanceProblem(fromNodeId, toNodeId,
                leastTicketPirce);
    }
    
    public int getLeastTransferCount(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        return shortestDistanceProblem(fromNodeId, toNodeId,
                leastTransferCount);
    }
    
    public int getLeastUnpleasantValue(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        return shortestDistanceProblem(fromNodeId, toNodeId,
                leastUnpleasantValue);
    }
    
    private int shortestDistanceProblem(int fromNodeId, int toNodeId,
                                        WeightGraph weightGraph)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!startNode.containsKey(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!startNode.containsKey(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        } else {
            int fromIndex = startNode.get(fromNodeId);
            int toIndex = startNode.get(toNodeId) + 120;
            return weightGraph.calDist(fromIndex, toIndex);
        }
    }
    
    public int size() {
        return idAsKey.size();
    }
    
    public boolean containsPath(Path path) {
        if (path != null) {
            MyPath myPath = (MyPath) path;
            return pathAsKey.containsKey(myPath);
        } else {
            return false;
        }
    }
    
    public boolean containsPathId(int pathId) {
        return idAsKey.containsKey(pathId);
    }
    
    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            return idAsKey.get(pathId);
        }
    }
    
    public int getPathId(Path path) throws PathNotFoundException {
        MyPath myPath = (MyPath) path;
        if (myPath == null || !myPath.isValid() || !containsPath(myPath)) {
            throw new PathNotFoundException(myPath);
        } else {
            return pathAsKey.get(myPath);
        }
    }
    
    public int getDistinctNodeCount() {
        return allNodeSet.size();
    }
    
    public boolean containsNode(int nodeId) {
        return allNodeSet.contains(nodeId);
    }
    
    public boolean containsEdge(int fromNodeId, int toNodeId) {
        return simpleEdge.contains(new Pair(fromNodeId, toNodeId));
    }
    
    ArrayList<Integer> getUsedIndex() {
        return usedIndex;
    }
    
    int[] getNodeIndexAsKey() {
        return nodeIndexAsKey;
    }
    
    ArrayList<ArrayList<Integer>> getEdge() {
        return edge;
    }
    
    public int getUnpleasantValue(Path path, int fromIndex, int toIndex) {
        return 0;
    }
}
