package com.kalepso.util;

public class HistogramQueries extends Queries{
	 private float[][] queries;
	 
	 //Initialization of a set of queries
	 public HistogramQueries(int d, int n)
	 {
		 this.queries = new float[d][n];
	 }
	 
	 public HistogramQueries(float[][] queries)
	 {
		 this.queries = queries;
	 }

	public HistogramQueries() {
	}

	public float[][] getQueries() {
		return queries;
	}

	public void setQueries(float[][] queries) {
		this.queries = queries;
	}
	
	
	//Get the query at the i-th column
	public float[] getQueryAtIndex(int col) {
		int len = this.queries.length;
		float[] query = new float[len];
		for(int i=0;i<len;i++)
		       query[i] = this.queries[0][col];
		return query;
	}
	 
	 
	 

}
