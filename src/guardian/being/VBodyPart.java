package guardian.being;

import util.*;

public class VBodyPart extends ObjectVector{
	public BodyPart elementAt(int i){
		return (BodyPart) v.elementAt(i);
	}

	public VBodyPart(BodyPart[] init){
		for (int i=0; i<init.length; i++){
			v.add(init[i]);
		}
	}

	public VBodyPart(){

	}
}