package org.jessay;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 *
 * @author Serg Zubovich
 */
public class JEssay extends Suite {

    public JEssay(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(builder, getEssayClasses(klass));        
    }    
    
    
    private static Class<?>[] getEssayClasses(Class<?> klass){
        List<Class<?>> essays = new ArrayList<Class<?>>();
        for(Class<?> declaredClass : klass.getDeclaredClasses()){            
            if ((EssayI.class.isAssignableFrom(declaredClass) || declaredClass.getAnnotation(Essay.class) !=null) && 
                    !Modifier.isAbstract(declaredClass.getModifiers())){
                essays.add(declaredClass);
            }
        }
        return essays.toArray(new Class<?>[essays.size()]);
    }
    
    
    
    
}


 