package com.kalepso.util;
import com.kalepso.util.HelperFunc;
import com.kalepso.main.HistogramGen;

public class Histogram extends Data {
	private float[] weights;
    private int num_samples;
    
    public Histogram(float[] weights)
    {
        this.weights = weights;
        this.num_samples = 0;
    }
    
    public Histogram(float[] weights, int num_sample)
    {
        this.weights = weights;
        this.num_samples = num_sample;
    }
    
    /**
     * Create histogram representation from tabular data.
     * */
    //Originally: public Histogram(Tabular table) {
    public Histogram(Tabular table) {
    	int d = table.getData().length;
    	int n = table.getData()[0].length;
    	int len = (int)Math.pow(2, d);
    	float[] hg = HelperFunc.zeros(len);// array of length of len
    	for(int i=0;i<n;i++) {
    		int num = 0;
    		float[] x = HelperFunc.findiCol(table.getData(),i);
    		for(int j=0;j<d;j++) {
    			num += Math.round(x[d-j-1])*Math.pow(2,  j); // reverse index of x[]
    			//num += Math.round(x)*Math.pow(2,  j);
    		}
    		hg[num] += 1.0;
    	}
    	
    	float[] weights = HelperFunc.div(hg, HelperFunc.sum(hg));
    	this.weights = weights;
    	this.num_samples = n;    	
    }

    

  	public float[] getWeights() {
		return weights;
	}

	public void setWeights(float[] weights) {
		this.weights = weights;
	}

	public int getNum_samples() {
		return num_samples;
	}

	public void setNum_samples(int num_samples) {
		this.num_samples = num_samples;
	}
    
    
}
