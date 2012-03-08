package org.jessay;

import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author Serg Zubovich
 */
public class JMockLegacyEssay extends JMockEssay {

    public JMockLegacyEssay() {
        super(new JUnit4Mockery(){{
            setImposteriser(ClassImposteriser.INSTANCE);
            setThreadingPolicy(new Synchroniser());
        }});
    }        
   
    
}
