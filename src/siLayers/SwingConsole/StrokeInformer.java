package siLayers.SwingConsole;

import java.awt.event.*;
import uiLayers.consoleUI.*;

public class StrokeInformer implements KeyListener{
	private int bufferCode;
	private ConsoleUI aUI;

	public StrokeInformer(){
		bufferCode = -1;
	}

	public void resetBuffer(){
		bufferCode = -1;
	}
	public int getInkeyBuffer(){
		return bufferCode;
	}

	public void setUI(ConsoleUI pUI){
		aUI = pUI;
	}

	public void keyPressed(KeyEvent e) {
		//Debug.enterMethod("SwingConsoleSI.keyPressed("+e+")");
	    bufferCode = charCode(e);
	    //Debug.exitMethod();
    }

    private int charCode(KeyEvent x){
		switch (x.getKeyCode()){
			case KeyEvent.VK_A:
				return CharKey.A;
			case KeyEvent.VK_I:
				return CharKey.I;
			case KeyEvent.VK_S:
				return CharKey.S;
			case KeyEvent.VK_D:
				return CharKey.D;
			case KeyEvent.VK_W:
				return CharKey.W;
			case KeyEvent.VK_Q:
				return CharKey.Q;
			case KeyEvent.VK_SPACE:
				return CharKey.SPACE;
			case KeyEvent.VK_O:
				return CharKey.O;
			case KeyEvent.VK_C:
				return CharKey.C;
			case KeyEvent.VK_COMMA:
				return CharKey.COMMA;
			case KeyEvent.VK_E:
				return CharKey.E;
			case KeyEvent.VK_U:
				return CharKey.U;
			case KeyEvent.VK_F:
				return CharKey.F;
			case KeyEvent.VK_Z:
				return CharKey.Z;
			case KeyEvent.VK_T:
				return CharKey.T;
			case KeyEvent.VK_J:
				return CharKey.J;
			case KeyEvent.VK_K:
				return CharKey.K;
			case KeyEvent.VK_NUMPAD0:
				return CharKey.N0;
			case KeyEvent.VK_NUMPAD1:
				return CharKey.N1;
			case KeyEvent.VK_NUMPAD2:
				return CharKey.N2;
			case KeyEvent.VK_NUMPAD3:
				return CharKey.N3;
			case KeyEvent.VK_NUMPAD4:
				return CharKey.N4;
			case KeyEvent.VK_NUMPAD5:
				return CharKey.N5;
			case KeyEvent.VK_NUMPAD6:
				return CharKey.N6;
			case KeyEvent.VK_NUMPAD7:
				return CharKey.N7;
			case KeyEvent.VK_NUMPAD8:
				return CharKey.N8;
			case KeyEvent.VK_NUMPAD9:
				return CharKey.N9;

			case 0:
				switch (x.getKeyChar()){
					case 'A':
						return CharKey.A;
					case 'I':
						return CharKey.I;
					case 'W':
						return CharKey.W;
					case 'D':
						return CharKey.D;
					case 'S':
						return CharKey.S;
					case 'Q':
						return CharKey.Q;
					case ' ':
						return CharKey.SPACE;
					case 'O':
						return CharKey.O;
					case 'C':
						return CharKey.C;
					case 'T':
						return CharKey.T;
					case 'E':
						return CharKey.E;
					case ',':
						return CharKey.COMMA;
					case 'U':
						return CharKey.U;
					case 'F':
						return CharKey.F;
					case 'Z':
						return CharKey.Z;
					case 'J':
						return CharKey.J;
					case 'K':
						return CharKey.K;
					case '0':
						return CharKey.N0;
					case '1':
						return CharKey.N1;
					case '2':
						return CharKey.N2;
					case '3':
						return CharKey.N3;
					case '4':
						return CharKey.N4;
					case '5':
						return CharKey.N5;
					case '6':
						return CharKey.N6;
					case '7':
						return CharKey.N7;
					case '8':
						return CharKey.N8;
					case '9':
						return CharKey.N9;
				}
				break;
		}
		return -1;
	}


    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }
}