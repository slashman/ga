package util;

public class Plasma2 {
    private int MAX, MIN;
	private int [][] map;
    private double var;
    /** Var > cuanto puede variar el promedio con respecto a sus limites
     * Si var = 1 puede varios en +- rango/2,
     * Si var = 2 puede varias en +- rango
     *
     * nuevo = prom +- var * rango/2;
     * */


    private double seed;

	private int calculateHeight(int a, int b, RandomGen rg){

		int rango = Math.abs(a-b);
		if (rango == 0) rango = 1;
		int h = (int) (avg(a, b) - var * (rango / 2) + (rango * var) * rg.nextDouble());
		/* this random is from 0 to 1 */
		return validateHeight(h);
	}

	private int calculateHeight(int a, int b, int c, int d, RandomGen rg){
		return calculateHeight((int)avg(a,b), (int) avg (a,b), rg);
	}

	public int [] buildMonoPlasma(int MAX, int MIN, int var, int SIZE, long seed){
		Debug.enterMethod(this, "buildMonoPlasma MAX "+MAX+", MIN "+MIN+", var "+var+" SIZE "+SIZE+" seed "+seed);

		this.seed=seed;
		this.MAX = MAX;
		this.MIN = MIN;
	    this.var = var;


	    this.var = 0.01D;

		int [] map = new int[SIZE+1];

        for (int x = 0; x < SIZE; x++) {
           	map[x] = -1;
		}

		RandomGen rg = new RandomGen(seed);

/*		map[0] = MathF.rand(MIN+(MAX-MIN)*1/3, MIN+(MAX-MIN)*2/3, rg);
		map[SIZE] = map[0];
*/
        map[0] = MathF.rand(MIN, MAX, rg);
		map[SIZE] = map[0];

		//Debug.say ("Map[0] " + map[0]);
		//Debug.say ("Map[SIZE] " + map[SIZE]);

		int inc = SIZE;

        while (inc > 1){
            for (int x = 0; x+inc/2 < SIZE; x+= inc){
	            if (map [x+ inc / 2] == -1)
                	map [x+ inc / 2] = validateHeight((int)(avg(map[x], map[x+inc]) + var * (rg.nextDouble()-0.5)));

            }
			inc = inc / 2;
		}
		Debug.exitMethod();
		return map;
	}

	private int validateHeight(int h){

		if (h <= MAX && h >= MIN){
			return h;
		} else {
			if (h < MIN){
				return MIN;
			} else {
            	return MAX;
			}
		}

	}

	public int[][] buildStripPlasma(int MAX, int MIN, double var, int SIZE, long seed, int [] strips){
		Debug.enterMethod(this, "buildStripPlasma MAX "+MAX+", MIN "+MIN+", var "+var+" SIZE "+SIZE+" seed "+seed);

		/** Generates a Stripped plasma fractal
		  	* First used for the temperature fractal
			*/

		this.MAX = MAX;
		this.MIN = MIN;
	    this.var = var;
		map = new int[SIZE+1][SIZE+1];
        for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++){
              	map[x][y] = -1;
			}
		}

		Debug.doAssert(strips.length == 8, "Strips Vector length of the fractal = 8");

		for (int i = 0; i<8; i++){
			map [0][(SIZE/8)*i] = strips[i];
			map [SIZE][(SIZE/8)*i] = strips[i];
			map [SIZE/4][(SIZE/8)*i] = strips[i];
			map [SIZE/2][(SIZE/8)*i] = strips[i];
			map [SIZE*(3/4)][(SIZE/8)*i] = strips[i];
			map [SIZE*(1/8)][(SIZE/8)*i] = strips[i];
			map [SIZE*(3/8)][(SIZE/8)*i] = strips[i];
			map [SIZE*(5/8)][(SIZE/8)*i] = strips[i];
			map [SIZE*(7/8)][(SIZE/8)*i] = strips[i];
		}

		map [0][SIZE] = strips[0];
		map [SIZE][SIZE] = strips[0];
		map [SIZE/4][SIZE] = strips[0];
		map [SIZE/2][SIZE] = strips[0];
		map [3*(SIZE/4)][SIZE] = strips[0];
		map [SIZE*(3/8)][SIZE] = strips[0];
		map [SIZE*(5/8)][SIZE] = strips[0];
		map [SIZE*(7/8)][SIZE] = strips[0];

		Debug.exitMethod();
		return makePlasma(MAX, MIN, var, SIZE, new RandomGen(seed));
	}

	public int[][] buildBoundedPlasma(int MAX, int MIN, double var, int SIZE, long seed, int [] strips){
		Debug.enterMethod(this, "buildBoundedPlasma MAX "+MAX+", MIN "+MIN+", var "+var+" SIZE "+SIZE+" seed "+seed);

		/** Generates a Bounded plasma fractal
		  	* First used for the rainfall fractal
			*/

		this.MAX = MAX;
		this.MIN = MIN;
	    this.var = var;
		map = new int[SIZE+1][SIZE+1];

		int RAINVAR = 80;

        for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++){
              	map[x][y] = -1;
			}
		}

		Debug.doAssert(strips.length == 8, "Bounded Vector length of the fractal = 8");

        int temp = -1;
		for (int i = 0; i<8; i++){

			temp = strips[i] + MathF.rand(-RAINVAR,RAINVAR);
			map [0][(SIZE/8)*i] = temp;
			map [SIZE][(SIZE/8)*i] = temp;
			if (i==0){
				map [SIZE/2][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR,RAINVAR);
				map [SIZE/4][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR,RAINVAR);
				map [3*(SIZE/4)][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR,RAINVAR);
			}

			if (i==2 || i==4 || i == 6)
				map [SIZE/2][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR*2,RAINVAR*2);
			else{
				map [SIZE/4][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR*2,RAINVAR*2);
				map [3*(SIZE/4)][(SIZE/8)*i] = strips[i]+ MathF.rand(-RAINVAR*2,RAINVAR*2);
			}

		}

		map [0][SIZE] = temp;
		map [SIZE][SIZE] = temp;

		//map [SIZE/4][SIZE] = strips[0]+ MathF.rand(-RAINVAR,RAINVAR);
		map [SIZE/2][SIZE] = strips[0]+ MathF.rand(-RAINVAR,RAINVAR);
		//map [3*(SIZE/4)][SIZE] = strips[0]+ MathF.rand(-RAINVAR,RAINVAR);

		Debug.exitMethod();
		return makePlasma(MAX, MIN, var, SIZE, new RandomGen(seed));
	}

    public int [][] buildPlasma(int MAX, int MIN, double var, int SIZE, long seed){
	    Debug.enterMethod(this, "buildPlasma MAX "+MAX+", MIN "+MIN+", var "+var+" SIZE "+SIZE+" seed "+seed);
	    /** Generates a continuous plasma fractal */
		this.MAX = MAX;
		this.MIN = MIN;
	    this.var = var;

        map = new int[SIZE+1][SIZE+1];

        for (int x = 0; x < SIZE+1; x++) {
			for (int y = 0; y < SIZE+1; y++){
              	map[x][y] = -1;
			}
		}

		RandomGen rg = new RandomGen(seed);

		int temp = rg.nextInt(MIN + (MAX-MIN)/3, MAX - (MAX-MIN)/3);

		map[0][0] = temp;
        map[0][SIZE] = temp;
        map[SIZE][0] = temp;
        map[SIZE][SIZE] = temp;


		return makePlasma(MAX, MIN, var, SIZE, rg);
	}


	private int[][] makePlasma(int MAX, int MIN, double var, int SIZE, RandomGen rg){
        int inc = SIZE;
        while (inc > 1){
	        //System.out.println("Increment: "+inc);
            for (int x = 0; x < SIZE; x+= inc){
                for (int y = 0; y < SIZE; y+= inc){
                    int sinc = inc / 2;
	                if (map[x+sinc][y] == -1) {
		                //System.out.println("Interpoling between map["+x+"]["+y+"] and map ["+(x+inc)+"]["+y+"]");
	                    int height = calculateHeight(map[x][y], map[x+inc][y], rg);
	                    map[x+sinc][y] = height;
	                    if (y == 0){
							map[x+sinc][SIZE] = height;
						}
					}

                    if (map[x][y+sinc] == -1) {
	                    //System.out.println("Interpoling between map["+x+"]["+y+"] and map ["+(x)+"]["+(y+inc)+"]");
	                    int height = calculateHeight(map[x][y], map[x][y+inc], rg);
						map[x][y+sinc]= height;
						if (x == 0){
							map[SIZE][y+sinc]= height;
						}
					}

					if (map[x+inc][y+sinc] == -1 && x+inc != SIZE) {
						//System.out.println("Interpoling between map["+(x+inc)+"]["+(y+inc)+"] and map ["+(x+inc)+"]["+y+"]");
						map[x+inc][y+sinc] = calculateHeight(map[x+inc][y+inc], map[x+inc][y], rg);
					}

                    if (map[x+sinc][y+inc] == -1 && y+inc != SIZE) {
	                    //System.out.println("Interpoling between map["+(x)+"]["+(y+inc)+"] and map ["+(x+inc)+"]["+(y+inc)+"]");
	                    map[x+sinc][y+inc] = calculateHeight(map[x][y+inc], map[x+inc][y+inc], rg);
					}

                    if (map[x+sinc][y+sinc] == -1) {
	                    //System.out.println("Interpoling between map["+(x+inc)+"]["+(y+inc)+"] and map ["+(x+inc)+"]["+y+"]");
	                    //map[x+sinc][y+sinc] = calculateHeight(map[x][y], map[x+inc][y], map[x][y+inc], map[x+inc][y+inc], rg);
	                    map[x+sinc][y+sinc] = calculateHeight(map[x+sinc][y], map[x+sinc][y+inc], map[x][y+sinc], map[x+inc][y+sinc], rg);
					}
                }
            }
			inc = inc / 2;
		}
		Debug.exitMethod();
		return map;
    }

    private double avg(double a, double b){
		return (a + b) / 2;
    }

}