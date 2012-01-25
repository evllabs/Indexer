package com.JavaIndexer.generics;

import java.io.Serializable;

public class ParticularWord implements Serializable{
	private static final long serialVersionUID = 8132736068159703688L;
	private int startIndex;
	private int endIndex;
	private float WSDXValue;
	private float WSDYValue;
	private boolean indexed = false;
	
	ParticularWord(int start, int end){
		this.startIndex = start;
		this.endIndex = end;
		this.indexed = true;
	}
	public void setIndexed(boolean b){
		indexed = b;
	}
	public boolean isIndexed(){
		return this.indexed;
	}
	public int getStartIndex() {
		return this.startIndex;
	}
	public int getEndIndex() {
		return this.endIndex;
	}
	public float getWSDXValue(){
		return this.WSDXValue;
	}
	public float getWSDYValue(){
		return this.WSDYValue;
	}
	public void setWSDXValue(float i){
		this.WSDXValue = i;
	}
	public void setWSDYValue(float i){
		this.WSDYValue = i;
	}
}