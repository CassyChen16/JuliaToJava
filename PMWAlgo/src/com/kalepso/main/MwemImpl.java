package com.kalepso.main;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.kalepso.util.CustomException;
import com.kalepso.util.Data;
import com.kalepso.util.HelperFunc;
import com.kalepso.util.MWParameters;
import com.kalepso.util.MWState;
import com.kalepso.util.Queries;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;

public class MwemImpl {
	
	/*
	    noisy_max(mwstate::MWState) 
		Select index of query with largest error after noise addition.
	*/
	@SuppressWarnings({ "unchecked", "hiding" })	
	public static <Q extends Queries, D extends Data> int noisy_max(MWState mw) {
		Q qs = (Q)mw.getQueries();
		D synthetic = (D)mw.getSynthetic();		
		
		float[] real_answers = mw.getReal_answers();
		float[] eval = DataProcess.evaluate(qs, synthetic);
		float[] diffs = HelperFunc.subtract(real_answers, eval);
				
		//do not select previously measured queries
		HashMap<Integer, Float> measurements = mw.getMeasurements();
		Set<Integer> keys = measurements.keySet();
        for(Integer e: keys)
        	if(e >=0 && e< diffs.length)
        		diffs[e] = 0;
        	else
        		throw new CustomException("index out of boundary of diffs");        
		//indmax(abs(diffs) + rand(Laplace(0.0, mw.getScale()), diffs.length))
		RandomEngine generator;
		generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
		float[] rands = new float[diffs.length];
		for(int i=0;i<diffs.length;i++) //// noises ..............
			rands[i]=(float)(mw.getScale()*Distributions.nextLaplace(generator));
		float[] array = new float[diffs.length];
		for(int i=0;i<diffs.length;i++) {
			array[i] = Math.abs(diffs[i]) + rands[i];
		}
		
		float maxd = array[0];
		int maxIndex = 0;
		for(int i=1;i<diffs.length;i++) {
			if(array[i] > maxd) {
				maxd = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}



	/*
	    update!(mwstate::MWState, qindex::QueryIndex)
	
	Perform multiplicative weights update with query given by `qindex`.
	*/
	public static <Q extends Queries> ?? update(MWState mw, int qindex)
	{
		//Q qs = (Q)mw.getQueries();
		float[] query = DataProcess.get(mw.getQueries(), qindex);
		float error = (mw.getMeasurements().get(qindex) - DataProcess.evaluate(query, mw.getSynthetic());
		return DataProcess.update(query, mw.getSynthetic(), error);				
	}
	   

	/*
	    mwem(queries::Queries, data::Data, ps=MWParameters())
	
	Private Multiplicative Weights (MWEM) repeatedly selects largest error query
	and performs multiplicative weights update.
	*/
	public  static MWState mwem(Queries queries, Data data, MWParameters ps) {
	    MWState mwstate;
	    long startTime, endTime, time;
	    float error;
	    int qindex;
	    
	    // Initialization
	    startTime = System.nanoTime();  //System.currentTimeMillis();  
	    mwstate = DataProcess.initialize(queries, data, ps);
	    endTime =  System.nanoTime(); 
	    time = endTime - startTime;
	    
	    if (ps.isVerbose())
	    {
	        error = Error.mean_squared_error(mwstate);
	        System.out.println("Iter.\t Mean sq err\t time (sec)\n");
	        System.out.format("0\t %.3f\t\t %.3f\n", error, time);
	    }
	
	   // Iterations
	    for(int t = 1; t<=ps.getIterations(); t++){
	    	startTime = System.nanoTime();
	       
            // select query via noisy max
            qindex = noisy_max(mwstate);
            HashMap<Integer, Float> measurements = mwstate.getMeasurements();
            RandomEngine generator;
			generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
			float rand_laplace=(float) (mwstate.getScale()*Distributions.nextLaplace(generator));
            float value = mwstate.getReal_answers()[qindex] + rand_laplace; 
            measurements.put(new Integer(qindex), value); 
             

            // update synthetic data approximation
            DataProcess.update(mwstate, qindex);

            //repeatedly update on previously measured queries in random order
            for(int i = 1;i<=ps.getRepetitions();i++)
            {
            	Set<Integer> keys = mwstate.getMeasurements().keySet();
            	List keys_list = new ArrayList(keys);
            	Collections.shuffle(keys_list);
            	int len = keys_list.size();
            	for(int j=0;j<len;j++)
            	{
                    int index =  (int) keys_list.get(j);
                	DataProcess.update(mwstate, index);
                }
            }
            
	        endTime =  System.nanoTime(); 
	        time = endTime - startTime;
	
	        if(ps.isVerbose())
	        {	            
				error = Error.mean_squared_error(mwstate);	           
	            System.out.format("%d\t %.3f\t\t %.3f\n", t, error, time);
	        }
	    }	
	    return mwstate;
     }


}
