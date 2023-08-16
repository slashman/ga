package uiLayers.consoleUI;

public interface ConsoleSI {


    public void print (String x);
    /** Prints the String at the current cursor location.
     * @param x The String that will be shown
     */

    //public void setUI(ConsoleUI cui);

    public void print (String x, Color color);
    /** Prints the String in the specified Color.
     * @param x The String that will be printed.
     * @param color The Color in which the String will be painted.
     */

    public void print (String x, Color color, Color back);

    public void locate (int x, int y);

    public void refresh();

    public void cls();

    public int getXDim();

    public int getYDim();

    public CharKey inkey();

    /**  Waits until a key is pressed and returns it */
}