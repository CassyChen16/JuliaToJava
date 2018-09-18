package com.kalepso.util;

import java.util.HashMap;

public class MWState<D extends Data, Q extends Queries>{
	
	private D real;
	private D  synthetic;
	private Q  queries;
	private float[] real_answers;
	private HashMap<Integer, Float> measurements = new HashMap<Integer, Float>();
	private float scale;
	
	public MWState(D d, D synthetic, Q qs, float[] real_answers, HashMap<Integer, Float> dict, float scale) {
		this.real = d;
		this.synthetic = synthetic;
		this.queries = qs;
		this.real_answers = real_answers;
		this.measurements = dict;
		this.scale = scale;
	}

	//Initialize MWState in terms of Tabular type
	public MWState(Tabular d, Tabular synthetic, Queries qs, float[] real_answers, HashMap<Integer, Float> dict, float scale) {
		this.real = d;
		this.synthetic = synthetic;
		this.queries = qs;
		this.real_answers = real_answers;
		this.measurements = dict;
		this.scale = scale;
	}
	
	//Initialize MWState in terms of Histogram and HistogramQueries type
	public MWState(Histogram d, Histogram synthetic, HistogramQueries qs, float[] real_answers, HashMap<Integer, Float> dict, float scale) {
		this.real = d;
		this.synthetic = synthetic;
		this.queries = qs;
		this.real_answers = real_answers;
		this.measurements = dict;
		this.scale = scale;
	}
	
	//MWState(Histogram, Histogram, HistogramQueries, float[], HashMap<Integer,Float>, float) is undefined


	public MWState(Tabular d, FactorHistogram synthetic, FactorParities qs, float[] real_answers,
			HashMap<Integer, Float> dict, float scale) {
		this.real = d;
		this.synthetic = synthetic;
		this.queries = qs;
		this.real_answers = real_answers;
		this.measurements = dict;
		this.scale = scale;
	}

	public Data getReal() {
		return real;
	}

	public void setReal(Tabular real) {
		this.real = real;
	}

	public Data getSynthetic() {
		return synthetic;
	}

	public void setSynthetic(Data synthetic) {
		this.synthetic = synthetic;
	}

	public Queries getQueries() {
		return queries;
	}

	public void setQueries(Queries queries) {
		this.queries = queries;
	}

	public float[] getReal_answers() {
		return real_answers;
	}

	public void setReal_answers(float[] real_answers) {
		this.real_answers = real_answers;
	}

	public HashMap<Integer, Float> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(HashMap<Integer, Float> measurements) {
		this.measurements = measurements;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
		

}
