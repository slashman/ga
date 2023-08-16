package engine;

import util.*;


public class VActor extends ObjectVector {
   	public Actor elementAt(int i){
		return (Actor) v.elementAt(i);
	}

	public void insert(Actor a ){
		/**Inserts the Being in the priority queue based on his nexttime*/
		Debug.enterMethod(this, "insert", a);
		if (v.size() > 0){
			//Debug.say("Entro a vsize >0");
	        for (int i=0; i<v.size();i++){
	        	//Debug.say("Dentro del for " + i);
				if (a.getTime() < elementAt(i).getTime()){
					//Debug.say("va a insertar");
					v.insertElementAt(a, i);
					//Debug.say("Inserted "+a+" with time "+a.getTime() + " at position " + i);
					Debug.exitMethod();
					return;
				}
			}
			v.insertElementAt(a, v.size());
			Debug.exitMethod();
			return;
		} else {
        	v.add(a);
        	//Debug.say("Inserted "+a+" with time "+a.getTime() + " at position 0");
        	//Debug.exitMethod();
		}


		Debug.exitMethod();
	}

}