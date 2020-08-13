package collaboration;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyInteraction {
    private UmlInteraction umlInteraction;
    private int lifelineCount = 0;
    private int messageCount = 0;
    private HashMap<String, ArrayList<String>> lifelineName2ids =
            new HashMap<>();
    private HashMap<String, Integer> incomingMsg = new HashMap<>();
    //lifelineId->inMsgCount
    
    public MyInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }
    
    public void addLifeline(UmlLifeline umlLifeline) {
        lifelineCount++;
        String name = umlLifeline.getName();
        String id = umlLifeline.getId();
        if (lifelineName2ids.containsKey(name)) {
            lifelineName2ids.get(name).add(id);
        } else {
            lifelineName2ids.put(name, new ArrayList<String>() {{
                    add(id); }});
        }
        incomingMsg.put(id, 0);
    }
    
    public void addMessage(UmlMessage umlMessage) {
        messageCount++;
        String target = umlMessage.getTarget();
        if (incomingMsg.get(target) != null) {
            incomingMsg.put(target, incomingMsg.get(target) + 1);
        }
    }
    
    public int getLifelineCount() { return lifelineCount; }
    
    public int getMessageCount() { return messageCount; }
    
    public int getIncomingMessageCount(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        String lifelineId;
        String interactName = umlInteraction.getName();
        if (!lifelineName2ids.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(interactName, lifelineName);
        } else if (lifelineName2ids.get(lifelineName).size() > 1) {
            throw new LifelineDuplicatedException(interactName, lifelineName);
        } else {
            lifelineId = lifelineName2ids.get(lifelineName).get(0);
            return incomingMsg.get(lifelineId);
        }
    }
}
