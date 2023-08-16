/*
 * Property.java
 *
 * Created on 2 de septiembre de 2004, 18:24
 */

package util;

/**
 *
 * @author  clase05
 */
public class Property {
    private String name;

    private Object value;
    /** Creates a new instance of Property */
    public Property(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String toString(){
		return "("+name+","+value+")";
	}
}