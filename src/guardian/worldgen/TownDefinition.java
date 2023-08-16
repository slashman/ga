package guardian.worldgen;

import util.*;

public class TownDefinition {
	private int sizex, sizey;
	private Point position;
	private String name;
	private FeatureProfile[] featureProfiles;

	public int getSizex() {
		return sizex;
	}

	public int getSizey() {
		return sizey;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point whatTheHeck){
		position = whatTheHeck;
	}

	public TownDefinition (String pname, int psizex, int psizey, FeatureProfile[] pProfiles){
		name = pname;
		sizex = psizex;
		sizey = psizey;
		featureProfiles = pProfiles;
	}

	public FeatureProfile[] getFeatureProfiles() {
		return featureProfiles;
	}
}
