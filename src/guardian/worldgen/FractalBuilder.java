package guardian.worldgen;

import engine.*;
import util.*;

import guardian.item.*;
import guardian.being.*;
import guardian.world.*;

public class FractalBuilder {

    private GItemFactory itemFactory;
	private GMapCellFactory mapCellFactory;
	private GBeingFactory beingFactory;
				/*
    int ceil;
	int bottom;
    int seaLevel;
	int midSea;
	int plains;
	int hills;*/

	private int worldDimension;
	private long worldSeed = -1;

	private boolean visited[][];
	private boolean filled[][];
	private IntStack xs, ys;

	private GWorld worldAtWork;
	private int [][] fractalAtWork;

	private FeatureBuilder aFeatureBuilder;

    public void init(GItemFactory ifact, GMapCellFactory mcf, GBeingFactory bf, FeatureBuilder pFeatureBuilder){
	    Debug.enterMethod(this, "init");
	    aFeatureBuilder = pFeatureBuilder;
		this.itemFactory = ifact;
		this.mapCellFactory = mcf;
		this.beingFactory = bf;
		Debug.exitMethod();
	}

	public GWorld buildWorld(int pWorldDimension){
		Debug.enterMethod(this, "buildWorld");
		worldDimension = pWorldDimension;
		worldAtWork = new GWorld();
		generatePhysical();
		generateWildPopulation();
		worldAtWork.setTime(new GTime(2004, 12, 20, 12, 14, 05));
		Debug.exitMethod();
		return worldAtWork;
	}

	public GWorld buildWord(int pWorldDimension, int seed){
		worldSeed = seed;
		return buildWorld (pWorldDimension);
	}

	private void generateWildPopulation(){
		try {
        	for (int j = 0; j < 20; j++){
				GBeing populator = beingFactory.createBeing("Wolf");
				populator.setPosition(new Point(MathF.rand(0, 200), MathF.rand(0,200)));
				worldAtWork.addPhysicalActor(populator,true);
			}
		} catch (EngineException ee){
			Debug.byebye("Error generating the wild population");
		}
	}


	private void generatePhysical(){
	    Debug.enterMethod(this, "generatePhysical");
		if (worldSeed == -1)
			worldSeed = (long) MathF.rand(1,32000);
		int FRACTALSIZE = worldDimension;

		Plasma p = new Plasma();
		Plasma2 p2 = new Plasma2();
		RandomGen rg = new RandomGen(worldSeed);


		Debug.startTimer();
		int [][] baseFractal = p.buildPlasma(255, 1, 10, FRACTALSIZE, worldSeed);
		Debug.stopTimer("buildPlasma");


		if (showImages){
			MatrixShower m3 = new MatrixShower();
			m3.setColoringMode(MatrixShower.RED);
			m3.setTitle("Plasma Fractal");
			m3.setMatrix(baseFractal);
		}

		/**
		 *  Render the basePlasma to heights 0-4 based on
		 * the defined proportions for deep sea, sea, plains
		 * and mountains */

		Debug.startTimer();
		int[][] surfaceHeights = surfaceRender(baseFractal);
		Debug.stopTimer("surfaceRender");

        if (showImages){
			MatrixShower m4 = new MatrixShower();
			m4.setColoringMode(MatrixShower.TERRAIN);
			m4.setTitle("Brute World");
			m4.setMatrix(surfaceHeights);
		}


        /* Clean-up the height Map*/
		Debug.startTimer();
		cleanUp(surfaceHeights);
		cleanUp(surfaceHeights);
		Debug.stopTimer("cleanUp");

		if (showImages){
			MatrixShower m4 = new MatrixShower();
			m4.setColoringMode(MatrixShower.TERRAIN);
			m4.setTitle("Pure World");
			m4.setMatrix(surfaceHeights);
		}


		/* Build the temperatures strips plasma */
		Debug.startTimer();
		int[][] tempMap = p2.buildStripPlasma(30, -3, 1.5, FRACTALSIZE, worldSeed,
			new int[] {
				0, 16, 24, 28, 28, 24, 16, 0
			}
		);
		Debug.stopTimer("temps");


		if (showImages){
			MatrixShower m = new MatrixShower();
			m.setColoringMode(MatrixShower.TEMP);
			m.setTitle("Temperature Graph");
			m.setMatrix(tempMap);
		}


		int [][] rainMap = p2.buildBoundedPlasma(200, 10, 1, FRACTALSIZE, worldSeed,
			new int[] {
				5, 60, 90, 160 + MathF.rand( -20, 20, rg), 160 + MathF.rand(-20,20, rg), 90, 60, 5
			}
		);

		if (showImages){
			MatrixShower m2 = new MatrixShower();
			m2.setTitle("Rainfall Graph");
			m2.setColoringMode(MatrixShower.RAIN);
			m2.setMatrix(rainMap);
		}


		int[][] biomeMap = renderBiomes(surfaceHeights, tempMap, rainMap);

//		int[][] preCellMap = setBiomeCells(biomeMap);
                int[][] preCellMap = biomeMap;

//		applyCARules(preCellMap);

		/* Render the heightMap to MapCell grid */
		GMapCell [][] ret = renderMapCells(preCellMap);

		GMapCell [][][] goWorld = new GMapCell [1][][];

		goWorld[0] = ret;
		worldAtWork.init(goWorld, 0);

		Debug.exitMethod();
	}



	private int [][] renderBiomes(int[][] fractal, int [][] temps, int [][] rain){

		int[][] ret = new int[fractal.length][fractal[0].length];

		for (int x=0; x < worldDimension; x++){
			for (int y=0; y < worldDimension; y++){
				if (fractal[x][y] == 2 ||fractal[x][y] == 3){
                	int temp = chooseTempRange(temps[x][y]);
					int rains = chooseRainRange(rain[x][y]);
					ret[x][y] = trans[rains][temp];
				}
				if (fractal[x][y] == 4){
					/*if (MathF.rand(0,10) > 6)
						ret[x][y] = 18;
					else*/
						ret[x][y] = 19;
				}

				if (fractal[x][y] == 0){
					/*if (temps[x][y] < 9)
						ret [x][y] = 1; // Change this to ICE Sea
					else*/
						ret [x][y] = 20;
				}

				if (fractal[x][y] == 1){
					/*
					if (temps[x][y] < 9)
						ret [x][y] = 1; // Change this to ICE Sea
					else                                         */
						ret [x][y] = 5;
				}
			}
		}

		return ret;

	}

	private int chooseTempRange(int x){
		if (x <= 2)
			return 0;

		if (x <= 8)
			return 1;

		if (x <= 13)
			return 2;

		if (x <= 18)
			return 3;

		if (x <= 22)
			return 4;

		if (x <= 24)
			return 5;

		if (x == 25)
			return 6;

		if (x == 26)
			return 7;

		if (x == 27)
			return 8;

		return 9;
	}

	private int chooseRainRange(int x){
		if (x <= 30)
			return 0;
		if (x <= 47)
			return 1;
		if (x <= 52)
			return 2;
		if (x <= 60)
			return 3;
		if (x <= 69)
			return 4;
		if (x <= 74)
			return 5;
		if (x <= 83)
			return 6;
		if (x <= 100)
			return 7;
		if (x <= 175)
			return 8;
		return 9;
	}

	private int[][] discretizeTemps(int[][] temps){
		int ret[][] = new int[temps.length][temps.length];
		for (int x = 0; x < temps.length; x++){
			for (int y = 0; y< temps.length; y++){
				if (temps[x][y]<3)
					//Tundra
					ret[x][y] = 0;
				else if (temps[x][y]<9)
					//Subpolar Humedo
					ret[x][y] = 1;
				else if (temps[x][y]<14)
					//Oceanico
					ret[x][y] = 2;
				else if (temps[x][y]<22)
					//Mediterraneo
					ret[x][y] = 3;
				else if (temps[x][y]<27)
					//Pluviselva
					ret[x][y] = 4;
				else if (temps[x][y]<=30)
					//Desierto
					ret[x][y] = 5;
			}
		}
		return ret;
	}

	private int [][] surfaceRender(int[][] fractal){
		Debug.enterMethod(this, "surfaceRender");
		int[] single = MathF.toSingleDim(fractal);
		int ceil = MathF.max(single);
		int bottom = MathF.min(single);

		int seaLevel = defineSeaLevel (single, 0.5);
		int midSea = (int) (seaLevel * 0.6);
		// 2/3 of the ocean are deep
		int air = ceil - seaLevel;
		int hills = (int) (seaLevel + air * 0.5);
		// 1/2 of the surface is plains
		int mountain = (int) (seaLevel + air *0.2);
		// 1/5 of the surface is mountain
                int[][] ret = new int[worldDimension][worldDimension];
		for (int x = 0; x < worldDimension; x++){
			for (int y = 0; y < worldDimension; y++){
				ret[x][y] = discretize(fractal[x][y], midSea, seaLevel, hills, mountain);
			}
		}

		Debug.exitMethod(ret);
                return ret;
	}

	private void cleanUp(int[][] fractal) {
		Debug.enterMethod(this, "cleanUp");
		visited = new boolean[fractal.length][fractal.length];
		filled = new boolean[fractal.length][fractal.length];
		xs = new IntStack(fractal.length*fractal.length*2);
		ys = new IntStack(fractal.length*fractal.length*2);
		for (int x = 0; x < fractal.length; x++){
			for (int y = 0; y< fractal.length; y++){
				if (!visited[x][y]){
                    switch (fractal[x][y]){
						case 2: case 3: case 4: // For the surface
							if (getSpotSize(x,y, new int[] {2,3,4}, fractal) < 300)
							// If the size of the island is less than 200
								fillSpot(x,y, 1, fractal)
								//Sink it
								;
							break;
						case 0: case 1: // If there is a small sea
							if (getSpotSize(x,y, new int[]{0,1}, fractal) < 1000)
								//Raise it
								fillSpot(x,y, 2, fractal)
								;
							break;
					}
				}
			}
		}
		xs.empty();
		ys.empty();
		System.gc();
		Debug.exitMethod();
	}

	private void cleanUp2(int[][] fractal) {
		Debug.enterMethod(this, "cleanUp");

		visited = new boolean[fractal.length][fractal.length];
		filled = new boolean[fractal.length][fractal.length];
		xs.empty();
		ys.empty();
		for (int x = 0; x < fractal.length; x++){
			for (int y = 0; y< fractal.length; y++){
				if (!visited[x][y]){
                    switch (fractal[x][y]){
						case 4:
							if (getSpotSize(x,y, fractal) < 30)
								fillSpot(x,y, 3, fractal);
							break;
					}
				}
			}
		}
		xs.empty();
		ys.empty();

		visited = new boolean[fractal.length][fractal.length];
		filled = new boolean[fractal.length][fractal.length];

		xs = new IntStack(fractal.length*fractal.length*3);
		ys = new IntStack(fractal.length*fractal.length*3);
		for (int x = 0; x < fractal.length; x++){
			for (int y = 0; y< fractal.length; y++){
				if (!visited[x][y]){
                    switch (fractal[x][y]){
						case 2:
							if (getSpotSize(x,y, new int[]{3,4}, new int[]{2}, fractal) < 30)
								fillSpot(x,y, 4, fractal);
							break;
						case 3:
							if (getSpotSize(x,y, new int[]{4}, new int[]{3}, fractal) < 30)
								fillSpot(x,y, 4, fractal);
							break;
						case 4:
							if (getSpotSize(x,y, fractal) < 30)
								fillSpot(x,y, 3, fractal);
							break;
					}
				}
			}
		}


		System.gc();
		Debug.exitMethod();
	}

	private int getSpotSize(int x, int y, int [][] fractal){
		int type = fractal[x][y];
		xs.empty();
		ys.empty();

		xs.push(x);
		ys.push(y);
		int cont = 0;
		//boolean visited[][] = new boolean[fractal.length][fractal[0].length];
		while (xs.isEmpty() == false && ys.isEmpty() == false){
			x = xs.pop();
			y = ys.pop();
        	if (fractal[x][y] == type && !visited[x][y]){
				visited[x][y] = true;
				cont++;
				xs.push(x);
				ys.push(upWrap(y));
				xs.push(x);
				ys.push(downWrap(y));
				xs.push(leftWrap(x));
				ys.push(y);
				xs.push(rightWrap(x));
				ys.push(y);
			}
		}
		return cont;

	}

	private int getSpotSize(int x, int y, int[] allowed, int[][] fractal){
		int type = fractal[x][y];
		xs.empty();
		ys.empty();

		xs.push(x);
		ys.push(y);
		int cont = 0;
		while (xs.isEmpty() == false && ys.isEmpty() == false){
			x = xs.pop();
			y = ys.pop();
			boolean go = false;

			for (int i=0; i<allowed.length; i++){
				if (fractal[x][y] == allowed[i])
					go = true;
			}

        	if (go && !visited[x][y]){
				visited[x][y] = true;
				cont++;
				xs.push(x);
				ys.push(upWrap(y));
				xs.push(x);
				ys.push(downWrap(y));
				xs.push(leftWrap(x));
				ys.push(y);
				xs.push(rightWrap(x));
				ys.push(y);
			}
		}
		return cont;

	}

	private int getSpotSize(int x, int y, int[] notAllowed, int[] allowed, int[][] fractal){
		int type = fractal[x][y];
		xs.empty();
		ys.empty();

		xs.push(x);
		ys.push(y);
		int cont = 0;
		while (xs.isEmpty() == false && ys.isEmpty() == false){
			x = xs.pop();
			y = ys.pop();
			boolean go = false;

			for (int i=0; i<allowed.length; i++){
				if (fractal[x][y] == allowed[i])
					go = true;
			}

			for (int i=0; i<notAllowed.length; i++){
				if (fractal[x][y] != notAllowed[i])
					go = true;
			}

        	if (go && !visited[x][y]){
				visited[x][y] = true;
				cont++;
				xs.push(x);
				ys.push(upWrap(y));
				xs.push(x);
				ys.push(downWrap(y));
				xs.push(leftWrap(x));
				ys.push(y);
				xs.push(rightWrap(x));
				ys.push(y);
			}
		}
		return cont;

	}



	private void fillSpot(int x, int y, int fType, int[][] fractal){
		fillSpot(x, y, fType, fractal[x][y], fractal);
	}

	private void fillSpot(int x, int y, int fType, int pType, int[][] fractal){

		if (fractal[x][y] == pType && !filled[x][y]){
			filled[x][y] = true;
			fractal[x][y] = fType;
			fillSpot(x, upWrap(y), fType, pType, fractal);
			fillSpot(x, downWrap(y), fType, pType, fractal);
			fillSpot(leftWrap(x), y, fType, pType, fractal);
			fillSpot(rightWrap(x), y, fType, pType, fractal);
		}
	}

	private int leftWrap(int x){
		if (x == 0) return worldDimension - 1; else return x-1;
	}

	private int rightWrap(int x){
		if (x == worldDimension - 1) return 0; else return x+1;
	}

	private int upWrap(int y){
		if (y == 0) return worldDimension - 1; else return y-1;
	}

	private int downWrap(int y){
		if (y == worldDimension - 1) return 0; else return y+1;
	}

	private int defineSeaLevel (int[] vals, double portion){
		return MathF.percentile(vals, portion);
	}

	private int discretize(int what, int midSea, int seaLevel, int hills, int mountain){
		if (what < midSea){
			return 0;
		} else {
			if (what < seaLevel){
				return 1;
			} else {
				if (what < hills){
					return 2;
				} else {
					if (what < mountain){
						return 3;
					} else {
						return 4;
					}

				}
			}
		}
	}

	private GMapCell[][] renderMapCells(int[][]fractal){
		Debug.enterMethod(this, "finalRender");

		GMapCell [][] ret = new GMapCell[fractal.length][fractal.length];
		for (int x = 0; x < worldDimension; x++){
			for (int y = 0; y < worldDimension; y++){
				String what = "";
				switch (fractal[x][y]){
					case 1:
						what = "ICE";
					break;
					case 2:
						what = "TUNDRA";
					break;
					case 3:
						what = "OCEANIC";
					break;
					case 4:
						what = "PRAIRIE";
					break;
					case 5:
						what = "SEA";
					break;
					case 6:
						what = "TAIGA";
					break;
					case 7:
						what = "MEDIT";
					break;
					case 8:
						what = "COAST";
					break;
					case 9:
						what = "ASDFOREST";
					break;
					case 10:
						what = "SAVANNA";
					break;
					case 11:
						what = "PLUVI";
					break;
					case 12:
						what = "STEEPE";
					break;
					case 13:
						what = "DESERT";
					break;
					case 14:
						what = "TROPICAL";
					break;
					case 15:
						what = "PLATEAU";
					break;
					case 16:
						what = "DRYFOREST";
					break;
					case 17:
						what = "HILLS";
					break;
					case 18:
						what = "ICEMNTN";
					break;
					case 19:
						what = "RCKMNTN";
					break;
					case 20:
						what = "DEEPSEA";
					break;
				}
				try {
					ret[x][y] = mapCellFactory.createMapCell(what);
				} catch (Exception e){
					Debug.doAssert(false, "MapCell choice in fractal generator");
				}
			}
		}

		Debug.exitMethod();
		return ret;
	}


	public final static boolean showImages = true;

	public final static String [] cellNames = {
		"WOODEN_FLOOR",
		"WOODEN_FLOOR",
		"PILLOW",
		"BED",
		"CLOSET",
		"CHEST",
		"TAPESTRY",
		"CLOSED_WOODEN_DOOR",
		"WOODEN_WALL",
		"TABLE",
		"CHAIR",
		"BRICK_WALL",
		"COUNTER",
		"KITCHEN_TABLE",
		"HOVEN",
		"CANDLE",
		"ATRIUM",
		"COLLECTIVE_CHAIR",
		"DIRT",
		"STATUE",
		"PASSAGE"
	};

	public final static int [][] trans = new int[][]{
		{ 1,  1,  2,  4,  4,  4,  4, 13, 13, 13},
		{ 2,  2,  2,  4,  4,  4,  4,  4, 12, 12},
		{ 2,  3,  3,  3,  9,  9, 12, 12, 12, 12},
		{ 3,  3,  3,  6,  9,  9,  9, 12, 12, 12},
		{ 3,  6,  6,  6,  9,  9,  9,  9, 12,  4},
		{ 4,  7,  7,  7,  7,  9,  9,  9,  4,  4},
		{ 4,  7,  8,  8,  8,  8,  9,  9, 10,  4},
		{ 4,  4,  8,  4, 10, 10, 10,  9, 10, 16},
		{ 4,  4,  4,  4, 11, 11, 11, 14, 14, 14},
		{ 4,  4,  4,  4,  4,  4,  4, 15, 15, 14}
	};

}