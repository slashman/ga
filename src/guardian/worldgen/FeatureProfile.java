package guardian.worldgen;

import util.*;

public class FeatureProfile {
	/** Describes a feature inside a town, this is used in the generation
	 * process */
	private String featureID;
	private PopulatorInfo[] populators;
	private ContentInfo[] contents;
	private int awayness;
	private Point position;

	public FeatureProfile(String pFeatureID, int pAwayness, PopulatorInfo[] pPopulators, ContentInfo[] pContents){
		featureID = pFeatureID;
		awayness = pAwayness;
		populators = pPopulators;
		contents = pContents;
	}

	public String getFeatureID() {
		return featureID;
	}

	public PopulatorInfo[] getPopulators() {
		return populators;
	}

	public ContentInfo[] getContents() {
		return contents;
	}

	public int getAwayness() {
		return awayness;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point value) {
		position = value;
	}
}

