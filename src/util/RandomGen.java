package util;

import java.util.Random;

public class RandomGen {
	private long seed;
	private Random rnd;

	public RandomGen(long seed){
		this.seed = seed;
		rnd = new Random(seed);
	}

	public double nextDouble(){
		return rnd.nextDouble();
	}

	public int nextInt(int low, int high){
		return (int) (rnd.nextDouble() * (high-low) + low);
	}

	public double nextDouble(int low, int high){
		return low + rnd.nextDouble() * (high-low);
	}


}