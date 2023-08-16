package guardian.worldgen;

import util.*;
import guardian.world.feature.*;
import guardian.being.*;
import guardian.item.*;
import guardian.world.*;

public class FirstPopulationBuilder extends PopulationBuilder{

    public void generateTowns(GWorld where){
		Debug.enterMethod(this, "generateTowns");
		worldAtWork = where;
		TownDefinition[] definitions = getDefinitions();
		for (int i = 0; i < definitions.length; i++){
			//pickLocation (definitions[i]);
			definitions[i].setPosition(new Point(250,200));

			buildTown(definitions[i]);
		}
		Debug.exitMethod();
	}

	private TownDefinition[] getDefinitions(){
		TownDefinition[] ret = new TownDefinition[] {
			new TownDefinition("Tesalonikki", 20,20,
				new FeatureProfile[] {
					new FeatureProfile("House", 1,
						new PopulatorInfo[] {
	                    	new PopulatorInfo(new Point (1,1),
	                    		new BeingProfile("Human", "Farmer")
							)
						},
						new ContentInfo[] {

						}
					),
					new FeatureProfile("DirtyPlaza", 4,
						new PopulatorInfo[] {

						},
						new ContentInfo[] {
							new ContentInfo(new Point(2,2),
								new ContentDefinition("VNK-SWD", "IRON")
							)
						}
					),
					new FeatureProfile("Temple", 4,
						new PopulatorInfo[] {
	                    	new PopulatorInfo(new Point (1,1),
	                    		new BeingProfile("Human", "Priest")
							)
						},
						new ContentInfo[] {

						}
					)
				}
			)
		};

		return ret;
	}

	private void buildTown (TownDefinition town){
		Debug.enterMethod(this, "buildTown");
		FeatureProfile [] features = town.getFeatureProfiles();
		for (int i = 0; i < features.length; i++){
			buildFeature(features[i], town);
			PopulatorInfo[] populators = features[i].getPopulators();
			try {
				for (int j = 0; j < populators.length; j++){
					BeingProfile populatorProfile = populators[j].getBeingProfile();
					GBeing populator = beingFactory.createBeing(populatorProfile);
					populator.setPosition(Point.add(populators[j].getPosition(), features[i].getPosition()));
					worldAtWork.addPhysicalActor(populator,true);
				}

				ContentInfo[] contents = features[i].getContents();
				for (int j=0; j < contents.length; j++){
					ContentDefinition contentDefinition = contents[j].getContentDefinition();
					GItem content = itemFactory.createItem(contentDefinition);
					content.setPosition(Point.add(contents[j].getPosition(), features[i].getPosition()));
					worldAtWork.addPhysicalActor(content,false);
				}
			}
			catch (EngineException ee){
				Debug.doAssert(false, "Error creating the population");
			}
		}
		/*
        buildFeature("DirtyPlaza", location, size,  4);
        buildFeature("TownHall", location, size, 4);
        buildFeature("Temple", location, size, 4);
        buildFeature("GeneralStore", location, size, 4);
        buildFeature("House", location, size, 1);
        buildFeature("House", location, size, 1);
        buildFeature("House", location, size, 1);
        buildFeature("House", location, size, 1);
        buildFeature("House", location, size, 1);
        buildFeature("House", location, size, 1);
		*/
		//int population = MathF.rand(6,10);
/*        int population = 1;
        //location = new Point(250,200);

        try {

	        for (int i = 0; i < population; i++){
				GBeing rat = beingFactory.createBeing("Human");
				rat.setPosition(location);
				worldAtWork.addPhysicalActor(rat,true);
			}
  */           /*
			for (int i=0; i < 5; i++){
				GItem crap = itemFactory.createMaterialItem("VNK-SWD", "IRON");
				crap.setPosition(location);
				worldAtWork.addPhysicalActor(crap, false);
			}

			for (int i = 0; i < 2; i++){
				GItem crap = itemFactory.createMaterialItem("MAG-CON", "IRON");
				crap.setPosition(new Point (252,200));
				worldAtWork.addPhysicalActor(crap, false);
			}*/
	}

	//private void buildFeature (String feature, Point townCenter, int townSize, int awayness){
	private void buildFeature (FeatureProfile featureProf, TownDefinition town){
		//Debug.enterMethod(this, "buildFeature ("+feature+","+townCenter+","+townSize+","+awayness);
		Feature f = aFeatureBuilder.getFeature(featureProf.getFeatureID());
		if (f instanceof StaticFeature){
        	int rotations = MathF.rand(0, 3);
			for (int i=0; i<rotations; i++)
				((StaticFeature)f).rotate();
		}

		Point p = null;
		int i = 0;
		int [][] cellsToTest = null;
		int awayness= featureProf.getAwayness();
		do{
	        p = Point.random(	town.getPosition().x - town.getSizex() / awayness,
	        					town.getPosition().y - town.getSizey() / awayness,
	        					town.getPosition().x + town.getSizex() / awayness,
	        					town.getPosition().y + town.getSizey() / awayness);
	        cellsToTest = f.getMap();
	    	i++;
		} while (!canBuild(cellsToTest, p) && i < 50);
		featureProf.setPosition(p);
		if (i<50)
			placeFeature(cellsToTest, p);
		//Debug.exitMethod();
	}

	private boolean canBuild(int[][] cells, Point position){
		//Debug.enterMethod(this, "canBuild("+feature+","+position);

		int xstart = position.x - cells[0].length / 2;
		int xend = xstart + cells[0].length;
		//Debug.say("feature.getWidth()"+       feature.getWidth());
		int ystart = position.y - cells.length /2;
		int yend = ystart + cells.length;
		//Debug.say("feature.getWidth()"+       feature.getHeight());

		for (int x = xstart; x <= xend; x++){
			for (int y = ystart; y <= yend; y++){
				if (worldAtWork.getMapCell(0, x, y).isCategory(GMapCell.C_GRASS) == false) {
					//Debug.exitMethod("false");
					return false;
				}
			}
		}
		//Debug.exitMethod("true");
		return true;
	}

	private void placeFeature(int[][] map, Point position){
		//Debug.enterMethod(this, "placeFeature("+feature+","+position);
		int xstart = position.x - map[0].length / 2;
		int xend = xstart + map[0].length;
		int ystart = position.y - map.length /2;
		int yend = ystart + map.length;

		for (int mapx = 0, x = xstart; mapx < map[0].length; x++, mapx++){
			for (int y = ystart, mapy = 0; mapy < map.length; y++, mapy++){

				try {
					//Debug.say(cellNames[map[mapy][mapx]]);
					worldAtWork.setCell(new Point(x,y), mapCellFactory.createMapCell(cellNames[map[mapy][mapx]]));
				} catch (EngineException ee){
					Debug.doAssert(false, "Feature error" + ee);
				} catch (ArrayIndexOutOfBoundsException aioob){
					Debug.say ("x"+x);
					Debug.say ("y"+y);
					Debug.say ("mapx"+mapx);
					Debug.say ("mapy"+mapy);
					Debug.say ("map[mapy][mapx] " + map[mapy][mapx]);
					Debug.say ("cellNames[map[mapy][mapx]] " + cellNames[map[mapy][mapx]]);
					Debug.doAssert(false, "Fractal Builder error");
				}

			}
		}
		//Debug.exitMethod();

	}


	//private Point pickLocation(int size){
	private void pickLocation(TownDefinition town){
		for (int i=0; i < 40; i++){
			int xdot = MathF.rand(town.getSizex() +1, worldAtWork.getWidth(0)-town.getSizex()-1);
			int ydot = MathF.rand(town.getSizey() +1, worldAtWork.getHeight(0)-town.getSizey()-1);
			// Calculate the fitness according to the point and the size
			int area = town.getSizex()*town.getSizey();

			int waterCount = 0;
			int mountainCount = 0;
			for (int x = xdot-town.getSizex(); x <= xdot+town.getSizex(); x++){
            	for (int y = ydot-town.getSizey(); y <= ydot + town.getSizey(); y++){
					if (worldAtWork.getMapCell(0, x, y).isCategory(GMapCell.C_OCEAN)){
						waterCount++;
					}
					if (worldAtWork.getMapCell(0, x, y).isCategory(GMapCell.C_MOUNTAIN)){
						mountainCount++;
					}
				}
			}
			if (waterCount < area * 0.3 && mountainCount < area * 0.2){
                town.setPosition(new Point (xdot, ydot));
				return;
			}

				//return new Point (xdot, ydot);
		}
		Debug.doAssert(false, "Sadly, couldnt find a position for the town");
		//return new Point (MathF.rand(size+1, worldAtWork.getWidth(0)-size-1), MathF.rand(size+1, worldAtWork.getHeight(0)-size-1));
	}



}