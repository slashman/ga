package guardian.being;

import util.*;

public class VBeing extends ObjectVector{
	public GBeing elementAt(int i){
		return (GBeing) v.elementAt(i);
	}
}