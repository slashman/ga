package siLayers.SwingConsole;

import java.awt.*;
import java.awt.event.*;


public class SwingConsoleFrame extends javax.swing.JFrame{
	/** This class is in charge of
	 * Showing the console
	 * Getting keyboard input */

    private SwingConsolePanel swingConsolePanel;

    public void init (Font f, int xdim, int ydim){
	    initComponents();
        setBounds(1,1, (xdim) * f.getSize(), (ydim + 1) * f.getSize());
        swingConsolePanel.init(f, xdim, ydim);
    }

    private void initComponents() {
        swingConsolePanel = new SwingConsolePanel();
        getContentPane().setLayout(null);
        setTitle("Guardian Angel");
        setBackground(Color.black);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        getContentPane().add(swingConsolePanel);
        swingConsolePanel.setBounds(0, 0, 0, 0);
        pack();
    }

    private void exitForm(WindowEvent evt) {
        System.exit(0);
    }

	public SwingConsolePanel getSwingConsolePanel() {
		return swingConsolePanel;
	}
}