package util;

public class MathF {
	public static double sqrt(double d){
		return Math.sqrt(d);
	}

	public static double pow(double base, double expo){
		return Math.pow(base, expo);
	}

    public static int rand(int low, int high){
		return (int) (Math.random() * (high-low) + low);
	}

	public static int avg(int a, int b){
		return (int)((a+b)/2);
	}

	public static int rand(int low, int high, RandomGen rg){
		return (int) (rg.nextDouble() * (high-low) + low);
	}

	public static double dRand(int low, int high, RandomGen rg){
		return low + rg.nextDouble() * (high-low);
	}

	public static int max (int [] values){
		Debug.enterMethod("Static MathF", "max");
		int max = values[0];
		for (int i = 0; i < values.length ; i++){
			if (values[i] > max)
				max = values[i];
		}
		Debug.exitMethod();
		return max;
	}

	public static int min(int [] values){
		Debug.enterMethod("Static MathF", "min");
        int min = values[0];
		for (int i = 0; i<values.length; i++){
			if (values[i] < min)
				min = values[i];
		}
		Debug.exitMethod();
		return min;
	}


	public static int percentile(int[] values, double perc){
		Debug.enterMethod("Static MathF", "percentile");
		shellOrder(values);
		int index = (int)(values.length * perc);
		Debug.exitMethod();
		return values[index];
	}

	public static void bsort(int[] array){
		int n = array.length;
        for (int i=0; i<n-1; i++) {
  			for (int j=0; j<n-1-i; j++)
    			if (array[j+1] < array[j]) {
      				int tmp = array[j];
      				array[j] = array[j+1];
      				array[j+1] = tmp;
  				}
		}
	}

	public static int[] toSingleDim(int[][] matrix){
		Debug.enterMethod("Static MathF", "toSingleDim");
		int[] ret = new int[matrix.length * matrix[0].length];

		int width = matrix[0].length;

		for (int y=0; y<matrix.length; y++){
			for (int x = 0; x < matrix[y].length; x++){
				ret [x + y*width] = matrix [y][x];
			}
		}
		Debug.exitMethod();

		return ret;
	}

	public static void swap(int a, int b, int[] array){
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

	public static void shellOrder(int[] a){
		int interval = a.length /2;
		while (interval > 0){
			for (int i = interval; i < a.length; i++){
				int j = i-interval;
				while (j >1){
					int k = j+interval;
					if (a[j] <= a[k]){
						j = -1;
					} else {
						swap(j,k,a);
					}
					j=j-interval;
				}
			}
			interval = interval / 2;
		}
	}

	public static double poly(double variable, double[] coeff){
		double ret = 0;
		for (int i = coeff.length - 1; i>=0; i--){
			ret += MathF.pow(variable, i) * coeff[coeff.length-i-1];
		}
		return ret;
	}



	public static void main(String args[]){
		int [][] v = {
			{1,2,3,4,5},
			{6,7,8,9,10},
			{11,12,13,14,15}
		};

		int [] ve = MathF.toSingleDim(v);

		for (int i=0; i< ve.length; i++){
			System.out.print(ve[i]+",");
		}
		System.out.println();
		System.out.print(ve.length);



		int tq = MathF.percentile(ve, 0.666666667);


	}

	/* QuickSort

	public static int getPivot(int i, int j, int[] a){
		int first = a[i];
		int k = i+1;

		while (k <= j){
			if (a[k] > first){
				return k;
			} else {
				if (a[k] < first){
					return i;
				} else {
					k++;
				}
			}
		}
		return -1;
	}

	public static int partition(int i, int j, int pivot, int[] a){
		int left = i;
		int right = j-1;
		while (right <= left){
			swap(right, left, a);
			while (a[right] < pivot){
				right++;
			}

			while (a[left] >= pivot){
				left--;
			}

		}
		return right;
	}

	public static void quickSort(int[] a, int i, int j){
		int pivotIndex = getPivot(i, j, a);
		if (pivotIndex != -1){
			int pivot = a[pivotIndex];
			swap(pivotIndex, j, a);
			int k = partition(i, j, pivot, a);
			quickSort(a, i, k-1);
			quickSort(a, k, j);
		}
	}

	public static void qsort(int[]a){
		quickSort(a, 0, a.length -1);
	}
	*/

}