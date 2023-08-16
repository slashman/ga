package guardian.worldgen;

public class BeingProfile {
	private String raceID;
	private String role;

	public String getRaceID() {
		return raceID;
	}

	public String getRole() {
		return role;
	}

	public BeingProfile (String pRaceID, String pRole){
		raceID = pRaceID;
		role = pRole;
	}
}