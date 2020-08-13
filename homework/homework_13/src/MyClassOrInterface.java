import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.oocourse.uml1.models.common.Visibility.PACKAGE;
import static com.oocourse.uml1.models.common.Visibility.PRIVATE;
import static com.oocourse.uml1.models.common.Visibility.PROTECTED;
import static com.oocourse.uml1.models.common.Visibility.PUBLIC;

class MyClassOrInterface {
    // Operation
    private ArrayList<MyOperation> selfOper = new ArrayList<>();
    private int[] operType = {0, 0, 0, 0, 0};
    //NON_RETURN, RETURN, NON_PARAM, PARAM, ALL
    // Attribute
    private ArrayList<UmlAttribute> selfAttr = new ArrayList<>();
    private ArrayList<UmlAttribute> allAttr = new ArrayList<>();
    private HashMap<String, ArrayList<UmlAttribute>> allAttrAgainstHidden =
            new HashMap<>();
    // Association
    private ArrayList<MyClass> selfAssoClass = new ArrayList<>();
    private ArrayList<MyClass> allAssoClass = new ArrayList<>();
    private ArrayList<MyInterface> selfAssoInter = new ArrayList<>();
    private ArrayList<MyInterface> allAssoInter = new ArrayList<>();
    private int assoCount;
    private HashSet<String> assoClassOutput = new HashSet<>(); //assoClassId
    // Recursed
    private boolean visited = false;
    
    void calOper() {
        for (MyOperation operation : selfOper) {
            if (!operation.getIsReturnParameter()) {
                operType[0]++;
            } else {
                operType[1]++;
            }
            if (!operation.getIsInParameter()) {
                operType[2]++;
            } else {
                operType[3]++;
            }
            operType[4]++;
        }
    }
    
    HashMap<Visibility, Integer> calOperVisibility(String operName) {
        int[] operVisibility = {0, 0, 0, 0};
        //public, protected, private, package-private
        for (MyOperation myOperation : selfOper) {
            if (myOperation.getUmlOperation().getName().equals(operName)) {
                switch (myOperation.getVisibility()) {
                    case PUBLIC:
                        operVisibility[0]++;
                        break;
                    case PROTECTED:
                        operVisibility[1]++;
                        break;
                    case PRIVATE:
                        operVisibility[2]++;
                        break;
                    default: //case PACKAGE:
                        operVisibility[3]++;
                        break;
                }
            }
        }
        return new HashMap<Visibility, Integer>() {
            {
                put(PUBLIC, operVisibility[0]);
                put(PROTECTED, operVisibility[1]);
                put(PRIVATE, operVisibility[2]);
                put(PACKAGE, operVisibility[3]);
            }
        };
    }
    
    void addOper(MyOperation operation) { selfOper.add(operation); }
    
    void addAttr(UmlAttribute umlAttribute) { selfAttr.add(umlAttribute); }
    
    void addAssoClass(MyClass myClass) { selfAssoClass.add(myClass); }
    
    void addAssoInter(MyInterface myInterface) {
        selfAssoInter.add(myInterface);
    }
    
    int[] getOperType() {
        return operType;
    }
    
    ArrayList<UmlAttribute> getSelfAttr() {
        return selfAttr;
    }
    
    ArrayList<UmlAttribute> getAllAttr() {
        return allAttr;
    }
    
    HashMap<String, ArrayList<UmlAttribute>> getAllAttrAgainstHidden() {
        return allAttrAgainstHidden;
    }
    
    ArrayList<MyClass> getSelfAssoClass() {
        return selfAssoClass;
    }
    
    ArrayList<MyClass> getAllAssoClass() {
        return allAssoClass;
    }
    
    ArrayList<MyInterface> getSelfAssoInter() {
        return selfAssoInter;
    }
    
    ArrayList<MyInterface> getAllAssoInter() {
        return allAssoInter;
    }

    void setAssoCount(int count) {
        assoCount = count;
    }
    
    int getAssoCount() {
        return assoCount;
    }
    
    HashSet<String> getAssoClassOutput() {
        return assoClassOutput;
    }
    
    void setVisited() {
        visited = true;
    }
    
    boolean getVisited() {
        return visited;
    }
}
