package org.jessay;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Test;

/**
 *
 * @author Serg Zubovich
 */
public abstract class JMockEssay extends Expectations implements Essay {
    
    private Mockery context;
    
    public JMockEssay(){
        this(new JUnit4Mockery(){{
            setThreadingPolicy(new Synchroniser());
        }});
    }

    protected JMockEssay(Mockery context) {
        this.context = context;
    }        

    private Essay essay = new AbstractEssay(){

        @Override
        public void expectations() throws Exception {
            JMockEssay.this.expectations();
            context().checking(JMockEssay.this);
        }

        @Override
        public void fixtures() throws Exception {
            JMockEssay.this.fixtures();
        }

        @Override
        public void test() throws Exception {
            JMockEssay.this.test();
        }

    };

    public final Mockery context(){      
        return context;        
    }
    
    protected Mockery newContext(){
        return new JUnit4Mockery();
    }

    public void expectations() throws Exception {
        
    }

    public void fixtures() throws Exception {
        
    }

    public void test() throws Exception {
        
    }

    @Test
    public void run() throws Exception {        
        essay.run();
    }

}
