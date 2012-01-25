package com.JavaIndexer.processing;
import java.util.List;
import java.util.ArrayList;


public class ProjectMain {
    public static void main (String args[]){
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        dataPoints.add(new DataPoint(1,2,"Hello"));
        dataPoints.add(new DataPoint(3,4,"How"));
        dataPoints.add(new DataPoint(5,6,"Are"));
        dataPoints.add(new DataPoint(1,1,"You"));
        dataPoints.add(new DataPoint(1,4,"Doing"));
        dataPoints.add(new DataPoint(3,6,"Test"));
        dataPoints.add(new DataPoint(1,11,"Zebra"));

        JCA jca = new JCA(5,100,dataPoints);
        jca.startAnalysis();
        int i = 0;
        for (List<DataPoint> tempV : jca.getClusterOutput()){
        	i++;
            System.out.println("-----------Cluster #" + i + " ---------");
            for (DataPoint dpTemp : tempV){
                System.out.println(dpTemp.getObjName()+
                                   "["+dpTemp.getX()+","+dpTemp.getY()+"]");
            }
        }
    }
}

