package statechart;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;

public class MyStatechart {
    private HashMap<String, UmlRegion> id2Region = new HashMap<>();
    private HashMap<String, MyStatemachine> id2Statemachine = new HashMap<>();
    private HashMap<String, ArrayList<String>> statemachineName2ids =
            new HashMap<>();
    private ArrayList<UmlTransition> umlTransitions = new ArrayList<>();
    
    public MyStatechart(UmlElement... elements) {
        readStatemachineAndRegion(elements);
        readState(elements);
        readTransition();
    }
    
    private void readStatemachineAndRegion(UmlElement... elements) {
        for (UmlElement element : elements) {
            String id = element.getId();
            if (element.getElementType() == ElementType.UML_REGION) {
                id2Region.put(id, (UmlRegion) element);
            } else if (element.getElementType() ==
                    ElementType.UML_STATE_MACHINE) {
                String name = element.getName();
                id2Statemachine.put(id,
                        new MyStatemachine((UmlStateMachine) element));
                if (statemachineName2ids.containsKey(name)) {
                    statemachineName2ids.get(name).add(id);
                } else {
                    statemachineName2ids.put(name,
                            new ArrayList<String>() {{ add(id); }});
                }
            } else if (element.getElementType() == ElementType.UML_TRANSITION) {
                umlTransitions.add((UmlTransition) element);
            }
        }
    }
    
    private void readState(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_PSEUDOSTATE ||
                    element.getElementType() == ElementType.UML_STATE ||
                    element.getElementType() == ElementType.UML_FINAL_STATE) {
                String regionId = element.getParentId();
                String statemachineId = id2Region.get(regionId).getParentId();
                id2Statemachine.get(statemachineId).addState(element);
            }
        }
    }
    
    private void readTransition() {
        for (UmlTransition umlTransition : umlTransitions) {
            String regionId = umlTransition.getParentId();
            String statemachineId = id2Region.get(regionId).getParentId();
            id2Statemachine.get(statemachineId).addTransition(umlTransition);
        }
        ArrayList<MyStatemachine> myStatemachines = new ArrayList<>(
                id2Statemachine.values());
        for (MyStatemachine myStatemachine : myStatemachines) {
            myStatemachine.calSubsequent();
        }
    }
    
    private MyStatemachine lookupStatemachine(String statemachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        String statemachineId;
        if (!statemachineName2ids.containsKey(statemachineName)) {
            throw new StateMachineNotFoundException(statemachineName);
        } else if (statemachineName2ids.get(statemachineName).size() > 1) {
            throw new StateMachineDuplicatedException(statemachineName);
        } else {
            statemachineId = statemachineName2ids.get(statemachineName).get(0);
            return id2Statemachine.get(statemachineId);
        }
    }
    
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStatemachine myStatemachine = lookupStatemachine(stateMachineName);
        return myStatemachine.getStateCount();
    }
    
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        MyStatemachine myStatemachine = lookupStatemachine(stateMachineName);
        return myStatemachine.getTransitionCount();
    }
    
    public int getSubsequentStateCount(String stateMachineName,
                                       String stateName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyStatemachine myStatemachine = lookupStatemachine(stateMachineName);
        return myStatemachine.getSubsequentStateCount(stateName);
    }
}
