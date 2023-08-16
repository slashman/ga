package guardian.worldgen;

public class ContentDefinition {
	private String itemID;
	private String material;



	public String getItemID() {
		return itemID;
	}

	public String getMaterial() {
		return material;
	}

	public ContentDefinition (String pItemID, String pMaterial){
		itemID = pItemID;
		material = pMaterial;
	}
}