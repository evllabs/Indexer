package com.JavaIndexer.processing;
public class Centroid{

private Cluster regCluster;
private double regX, regY;

	public Centroid(double rX, double rY)
	{
		regX = rX; // Average x values
		regY = rY; // Average y values
	}

	public void calcCentroid()
	{
		int numDataPoints = regCluster.getNumDataPoints();
		double tempX = 0, tempY = 0;
	
		//For Object datapoint in the cluster points
		for(DataPoint dp: regCluster.getDataPoints()){
			// get x and y total		
			tempX = tempX + dp.getX();
			tempY = tempY + dp.getY();
		}

		regX = tempX / numDataPoints;
		regY = tempY / numDataPoints;

		// Calculate the new distance for each point
		for(DataPoint dp : regCluster.getDataPoints()){
			dp.EuclideanDistance();
		}
			regCluster.sumofsquares();
	}

	public void setCluster(Cluster x)
	{
		regCluster = x;
	}

	public double getRegx()
	{
		return regX;
	}

	public double getRegy()
	{
		return regY;
	}

	public Cluster getCluster()
	{
		return regCluster;
	}
}



