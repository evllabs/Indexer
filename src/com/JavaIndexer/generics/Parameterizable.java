package com.JavaIndexer.generics;

import java.util.HashMap;

/**
 * A class of things-that-can-take-(label:value)-parameters.
 * 
 * Code taken from jgaap project
 * @author Juola
 * @since 1.0
 */
public class Parameterizable {

	public String displayName(){
	    return "Parameterizable";
	}

	public String tooltipText(){
	    return "Parameterizable";
	}

	public boolean showInGUI(){
	    return false;
	}

    /** Parameters are stored using pairs of Strings in a HashMap */
    private HashMap<String, String> Parameters;

    /** Construct new Parameterizable with empty set */
    public Parameterizable() {
        Parameters = new HashMap<String, String>();
    }

    /** Removes all label and their associated values */
    public void clearParameterSet() {
        Parameters.clear();
    }

    /**
     * Removes a label and its associated value
     * 
     * @param label
     *            the label to set
     */
    public void deleteParameter(String label) {
        Parameters.remove(label);
    }

    /**
     * return the value associated with label
     * 
     * @param label
     *            the label to set
     * @return the appropriate value stored in the parameter set
     */
    public String getParameter(String label) {
        if (Parameters.containsKey(label.toLowerCase())) {
            return Parameters.get(label.toLowerCase());
        } else {
            return new String("");
        }
    }

    /**
     * Set label=String.valueOf(value) (persistantly)
     * 
     * @param label
     *            the label to set
     * @param value
     *            the (double) value to set the label to
     */
    public void setParameter(String label, double value) {
        Parameters.put(label.toLowerCase(), String.valueOf(value));
    }

    /**
     * Set label=String.valueOf(value) (persistantly)
     * 
     * @param label
     *            the label to set
     * @param value
     *            the (integer) value to set the label to
     */
    public void setParameter(String label, int value) {
        Parameters.put(label.toLowerCase(), String.valueOf(value));
    }

    // do we need things that return Integers and so forth?

    /**
     * Set label=String.valueOf(value) (persistantly)
     * 
     * @param label
     *            the label to set
     * @param value
     *            the (long) value to set the label to
     */
    public void setParameter(String label, long value) {
        Parameters.put(label.toLowerCase(), String.valueOf(value));
    }

    /**
     * Set label=value (persistantly)
     * 
     * @param label
     *            the label to set
     * @param value
     *            the value to set the label to
     */
    public void setParameter(String label, String value) {
        Parameters.put(label.toLowerCase(), value);
    }
}
