package guardian.item;

public class Material {
	private String ID, name;

	/** The modificators here */

	public Material(String ID, String name){
		this.ID = ID;
		this.name = name;
	}

	public String getID(){
		return ID;
	}

	public String getName(){
		return name;
	}

}