package guardian.world;

import util.*;

public class TimeLine extends ObjectVector{
	/** A list of Events, sorted with the latest one in the first position */

	public void insert(Event e){
		/** Insert e into the timeline, bubbling it downward */
		Debug.enterMethod(this, "insert", e);

		int i =0;
		while (i<v.size()){
			if (!elementAt(i).getTime().isAfter(e.getTime())){
                v.insertElementAt(e,i);
                Debug.exitMethod();
                return;
			}
			i++;
		}
		v.insertElementAt(e,v.size());
		Debug.exitMethod();
	}

	public void insert (Event[] e){
		Debug.enterMethod(this, "insert", e);
		for (int i=0; i<e.length; i++)
			insert (e[i]);
		Debug.exitMethod();
	}

	public Event elementAt(int i){
		return (Event) v.elementAt(i);
	}

}