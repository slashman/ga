package siLayers.SwingConsole;

import uiLayers.consoleUI.*;
import util.*;

import java.awt.event.*;
import java.awt.Font;

public class SwingConsoleSI implements ConsoleSI{
	/** Must inform the Console User Interface object
	 * about the keystrokes */

	public static java.awt.Color WHITE = new java.awt.Color(255,255,255);
	public static java.awt.Color BLACK = new java.awt.Color(0,0,0);
	private SwingConsoleFrame targetFrame;
	private SwingConsolePanel targetPanel;

	private int fontSize;

	private int xdim, ydim;
	/** Width and height in characters */

	private int xpos, ypos;
	/** Current printing cursor position */

	private StrokeInformer aStrokeInformer;

    public SwingConsoleSI() {
	    Debug.enterMethod(this, "{constructor}");
	    Font f = new Font("Courier New", Font.BOLD, 16);
	    aStrokeInformer = new StrokeInformer();
	    //aStrokeInformer.setUI(this);
        fontSize = f.getSize();
        xdim = 70;
        ydim = 35;
        targetFrame = new SwingConsoleFrame();
		targetFrame.init(f, xdim, ydim);
        targetPanel = targetFrame.getSwingConsolePanel();
        targetFrame.addKeyListener(aStrokeInformer);
        locate (1,1);
		targetFrame.show();
		Debug.exitMethod();
    }

    public void cls() {
	    Debug.enterMethod(this, "cls");
	    //targetPanel.cls();
        for (int i = 0; i< xdim; i++){
            for (int j = 0; j< ydim; j++){
                targetPanel.plot(' ', i, j, WHITE, BLACK) ;
            }
        }
        Debug.exitMethod();
    }

    public void locate(int x, int y) {
        xpos = x;
		ypos = y;
    }

    public int getXDim() {
        return xdim;
    }

    public int getYDim() {
        return ydim;
    }

    public void refresh() {
        targetFrame.repaint();
    }

	public void print (String x){
		print (x, Color.WHITE, Color.BLACK);
	}

    public void print (String x, Color front){
	    print (x, front, Color.BLACK);
    }

    public void print (String x, Color front, Color back){
		java.awt.Color xFront = new java.awt.Color(front.getR(), front.getG(), front.getB());
		java.awt.Color xBack = new java.awt.Color(back.getR(), back.getG(), back.getB());
		for (int i = 0; i< x.length(); i++){
            targetPanel.plot(x.charAt(i), xpos, ypos, xFront, xBack);
            xpos ++;
            if (xpos > xdim){
				xpos = 0;
				ypos++;
			}

        }
    }

    public CharKey inkey(){
	    Debug.enterMethod(this, "inkey");
	    aStrokeInformer.resetBuffer();
		int code = -1;
		while (code == -1)
			code = aStrokeInformer.getInkeyBuffer();
		CharKey ret = new CharKey(code);
		Debug.exitMethod(ret);
		return ret;
	}
}