package org.jessay;

import org.junit.Test;

/**
 *
 * @author serg
 */
public abstract class AbstractEssay implements EssayI {   

    @Test
    public void run() throws Exception {
        fixtures();
        expectations();
        test();
        assertions();
    }

}
