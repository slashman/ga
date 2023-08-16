package siLayers.SwingConsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingConsolePanel extends JPanel{

    private char [][] charBuffer;
    private Color [][] colorBuffer, backColorBuffer;
    private Color backGround, foreGround;
    private boolean [][] updateBuffer;

    private Graphics graphicsBuff;
	private Image imageBuff;

    private int xpos, ypos, // Current Cursor Position
                width, height, // Size of the Panel in points
                xdim, ydim, // Size of the Panel in characters
                fontSize;

    private Font f;

    public void init(Font f, int xdim, int ydim){
        width =  (xdim + 5) * f.getSize();
        height = (ydim + 5) * f.getSize();
        setBounds(1,1, width , height);
        backGround = Color.black;
        foreGround = Color.white;
        setBackground(backGround);
        this.xdim = xdim;
		this.ydim = ydim;
		this.f = f;
        charBuffer = new char [xdim] [ydim];
        colorBuffer = new Color [xdim][ydim];
        backColorBuffer = new Color [xdim][ydim];
        updateBuffer = new boolean [xdim][ydim];

        for (int i = 0; i<xdim; i++)
            for(int j=0; j<ydim; j++){
                charBuffer [i][j] = ' ';
                colorBuffer [i][j] = foreGround;
                backColorBuffer [i][j] = backGround;
                updateBuffer [i][j] = true;
            }

        //Double Buffer
        imageBuff = createImage(width, height);
        graphicsBuff = imageBuff.getGraphics();
        graphicsBuff.setFont(f);
        fontSize = f.getSize();
        repaint();
    }


    public void plot (char c, int x, int y, Color co, Color back) {
        if ((x>0 && x< xdim) && (y> 0 && y < ydim)) {
            backColorBuffer [x][y] = back;
            colorBuffer [x][y] = co;
            charBuffer [x][y] = c;
            updateBuffer [x][y] = true;
        }
    }

    public void refresh(){
        repaint();
    }

     public void paintComponent(Graphics g){
	     int fontWidth = (int) (fontSize * 0.7);
	     int fontDown = (int)(fontSize * 1.3);
        for (int x = 0; x < charBuffer.length; x++)
           for (int y = 0; y < charBuffer[0].length; y++){
               if (updateBuffer[x][y]) {
                   graphicsBuff.setColor (backColorBuffer [x][y]);
                   graphicsBuff.fillRect (x*fontWidth,(y-1)*fontSize,fontWidth,fontDown);
                   // Fix upper and lower positions if possible
                   if (y-1 >= 0){
                       graphicsBuff.setColor (colorBuffer[x][y-1]);
                       graphicsBuff.drawString(""+charBuffer[x][y-1], x * fontWidth, (y-1) * fontSize);
                   }
                   if (y+1 < ydim){
                       graphicsBuff.setColor(colorBuffer[x][y+1]);
                       graphicsBuff.drawString(""+charBuffer[x][y+1], x * fontWidth, (y+1) * fontSize);
                   }

                   graphicsBuff.setColor (colorBuffer[x][y]);
                   graphicsBuff.drawString(""+charBuffer[x][y], x * fontWidth, y * fontSize);
                   updateBuffer[x][y] = false;
               }
           }
        g.drawImage(imageBuff, 0, 0, null);
    }
}