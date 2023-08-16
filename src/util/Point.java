package util;


public class Point {
	public int x;
	public int y;
	public int z;

	/**
	 * Method Point
	 *
	 *
	 * @param x
	 * @param y
	 *
	 */
	public Point(int x, int y, int z) {
		this(x,y);
		this.z = z;
	}

	public Point (int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point (){
	}

	public void setValues(int px, int py, int pz){
		x = px; y = py; z = pz;
	}

	public static double distance(Point a, Point b){
		/** Calculates the distance between point a and b*/
		return 1;
	}

	public static Point random(int xfrom, int yfrom, int xto, int yto){
		return new Point(MathF.rand(xfrom, xto), MathF.rand(yfrom, yto));
	}

	public static Point add(Point a, Point b){
		//Debug.enterMethod("Static Point", "add", a + ","+b);
		if (a == null || b == null) {
			//Debug.exitMethod(null);
			return null;
		}
		Point ret =  new Point(a.x+b.x, a.y+b.y, a.z+b.z);
		//Debug.exitMethod(ret);
		return ret;
	}

	public static Point rest(Point a, Point b){
		return new Point (a.x - b.x, a.y-b.y, a.z-b.z);
	}

	public boolean equals(Point a){
		return a.x == x && a.y == y && a.z == z;
	}

	public static boolean equals(Point a, Point b){

		return a.x == b.x && a.y == b.y && a.z == b.z;
	}

	public String toString(){
		return "("+x+","+y+","+z+")";
	}

	private static Point[] pointPool;
	private static boolean[] inUse;
	private static int lastPosition;
	private static boolean poolInitialized = false;

	public static void initializePool(int size){
		pointPool = new Point[size];
		inUse = new boolean[size];
		for (int i= 0; i<size; i++){
			pointPool[i] = new Point();
		}
		lastPosition = 0;
		poolInitialized = true;
	}

	public static Point newPoint(int x, int y){
		return newPoint(x,y,0);
	}

	public static Point newPoint(int x, int y, int z){

		if (poolInitialized){
			int i = lastPosition;
			int endPosition = i-1;
			if (i == 0)
				endPosition = pointPool.length-1;

			while (inUse[i] == true && i != endPosition){
                i++;
                lastPosition ++;
                //if (i == endPosition) break;
                if (i == pointPool.length){
                    i = 0;lastPosition = 0;
				}

			}

			if (i == endPosition){
				System.out.println("Point pool is full!!!!!");
                return new Point(x,y,z);
			}

			pointPool[i].setValues(x,y,z);
			inUse[i] = true;
			System.out.println("Point pool sucessfully used");
			return pointPool[i];
		} else {
			return null;
		}
	}



	public static boolean freePoint(Point what){

		for (int i=0; i<pointPool.length; i++){
			if (pointPool[i] == what){
				inUse[i] = false;
				lastPosition = i;
				System.out.println("Point freed");
				return true;
			}
		}
		return false;
	}

	public static Point multiplyFlat(Point what, int scale){
		return new Point (what.x * scale, what.y * scale, what.z);
	}
}