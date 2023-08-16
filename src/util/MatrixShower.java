package util;

import javax.swing.*;
import java.awt.*;

public class MatrixShower extends JFrame{
    Image im;
    Graphics aaa;
    int xd,yd;

	public void setMatrix(boolean[][]b){
		hasBool = false;
		this.b = b;
		repaint();
	}

	public void setMatrix(int[][]m){
		hasInt = true;
		this.m = m;
		repaint();
	}

	public void setDot(int x, int y){
		this.xd = x;
		this.yd = y;
	}

	private int [][] m;
	private boolean [][] b;
	private boolean hasInt;
	private boolean hasBool;
	private int coloringMode;

	public final static int TERRAIN = 0, TEMP = 1, DISCRETETEMPS = 2, RAIN = 3, GEO = 4, TEMPZONES = 5, RED = 6;

	public MatrixShower(){
		coloringMode = TEMP;
		setBounds(0,0,590,590);
		show();
		im = createImage(getHeight(), getWidth());
    	aaa = im.getGraphics();
	}

	public void setColoringMode(int m){
		coloringMode = m;
	}

	private Color pickColor(int x){
		switch (coloringMode){
			case TERRAIN:
				switch (x){
					case 0:
						return new Color(0,0,120);

					case 1:
						return new Color(0,0,200);

					case 2:
						return new Color(0,200,0);

					case 3:
						return new Color(0,120,120);
					case 4:
						return new Color(200,120,120);

				}
			break;
			case TEMP:
			/*
				if (x<3)
					//Tundra
					return new Color(120,120,200);
				else if (x<9)
					//Subpolar Humedo
					return new Color(90,90,200);
				else if (x<14)
					//Oceanico
					return new Color(0,120,120);
				else if (x<22)
					//Mediterraneo
					return new Color(0,180,180);
				else if (x<27)
					//Pluviselva
					return new Color(0,200,80);
				else if (x<=30)
					//Desierto
					return new Color(200,120,120);
				//return new Color(x*4, 200-x*3, 255-x*4);*/
				if (x<4)
					return new Color(255,255,255);
				else if (x<8)
					return new Color(0,0,90);
				else if (x<15)
					return new Color(0,90,90);
				else if (x<18)
					return new Color(0,120,90);
				else if (x<22)
					return new Color(0,120,120);
				else if (x<25)
					return new Color(0,120,180);
				else if (x<26)
					return new Color(0,120,240);
				else if (x<28)
					return new Color(200,120,0);
				else if (x<29)
					return new Color(120,0,0);
				else
					return new Color(200,0,0);
			case TEMPZONES:
				if (x<0)
					return new Color(255,255,255);
				else if (x<10)
					return new Color(0,0,90);
				else if (x<20)
					return new Color(0,90,90);
				else if (x<25)
					return new Color(0,120,90);
				else if (x<30)
					return new Color(0,120,120);
				else
					return new Color(200,0,0);

			case DISCRETETEMPS:
				switch (x){
                    case 0:
					//Tundra
					return new Color(120,120,200);
				case 1:
					//Subpolar Humedo
					return new Color(90,90,200);
				case 2:
					//Oceanico
					return new Color(0,120,120);
				case 3:
					//Mediterraneo
					return new Color(0,180,180);
				case 4:
					//Pluviselva
					return new Color(0,200,80);
					//return new Color(255,0,0);
				case 5:
					//Desierto
					return new Color(200,120,120);
					//return new Color(0,255,0);
				}
			break;
			case RAIN:
				if (x<25)
					//
					return new Color(255,255,255);

				else if (x<45)
					//
					return new Color(90,90,0);
				else if (x<55)
					//
					return new Color(120,120,0);
				else if (x<65)
					//
					return new Color(180,180,0);


				else if (x<70)
					//
					return new Color(0,120,120);
				else if (x<78)
					//
					return new Color(0,120,180);
				else if (x<92)
					//
					return new Color(0,120,240);


				else if (x<140)
					//
					return new Color(200,120,120);
				else if (x<180)
					//
					return new Color(120,0,120);
				//else if (x<=200)
				else
					//
					return new Color(200,0,200);
			case RED:
				return new Color(x,0,0);
			case GEO:
				switch(x){
					case 1:
						return new Color(255,255,255);
					case 2:
						return new Color(0,200,255);
					case 3:
						return new Color(0,200,255);
					case 4:
						return new Color(0,255,0);
					case 5:
						return new Color(0,0,255);
					case 6:
						return new Color(0,200,255);
					case 7:
						return new Color(0,100,255);
					case 8:
						return new Color(80,100,255);
					case 9:
						return new Color(180,200,80);
					case 10:
						return new Color(255,200,0);
					case 11:
						return new Color(0,100,0);
					case 12:
						return new Color(200,150,100);
					case 13:
						return new Color(250,200,150);
					case 14:
						return new Color(0,100,80);
					case 15:
						return new Color(250,100,100);
					case 16:
						return new Color(250,100,100);
					case 17:
						return new Color(0,180,0);
					case 18:
						return new Color(0,255,255);
					case 19:
						return new Color(200,100,100);
					case 20:
						return new Color(0,0,120);
				}
			break;

		}

		//Debug.doAssert(false, "Couldn't pick Color");
		return Color.RED;
	}


	public void paint(Graphics g){
		if (im != null){
			if (hasInt){
            	for (int x = 0; x < m.length; x++){
					for (int y = 0; y< m[0].length; y++){
						aaa.setColor(pickColor(m[x][y]));
						aaa.drawLine(x+30,y+60,x+31,y+61);
	        		}
				}
			}

			if (hasBool){
	            for (int x = 0; x < b.length; x++){
					for (int y = 0; y< b[0].length; y++){
						if (b[x][y])
							aaa.setColor(Color.RED);
						else
							aaa.setColor(Color.BLUE);
						aaa.drawLine(x+300,y+60,x+301,y+61);
	        		}
				}
			}


			/*aaa.setColor(Color.YELLOW);
			aaa.fillRect(xd+30,yd+60,3,3);

			aaa.setColor(Color.BLACK);

			/*for (int i = 0; i<8; i++){
				aaa.drawLine(30, (m.length/8)*i + 60, m.length+30, (m.length/8)*i+60);
			} */

			g.drawImage(im, 0,0, null);
		}

	}
}