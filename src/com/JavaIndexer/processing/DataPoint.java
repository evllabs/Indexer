package com.JavaIndexer.processing;
public class DataPoint{
	private double regX, regY;
	private String regName;
	private Cluster regCluster;
	private double regEuDt;
	
	public DataPoint( double x, double y, String name)
	{
		regX = x;
		regY = y;
		regName = name;
	}

	public void setCluster(Cluster cluster)
	{
		regCluster = cluster;
		EuclideanDistance();
	}

	public void EuclideanDistance()
	{
		// x^2 + y^2
		regEuDt = Math.hypot(regX - regCluster.getCentroid().getRegx(),
					regY - regCluster.getCentroid().getRegy());
	}

	
	public double tEuclideanDistance(Centroid c)
	{
		return Math.sqrt(Math.pow((regX - c.getRegx()), 2) + Math.pow((regY	- c.getRegy()),2));
	}

	
    public double getX() {
        return regX;
    }

    public double getY() {
        return regY;
    }

    public Cluster getCluster() {
        return regCluster;
    }

    public double getCurrentEuDt() {
        return regEuDt;
    }

    public String getObjName() {
        return regName;
    }

}
	
