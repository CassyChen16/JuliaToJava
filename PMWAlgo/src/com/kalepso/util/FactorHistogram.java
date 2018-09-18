package com.kalepso.util;

import java.util.HashMap;



public class FactorHistogram extends Data{
	Factor[] factors;
	HashMap<Integer, Integer> lookup;
	
	public FactorHistogram(Factor[] factors, HashMap<Integer, Integer> lookup){
		this.factors = factors;
		this.lookup.putAll(lookup);
	}
	
	public Factor[] getFactors() {
		return factors;
	}
	public void setFactors(Factor[] factors) {
		this.factors = factors;
	}
	public HashMap<Integer, Integer> getLookup() {
		return lookup;
	}
	public void setLookup(HashMap<Integer, Integer> lookup) {
		this.lookup = lookup;
	}
	
	

}
