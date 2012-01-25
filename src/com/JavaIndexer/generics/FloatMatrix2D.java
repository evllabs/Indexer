package com.JavaIndexer.generics;

import java.io.Serializable;
import java.util.HashMap;

public class FloatMatrix2D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Float> theHash;
	private int rowLength;
	private int columnLength;
	
	public FloatMatrix2D(int rowLength, int columnLength){
		this.theHash = new HashMap<Integer, Float>();
		this.rowLength = rowLength;
		this.columnLength = columnLength;
	}
	public void put(int row, int column, float val){
		if (row<this.rowLength && column<this.columnLength){
			theHash.put(row*this.rowLength+column, val);
		}
		else {
			System.out.println("Array not within bounds");
		}
	}
	public Float get(int row, int column){
		int idx = row*this.columnLength+column;
		if (theHash.containsKey(idx))
			return theHash.get(idx);
		else
			return new Float(0.0);
	}
}
