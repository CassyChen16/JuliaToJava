package com.kalepso.util;
import com.kalepso.util.WeightedSampling;
import com.kalepso.util.HelperFunc;;

public class Tabular extends Data{

	private float[][] data;
	
	public Tabular(float[][] data) {
		this.data = data;
	}
	
	/**
	 * Create tabular data by sampling n times weighted by histogram.
	 * */
	public Tabular(Histogram hist, int n){
		int N = hist.getWeights().length; 
		int d = (int)(Math.log(N)/Math.log(2));
		int[] idx = WeightedSampling.wsample(collect(0,N-1), Double(hist.getWeights()), n);
		
        float[][] data_matrix = HelperFunc.gen_matrix(d, n,1);
        for(int col = 0  ;col < n; col++) 
        {
        	int[] arr_digits = HelperFunc.digits(idx[col], d);
        	data_matrix[col] = HelperFunc.intoTofloat(HelperFunc.reverse(arr_digits));
        }
        this.data = data_matrix;

    }
	
	public float[][] getData() {
		return data;
	}

	public void setData(float[][] data) {
		this.data = data;
	}
	
	public int[] collect(int start, int end) {
		int[] arr = new int[end+1];
		for(int i=start;i<=end;i++)
			arr[i] = i;
		return arr;
	}

	public double[] Double(float[] weights) {
		double[] result = new double[weights.length];
		for(int i=0;i<weights.length;i++) {
			result[i] = Double.valueOf(weights[i]);
		}
		return result;
	}
	
	public float[] getCol(int i) {
		float[][] data = this.getData();
		float[] col = new float[data.length];
		for(int j=0;j<data.length;j++) {
			col[j] = data[j][i];
		}
		return col;
	}
	
}