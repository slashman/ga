/*
 * Properties.java
 *
 * Created on 2 de septiembre de 2004, 18:53
 */

package util;

public class Properties extends ObjectVector{
    public Property elementAt(int i){
	return (Property) v.elementAt(i);
    }

    public Property getProperty (String name){
        for (int i=0; i<v.size(); i++)
            if (elementAt(i).getName().equals(name)){
                return elementAt(i);
            }
        return null;
    }
}