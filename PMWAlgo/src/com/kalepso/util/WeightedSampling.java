package com.kalepso.util;

import java.lang.Math; 
import java.util.stream.*;


public class WeightedSampling 
{

	public static int find_interval(double[] weights, double number)
	  {
	      int pos =-1;
	      int l = weights.length;
	      double acc = 0;
	      double[] weights_acc = new double[l];
	      for (int i =0; i<l; i++ )
	      {
	          weights_acc[i] = weights[i] + acc;
	          acc = weights_acc[i];
	          if (acc >= number)
	          {
	              pos = i;
	              break;
	          }
	      }
	      return pos;
	      
	  }
	  
	  public static int[] wsample (int[] inarray, double[] weights,int n)
	  {
	    int range = weights.length;
	    int[] results = new int[n];
	    double sum = DoubleStream.of(weights).sum(); 
	    double Max = sum;
	    double Min = 0;
	    for (int i=0;i<n;i++)
	    {
	        double nombreAleatoire = Min + Math.random() * ((Max - Min));
	        results[i] = inarray[find_interval(weights,nombreAleatoire)];
	    }
	    return results;
	  }
	  
	  // arguments are passed using the text field below this editor
	  public static void main(String[] args)
	  {
	    int[] a = {0,100,200,300,400};
	    double[] w = {0.05,0.05,0.5,0.2,0.4};
	    int n = 50;
	    int[] results = wsample(a,w,n);
	    for (int i=0;i<n;i++)
	    {
	        System.out.println(results[i]);
	    }
	  }
	
}
