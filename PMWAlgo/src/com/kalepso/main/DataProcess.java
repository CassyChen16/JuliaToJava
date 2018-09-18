package com.kalepso.main;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.kalepso.util.CustomException;
import com.kalepso.util.Data;
import com.kalepso.util.Factor;
import com.kalepso.util.FactorHistogram;
import com.kalepso.util.FactorHistogramQueries;
import com.kalepso.util.FactorHistogramQuery;
import com.kalepso.util.FactorParities;
import com.kalepso.util.FactorParity;
import com.kalepso.util.HelperFunc;
import com.kalepso.util.Histogram;
import com.kalepso.util.HistogramQueries;
import com.kalepso.util.HistogramQuery;
import com.kalepso.util.MWParameters;
import com.kalepso.util.MWState;
import com.kalepso.util.MwmException;
import com.kalepso.util.Parities;
import com.kalepso.util.Queries;
import com.kalepso.util.RangeQueries;
import com.kalepso.util.Tabular;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;

public class DataProcess {
	
	// From histogram.lj file
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
	 public static <Q extends Queries> MWState initialize(Queries queries, Histogram d, MWParameters ps) throws Exception {
		    @SuppressWarnings("unchecked")
			Q qs = (Q)queries;
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
				//int r = cern.jet.random.Binomial.staticNextInt(5, 2);
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
	 
	 
	 //From factor.lj
	  public static int get_parent(FactorHistogram d, int[] attributes) {
			int len = attributes.length;
			int[] factor_idxs = new int[len];
			for(int i=0;i<len;i++)
				factor_idxs[i] = d.getLookup().get(i);
			//check factor_idexs every element and compare with factor_idxs[0], if all equal to factor_idxs[0], return 
			//true; Otherwise, return false.
			boolean flag = true;
			for(int i=0;i<len;i++)
				if(factor_idxs[i] != factor_idxs[0]) {
					flag = false;
					break;
				}
			if(flag)
				return factor_idxs[0];
			else 
				return 0;
				
		}

		public static Factor merge_factors(Factor f, Factor g) {
			int len_f = f.getAttributes().length;
			int len_g = g.getAttributes().length;
			int[] merge_attr = new int[len_f + len_g];	
			int i, j;
			for(i=0;i<len_f;i++)
				merge_attr[i] = f.getAttributes()[i];
			int index = i++;
			for(j=0;j<len_g;j++)
				merge_attr[index++] = g.getAttributes()[j];
			
			float[] weights = HelperFunc.kron(f.getHistogram().getWeights(), g.getHistogram().getWeights()) ;
			Histogram histogram = new Histogram(weights);
			Factor factor = new Factor(merge_attr, histogram);
			return factor;
		}
		  

		public  static void merge_components(FactorParity q, FactorHistogram d){
			int[] arr_att = q.getAttributes();
			HashMap<Integer, Integer> lookup = d.getLookup();
			Factor[] factors = d.getFactors();
			int parent_index = lookup.get(arr_att[0]); 
			int current_index;
			for(int ele: arr_att) {
				current_index = lookup.get(ele);
				if (current_index != parent_index) {
					factors[parent_index] = merge_factors(factors[parent_index], factors[current_index]);				
					int[] attributes = factors[current_index].getAttributes();
					for(int a: attributes) {
						parent_index = lookup.get(a);					
					}
					factors[current_index].setAttributes(null); // reset list to empty
					float[] weights = {(float)1.0};
					factors[current_index].setHistogram(new Histogram(weights));
				}
			}
					
		}
		
		/**
		 * Original function signature (FactorHistogramQuery is an abstract type, 
		 * FactorHistogramQuery only inherited by FactorParity)
		 * public static  float evaluate(FactorHistogramQuery q, FactorHistogram d) {
		 * */
		public static  float evaluate(FactorParity q, FactorHistogram d) {
			Factor[] factors = d.getFactors();
			int parent_index = get_parent(d, q.getAttributes());
			if(parent_index > -1)  // if parent_index exists
			{
				Factor c = factors[parent_index];
				HistogramQuery qc = restrict(q, c.getAttributes());
				return evaluate(qc, c.getHistogram());
			}else{
				return default_value(q);
			}			      
		}
		
		
		public  Histogram update(FactorParity q, FactorHistogram d, float error){
			int parent_index = get_parent(d, attributes(q));
			if(parent_index > 0) {
				Factor c = d.getFactors()[parent_index];
				HistogramQuery qc = restrict(q, c.getAttributes());
				update(qc, c.getHistogram(), error);
				return normalize(c.getHistogram());
			}else {
				merge_components(q, d);
				Histogram hg = update(q, d, error);
				return hg;
			} 
		}
		
		// normalize happens inside update!
		public static FactorHistogram normalize(FactorHistogram h){
			return h;
		}
		
		/*
		 * Original function signature is 
		 * public MWState initialize(FactorHistogramQueries queries, Tabular table, MWParameters ps)
		 * here FactorHistogramQueries is abstract type and only inherited by FactorParities
		 * */
		public MWState initialize(FactorParities queries, Tabular table, MWParameters ps) {
			int d = table.getData().length; // # of rows
			int n = table.getData()[0].length; // # of columns
			float[] weights = new float[ ] {(float)0.5, (float)0.5};		
			Factor[] components = new Factor[d];
			for(int i=0;i<d;i++) {
				int[] att = new int[] {i};
				Histogram hg = new Histogram(weights);
				Factor f = new Factor(att,hg);
				components[i] = f;
			}
			HashMap<Integer, Integer> lookup = new HashMap<Integer, Integer>();
	        for(int i=0;i<d;i++)
				lookup.put(new Integer(i), new Integer(i));
	        FactorHistogram synthetic = new FactorHistogram(components, lookup);
	        float[] real_answers = evaluate(queries, table); ///??????????????
	        float scale = (float)(2.0/(ps.getEpsilon()*n));
	        MWState ms = new MWState(table, synthetic, queries, real_answers, new HashMap<Integer, Float>(), scale);
	        return ms;
		}

	 public static MWState initialize(HistogramQueries qs, Tabular d, MWParameters ps) throws Exception {
			// TODO Auto-generated method stub
		    Histogram data = new Histogram(d);
		    return initialize(qs, data, ps);
	 }
	 
	 
	 //From parities.jl
	 public static int[] attributes(FactorParity q){		
		return q.getAttributes();
	 }
	
	 public static float default_value(FactorParity q){
		 return (float)0.0;
	 }

	 
	 public FactorParity get(FactorParities qs, int i) {
			FactorParity[] queries = qs.getQueries();
			return queries[i];
	}
	 
	 public static HistogramQuery get(Parities queries, int i) {
		 float[] vector = HelperFunc.hadamard_basis_vector(queries.getIdx()[i]-1, queries.getDimension());
		 HistogramQuery hq = new HistogramQuery(vector);
		 return hq;
	 }
		 
	 
 	 public static HistogramQuery restrict(FactorParity q, int[] attributes) {
		HistogramQuery histquery;
		int idx = 0, i;
		int d = attributes.length;
		for(int a: q.getAttributes()) {
			i = HelperFunc.findfirst(attributes, a);
			idx += Math.pow(2, d-i);	
		}
		float[] vec = HelperFunc.hadamard_basis_vector(idx, d);
		histquery = new HistogramQuery(vec);
		return 	histquery;  		
	 }
 	 
 	 public static float parity(float[] x, int[] attributes) {
 		 float sum = 0;
 		 for(int i=0;i<attributes.length;i++) {
 			 int index = attributes[i];
 			  sum += x[index];
 		 } 			 
 		 return (float)Math.pow(-1.0, sum);
 	 }
			
 	public static float evaluate(FactorParity q, Tabular table) {
 		float  s = (float)0.0;
 		float[][] data =  table.getData();
 		int n = data.length * table.getData()[0].length; // number of elements in data
 		int[] a = attributes(q);
 		for(int i=0;i<n;i++) {
 			float[] arr = new float[data.length];
			arr = HelperFunc.findiCol(data, i);
			s += parity(arr, a);
 		} 		 		
 		return s/n;  		
 	}
   
 	public static float[] evaluate(FactorParities qs,  Tabular table) {
 		FactorParity[] queries = qs.getQueries();
 		int len = queries.length;
 		float[] evals = new float[len];
 		for(int i=0;i<len;i++)
 			evals[i] = evaluate(queries[i], table);
        return evals;
 	}


    public static float[] evaluate(FactorParities qs, FactorHistogram d) {
    	 FactorParity[] queries = qs.getQueries();
    	 int len = queries.length;
    	 float[] arr = new float[len];
    	 for(int i=0;i<len;i++) {
    		 FactorParity q = queries[i];
    		 arr[i] = evaluate(q, d);
    	 }
    	 return arr;
    }
   
    /*
    public MWState initialize(Parities queries, Tabular table, MWParameters ps) {
    	Histogram hg = new Histogram(table);
    	initialize(queries, hg, ps);
    }
    */
    
    //rangequeries.lj
    /*
    	get(queries::RangeQueries, i::Int)
    	Return -1/+1 indicator vector of interval [1:i].
    */

	public static HistogramQuery get(RangeQueries queries, int i) {
		int count = queries.getDomain()-i;
		float[] a = HelperFunc.ones(i);
		float[] b = HelperFunc.onesMultiple(count, -1);
		float[] c = HelperFunc.arrayCat(a, b);
		HistogramQuery hq = new HistogramQuery(c);
		return hq;
	}
	
	/*
	   evaluate(queries::RangeQueries, h::Histogram) 
	   Evaluate all range queries on the given histogram.
	*/
	public static float[] evaluate(RangeQueries queries, Histogram h) throws CustomException {
	
		if(queries.getDomain() == h.getWeights().length)
			throw new CustomException("Queries domain length is not equal to h weight length.");
	    float[] answers = new float[queries.getDomain()];
	    float running_sum = -1;
	    for(int j=0;j<queries.getDomain();j++) {
	    	running_sum += 2.0 * h.getWeights()[j];
	    	answers[j] = running_sum;
	    }	       
	    return answers;
	}

    
    //Extended functions
    
    //("HistogramQueries") &&  ("Histogram");
    //("FactorParities")  && ("Tabular")
    //("FactorParities")  && ("FactorHistogram")
    public static <Q extends Queries, D extends Data> float[] evaluate(Q qs, D h) throws CustomException {
    	//Get class names
		String qs_path = qs.getClass().getName();
		String qs_name = qs_path.substring(qs_path.lastIndexOf(".")+1);		 
		String s_path = h.getClass().getName();
		String s_name = s_path.substring(s_path.lastIndexOf(".")+1);
		
		
		
		if(qs_name.equals("HistogramQueries") && s_name.equals("Histogram") || 
		   qs_name.equals("FactorParities")  && s_name.equals("Tabular") ||
		   qs_name.equals("FactorParities")  && s_name.equals("FactorHistogram")) 
		{
			return evaluate(qs, h);      
      		
		}    	  
    	else
    		throw new CustomException("Queries or Data is abstract types, no evaluate method exists.");
    }

    /**
     * 1)function get(queries::Parities, i::Int) (parties.jl): return HistogramQuery
     * 2)get(qs::FactorParities, i::QueryIndex) = qs.queries[i] (parties.jl)
     * 3)function get(queries::HistogramQueries, i::QueryIndex) (histogram.jl): return HistogramQuery
     * 4) function get(queries::RangeQueries, i::Int) (rangequeries.jl): return HistogramQuery
     * /
     
    public static <Q extends Queries> float[] get(Q qs, int queryIndex) {
    	
    }
    */
	 
	 public static void main(String[] args){
		 // To test Histogram		 
		 float[][] mat = {{1, 0, 1, 0, 1}, {0, 1, 0, 0, 1}};
		 Tabular  tab = new Tabular(mat);
		 Histogram hg = new Histogram(tab);
		 HelperFunc.print(hg.getWeights());
		 
		 // To test Tabular
		
	 }
	 
	 //Note: Java implements multiple dispatch
	 //https://www.linkedin.com/pulse/multiple-dispatch-multimethods-java-shyam-baitmangalkar/

}
