package classmodel;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

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
    
    public MyOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
        visibility = umlOperation.getVisibility();
    }
    
    public void calInAndReturn() {
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
    
    public void addParameter(UmlParameter umlParameter) {
        parameter.add(umlParameter);
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public boolean getIsInParameter() {
        return isInParameter;
    }
    
    public boolean getIsReturnParameter() {
        return isReturnParameter;
    }
    
    public UmlOperation getUmlOperation() {
        return umlOperation;
    }
}
