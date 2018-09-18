package com.kalepso.util;

public class HistogramQuery extends Query{
	 private float[] weights;   
     
	 public HistogramQuery(int n)
	 {
		 this.weights = new float[n];
	 }
	 
	 public HistogramQuery(float[] weights)
	 {
		 this.weights = weights;
	 }

	public float[] getWeights() {
		return weights;
	}

	public void setWeights(float[] weights) {
		this.weights = weights;
	}
	 
	 	 
}
