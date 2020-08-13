package collaboration;

import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCollaboration {
    private HashMap<String, MyInteraction> id2Interaction = new HashMap<>();
    private HashMap<String, ArrayList<String>> interactionName2ids =
            new HashMap<>();
    private ArrayList<UmlLifeline> umlLifelines = new ArrayList<>();
    private ArrayList<UmlMessage> umlMessages = new ArrayList<>();
    
    public MyCollaboration(UmlElement... elements) {
        readInteraction(elements);
        readLifeline();
        readMessage();
    }
    
    private void readInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_INTERACTION) {
                String id = element.getId();
                String name = element.getName();
                id2Interaction.put(id, new MyInteraction(
                        (UmlInteraction) element));
                if (interactionName2ids.containsKey(name)) {
                    interactionName2ids.get(name).add(id);
                } else {
                    interactionName2ids.put(name,
                            new ArrayList<String>() {{ add(id); }});
                }
            } else if (element.getElementType() == ElementType.UML_LIFELINE) {
                umlLifelines.add((UmlLifeline) element);
            } else if (element.getElementType() == ElementType.UML_MESSAGE) {
                umlMessages.add((UmlMessage) element);
            }
        }
    }
    
    private void readLifeline() {
        for (UmlLifeline umlLifeline : umlLifelines) {
            String parentId = umlLifeline.getParentId();
            MyInteraction myInteraction = id2Interaction.get(parentId);
            myInteraction.addLifeline(umlLifeline);
        }
    }
    
    private void readMessage() {
        for (UmlMessage umlMessage : umlMessages) {
            String parentId = umlMessage.getParentId();
            MyInteraction myInteraction = id2Interaction.get(parentId);
            myInteraction.addMessage(umlMessage);
        }

    }
    
    private MyInteraction lookupInteraction(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        String interactionId;
        if (!interactionName2ids.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (interactionName2ids.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            interactionId = interactionName2ids.get(interactionName).get(0);
            return id2Interaction.get(interactionId);
        }
    }
    
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraction myInteraction = lookupInteraction(interactionName);
        return myInteraction.getLifelineCount();
    }
    
    public int getMessageCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraction myInteraction = lookupInteraction(interactionName);
        return myInteraction.getMessageCount();
    }
    
    public int getIncomingMessageCount(String interactionName,
                                       String lifelineName) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraction myInteraction = lookupInteraction(interactionName);
        return myInteraction.getIncomingMessageCount(lifelineName);
    }
}
