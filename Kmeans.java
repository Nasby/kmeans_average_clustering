import java.lang.Math;
import java.lang.Object;
import java.util.Vector;
import java.util.Random;
import java.util.Arrays;

class Kmeans{
	
	double[][] data;
	int clusters;
	
	Kmeans(){
		data = new double[1][1];
		data[0][0] = (double)0;
	}
	
	Kmeans(double[][] a, int b){
		data = a;
		clusters = b;
		
	}
	
	public static int[] returnSet(Kmeans set){
		int[] randoms = new int[set.clusters];
		int random;
		double[][] centers = new double[set.clusters][set.data[0].length];
		int[] clusterSet = new int[set.data.length];
		int[] oldClusterSet = new int[set.data.length];
		
		//initialize oldClusterSet
		for(int i = 0; i < oldClusterSet.length; i++)
			oldClusterSet[i] = -1;
			
		//initialize randoms
		for(int y = 0; y < set.clusters; y++)
			randoms[y] = -1;
		
		//select cluster amount of random starting points
		for(int i = 0; i < set.clusters; i++){
			random = randInt(0, set.data.length-1);
			
			//makes sure there is no one point is the center twice
			for(int y = 0; y < set.clusters; y++){
				if( random == randoms[y] ){
					i--;
					break;
				}
			}
			randoms[i] = random;
		}
		
		//initializes the centers
		for(int i = 0; i < set.clusters; i++){
			for(int y = 0; y < set.data[0].length; y++){
				centers[i][y] = set.data[randoms[i]][y];
				if( centers[i][y] == (double)-1 ){
					centers[i][y] = (double)i;
					set.data[randoms[i]][y] = (double)i;
				}
			}
		}
		
		
		/*
		System.out.printf("Centers are:\n");
		for(int i = 0; i < set.clusters; i++){
			for(int y = 0; y < centers[0].length; y++)
				System.out.printf("%f ", centers[i][y]);
			System.out.printf("\n");
		}*/
		
		int[][] clusterSetSet = new int[100][set.data.length];
		double shortestLength, currentLength, currentCluster, bestCluster;
		double[] averages = new double[100];
		final long startTime = System.currentTimeMillis();
		for(int hundo = 0; hundo < 100; hundo++){
		
			//initialize randoms
			for(int y = 0; y < set.clusters; y++)
				randoms[y] = -1;
			
			//select cluster amount of random starting points
			for(int i = 0; i < set.clusters; i++){
				random = randInt(0, set.data.length-1);
				
				//makes sure there is no one point is the center twice
				for(int y = 0; y < set.clusters; y++){
					if( random == randoms[y] ){
						i--;
						break;
					}
				}
				randoms[i] = random;
			}
			
			//initializes the centers
			for(int i = 0; i < set.clusters; i++){
				for(int y = 0; y < set.data[0].length; y++){
					centers[i][y] = set.data[randoms[i]][y];
					if( centers[i][y] == (double)-1 ){
						centers[i][y] = (double)i;
						set.data[randoms[i]][y] = (double)i;
					}
				}
			}
			
			currentCluster = (double)0;
			bestCluster = (double)0;
			shortestLength = (double)0;
			currentLength = (double)0;
			int[] averager = new int[set.clusters];
			for(int i = 0; i < set.clusters; i++)
				averager[i] = 0;
			
			for( int i = 0; i < set.data.length; i++ ){			//runs for every row
				shortestLength = (double)0;
				if( set.data[i][set.data[0].length-1] == -1 ){		//if spot hasn't been assigned a cluster yet
					for( int y = 0; y < set.clusters; y++ ){		//runs for every cluster
						currentCluster = (double)y;
						currentLength = (double)0;
						for( int b = 0; b < set.data[0].length-1; b++ ){		//runs for every dimension
							currentLength = Math.abs( currentLength + (double)Math.pow(Math.abs(set.data[i][b] - centers[y][b]), 2) );
						}
						currentLength = (double)Math.sqrt(currentLength);
						if( y == 0 ){
							shortestLength = currentLength;
						}
						if( currentLength < shortestLength ){
							shortestLength = currentLength;
							bestCluster = currentCluster;
						}
					}
					clusterSet[i] = (int)bestCluster;
					averager[(int)bestCluster]++;
					for( int z = 0; z < set.data[0].length-1; z++){
						centers[(int)bestCluster][z] = ( (centers[(int)bestCluster][z]*averager[(int)bestCluster])+set.data[i][z] )/ (averager[(int)bestCluster]+1);
					}
				}
				
			}
			
			while( !(compareArrays(clusterSet, oldClusterSet)) ){
				oldClusterSet = clusterSet.clone();
				for( int i = 0; i < set.data.length; i++ ){			//runs for every row
					shortestLength = (double)0;
					if( set.data[i][set.data[0].length-1] == -1 ){		//if spot hasn't been assigned a cluster yet
						for( int y = 0; y < set.clusters; y++ ){		//runs for every cluster
							currentCluster = (double)y;
							currentLength = (double)0;
							for( int b = 0; b < set.data[0].length-1; b++ ){		//runs for every dimension
								currentLength = Math.abs( currentLength + (double)Math.pow(Math.abs(set.data[i][b] - centers[y][b]), 2) );
							}
							currentLength = (double)Math.sqrt(currentLength);
							if( y == 0){
								shortestLength = currentLength;
							}
							if( currentLength < shortestLength ){
								shortestLength = currentLength;
								bestCluster = currentCluster;
							}
						}
						clusterSet[i] = (int)bestCluster;
						averager[(int)bestCluster]++;
						for( int z = 0; z < set.data[0].length-1; z++){
							centers[(int)bestCluster][z] = ( (centers[(int)bestCluster][z]*averager[(int)bestCluster])+set.data[i][z] )/ (averager[(int)bestCluster]+1);
						}
					}
					
				}
			}
			double findingAverage = 0;
			double totalAverage = 0;
			for( int i = 0; i < set.data.length; i++ ){
				for( int y = 0; y < set.data[0].length-1; y++ ){
					findingAverage = Math.abs( findingAverage + (double)Math.pow(Math.abs(set.data[clusterSet[i]][y] - centers[clusterSet[i]][y]),2) );
				}
				findingAverage = (double)Math.sqrt(findingAverage);
				totalAverage += findingAverage;
			}
			totalAverage = totalAverage/clusterSet.length;
			averages[hundo] = totalAverage;
			clusterSetSet[hundo] = clusterSet;
		}
		int lowest = 1000000000;
		for( int hundo = 0; hundo < 100; hundo++ ){
			if( averages[hundo] < lowest )
				lowest = hundo;
			if (hundo == 99)
				break;
		}
		int [] returnSet = new int[clusterSetSet.length];
		final long endTime = System.currentTimeMillis();
		System.out.printf("A = [");
		for(int i = 0; i < clusterSet.length; i++)
			if( i == clusterSet.length-1 ){
				System.out.printf("%d", clusterSetSet[lowest][i]);
				returnSet[i] = clusterSetSet[lowest][i];
			}
			else{
				System.out.printf("%d,", clusterSetSet[lowest][i]);
				returnSet[i] = clusterSetSet[lowest][i];
			}
		System.out.printf("].\n");
		System.out.println("Total execution time: " + (endTime - startTime) + " milliseconds" );
		return returnSet;
	}
	
	public static Boolean compareArrays(int[] array1, int[] array2) {
        boolean b = true;
        if (array1 != null && array2 != null){
          if (array1.length != array2.length)
              b = false;
          else
              for (int i = 0; i < array2.length; i++) {
                  if (array2[i] != array1[i]) {
                      b = false;    
                  }                 
            }
        }else{
          b = false;
        }
        return b;
    }
	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}