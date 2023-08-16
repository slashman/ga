package guardian.world;

import util.*;
import java.util.*;
import guardian.world.feature.*;

public class FeatureBuilder {

	private Feature[] featureList;

	public int[][] getFeatureVector(String description){
		return getFeature(description).getMap();
	}

	public Feature getFeature(String description){
		Vector v = new Vector(10);
		for (int i=0; i<featureList.length; i++){
			if (featureList[i].getType().equals(description)){
				v.add(featureList[i]);
			}
		}
		return (Feature) v.elementAt(MathF.rand(0, v.size()));
	}

	public void setFeatures(Feature[] pFeatureList){
		featureList=pFeatureList;
	}

}