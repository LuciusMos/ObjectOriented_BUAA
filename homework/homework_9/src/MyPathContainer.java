import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    private HashMap<Integer, Path> idAsKey;
    private HashMap<Path, Integer> pathAsKey;
    private RepeatableSet allNodeSet;
    private int id;
    
    public MyPathContainer() {
        idAsKey = new HashMap<>();
        pathAsKey = new HashMap<>();
        allNodeSet = new RepeatableSet();
        id = 0;
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
                return id;
            }
        }
    }
    
    public int removePath(Path path) throws PathNotFoundException {
        MyPath myPath = (MyPath) path;
        if (myPath == null || !myPath.isValid() || !containsPath(myPath)) {
            throw new PathNotFoundException(myPath);
        } else {
            int id = getPathId(myPath);
            idAsKey.remove(id);
            pathAsKey.remove(myPath);
            allNodeSet.removeAll(myPath.getNodeList());
            return id;
        }
    }
    
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            MyPath myPath = (MyPath) getPathById(pathId);
            idAsKey.remove(pathId);
            pathAsKey.remove(myPath);
            allNodeSet.removeAll(myPath.getNodeList());
        }
    }
    
    public int getDistinctNodeCount() {
        return allNodeSet.size();
    }
}
