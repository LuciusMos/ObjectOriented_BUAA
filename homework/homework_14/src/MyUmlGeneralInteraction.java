import classmodel.MyClass;
import classmodel.MyInterface;
import classmodel.MyOperation;
import collaboration.MyCollaboration;
import statechart.MyStatechart;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    // Class
    private HashMap<String, MyClass> id2Class = new HashMap<>();
    private HashMap<String, ArrayList<String>> className2ids = new HashMap<>();
    // Interface
    private HashMap<String, MyInterface> id2Inter = new HashMap<>();
    // Operation
    private HashMap<String, MyOperation> id2Oper = new HashMap<>();
    // Association
    private HashMap<String, UmlAssociationEnd> id2AssociationEnd =
            new HashMap<>();
    // NotHidden
    private HashMap<String, ArrayList<AttributeClassInformation>>
            classNotHidden = new HashMap<>();
    // UmlPreCheck
    private ArrayList<MyClass> myClasses;
    private ArrayList<MyInterface> myInterfaces;
    // Collaboration
    private MyCollaboration myCollaboration;
    // StateChart
    private MyStatechart myStatechart;
    
    private ArrayList<UmlGeneralization> umlGeneralizations = new ArrayList<>();
    private ArrayList<UmlOperation> umlOperations = new ArrayList<>();
    private ArrayList<UmlParameter> umlParameters = new ArrayList<>();
    private ArrayList<UmlAttribute> umlAttributes = new ArrayList<>();
    private ArrayList<UmlInterfaceRealization> umlInterfaceRealizations =
            new ArrayList<>();
    private ArrayList<UmlAssociationEnd> umlAssociationEnds = new ArrayList<>();
    private ArrayList<UmlAssociation> umlAssociations = new ArrayList<>();
    
    public MyUmlGeneralInteraction(UmlElement... elements) {
        readClassAndInterface(elements);
        readGeneralization();
        readOperation();
        readParameter();
        readAttribute();
        readInterfaceRealization();
        readAssociationEnd();
        readAssociation();
        myCollaboration = new MyCollaboration(elements);
        myStatechart = new MyStatechart(elements);
        myClasses = new ArrayList<>(id2Class.values());
        myInterfaces = new ArrayList<>(id2Inter.values());
    }
    
    void classRecurse(String sonId) {
        MyClass sonClass = id2Class.get(sonId);
        if (sonClass.getVisited()) { return; }
        MyClass fatherClass = sonClass.getFather();
        if (fatherClass == null) { //是顶级父类
            sonClass.update();
            sonClass.setTopFather(sonClass);
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
    
    private void readClassAndInterface(UmlElement... elements) {
        for (UmlElement element : elements) {
            String id = element.getId();
            String name = element.getName();
            switch (element.getElementType()) {
                case UML_CLASS:
                    id2Class.put(id, new MyClass((UmlClass) element));
                    if (className2ids.containsKey(name)) {
                        className2ids.get(name).add(id);
                    } else {
                        className2ids.put(name,
                                new ArrayList<String>() {{ add(id); }});
                    }
                    break;
                case UML_INTERFACE:
                    id2Inter.put(id, new MyInterface((UmlInterface) element));
                    break;
                case UML_GENERALIZATION:
                    umlGeneralizations.add((UmlGeneralization) element);
                    break;
                case UML_OPERATION:
                    umlOperations.add((UmlOperation) element);
                    break;
                case UML_PARAMETER:
                    umlParameters.add((UmlParameter) element);
                    break;
                case UML_ATTRIBUTE:
                    umlAttributes.add((UmlAttribute) element);
                    break;
                case UML_INTERFACE_REALIZATION:
                    umlInterfaceRealizations.add(
                            (UmlInterfaceRealization) element);
                    break;
                case UML_ASSOCIATION_END:
                    umlAssociationEnds.add((UmlAssociationEnd) element);
                    break;
                case UML_ASSOCIATION:
                    umlAssociations.add((UmlAssociation) element);
                    break;
                default:
            }
        }
    }
    
    private void readGeneralization() {
        MyClass sonClass;
        MyInterface sonInterface;
        for (UmlGeneralization umlGeneralization : umlGeneralizations) {
            String sonId = umlGeneralization.getSource();
            String fatherId = umlGeneralization.getTarget();
            sonClass = id2Class.get(sonId);
            sonInterface = id2Inter.get(sonId);
            if (sonClass != null) {
                sonClass.setFather(id2Class.get(fatherId));
            } else if (sonInterface != null) {
                sonInterface.addFather(id2Inter.get(fatherId));
            }
        }
    }
    
    private void readOperation() {
        MyClass myClass;
        MyInterface myInterface;
        for (UmlOperation umlOperation : umlOperations) {
            String id = umlOperation.getParentId();
            myClass = id2Class.get(id);
            myInterface = id2Inter.get(id);
            MyOperation myOperation = new MyOperation(umlOperation);
            if (myClass != null) {
                myClass.addOper(myOperation);
            } else if (myInterface != null) {
                myInterface.addOper(myOperation);
            }
            id2Oper.put(umlOperation.getId(), myOperation);
        }
    }
    
    private void readParameter() {
        ;
        MyOperation myOperation;
        for (UmlParameter umlParameter : umlParameters) {
            String id = umlParameter.getParentId();
            myOperation = id2Oper.get(id);
            myOperation.addParameter(umlParameter);
        }
        Collection<MyOperation> operations = id2Oper.values();
        for (MyOperation operation : operations) {
            operation.calInAndReturn();
        }
    }
    
    private void readAttribute() {
        MyClass myClass;
        MyInterface myInterface;
        for (UmlAttribute umlAttribute : umlAttributes) {
            String id = umlAttribute.getParentId();
            myClass = id2Class.get(id);
            myInterface = id2Inter.get(id);
            if (myClass != null) {
                myClass.addAttr(umlAttribute);
            } else if (myInterface != null) {
                myInterface.addAttr(umlAttribute);
            }
        }
    }
    
    private void readInterfaceRealization() {
        MyClass myClass;
        MyInterface myInterface;
        for (UmlInterfaceRealization umlInterfaceRealization :
                umlInterfaceRealizations) {
            String classId = umlInterfaceRealization.getSource();
            String interId = umlInterfaceRealization.getTarget();
            myClass = id2Class.get(classId);
            myInterface = id2Inter.get(interId);
            myClass.addInter(myInterface);
        }
    }
    
    private void readAssociationEnd() {
        for (UmlAssociationEnd umlAssociationEnd : umlAssociationEnds) {
            id2AssociationEnd.put(umlAssociationEnd.getId(), umlAssociationEnd);
        }
    }
    
    private void readAssociation() {
        for (UmlAssociation umlAssociation : umlAssociations) {
            String id1 = umlAssociation.getEnd1(); //UmlAssociationEnd id1
            String id2 = umlAssociation.getEnd2(); //UmlAssociationEnd id2
            buildAssociationMutal(id1, id2);
        }
    }
    
    private void buildAssociationMutal(String endId1, String endId2) {
        UmlAssociationEnd umlAssociationEnd1 = id2AssociationEnd.get(endId1);
        UmlAssociationEnd umlAssociationEnd2 = id2AssociationEnd.get(endId2);
        MyClass endClass1 = id2Class.get(umlAssociationEnd1.getReference());
        MyInterface endInter1 = id2Inter.get(umlAssociationEnd1.getReference());
        MyClass endClass2 = id2Class.get(umlAssociationEnd2.getReference());
        MyInterface endInter2 = id2Inter.get(umlAssociationEnd2.getReference());
        if (endClass1 != null) {
            if (endClass2 != null) {
                endClass1.addAssoClass(endClass2);
                endClass2.addAssoClass(endClass1);
                endClass2.addAssoEnd(umlAssociationEnd1);
            } else if (endInter2 != null) {
                endClass1.addAssoInter(endInter2);
                endInter2.addAssoClass(endClass1);
                endInter2.addAssoEnd(umlAssociationEnd1);
            }
            endClass1.addAssoEnd(umlAssociationEnd2);
        } else if (endInter1 != null) {
            if (endClass2 != null) {
                endInter1.addAssoClass(endClass2);
                endClass2.addAssoInter(endInter1);
                endClass2.addAssoEnd(umlAssociationEnd1);
            } else if (endInter2 != null) {
                endInter1.addAssoInter(endInter2);
                endInter2.addAssoInter(endInter1);
                endInter2.addAssoEnd(umlAssociationEnd1);
            }
            endInter1.addAssoEnd(umlAssociationEnd2);
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
    
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return myCollaboration.getParticipantCount(interactionName);
    }
    
    public int getMessageCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return myCollaboration.getMessageCount(interactionName);
    }
    
    public int getIncomingMessageCount(String interactionName,
                                       String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myCollaboration.getIncomingMessageCount(
                interactionName, lifelineName);
    }
    
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return myStatechart.getStateCount(stateMachineName);
    }
    
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        return myStatechart.getTransitionCount(stateMachineName);
    }
    
    public int getSubsequentStateCount(String stateMachineName,
                                       String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return myStatechart.getSubsequentStateCount(stateMachineName,
                stateName);
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        UmlPreCheck.checkForUml002(myClasses);
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        UmlPreCheck.checkForUml008(myClasses, myInterfaces);
    }
    
    public void checkForUml009() throws UmlRule009Exception {
        UmlPreCheck.checkForUml009(myClasses, myInterfaces,
                this);
    }
}
