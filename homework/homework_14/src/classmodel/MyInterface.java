package classmodel;

import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class MyInterface extends MyClassOrInterface {
    private UmlInterface umlInterface;
    private ArrayList<MyInterface> directFathers = new ArrayList<>();
    private ArrayList<MyInterface> fathers = new ArrayList<>();
    // Circular Inheritance
    private HashSet<UmlInterface> circularInherSet = new HashSet<>();
    private ArrayList<MyInterface> circularInherList = new ArrayList<>();
    // Duplicate Inheritance
    private boolean duplicateInher = false;
    
    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }
    
    private void recurse() {
        for (int i = 0; i < fathers.size(); i++) {
            fathers.addAll(fathers.get(i).directFathers);
        }
    }
    
    public boolean uml008Check() {
        if (circularInher) {
            return true;
        }
        circularDfs(this);
        return circularInher;
    }
    
    private boolean circularInher = false;
    
    private void circularDfs(MyInterface myInterface) {
        if (circularInher) {
            return;
        }
        if (circularInherSet.contains(myInterface.getUmlInterface())) {
            if (circularInherList.get(0).getUmlInterface().getId().equals(
                    myInterface.getUmlInterface().getId())) {
                for (MyInterface circularInter : circularInherList) {
                    circularInter.setCircularInher(true);
                }
            }
            return;
        }
        circularInherList.add(myInterface);
        circularInherSet.add(myInterface.getUmlInterface());
        for (MyInterface fatherInter : myInterface.fathers) {
            circularDfs(fatherInter);
        }
        circularInherList.remove(circularInherList.size() - 1);
        circularInherSet.remove(myInterface.getUmlInterface());
    }
    
    public boolean uml009Check() {
        recurse();
        HashSet<String> fathersId = new HashSet<>();
        for (MyInterface father : fathers) {
            String fatherId = father.getUmlInterface().getId();
            if (fathersId.contains(fatherId)) {
                duplicateInher = true;
                return true;
            }
            fathersId.add(fatherId);
        }
        return false;
    }
    
    public void update() {
        setVisited();
    }
    
    public void update(MyInterface fatherInter) {
        fathers.addAll(fatherInter.getFathers());
        setVisited();
    }
    
    public void addFather(MyInterface fatherInterface) {
        this.directFathers.add(fatherInterface);
        this.fathers.add(fatherInterface);
    }
    
    public ArrayList<MyInterface> getFathers() { return fathers; }
    
    public UmlInterface getUmlInterface() { return umlInterface; }
    
    public boolean getDuplicateInher() { return duplicateInher; }
    
    public void setCircularInher(boolean temp) { circularInher = temp; }
}