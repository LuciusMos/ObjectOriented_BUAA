import classmodel.MyClass;
import classmodel.MyInterface;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class UmlPreCheck {
    public static void checkForUml002(ArrayList<MyClass> myClasses)
            throws UmlRule002Exception {
        HashSet<AttributeClassInformation> result002 = new HashSet<>();
        for (MyClass myClass : myClasses) {
            HashSet<String> dupNames = myClass.uml002Check();
            for (String dupName : dupNames) {
                result002.add(new AttributeClassInformation(dupName,
                        myClass.getUmlClass().getName()));
            }
        }
        if (!result002.isEmpty()) {
            throw new UmlRule002Exception(result002);
        }
    }
    
    public static void checkForUml008(ArrayList<MyClass> myClasses,
                                      ArrayList<MyInterface> myInterfaces)
            throws UmlRule008Exception {
        HashSet<UmlClassOrInterface> result008 = new HashSet<>();
        for (MyClass myClass : myClasses) {
            if (myClass.isCircularInher()) { continue; }
            result008.addAll(myClass.uml008Check());
        }
        for (MyInterface myInterface : myInterfaces) {
            // TODO: INTERFACE CIRCULAR INHERITANCE
            if (myInterface.uml008Check()) {
                result008.add(myInterface.getUmlInterface());
            }
        }
        if (!result008.isEmpty()) {
            throw new UmlRule008Exception(result008);
        }
    }
    
    public static void checkForUml009(ArrayList<MyClass> myClasses,
                                      ArrayList<MyInterface> myInterfaces,
                                      MyUmlGeneralInteraction
                                              myUmlGeneralInteraction)
            throws UmlRule009Exception {
        HashSet<UmlClassOrInterface> result009 = new HashSet<>();
        for (MyClass myClass : myClasses) {
            myClass.calOper();
        }
        for (MyInterface myInterface : myInterfaces) {
            if (myInterface.uml009Check()) {
                result009.add(myInterface.getUmlInterface());
            }
        }
        for (MyClass myClass : myClasses) {
            myClass.addInterFather();
        }
        for (MyClass myClass : myClasses) {
            if (myClass.getVisited()) { continue; }
            myUmlGeneralInteraction.classRecurse(myClass.getUmlClass().getId());
        }
        for (MyClass myClass : myClasses) {
            if (myClass.uml009Check()) {
                result009.add(myClass.getUmlClass());
            }
        }
        if (!result009.isEmpty()) {
            throw new UmlRule009Exception(result009);
        }
        for (MyClass myClass : myClasses) {
            myClass.calInterAndAssoAndAttr();
        }
    }
}
