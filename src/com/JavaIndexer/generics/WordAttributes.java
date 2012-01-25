package com.JavaIndexer.generics;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

class SearchTerm implements Serializable{
	private static final long serialVersionUID = 4592447414838147302L;
	private String term;
	private String tag;
	
	SearchTerm(String a, String b){
		this.term = a;
		this.tag = b;
	}
	public String getTerm(){
		return this.term;
	}
	public String getTag(){
		return this.tag;
	}
}
public class WordAttributes implements Serializable, Comparable<WordAttributes> {
	private static final long serialVersionUID = 3062529434227835672L;
	private boolean multipleSearchTerms = false;
	private String indexTerm;
	private ArrayList<SearchTerm> searchTerms = new ArrayList<SearchTerm>();
	private String higherTerm;
	private ArrayList<ParticularWord> words = new ArrayList<ParticularWord>();
	private boolean isIndexed = false;
	private float xVal;
	private float yVal;
	private int coordIndex;
	
	public WordAttributes(){
	}
	public WordAttributes(String indexTerm, String searchTerm, String tag){
			this.indexTerm = indexTerm;
			this.searchTerms.add(new SearchTerm(searchTerm,tag));
	}
	
	public WordAttributes(String indexTerm, String searchTerm, String tag, int start, int end){
		this(indexTerm,searchTerm,tag);
		this.addOffset(start,end);
	}
	public void addSearchTerm(String searchW, String tagW){
		this.searchTerms.add(new SearchTerm(searchW, tagW));
	}
	public void addOffset(int start, int end){
		words.add(new ParticularWord(start,end));
	}
	public void setIndexed(int count, boolean b){
		words.get(count).setIndexed(b);
	}
	public void setIndexed(int startIdx, int endIdx, boolean b){
		for (ParticularWord w : words){
			if (w.getStartIndex() == startIdx & w.getEndIndex() == endIdx){
				w.setIndexed(true);
				return;
			}
		}
	}
	public void makeIndexed(){
		this.isIndexed = true;
	}
	public boolean hasMultipleSearchTerms(){
		return this.multipleSearchTerms;
	}
	public String getIndexTerm(){
		return this.indexTerm;
	}
	public String getSearchTerm(int idx){
		return this.searchTerms.get(idx).getTerm();
	}
	public String getTag(int idx){
		return this.searchTerms.get(idx).getTag();
	}
	public int getCount(){
		return this.words.size();
	}
	public String toString(){
		//TODO return "<indexterm=" + this.indexTerm + "><searchterm=" + this.searchTerm + "><tag=" + this.tag + ">";
		return "<indexterm=" + this.indexTerm + ">";
	}/*
	public String returnString(){
		if (this.isIndexed)
			return "<index>" + this.searchTerm + "</index>";
		else
			return this.searchTerm;
	}*/
	public boolean isIndexed(){
		return this.isIndexed;
	}
	public boolean isIndexed(int num){
		return this.words.get(num).isIndexed();
	}
	/*
	@Override
	public boolean equals(Object comparedWord) {
		 if (((WordAttributes)comparedWord).indexTerm.compareTo(this.indexTerm) == 0 && ((WordAttributes)comparedWord).tag.compareTo(this.tag) == 0)
			return true;
		 else
			 return false;
	}*/
	
	public int compareTo(WordAttributes o) {
		return this.getIndexTerm().compareTo(o.getIndexTerm());
	}
	//@Override
	public int hashCode(){
		return this.indexTerm.hashCode();
	}
	public int getStartIndex(int count){
		return this.words.get(count).getStartIndex();
	}
	public int getEndIndex(int count){
		return this.words.get(count).getEndIndex();
	}
	public boolean areIndexes(int startIdx, int endIdx){
		for (ParticularWord item : words){
			if (item.getStartIndex() == startIdx && item.getEndIndex() == endIdx){
				return true;
			}
		}
		return false;
	}
	public float getXVal(){
		return this.xVal;
	}
	public float getYVal(){
		return this.yVal;
	}
	public float getWSDXValue(int i){
		return this.words.get(i).getWSDXValue();
	}
	public float getWSDYValue(int i){
		return this.words.get(i).getWSDYValue();
	}
	public void setWSDXValue(int j, float f) {
		this.words.get(j).setWSDXValue(f);
	}
	public void setWSDYValue(int j, float f) {
		this.words.get(j).setWSDYValue(f);
	}
	public float getAverageWSDXValue() {
		float xAvg=0;
		for (ParticularWord item : this.words){
			xAvg +=item.getWSDXValue();
		}
		return xAvg/this.words.size();
	}
	public float getAverageWSDYValue() {
		float yAvg=0;
		for (ParticularWord item : this.words){
			yAvg +=item.getWSDYValue();
		}
		return yAvg/this.words.size();
	}
	public void setVals(float f, float g) {
		this.xVal = f;
		this.yVal = g;
	}
	public int getCoordIndex(){
		return this.coordIndex;
	}
	public void setCoordIndex(int i){
		this.coordIndex = i;
	}
	public String getHigherTerm(){
		return this.higherTerm;
	}
	public String setHigherTerm(String term){
		return this.higherTerm = term;
	}
	public boolean hasHigherTerm(){
		return this.higherTerm != null;
	}
	public ParticularWord popInstance(int i) {
		ParticularWord temp = this.words.get(i);
		this.words.remove(i);
		return temp;
	}
	public void addInstance(ParticularWord term) {
		this.words.add(term);
		
	}
}
