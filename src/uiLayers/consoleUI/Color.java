package uiLayers.consoleUI;

import util.*;


public class Color {
	public final static Color
		RED = new Color(200,0,0),
		GREEN = new Color(0,200,0),
		BLUE = new Color(0,0,200),

		CYAN = new Color(0,200,200),
		MAGENTA = new Color(200, 0, 200),
		YELLOW = new Color(200,200,0),

		BROWN = new Color(200,100,100),
		ORANGE = new Color(200,150,150),

		GRAY = new Color(126,126,126),
		WHITE = new Color(200,200,200),
		BLACK = new Color(0,0,0);
		;

	private int r,g,b;

	public Color (int r, int g, int b) {
		//Debug.enterMethod(this, "{constructor}");
		try {
			if (r>255 || r<0 || g > 255 || g <0 || b > 255 || b < 0) throw new ParameterException("Invalid Color Value");
			this.r = r;
			this.g = g;
			this.b = b;
		} catch (ParameterException pe){
			Debug.say(pe.getMessage());
		}
		//Debug.exitMethod();
	}

	public Color modify(int pr, int pg, int pb){
		Color x = new Color(r,g,b);
		if (x.getR() + pr <= 255 && x.getR() + pr >= 0) x.setR(x.getR() + pr);
		if (x.getG() + pg <= 255 && x.getG() + pg >= 0) x.setG(x.getG() + pg);
		if (x.getB() + pb <= 255 && x.getB() + pb >= 0) x.setB(x.getB() + pb);
		return x;
	}


	public int getR() {
		return r;
	}

	public void setR(int value) {
		r = value;
	}

	public int getG() {
		return g;
	}

	public void setG(int value) {
		g = value;
	}

	public int getB() {
		return b;
	}

	public void setB(int value) {
		b = value;
	}
}