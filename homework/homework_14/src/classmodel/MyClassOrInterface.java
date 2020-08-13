package classmodel;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    private ArrayList<UmlAssociationEnd> selfAssoEnd = new ArrayList<>();
    private ArrayList<UmlAssociationEnd> allAssoEnd = new ArrayList<>();
    private int assoCount;
    private HashSet<String> assoClassOutput = new HashSet<>(); //assoClassId
    // Recursed
    private boolean visited = false;
    
    public void calOper() {
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
    
    public HashMap<Visibility, Integer> calOperVisibility(String operName) {
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
                put(Visibility.PUBLIC, operVisibility[0]);
                put(Visibility.PROTECTED, operVisibility[1]);
                put(Visibility.PRIVATE, operVisibility[2]);
                put(Visibility.PACKAGE, operVisibility[3]);
            }
        };
    }
    
    public void addOper(MyOperation operation) { selfOper.add(operation); }
    
    public void addAttr(UmlAttribute umlAttribute) {
        selfAttr.add(umlAttribute); }
    
    public void addAssoClass(MyClass myClass) { selfAssoClass.add(myClass); }
    
    public void addAssoInter(MyInterface myInterface) {
        selfAssoInter.add(myInterface);
    }
    
    public void addAssoEnd(UmlAssociationEnd umlAssociationEnd) {
        selfAssoEnd.add(umlAssociationEnd);
    }
    
    public ArrayList<UmlAssociationEnd> getSelfAssoEnd() { return selfAssoEnd; }
    
    public int[] getOperType() { return operType; }
    
    public ArrayList<UmlAttribute> getSelfAttr() { return selfAttr; }
    
    public ArrayList<UmlAttribute> getAllAttr() { return allAttr; }
    
    public HashMap<String, ArrayList<UmlAttribute>> getAllAttrAgainstHidden() {
        return allAttrAgainstHidden;
    }
    
    public ArrayList<MyClass> getSelfAssoClass() { return selfAssoClass; }
    
    public ArrayList<MyClass> getAllAssoClass() { return allAssoClass; }
    
    public ArrayList<MyInterface> getSelfAssoInter() {
        return selfAssoInter;
    }
    
    public ArrayList<MyInterface> getAllAssoInter() { return allAssoInter; }
    
    public void setAssoCount(int count) { assoCount = count; }
    
    public int getAssoCount() { return assoCount; }
    
    public HashSet<String> getAssoClassOutput() { return assoClassOutput; }
    
    public void setVisited() { visited = true; }
    
    public boolean getVisited() { return visited; }
}
