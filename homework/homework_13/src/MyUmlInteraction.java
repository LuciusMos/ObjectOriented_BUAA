import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.oocourse.uml1.models.common.ElementType.UML_CLASS;
import static com.oocourse.uml1.models.common.ElementType.UML_INTERFACE;

public class MyUmlInteraction implements UmlInteraction {
    // Class
    private HashMap<String, MyClass> id2Class = new HashMap<>();
    private HashMap<String, ArrayList<String>> className2ids = new HashMap<>();
    // Interface
    private HashMap<String, MyInterface> id2Inter = new HashMap<>();
    // Operation
    private HashMap<String, MyOperation> id2Oper = new HashMap<>();
    // Association
    //private HashSet<UmlAssociationEnd> umlAssociationEnds = new HashSet<>();
    private HashMap<String, UmlAssociationEnd> id2AssociationEnd =
            new HashMap<>();
    // NotHidden
    private HashMap<String, ArrayList<AttributeClassInformation>>
            classNotHidden = new HashMap<>();
    
    void classRecurse(String sonId) {
        MyClass sonClass = id2Class.get(sonId);
        if (sonClass.getVisited()) { return; }
        MyClass fatherClass = sonClass.getFather();
        if (fatherClass == null) { //是顶级父类
            sonClass.update();
            sonClass.setTopFather(sonClass);
            return;
        } else { //不是顶级父类
            classRecurse(fatherClass.getUmlClass().getId());
            sonClass.update(fatherClass);
            sonClass.setTopFather(fatherClass.getTopFather());
        }
    }
    
    void interRecurse(String sonId) {
        MyInterface sonInter = id2Inter.get(sonId);
        if (sonInter.getVisited()) { return; }
        final ArrayList<MyInterface> fathersInter = sonInter.getFathers();
        if (fathersInter.isEmpty()) {
            sonInter.update();
        } else {
            for (MyInterface fatherInter : fathersInter) {
                interRecurse(fatherInter.getUmlInterface().getId());
                sonInter.update(fatherInter);
            }
        }
    }
    
    public MyUmlInteraction(UmlElement... elements) {
        readClassAndInterface(elements);
        readGeneralization(elements);
        readOperation(elements);
        readParameter(elements);
        readAttribute(elements);
        readInterfaceRealization(elements);
        readAssociationEnd(elements);
        readAssociation(elements);
        interAndClassRecurse();
    }
    
    private void interAndClassRecurse() {
        ArrayList<MyClass> myClasses = new ArrayList<>(id2Class.values());
        ArrayList<MyInterface> myInterfaces =
                new ArrayList<>(id2Inter.values());
        for (MyClass myClass : myClasses) {
            myClass.calOper();
        }
        for (MyInterface myInterface : myInterfaces) {
            //if (myInterface.getVisited()) { continue; }
            //interRecurse(myInterface.getUmlInterface().getId());
            myInterface.recurse();
        }
        for (MyClass myClass : myClasses) {
            myClass.addInterFather();
        }
        for (MyClass myClass : myClasses) {
            if (myClass.getVisited()) { continue; }
            classRecurse(myClass.getUmlClass().getId());
        }
        for (MyClass myClass : myClasses) {
            myClass.calInterAndAssoAndAttr();
        }
    }
    
    private void readClassAndInterface(UmlElement... elements) {
        for (UmlElement element : elements) {
            String id = element.getId();
            String name = element.getName();
            if (element.getElementType() == UML_CLASS) {
                id2Class.put(id, new MyClass((UmlClass) element));
                if (className2ids.containsKey(name)) {
                    className2ids.get(name).add(id);
                } else {
                    className2ids.put(name,
                            new ArrayList<String>() {{ add(id); }});
                }
            } else if (element.getElementType() == UML_INTERFACE) {
                id2Inter.put(id, new MyInterface((UmlInterface) element));
            }
        }
    }
    
    private void readGeneralization(UmlElement... elements) {
        String sonId;
        String fatherId;
        MyClass sonClass;
        MyInterface sonInterface;
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_GENERALIZATION) {
                sonId = ((UmlGeneralization) element).getSource();
                fatherId = ((UmlGeneralization) element).getTarget();
                sonClass = id2Class.get(sonId);
                sonInterface = id2Inter.get(sonId);
                if (sonClass != null) {
                    sonClass.setFather(id2Class.get(fatherId));
                } else if (sonInterface != null) {
                    sonInterface.addFather(id2Inter.get(fatherId));
                }
            }
        }
    }
    
    private void readOperation(UmlElement... elements) {
        String id;
        MyClass myClass;
        MyInterface myInterface;
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_OPERATION) {
                id = element.getParentId();
                myClass = id2Class.get(id);
                myInterface = id2Inter.get(id);
                MyOperation myOperation =
                        new MyOperation((UmlOperation) element);
                if (myClass != null) {
                    myClass.addOper(myOperation);
                } else if (myInterface != null) {
                    myInterface.addOper(myOperation);
                }
                id2Oper.put(element.getId(), myOperation);
            }
        }
    }
    
    private void readParameter(UmlElement... elements) {
        String id;
        MyOperation myOperation;
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_PARAMETER) {
                id = element.getParentId();
                myOperation = id2Oper.get(id);
                myOperation.addParameter((UmlParameter) element);
            }
        }
        Collection<MyOperation> operations = id2Oper.values();
        for (MyOperation operation : operations) {
            operation.calInAndReturn();
        }
    }
    
    private void readAttribute(UmlElement... elements) {
        String id;
        MyClass myClass;
        MyInterface myInterface;
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_ATTRIBUTE) {
                id = element.getParentId();
                myClass = id2Class.get(id);
                myInterface = id2Inter.get(id);
                if (myClass != null) {
                    myClass.addAttr((UmlAttribute) element);
                } else if (myInterface != null) {
                    myInterface.addAttr((UmlAttribute) element);
                }
            }
        }
    }
    
    private void readInterfaceRealization(UmlElement... elements) {
        String classId;
        String interId;
        MyClass myClass;
        MyInterface myInterface;
        for (UmlElement element : elements) {
            if (element.getElementType() ==
                    ElementType.UML_INTERFACE_REALIZATION) {
                classId = ((UmlInterfaceRealization) element).getSource();
                interId = ((UmlInterfaceRealization) element).getTarget();
                myClass = id2Class.get(classId);
                myInterface = id2Inter.get(interId);
                myClass.addInter(myInterface);
            }
        }
    }
    
    private void readAssociationEnd(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_ASSOCIATION_END) {
                id2AssociationEnd.put(element.getId(),
                        (UmlAssociationEnd) element);
            }
        }
    }
    
    private void readAssociation(UmlElement... elements) {
        String id1; //UmlAssociationEnd id1
        String id2; //UmlAssociationEnd id2
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_ASSOCIATION) {
                id1 = ((UmlAssociation) element).getEnd1();
                id2 = ((UmlAssociation) element).getEnd2();
                buildAssociation(id1, id2);
                buildAssociation(id2, id1);
            }
        }
    }
    
    private void buildAssociation(String endId1, String endId2) {
        UmlAssociationEnd umlAssociationEnd1 = id2AssociationEnd.get(endId1);
        UmlAssociationEnd umlAssociationEnd2 = id2AssociationEnd.get(endId2);
        MyClass endClass1 = id2Class.get(umlAssociationEnd1.getReference());
        MyInterface endInter1 = id2Inter.get(umlAssociationEnd1.getReference());
        MyClass endClass2 = id2Class.get(umlAssociationEnd2.getReference());
        MyInterface endInter2 = id2Inter.get(umlAssociationEnd2.getReference());
        if (endClass1 != null) {
            if (endClass2 != null) {
                endClass1.addAssoClass(endClass2);
            } else if (endInter2 != null) {
                endClass1.addAssoInter(endInter2);
            }
        } else if (endInter1 != null) {
            if (endClass2 != null) {
                endInter1.addAssoClass(endClass2);
            } else if (endInter2 != null) {
                endInter1.addAssoInter(endInter2);
            }
        }
    }
    
    private MyClass lookupClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        String classId;
        if (!className2ids.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (className2ids.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            classId = className2ids.get(className).get(0);
            return id2Class.get(classId);
        }
    }

    public int getClassCount() {
        return id2Class.size();
    }
    
    public int getClassOperationCount(String className,
                                      OperationQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        int[] type = myClass.getOperType();
        switch (queryType) {
            case NON_RETURN:
                return type[0];
            case RETURN:
                return type[1];
            case NON_PARAM:
                return type[2];
            case PARAM:
                return type[3];
            default: //case ALL:
                return type[4];
        }
    }
    
    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        if (queryType == AttributeQueryType.SELF_ONLY) {
            return myClass.getSelfAttr().size();
        } else { //AttributeQueryType.ALL
            return myClass.getAllAttr().size();
        }
    }
    
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        return myClass.getAssoCount();
    }
    
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        List<String> classNames = new ArrayList<>();
        HashSet<String> assoClassOutput = myClass.getAssoClassOutput();
        for (String classId : assoClassOutput) {
            classNames.add(id2Class.get(classId).getUmlClass().getName());
        }
        return classNames;
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        return myClass.calOperVisibility(operationName);
    }

    public Visibility getClassAttributeVisibility(
            String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass myClass = lookupClass(className);
        return myClass.calAttrVisibility(attributeName);
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        return myClass.getTopFatherName();
    }
    
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        // getClassAssociatedClassList
        MyClass myClass = lookupClass(className);
        List<String> interNames = new ArrayList<>();
        HashSet<String> interOutput = myClass.getInterOutput();
        for (String interId : interOutput) {
            interNames.add(id2Inter.get(interId).getUmlInterface().getName());
        }
        return interNames;
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = lookupClass(className);
        String classId = myClass.getUmlClass().getId();
        if (classNotHidden.containsKey(classId)) {
            return classNotHidden.get(classId);
        }
        List<AttributeClassInformation> result = new ArrayList<>();
        Iterator<Map.Entry<String, ArrayList<UmlAttribute>>> it =
                myClass.getAllAttrAgainstHidden().entrySet().iterator();
        Map.Entry<String, ArrayList<UmlAttribute>> entry;
        while (it.hasNext()) {
            entry = it.next();
            String classNameResult = id2Class.get(entry.getKey()).
                    getUmlClass().getName();
            for (UmlAttribute umlAttribute : entry.getValue()) {
                String attrNameResult = umlAttribute.getName();
                result.add(new AttributeClassInformation(
                        attrNameResult, classNameResult));
            }
        }
        classNotHidden.put(classId,
                (ArrayList<AttributeClassInformation>) result);
        return result;
    }
}
