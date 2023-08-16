package guardian.world.feature;

import util.*;

public class House implements Feature{
	public final static int
		CAT_HUT = 0,
		CAT_SMALLWOODEN = 1,
		CAT_SMALLBRICK = 2,
		CAT_MEDIUMWOODEN = 3,
		CAT_MEDIUMBRICK = 4;

	private final static int
		PIL = 0,
		BED = 1,
		DIRTFLOOR = 2,
		BRICKFLOOR = 3,
		WOODFLOOR = 4,
		WOODWALL = 5,
		BRICKWALL = 6,
		WOODDOOR = 7,
		PLAINWINDOW = 8,
		WOODWINDOW = 9
		;

	public String getType(){
		//return "House"; Removed because it is buggy
		return "Idiot";
	}

	public String getID(){
		return "DynamicHouse";
	}

	private final int [][][] beds = new int [][][]{
			{{PIL},
			 {BED}
			},
			{{PIL,PIL},
			 {BED,BED}
			}
	};

	private final int[] floorCells = new int[]{ 	DIRTFLOOR, 	WOODFLOOR, 	BRICKFLOOR,	WOODFLOOR,	BRICKFLOOR };
	private final int[] wallCells = new int[]{		WOODWALL,	WOODWALL,	BRICKWALL,	WOODWALL,	BRICKWALL };
	private final int[] doorCells = new int[]{		WOODDOOR,	WOODDOOR,	WOODDOOR,	WOODDOOR,	WOODDOOR };
	private final int[] windowCells = new int[]{	PLAINWINDOW,PLAINWINDOW,PLAINWINDOW,WOODWINDOW,	WOODWINDOW };
	private final int[] baseSizesX = new int []{	4, 			6, 			6, 			7, 			7};
    private final int[] baseSizesY = new int []{	5, 			7, 			7, 			8, 			8};

	private int category;
	private int population;

	private int width, height;

	private void determineSize(){
		width = baseSizesX[category];
		height = baseSizesY[category];
		for (int i = 0; i < population - 1; i++){
			width += width * 1.5;
			height += height * 1.5;
		}
	}

	private int floorCell, wallCell, doorCell, windowCell;

	private void determine(){
		determineSize();
		floorCell = floorCells[category];
		wallCell = wallCells[category];
		doorCell = doorCells[category];
		windowCell = windowCells[category];

	}

	private int getWindowsToMake(){
		return MathF.rand(0,3);
	}

	public int [][] getMap(){
		determine();
		int[][] ret = new int[width][height];
		// Make the floor
		for (int x = 1; x < width -1; x++){
			for (int y = 1; y < height -1; y++){
				ret[x][y] = floorCell;
			}
		}

		// Make the walls
		for (int x = 0; x < width; x++){
			ret[x][0] = wallCell;
			ret[x][height - 1] = wallCell;
		}
		for (int y = 0; y < height; y++){
			ret[0][y] = wallCell;
			ret[width - 1][y] = wallCell;
		}

		// Put a door
		int doorLoc = MathF.rand(0, width -1);
		ret [doorLoc][height - 1] = doorCell;

		// make windows
		int windows = getWindowsToMake();
		for (int i = 0; i < windows; i++){
			switch (MathF.rand(0,3)){
				case 0:
					ret [0][MathF.rand(1,height -2)] = windowCell;
					break;
				case 1:
					ret [MathF.rand(1,width -2)][0] = windowCell;
					break;
				case 2:
					ret [width - 1][MathF.rand(1,height -2)] = windowCell;
					break;
				case 3:
					ret [MathF.rand(1,width -2)][height -1] = windowCell;
					break;
			}
		}

		// Make resting places
		int restingPlaces = 1 + MathF.rand(0,population);

		for (int i = 0; i < restingPlaces; i++){
			int bedType = MathF.rand(0, beds.length -1);
			int restingHeight = beds[bedType].length;
			int restingWidth = beds[bedType][0].length;
			int rnx = MathF.rand(1, width - 1 - restingWidth);
			int rny = MathF.rand(1, height - 1 - restingHeight);
			int rotations = MathF.rand(0,3);
			for (int r = 0; r <rotations; r++){
				beds[bedType] = rotate(beds[bedType]);
			}


			if (rny == height - 1 - restingHeight){
				if (rnx >= doorLoc && rnx <= doorLoc + restingWidth){
					i--;
					continue;
				}
			}

			//Plot the bed

			for (int x = rnx; x <= rnx + restingWidth; x++){
				for (int y = rny; y <= rny + restingHeight; y++){
					ret [x][y] = beds[bedType][x-rnx][y-rny];
				}
			}


		}

		return ret;

	}

	public int[][] rotate(int [][] what){
		int[][] newMatrix = new int[what[0].length][what.length];
		for (int x = 0; x < what[0].length; x++)
			for (int y = 0; y < what.length; y++){
				//System.out.println("X = " + x +", new x = "+ (getWidth() - y));
				//System.out.println("Y = " + y +", new y = "+ x);
				newMatrix [x][what.length - y -1] = what[y][x];
				//newMatrix [x][y] = representation[y][x];
			}

		return newMatrix;
	}


}