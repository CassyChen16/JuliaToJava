package com.kalepso.util;

public class HelperFunc {
	
	public static float dot(float[] a, float[] b) {
	    float sum = 0;
	    for (int i = 0; i < a.length; i++) {
	      sum += a[i] * b[i];
	    }
	    return sum;
	  }
	 
	 public static float[] add(float[] a, float num) {
		    float[] b = a;
		    for (int i = 0; i < a.length; i++) {
		      b[i] += b[i] +num;
		    }
		    return b;
	 }
	 
	 public static float[] subtract(float[] a, float[] b) {
		    if(a.length != b.length) return null;
		    float[] c = new float[a.length];
		    for (int i = 0; i < a.length; i++) {
		      c[i] = a[i] - b[i];
		    }
		    return c;
	 }
	 
	 public static float sum(float[] a) {
		    float s = 0;
		    for (int i = 0; i < a.length; i++) {
		      s += a[i] ;
		    }
		    return s;
	 }
	 
	 public static float[] abs(float[] a) {
		    float s[] = new float[a.length];
		    for (int i = 0; i < a.length; i++) {
		       s[i] = Math.abs(a[i]) ;
		    }
		    return s;
	 } 
	 
	 
	 public static float max(float[] a) {
		    float s = a[0];
		    for (int i = 1; i < a.length; i++) {
		    	if(a[i] > s)
		           s = a[i] ;
		    }
		    return s;
	 }
	 
	 
	 
	 
	 public static float[] div(float[] a, float num) {
		    float[] b = a;
		    for (int i = 0; i < a.length; i++) {
		      b[i] = b[i]/num ;
		    }
		    return b;
	 }
	 
	 public static float[] ones(int len) {
		 float[] a = new float[len];
		 for(int i=0;i<len;i++) {
			 a[i] = 1;
		 }
		 return a;
	 }
	 
	 public static float[] onesMultiple(int len, float muliplenum) {
		 float[] a = new float[len];
		 for(int i=0;i<len;i++) {
			 a[i] = 1*muliplenum;
		 }
		 return a;
	 }
	 
	 public static float[] arrayCat(float[] a, float[] b) {
		 int len = a.length + b.length;
		 float[] arr = new float[len];
		 int index=0;
		 for(int i=0;i<a.length-1;i++) {
			 arr[index] = a[i];
			 index++;
		 }
		 for(int i=0;i<b.length-1;i++) {
			 arr[index] = b[i];
			 index++;
		 }
		 return arr;
	 }
	 
	 public static float[] zeros(int len) {
		 float[] a = new float[len];
		 for(int i=0;i<len;i++) {
			 a[i] = 0;
		 }
		 return a;
	 }
	 
	//public static float[][] ones(int row, int col) {	 
	 public static float[][] gen_matrix(int row, int col, float value) {
		 float[][] a = new float[row][col];
		 for(int i=0;i<row;i++) 
			 for(int j=0;j<col;j++)
			     a[i][j] = value;
		 return a;
	 }
	 
	 public static int[] reverse(int[] digits) {
		 int len = digits.length;
		 int[] arr = new int[len];
		 for(int i=0;i<len;i++)
			 arr[i] = digits[len-1-i];
		 return arr;
	 }
	 
	 /**
	  * Returns an array with element (Int type) of the digits of idx in the given base	  * 
      *optionally padded with zeros to a specified size. More significant digits are at higher indexes, 
      *such that n == sum([digits[k]*base^(k-1) for k=1:length(digits)])
	  * digits(10, 10) = [0 1]
	  * digits(10, 2) = [0 1 0 1]
	  * digits(10, 2,6) = [0 1 0 1 0 0]
	  * 
	  * */
	 public static int[] digits(int idx, int d) {
		 int[] arr = new int[d];
		 for(int i=0;i<d;i++)
			 arr[i] = 0;
		 int i=0;
		 int number = idx;
		 while (number != 0) {
		        arr[i] = number % 2;
		        number = number / 2;	
		        i++;
		 }
		 return arr;
	 }
	 
	 public static float[] intoTofloat(int[] arr_int) {
		 float[] arr_float = new float[arr_int.length];
		 for(int i=0;i<arr_int.length;i++) {
			 arr_float[i] = (float)arr_int[i];
		 }
		 return arr_float;
	 }

	 //Kronecker tensor product of two vectors, A=(a_1, b_1), B=(b_1, b_2);
	 // A tensor B=(a_1*B, a_2*B)=(a_1*b_1, a_1*b_2, a_2*b_1, a_2*b_2)
	 public static float[] kron(float[] A, float[] B){
		 int len = A.length * B.length;
		 float[] C = new float[len];
		 int index = 0;
		 for(int i=0;i<A.length;i++) 
			 for(int j=0;j<B.length;j++)
				 C[index++] = A[i]*B[j];
		 return C;		 
	 }
	 
	
        /***test hadamard_basis_vector 
	     public static void main(String args[]) {

	         double [] result;
	         int index = 4;
	         int dimension = 4;
	         result = hadamard_basis_vector(index,dimension);
	         for (int i=0;i<result.length;i++)
	         {
	             System.out.println(result[i]);
	         }
	     }
	     ***/
	 
	     public static float[]  hadamard_basis_vector (int index, int dimension){
	    	 float[] hadamard = new float [(int)Math.pow(2,dimension)];
	         hadamard[0] = (float)1.0;
	         for (int i = 0;i<=dimension-1;i++)
	         {
	        	 float sign = (index & (int)Math.pow(2,i) )> 0 ? (float)-1.0 : (float)1.0;
	             for (int j = 0;j<Math.pow(2,i);j++)
	             {
	                 hadamard[(int)(j + Math.pow(2,i))] = sign * hadamard[j];
	             }
	         }
	         return hadamard;
	     }
	     
	 	//find the position of a that first appears in attributes
	 	public static int findfirst(int[] attributes, int a){
	 		for(int i=0;i<attributes.length;i++) {
	 			if(attributes[i] == a) 
	                 return i;
	 		}
	 		return -1; // if a does not exist in 
	 	}
	 	
	 	//find the i-th column of data[][]
	 	public static float[] findiCol(float[][] data, int index) {
	 		int len = data.length;
	 		float[] arr = new float[len];
	 		for(int i=0;i<len;i++)
	 			arr[i] = data[i][index];
	 		return arr;
	 	}

	 	public static float l2norma(float[] d){
	 		  float norm = 0;
	 		  for(float x: d)
	 		  {
	 		    norm += x*x;
	 		  }
	 		  norm = (float)Math.sqrt(norm);
	 		  return norm;
	 	}
	 	
	 	//For test purpose
	 	public static void print(float[][] matrix) {
	 		System.out.println("Matrix printout:");
	 		int dim = matrix.length;
	 		int col = matrix[0].length;
	 		for(int i=0;i<dim;i++) 
	 		{
	 			System.out.print("line " + i +": ");
	 			for(int j=0;j<col;j++) {
	 				System.out.print(matrix[i][j]+" ");
	 			}
	 			System.out.println();
	 		}
	 	}
	 	
	 	public static void print(float[] arr) {
	 		System.out.print ("Array printout: ");
	 		int len = arr.length;
	 		for(int i=0;i<len;i++) 
	 		{
	 			System.out.print(arr[i]+", ");
	 		}
	 	}
	 	
	 	public static float[] transfer(float[][] data) {
	 		
	 		int dim = data.length;
	 		int col = data[0].length;
	 		float[] arr = new float[dim*col];
	 		int z = 0;
	 		for(int i=0;i<dim;i++)
	 			for(int j=0;j<col;j++) 
	 			{
	 				arr[z++] = data[i][j];
	 			}
	 		return arr;
	 	}


}
