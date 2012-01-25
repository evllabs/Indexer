
package com.JavaIndexer.gui.generics;

/**
 * Interface making it possible to execute blocks of code in the main GUI class
 * specific to each step of the process.
 * 
 * File taken from jgaap project
 * @author Chuck Liddell
 */
public interface StepListener {

    /**
     * This method is called to indicate that a step is ready to be executed.
     * 
     * @param stepName
     *            String representing the name of the step to be executed
     */
    public void executeStep(String stepName);
}
