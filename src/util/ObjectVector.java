package util;

import java.util.Vector;

/** Base class for all the convenience class bound Vectors
 * June 19/2004
 */

public abstract class ObjectVector implements java.lang.Cloneable{
	protected Vector v;

	public ObjectVector(){
		v = new Vector(15);
	}

	public int size(){
		return v.size();
	}

	public boolean has(Object o){
		/** Checks if the BodyPart is already here*/
		return v.contains(o);
	}

	public boolean isEmpty(){
		return v.isEmpty();
	}

	public void remove(Object o){
		v.remove(o);
	}

	public void removeAll(){
		v.removeAllElements();
	}

	public void add(Object o){
		v.add(o);
	}

	public Object clone(){
		try {
			return super.clone();
		} catch(CloneNotSupportedException cnse){
			return null;
		}
	}

/*	public String toString(){
		String ret = "";
		for (int i=0; i < v.size(); i++){
			ret += v.elementAt(i).toString();
		}
		return ret;
	}            */

	public void removeElementAt(int i){
		v.removeElementAt(i);
	}

}