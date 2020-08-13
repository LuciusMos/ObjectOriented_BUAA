package classmodel;

import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashSet;

public class MyClass extends MyClassOrInterface {
    private UmlClass umlClass;
    // Interface
    private ArrayList<MyInterface> selfInter = new ArrayList<>();
    private ArrayList<MyInterface> allInter = new ArrayList<>();
    private HashSet<String> interOutput = new HashSet<>(); //interId
    //private HashMap<String, MyInterface> allInter;
    // Generalization
    private MyClass father = null;
    private MyClass topFather = null;
    // Circular Inheritance
    private boolean circularInher = false;
    
    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }
    
    public HashSet<String> uml002Check() {
        HashSet<String> result = new HashSet<>(); //return: duplicate
        HashSet<String> names = new HashSet<>(); //calculate: all
        ArrayList<UmlElement> umlElements = new ArrayList<>();
        umlElements.addAll(getSelfAttr());
        umlElements.addAll(getSelfAssoEnd());
        for (UmlElement umlElement : umlElements) {
            String eleName = umlElement.getName();
            if (eleName == null) { continue; }
            if (names.contains(eleName)) {
                result.add(eleName);
                continue;
            }
            names.add(eleName);
        }
        return result;
    }
    
    public HashSet<UmlClass> uml008Check() {
        MyClass iter = this.father;
        HashSet<UmlClass> circleSet = new HashSet<UmlClass>() {{
                add(umlClass); }};
        ArrayList<MyClass> circleList = new ArrayList<>();
        circleList.add(this);
        while (iter != null) {
            if (iter.getUmlClass().equals(getUmlClass())) { //恰好成环
                for (MyClass myClass : circleList) {
                    myClass.setCircularInher(true);
                }
                return circleSet;
            }
            if (circleSet.contains(iter.getUmlClass())) { //成环但有非环元素
                circleSet.clear();
                return circleSet;
            }
            circleSet.add(iter.getUmlClass());
            circleList.add(iter);
            iter = iter.father;
        }
        circleSet.clear();
        return circleSet;
    }
    
    public boolean uml009Check() {
        HashSet<String> intersId = new HashSet<>();
        for (MyInterface myInterface : allInter) {
            if (myInterface.getDuplicateInher()) {
                return true;
            }
            String interId = myInterface.getUmlInterface().getId();
            if (intersId.contains(interId)) {
                return true;
            }
            intersId.add(interId);
        }
        return false;
    }
    
    public void recurse() {
        MyClass iter = this;
        MyClass lastIter = this;
        while (iter != null) {
            update(iter);
            lastIter = iter;
            iter = iter.father;
        }
        calInterAndAssoAndAttr();
        this.topFather = lastIter;
    }
    
    public void addInter(MyInterface myInterface) {
        selfInter.add(myInterface);
        //selfInter.addAll(myInterface.getFathers());
    }
    
    public void addInterFather() {
        ArrayList<MyInterface> tempInter = new ArrayList<>();
        for (MyInterface sonInterface : selfInter) {
            tempInter.addAll(sonInterface.getFathers());
        }
        selfInter.addAll(tempInter);
    }
    
    public Visibility calAttrVisibility(String attributeName)
            throws AttributeNotFoundException, AttributeDuplicatedException {
        ArrayList<UmlAttribute> attributes = getAllAttr();
        Visibility visibility = Visibility.PUBLIC;
        boolean hasOneAttr = false;
        for (UmlAttribute umlAttribute : attributes) {
            if (umlAttribute.getName().equals(attributeName)) {
                if (!hasOneAttr) {
                    visibility = umlAttribute.getVisibility();
                    hasOneAttr = true;
                } else {
                    throw new AttributeDuplicatedException(
                            umlClass.getName(), attributeName);
                }
            }
        }
        if (!hasOneAttr) {
            throw new AttributeNotFoundException(
                    umlClass.getName(), attributeName);
        }
        return visibility;
    }
    
    public void calInterAndAssoAndAttr() {
        // CLASS_IMPLEMENT_INTERFACE_LIST classname (id -> classname)
        for (MyInterface myInterface : allInter) {
            interOutput.add(myInterface.getUmlInterface().getId());
        }
        // CLASS_ASSO_CLASS_LIST classname (id -> classname)
        for (MyClass assoClass : getAllAssoClass()) {
            getAssoClassOutput().add(assoClass.getUmlClass().getId());
        }
        // CLASS_ASSO_COUNT classname
        setAssoCount(getAllAssoClass().size() + getAllAssoInter().size());
    }
    
    public void update(MyClass fatherClass) {
        updateAsso(fatherClass);
        updateAttr(fatherClass);
        updateInter(fatherClass);
        setVisited();
    }
    
    public void update() {
        updateAsso();
        updateAttr();
        updateInter();
        setVisited();
    }
    
    private void updateInter(MyClass fatherClass) {
        allInter.addAll(this.getSelfInter());
        allInter.addAll(fatherClass.getAllInter());
    }
    
    private void updateInter() {
        allInter.addAll(this.getSelfInter());
    }
    
    private void updateAsso(MyClass fatherClass) {
        getAllAssoClass().addAll(this.getSelfAssoClass());
        getAllAssoClass().addAll(fatherClass.getAllAssoClass());
        getAllAssoInter().addAll(this.getSelfAssoInter());
        getAllAssoInter().addAll(fatherClass.getAllAssoInter());
    }
    
    private void updateAsso() {
        getAllAssoClass().addAll(this.getSelfAssoClass());
        getAllAssoInter().addAll(this.getSelfAssoInter());
    }
    
    private void updateAttr(MyClass fatherClass) {
        getAllAttr().addAll(this.getSelfAttr());
        getAllAttr().addAll(fatherClass.getAllAttr());
        String classId = this.getUmlClass().getId();
        for (UmlAttribute umlAttribute : getSelfAttr()) {
            if (umlAttribute.getVisibility() != Visibility.PRIVATE) {
                if (getAllAttrAgainstHidden().containsKey(classId)) {
                    getAllAttrAgainstHidden().get(classId).add(umlAttribute);
                } else {
                    getAllAttrAgainstHidden().put(classId, new
                            ArrayList<UmlAttribute>() {{ add(umlAttribute); }});
                }
            }
        }
        getAllAttrAgainstHidden().putAll(fatherClass.getAllAttrAgainstHidden());
    }
    
    private void updateAttr() {
        getAllAttr().addAll(this.getSelfAttr());
        String classId = this.getUmlClass().getId();
        for (UmlAttribute umlAttribute : getSelfAttr()) {
            if (umlAttribute.getVisibility() != Visibility.PRIVATE) {
                if (getAllAttrAgainstHidden().containsKey(classId)) {
                    getAllAttrAgainstHidden().get(classId).add(umlAttribute);
                } else {
                    getAllAttrAgainstHidden().put(classId, new
                            ArrayList<UmlAttribute>() {{ add(umlAttribute); }});
                }
            }
        }
    }
    
    private ArrayList<MyInterface> getSelfInter() { return selfInter; }
    
    public String getTopFatherName() {
        return topFather.getUmlClass().getName(); }
    
    public void setTopFather(MyClass myClass) { this.topFather = myClass; }
    
    public MyClass getTopFather() { return this.topFather; }
    
    public UmlClass getUmlClass() { return umlClass; }
    
    public MyClass getFather() { return father; }
    
    public void setFather(MyClass fatherClass) { this.father = fatherClass; }
    
    public HashSet<String> getInterOutput() { return interOutput; }
    
    public ArrayList<MyInterface> getAllInter() { return allInter; }
    
    public boolean isCircularInher() { return circularInher; }
    
    public void setCircularInher(boolean circularInher) {
        this.circularInher = circularInher;
    }
}
