import java.lang.Math;
import java.lang.Object;
import java.util.*;

class Average{
	
	double[][] data;
	int clusters;
	
	Average(){
		data = new double[1][1];
		data[0][0] = (double)0;
	}
	
	Average(double[][] a, int b){
		data = a;
		clusters = b;
	}
	
	public static int[] returnSet(Average set){
		double closest = (double)1000000000;
		double currentClosest;
		double[] closestArray = new double[set.data.length];
		double[] maxClosestArray = new double[set.data.length];
		int[] maxClosestPoint = new int[set.data.length];
		int closestPoint = -1;
		int startingPoint = -1;
		int clusterCount = set.data.length;
		int[] averager = new int[set.data.length];
		int[] closestWith = new int[set.data.length];
		int k = 0;
		//int z = 0;
		for(int i = 0; i < averager.length; i++)
			averager[i] = 1;
			
		double centers[][] = new double[set.data.length][set.data[0].length];
		for( int i = 0; i < centers.length; i++ ){
			centers[i] = set.data[i].clone();
		}
		final long startTime = System.currentTimeMillis();
		while( wrongSets(set) ){
		//for( int z = 0; z < set.data.length; z++ ){
			//maxClosestArray[z] = 10000;
			for( int i = 0; i < set.data.length; i++ ){
				closestArray[i] = 10000;
				for( int y = 0; y < set.data.length; y++ ){
					if( i != y && ( (set.data[i][set.data[0].length-1] != set.data[y][set.data[0].length-1]) || (set.data[i][set.data[0].length-1] == (double)-1) ) ){	 //if it is not in the same cluster, run
						currentClosest = 0;
						for( int b = 0; b < set.data[0].length-1; b++ ){
							currentClosest = currentClosest + (double)Math.pow(Math.abs(set.data[i][b] - set.data[y][b]), 2);
						}
						//System.out.printf("Sqrt of %f is %f\n", currentClosest, Math.sqrt(currentClosest));
						closestArray[y] = Math.sqrt(currentClosest);
					}
				}
				currentClosest = 100000;
				for( int y = 0; y < closestArray.length; y++ ){
					if( (currentClosest > closestArray[y]) && i != y ){
						currentClosest = closestArray[y];
						maxClosestArray[i] = closestArray[y];
						maxClosestPoint[i] = y;
					}
				}
			}
			currentClosest = 100000;
			for( int i = 0; i < maxClosestArray.length; i++ ){
				if( ((currentClosest > maxClosestArray[i])) && ( maxClosestPoint[i] != i ) && 
				( ( set.data[i][set.data[0].length-1] != set.data[maxClosestPoint[i]][set.data[0].length-1] ) || ( set.data[i][set.data[0].length-1] == (double)-1 ) ) ){
					currentClosest = maxClosestArray[i];
					startingPoint = i;
					closestPoint = maxClosestPoint[i];
				}
				//System.out.printf("%f\n", maxClosestArray[0]);
				//System.out.printf("%f and %f\n", set.data[i][set.data[0].length-1], set.data[maxClosestPoint[i]][set.data[0].length-1]);
			}
			
			//System.out.printf("%d is closest with point %d with space %f\n", startingPoint, closestPoint, currentClosest);
			//System.out.printf("%d is part of cluster %d, %d is part of cluster %d\n", startingPoint, (int)set.data[startingPoint][set.data[0].length-1], closestPoint, (int)set.data[closestPoint][set.data[0].length-1]);
			if( set.data[startingPoint][set.data[0].length-1] == set.data[closestPoint][set.data[0].length-1] && set.data[startingPoint][set.data[0].length-1] != (double)-1 )
				break;
			//MERGE BEGINS HERE
			for( int b = 0; b < set.data[0].length-1; b++ ){
				set.data[startingPoint][b] = ( (averager[startingPoint]*set.data[startingPoint][b])+(averager[closestPoint]*set.data[closestPoint][b]) ) / (averager[startingPoint]+averager[closestPoint]);
				set.data[closestPoint][b] = set.data[startingPoint][b];
			}
			double tempDub;
			averager[startingPoint] = averager[startingPoint] + averager[closestPoint];
			averager[closestPoint] = averager[startingPoint];
			if( set.data[startingPoint][set.data[0].length-1] == (double)-1 && set.data[closestPoint][set.data[0].length-1] != (double)-1 ){
				set.data[startingPoint][set.data[0].length-1] = set.data[closestPoint][set.data[0].length-1];
			}
			else if( set.data[startingPoint][set.data[0].length-1] != (double)-1 && set.data[closestPoint][set.data[0].length-1] == (double)-1 ){
				set.data[closestPoint][set.data[0].length-1] = set.data[startingPoint][set.data[0].length-1];
			}
			else if( set.data[startingPoint][set.data[0].length-1] == (double)-1 && set.data[closestPoint][set.data[0].length-1] == (double)-1 ){
				//System.out.printf("Set %d and %d to %d\n", startingPoint, closestPoint, k, set.data[closestPoint][set.data[0].length-1], set.data[startingPoint][set.data[0].length-1]);
				set.data[closestPoint][set.data[0].length-1] = (double)k;
				set.data[startingPoint][set.data[0].length-1] = (double)k;
				//System.out.printf("Set %d and %d to %d\n Actually are %f and %f\n", startingPoint, closestPoint, k, set.data[closestPoint][set.data[0].length-1], set.data[startingPoint][set.data[0].length-1]);
				k++;
			}
			else{
				tempDub = set.data[closestPoint][set.data[0].length-1];
				//System.out.printf("%f joined %f\n", tempDub, set.data[startingPoint][set.data[0].length-1]);
				set.data[closestPoint][set.data[0].length-1] = set.data[startingPoint][set.data[0].length-1];
				for( int y = 0; y < set.data.length; y++ ){
					if( set.data[y][set.data[0].length-1] == tempDub ){
						//System.out.printf("%f joined %f\n", tempDub, set.data[closestPoint][set.data[0].length-1]);
						for( int b = 0; b < set.data[0].length; b++ ){
							set.data[y][b] = set.data[startingPoint][b];
						}
					}
				}
			}
		}
		
		final long endTime = System.currentTimeMillis();		
		System.out.printf("A = [");
		int[] returnSet = new int[set.data.length];
		for(int i = 0; i < set.data.length; i++)
			if( i == set.data.length-1 ){
				System.out.printf("%d", (int)set.data[i][set.data[0].length-1]);
				returnSet[i] = (int)set.data[i][set.data[0].length-1];
			}
			else{
				System.out.printf("%d,", (int)set.data[i][set.data[0].length-1]);
				returnSet[i] = (int)set.data[i][set.data[0].length-1];
			}
		System.out.printf("].\n");
		System.out.println("Total execution time: " + (endTime - startTime) + " milliseconds" );
		return returnSet;
	}
	
	public static Boolean wrongSets ( Average thisSet ) {
		int counter1 = 0;
		int counter2 = 0;
		Set<Double> setUniqueNumbers = new LinkedHashSet<Double>();
		for(int i = 0; i < thisSet.data.length; i++) {
			setUniqueNumbers.add(thisSet.data[i][thisSet.data[0].length-1]);
		}
		for(Double x : setUniqueNumbers) {
			if( x != (double)-1 )
				counter1++;
			else
				counter2++;
		}
		if ( counter2 == 0 && counter1 == thisSet.clusters )
			return false;
		else
			return true;
	}
}