import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.PathNotFoundException;
import com.oocourse.specs2.models.Path;

import java.util.HashMap;

public class MyGraph implements Graph {
    private HashMap<Integer, Path> idAsKey;
    private HashMap<Path, Integer> pathAsKey;
    private RepeatableSet allNodeSet;
    private int id;
    private Floyd floyd;
    //private FloydHashMap floydHashMap;
    
    public MyGraph() {
        idAsKey = new HashMap<>();
        pathAsKey = new HashMap<>();
        allNodeSet = new RepeatableSet();
        id = 0;
        floyd = new Floyd(allNodeSet);
        //floydHashMap = new FloydHashMap(allNodeSet);
    }
    
    public boolean containsNode(int nodeId) {
        return allNodeSet.contains(nodeId);
    }
    
    public boolean containsEdge(int fromNodeId, int toNodeId) {
        //return floydHashMap.containsEdge(fromNodeId, toNodeId);
        try {
            int fromNodeIndex = floyd.lookup(fromNodeId);
            int toNodeIndex = floyd.lookup(toNodeId);
            return floyd.containsEdge(fromNodeIndex, toNodeIndex);
        } catch (NodeIdNotFoundException ex) {
            return false;
        }
    }
    
    public boolean isConnected(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException {
        /*if (!allNodeSet.contains(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!allNodeSet.contains(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            return floydHashMap.isConnected(fromNodeId, toNodeId);
        }*/
        int fromNodeIndex = floyd.lookup(fromNodeId);
        int toNodeIndex = floyd.lookup(toNodeId);
        return floyd.isConnected(fromNodeIndex, toNodeIndex);
    }
    
    public int getShortestPathLength(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        /*if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        } else {
            return floydHashMap.getShortestPathLength(fromNodeId, toNodeId);
        }*/
        int fromNodeIndex = floyd.lookup(fromNodeId);
        int toNodeIndex = floyd.lookup(toNodeId);
        if (!floyd.isConnected(fromNodeIndex, toNodeIndex)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        } else {
            return floyd.getShortestPathLength(fromNodeIndex, toNodeIndex);
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
    
    public int addPath(Path path) {
        MyPath myPath = (MyPath) path;
        if (myPath == null || !myPath.isValid()) {
            return 0;
        } else {
            if (containsPath(myPath)) {
                //return getPathId(myPath);
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
                floyd.addAll(myPath.getNodeList());
                floyd.calShortestPath();
                /*floydHashMap.addAll(myPath.getNodeList());
                floydHashMap.calShortestPath();*/
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
        floyd.removeAll(myPath.getNodeList());
        floyd.calShortestPath();
        /*floydHashMap.removeAll(myPath.getNodeList());
        floydHashMap.calShortestPath();*/
    }
    
    public int getDistinctNodeCount() {
        return allNodeSet.size();
    }
}
