import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.ArrayList;

class MyInterface extends MyClassOrInterface {
    private UmlInterface umlInterface;
    private ArrayList<MyInterface> fathers = new ArrayList<>();
    //private ArrayList<MyInterface> ancestors = new ArrayList<>();
    
    MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }
    
    void recurse() {
        //ancestors.addAll(fathers);
        for (int i = 0; i < fathers.size(); i++) {
            fathers.addAll(fathers.get(i).fathers);
        }
    }
    
    void update() {
        setVisited();
    }
    
    void update(MyInterface fatherInter) {
        fathers.addAll(fatherInter.getFathers());
        setVisited();
    }
    
    void addFather(MyInterface fatherInterface) {
        this.fathers.add(fatherInterface);
    }
    
    ArrayList<MyInterface> getFathers() {
        return fathers;
    }
    
    UmlInterface getUmlInterface() {
        return umlInterface;
    }
    
}
