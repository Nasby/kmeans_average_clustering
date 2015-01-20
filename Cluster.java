import java.lang.Object;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.math.BigInteger;
import java.math.BigDecimal;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class Cluster{

	public static void main(String[] args)
    {
		Boolean kmeans = false;
		Boolean average = false;
		StringTokenizer st;
		int x = 0;
		int clusterSize = 0;
		double[][] data;
		
		if( args.length < 3 ){
			System.err.println("Usage:  java Cluster [.txt Data File] [Int Amt. of Clusters] [kmeans/average]");
			return;
		}
		else{
		
			File file = new File(args[0]);
			clusterSize = Integer.parseInt(args[1]);
			try{
				FileReader reader = new FileReader(file);
				BufferedReader buffReader = new BufferedReader(reader);
				int dimensions = 1;
				String s;
				
				//Figures out points and dimensions 
				while(( s = buffReader.readLine() ) != null ){
					if( (dimensions = s.length() - s.replaceAll(" ", "").length()) > 0)
						dimensions = s.length() - s.replaceAll(" ", "").length();
					else if( (dimensions = s.length() - s.replaceAll("\t", "").length()) > 0)
						dimensions = s.length() - s.replaceAll("\t", "").length();
					x++;
				}
				data = new double[x][dimensions+2];
				//System.out.printf("Dimensions: %d\n, Rows: %d\n", dimensions, x);
				//store the points
				reader = new FileReader(file);
				buffReader = new BufferedReader(reader);
				x = 0;
				int y = 0;
				while(( s = buffReader.readLine() ) != null ){
					y = 0;
					st = new StringTokenizer(s);
					while( st.hasMoreTokens() ){
						data[x][y] = Double.parseDouble(st.nextToken());
						y++;
					}
					data[x][y] = (double)-1;
					x++;
				}
			}
			catch(IOException e){
				return;
			}
			if( args[2].equals("kmeans") )
				kmeans = true;
			else if( args[2].equals("average") ) 
				average = true;
			else{
				System.err.println("Usage:  java Cluster [.txt Data File] [Int Amt. of Clusters] [kmeans/average]");
				return;
			}
		}
		//prints 2d array
		/*for(int i = 0; i < data.length; i++){
			for(int y = 0; y < data[i].length; y++)
				System.out.printf("%f ", data[i][y]);
			System.out.println();
		}*/
		
		if( kmeans == true ){
			Kmeans mySet1 = new Kmeans(data, clusterSize);
			int[] kSet = Kmeans.returnSet(mySet1);
			return;  
		}
		else if( average == true ){
			Average mySet2 = new Average(data, clusterSize);
			int[] aSet = Average.returnSet(mySet2);
			return;
		}
		else{
			System.out.printf("Invalid function to run on data set\n");
			//return;
		}
		/*
		int numerator = 0;
		//Hamming Distance
		for( int i = 0; i < aSet.length-1; i++ ){
			for( int y = 1; y + i < aSet.length; y++ ){
				if( aSet[i] == aSet[i+y] ){
					if ( kSet[i] != kSet[i+y] )
						numerator++;
				}
				else{
					if( kSet[i] == kSet[i+y] )
						numerator++;
				}
			}
		}
		double b = (double)numerator;
		BigInteger a = binomial(aSet.length,2);
		double c = a.doubleValue();
		System.out.println("Hamming Distance is " + b/c);
		return;*/
		
	}
	
	static BigInteger binomial(final int N, final int K) {
		BigInteger ret = BigInteger.ONE;
		for (int k = 0; k < K; k++) {
			ret = ret.multiply(BigInteger.valueOf(N-k))
					 .divide(BigInteger.valueOf(k+1));
		}
		return ret;
	}

}