package com.kalepso.main;

import com.kalepso.util.MWState;
import com.kalepso.util.MwmException;
import com.kalepso.util.Queries;
import com.kalepso.util.Tabular;
import com.kalepso.util.CustomException;
import com.kalepso.util.Data;
import com.kalepso.util.FactorHistogram;
import com.kalepso.util.FactorParities;
import com.kalepso.util.HelperFunc;
import com.kalepso.util.Histogram;
import com.kalepso.util.HistogramQueries;

public class Error<Q extends Queries, D extends Data> {
	
		
	/**
	 *  maximum_error(mw::MWState)
	 *  (Calculate error in terms of MWState.queries, MWState.synthetic, and MWState.real_answers
	 *  In order to implement it, has method has to be overloaded)
		Compute maximum error of synthetic data on the query set. Result is not
		differentially private.
	 * @throws CustomException 
	 * */
	//public static float[] evaluate(HistogramQueries qs, Histogram h) 
	//public static float[] evaluate(FactorParities qs,  Tabular table)
	//public static float[] evaluate(FactorParities qs, FactorHistogram d) 
	
	@SuppressWarnings({ "unchecked", "hiding" })	
	public static <Q extends Queries, D extends Data> float maximum_error(MWState mw) throws CustomException {
		Q qs = (Q)mw.getQueries();
		D synthetic = (D)mw.getSynthetic();		
		
		float eval[] = DataProcess.evaluate(qs, synthetic);
		float[] minus =  HelperFunc.subtract(eval, mw.getReal_answers());
		float[] rs = HelperFunc.abs(minus);
	    return HelperFunc.max(rs);
	}
	
/**
 *   mean_squared_error(mw::MWState)

	Compute mean_squared_error of synthetic data on the query set. Result is not
	differentially private.
 * @throws CustomException 
 * */
	@SuppressWarnings({ "unchecked", "hiding" })
	public static <Q extends Queries, D extends Data> float mean_squared_error(MWState mw) throws CustomException {
		Q qs = (Q)mw.getQueries();
		D synthetic = (D)mw.getSynthetic();	
		
		float eval[] = DataProcess.evaluate(qs, synthetic);
		float[] errors =  HelperFunc.subtract(eval, mw.getReal_answers());
		int length = errors.length;
		float result = (float)Math.pow(HelperFunc.l2norma(errors),2)/length;
		
		return result;
	}

/**
 *  kl_divergence(a, b)
	Compute KL-divergence between two normalized histograms a and b.
 * @throws CustomException 
 * */
  

	public static <Q extends Queries, D extends Data> float kl_divergence(float[] a, float[] b) throws CustomException{
		float r = 0;
		if(a.length != b.length)
			throw new CustomException("a[] and b[] length are not equal"); 
		for(int i=0; i<a.length;i++) {		
			float tmp = a[i]/b[i];
			if(a[i] > 0) 
				r += a[i]*(Math.log(tmp)/Math.log(2)+1e-10);
		}
		return r;
	}
	   
}
