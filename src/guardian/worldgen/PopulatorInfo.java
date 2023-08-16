package guardian.worldgen;

import util.*;

public class PopulatorInfo {
	private BeingProfile beingProfile;
	private Point position;

	public BeingProfile getBeingProfile() {
		return beingProfile;
	}

	public Point getPosition() {
		return position;
	}

	public PopulatorInfo(Point pPosition, BeingProfile pProfile){
		beingProfile = pProfile;
		position = pPosition;
	}




}