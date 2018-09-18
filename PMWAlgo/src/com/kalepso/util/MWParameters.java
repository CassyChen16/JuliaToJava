package com.kalepso.util;

public class MWParameters {

	private float epsilon;
	private long iterations;
	private long repetitions;
	private boolean noisy_init;
	private boolean verbose;
	
	public MWParameters() 
	{
			this.epsilon=1;
            this.iterations=10;
            this.repetitions=10;
            this.noisy_init=false;
            this.verbose=false;
	}

	
	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}
	public long getIterations() {
		return iterations;
	}
	public void setIterations(long iterations) {
		this.iterations = iterations;
	}
	public long getRepetitions() {
		return repetitions;
	}
	public void setRepetitions(long repetitions) {
		this.repetitions = repetitions;
	}
	public boolean isNoisy_init() {
		return noisy_init;
	}
	public void setNoisy_init(boolean noisy_init) {
		this.noisy_init = noisy_init;
	}
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	

}
