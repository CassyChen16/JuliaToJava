package com.kalepso.main;

import java.util.HashMap;

import com.kalepso.util.CustomException;
import com.kalepso.util.Histogram;
import com.kalepso.util.HistogramQueries;
import com.kalepso.util.HistogramQuery;
import com.kalepso.util.MWParameters;
import com.kalepso.util.MWState;
import com.kalepso.util.Queries;
import com.kalepso.util.HelperFunc;
import com.kalepso.util.Tabular;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;

public class HistogramGen {
	
	 /**
	  * Get the i-th query in the i-th column of the query matrix
	  * */
	 public static HistogramQuery get(HistogramQueries queries, int qIndex) throws Exception {
		 /*
		 int dim = queries.getQueries().length;
		 int col = queries.getQueries()[0].length;
		 
		 if (dim < 1 || col < qIndex) {
		        throw new IndexOutOfBoundsException();
		 }
		 
		 float[] arr = new float[dim];		 
		 for(int j=0;j<dim;j++) {
			 arr[j] = (float) queries.getQueries()[j][qIndex];
		 }

		HistogramQuery hq = new HistogramQuery(arr);
		return  hq;*/
		 
		float[] query = queries.getQueryAtIndex(qIndex);
		HistogramQuery q = new HistogramQuery(query);
		return q;		
	}

	//called by evaluate method in FactorHistGen.java
	public static  float evaluate(HistogramQuery query, Histogram h) {
       float result = HelperFunc.dot(query.getWeights(), h.getWeights());
       return result;
   }

	 
	public static float[] evaluate(HistogramQueries qs, Histogram h) throws Exception {
		
		int num_queries = qs.getQueries()[0].length; //number of columns
		
		if(num_queries != h.getWeights().length)
			throw new CustomException("Query (in Queries) length is different from weights length");
		
		float[] vec = new float[num_queries];
		for(int i=0; i<num_queries; i++) {
			vec[i] = HelperFunc.dot(qs.getQueries()[i], h.getWeights());
		}
		return vec;
	}

	
	  public static Histogram normalize(Histogram h) {		  
		  float[] weights = h.getWeights();
		  h.setWeights(HelperFunc.div(weights, HelperFunc.sum(weights)));
	      return h;
	  }
   
  
    /**
     * # if error is > 0, increase weight on positive elements of queries[query] and 
     * decrease weight on negative elements. Magnitude of error determines step size.
     * **/
    ////called by update method in FactorHistGen.java
    public static Histogram update(HistogramQuery q, Histogram h, float error) {
        for(int i=0;i<h.getWeights().length;i++)
       	h.getWeights()[i] *= Math.exp(error * q.getWeights()[i] / 2.0);
        return  normalize(h);
    }

     //?????????
     // Original parameter "qs" is Queries, see if later other functions need similar data type
	 public static MWState initialize(HistogramQueries qs, Histogram d, MWParameters ps) throws Exception {
		    Histogram data = normalize(d);
		    float[] dweights = data.getWeights();
			int hg_length = data.getWeights().length;
			int num_samples = d.getNum_samples();
			Histogram synthetic;
			//LaplaceDistribution laplace = new LaplaceDistribution(0.0, 1.0/(ps.getEpsilon()*num_samples));
			
			RandomEngine generator;
			generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
			
			
			if(ps.isNoisy_init()) 
			{
				float[] weights = new float[hg_length];
				float[] noises = new float[hg_length];
				// noise = rand(laplace,hg_lengh)
				for(int i=0;i<hg_length;i++) //// noises ..............
					noises[i]=(float) (1/(ps.getEpsilon()*num_samples)*Distributions.nextLaplace(generator));
					
				for(int i=0;i<hg_length;i++) {
					// num_samples is correct??
					weights[i] = (float) Math.max(dweights[i] + noises[i] - 1.0/(Math.E*num_samples*ps.getEpsilon()), 0.0);
				}
				
				weights = HelperFunc.div(weights, HelperFunc.sum(weights));
				
				float[] tmp = HelperFunc.add(HelperFunc.div(weights, 2), (float)0.5/hg_length);
				synthetic = new Histogram(tmp);
			}
			else 
			{
				synthetic = new Histogram(HelperFunc.div(HelperFunc.ones(hg_length),hg_length));
			}
			float[] real_answer = evaluate(qs, d);
			float scale = (float) ((float)2.0/(ps.getEpsilon()*num_samples));
			HashMap<Integer, Float> dict = new HashMap<>();
			MWState ms = new MWState(d, synthetic, qs, real_answer, dict, scale);
			
			return ms;
	 }

	 public static MWState initialize(HistogramQueries qs, Tabular d, MWParameters ps) throws Exception {
			// TODO Auto-generated method stub
		    Histogram data = new Histogram(d);
		    initialize(qs, data, ps);
			return null;
	 }
	 
	 public static void main(String[] args){
		// Queries ds = new Queries(0);
		 
		 HistogramQueries hqs = new HistogramQueries();
		 
		 Queries ds1 = hqs;
		 
		 String fullname = hqs.getClass().getName();
		 String classname = fullname.substring(fullname.lastIndexOf(".")+1);
		 if(classname.equals("HistogramQueries"))
			 System.out.println("ok");
		 else 
			 System.out.println("no");
		 
		 System.out.println(classname);
		 System.out.println(ds1.getClass().getName());
	 }
	 
	 
	 
}
