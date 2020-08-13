import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashSet;

class MyClass extends MyClassOrInterface {
    private UmlClass umlClass;
    // Interface
    private ArrayList<MyInterface> selfInter = new ArrayList<>();
    private ArrayList<MyInterface> allInter = new ArrayList<>();
    private HashSet<String> interOutput = new HashSet<>(); //interId
    //private HashMap<String, MyInterface> allInter;
    // Generalization
    private MyClass father = null;
    private MyClass topFather = null;
    
    MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }
    
    void recurse() {
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
    
    void addInter(MyInterface myInterface) {
        selfInter.add(myInterface);
        //selfInter.addAll(myInterface.getFathers());
    }
    
    void addInterFather() {
        ArrayList<MyInterface> tempInter = new ArrayList<>();
        for (MyInterface sonInterface : selfInter) {
            tempInter.addAll(sonInterface.getFathers());
        }
        selfInter.addAll(tempInter);
    }
    
    Visibility calAttrVisibility(String attributeName)
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
    
    void calInterAndAssoAndAttr() {
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
    
    void update(MyClass fatherClass) {
        updateAsso(fatherClass);
        updateAttr(fatherClass);
        updateInter(fatherClass);
        setVisited();
    }
    
    void update() {
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
    
    private ArrayList<MyInterface> getSelfInter() {
        return selfInter;
    }
    
    String getTopFatherName() {
        return topFather.getUmlClass().getName();
    }
    
    void setTopFather(MyClass myClass) {
        this.topFather = myClass;
    }
    
    MyClass getTopFather() {
        return this.topFather;
    }
    
    UmlClass getUmlClass() {
        return umlClass;
    }
    
    MyClass getFather() {
        return father;
    }
    
    void setFather(MyClass fatherClass) {
        this.father = fatherClass;
    }
    
    HashSet<String> getInterOutput() {
        return interOutput;
    }
    
    ArrayList<MyInterface> getAllInter() {
        return allInter;
    }
}
