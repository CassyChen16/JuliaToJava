package com.kalepso.util;

public class Factor {
	
	private int[] attributes;
    private Histogram histogram;
    
    public Factor(int[] attributes, Histogram histogram) {
    	this.attributes = attributes;
    	this.histogram = histogram;
    }
    
	public int[] getAttributes() {
		return attributes;
	}
	public void setAttributes(int[] attributes) {
		this.attributes = attributes;
	}
	public Histogram getHistogram() {
		return histogram;
	}
	public void setHistogram(Histogram histogram) {
		this.histogram = histogram;
	}
    
    

}
