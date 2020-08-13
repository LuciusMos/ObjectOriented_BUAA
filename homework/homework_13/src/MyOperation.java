import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;

public class MyOperation {
    private UmlOperation umlOperation;
    private ArrayList<UmlParameter> parameter = new ArrayList<>();
    //private ArrayList<UmlParameter> inParameter = new ArrayList<>();
    //private ArrayList<UmlParameter> returnParameter = new ArrayList<>();
    private int inParameterCount = 0;
    private int returnParameterCount = 0;
    private boolean isInParameter;
    private boolean isReturnParameter;
    private Visibility visibility;
    
    MyOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
        visibility = umlOperation.getVisibility();
    }
    
    void calInAndReturn() {
        for (UmlParameter umlParameter : parameter) {
            if (umlParameter.getDirection() == Direction.IN) {
                //inParameter.add(umlParameter);
                inParameterCount++;
            } else if (umlParameter.getDirection() == Direction.RETURN) {
                //returnParameter.add(umlParameter);
                returnParameterCount++;
            }
        }
        //isInParameter = (!inParameter.isEmpty());
        //isReturnParameter = (!returnParameter.isEmpty());
        isInParameter = (inParameterCount > 0);
        isReturnParameter = (returnParameterCount > 0);
    }
    
    void addParameter(UmlParameter umlParameter) {
        parameter.add(umlParameter);
    }
    
    Visibility getVisibility() {
        return visibility;
    }
    
    boolean getIsInParameter() {
        return isInParameter;
    }
    
    boolean getIsReturnParameter() {
        return isReturnParameter;
    }
    
    UmlOperation getUmlOperation() {
        return umlOperation;
    }
}
