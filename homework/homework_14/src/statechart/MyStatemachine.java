package statechart;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyStatemachine {
    private UmlStateMachine umlStateMachine;
    private ArrayList<UmlElement> states = new ArrayList<>();
    private ArrayList<UmlTransition> transitions = new ArrayList<>();
    private HashMap<String, UmlElement> id2State = new HashMap<>();
    private HashMap<String, ArrayList<String>> stateName2ids = new HashMap<>();
    private HashMap<String, ArrayList<String>> subsequentStates =
            new HashMap<>();
    
    public MyStatemachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }
    
    public void addState(UmlElement umlElement) {
        states.add(umlElement);
        String name = umlElement.getName();
        String id = umlElement.getId();
        id2State.put(id, umlElement);
        if (stateName2ids.containsKey(name)) {
            stateName2ids.get(name).add(id);
        } else {
            stateName2ids.put(name, new ArrayList<String>() {{
                    add(id); }});
        }
        subsequentStates.put(id, new ArrayList<>());
    }
    
    public void addTransition(UmlTransition umlTransition) {
        transitions.add(umlTransition);
        String sourceId = umlTransition.getSource();
        String targetId = umlTransition.getTarget();
        subsequentStates.get(sourceId).add(targetId);
    }
    
    public void calSubsequent() {
        ArrayList<String> stateIds = new ArrayList<>(subsequentStates.keySet());
        for (String stateId : stateIds) {
            ArrayList<String> subsequentList = subsequentStates.get(stateId);
            HashSet<String> subsequentSet = new HashSet<>(subsequentList);
            int i;
            for (i = 0; i < subsequentList.size(); i++) {
                ArrayList<String> anotherList = subsequentStates.
                        get(subsequentList.get(i));
                if (!anotherList.isEmpty()) {
                    for (String id : anotherList) {
                        if (!subsequentSet.contains(id)) {
                            subsequentList.add(id);
                            subsequentSet.add(id);
                        }
                    }
                }
            }
        }
    }
    
    public int getStateCount() {
        return states.size();
    }
    
    public int getTransitionCount() {
        return transitions.size();
    }
    
    public int getSubsequentStateCount(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        String statemachineName = umlStateMachine.getName();
        String stateId;
        if (!stateName2ids.containsKey(stateName)) {
            throw new StateNotFoundException(statemachineName, stateName);
        } else if (stateName2ids.get(stateName).size() > 1) {
            throw new StateDuplicatedException(statemachineName, stateName);
        } else {
            stateId = stateName2ids.get(stateName).get(0);
        }
        return new HashSet<>(subsequentStates.get(stateId)).size();
    }
}
