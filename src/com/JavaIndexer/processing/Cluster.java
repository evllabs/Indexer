package com.JavaIndexer.processing;
import java.util.*;

public class Cluster{
	private String regName;
	private Centroid regCentroid;
	private double regSumSqr;
	private List<DataPoint> regDataPoints = new ArrayList<DataPoint>();

	public Cluster(String name)
	{
		regName = name;
	}
	
	public void setCentroid(Centroid cent)
	{
		regCentroid = cent;
	}
	
	public Centroid getCentroid()
	{
		return regCentroid;
	}

	public void addDataPoint(DataPoint dp)
	{
		dp.setCluster(this);
		regDataPoints.add(dp);
		sumofsquares();
	}

	public void removeDataPoint(DataPoint dp)
	{
		regDataPoints.remove(dp);
		sumofsquares();
	}

	public int getNumDataPoints()
	{
		return regDataPoints.size();
	}
	
	public DataPoint getDataPoint(int position)
	{
		return regDataPoints.get(position);
	}

	public void sumofsquares()
	{
		double t = 0;
		for(DataPoint dp: regDataPoints){
			t = t + dp.getCurrentEuDt();
		}
			regSumSqr = t;
	}

	public double getSumSqr()
	{
		return regSumSqr;
	}
	
	public List<DataPoint> getDataPoints()	
	{
		return regDataPoints;
	}
	
	public String getName()
	{
		return regName;
	}
}
	
