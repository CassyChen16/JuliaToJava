package com.kalepso.util;

import java.util.ArrayList;

public class Parities extends Queries{

	private int dimension;
	private int order;
	private int[] idx;
	
	public Parities(int dimension, int order, int[] idex){
		this.dimension = dimension;
		this.order = order;
		this.idx = idx;
	}
	
	public static Parities parities(int dimension, int order) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		GosperList<Integer> gosper;
		arr.add(new Integer(1));
		for(int r=1;r<=order;r++) {
			gosper = new GosperList<Integer>(3,2);
			for(Integer num : gosper) {
				arr.add(num+1); 
			}
		} 
		
		int[] idx = new int[arr.size()];
		int i=0;
		for(Integer ele: arr) {
			idx[i++] = ele;
		}
            
        return new Parities(dimension, order, idx);
	}

	
	
	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int[] getIdx() {
		return idx;
	}

	public void setIdx(int[] idx) {
		this.idx = idx;
	}
	
	
	
}
