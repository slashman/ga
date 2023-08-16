package engine;

public class VAction extends util.ObjectVector {

	public Action elementAt(int i){
		return (Action) v.elementAt(i);
	}

	/*public Action selectAction(String s){
		for (int i=0; i<v.size(); i++) {
			if (((Action) v.elementAt(i)).getID().equals(s)){
				return (Action)v.elementAt(i);
			}
		}
		return null;
	} */
}