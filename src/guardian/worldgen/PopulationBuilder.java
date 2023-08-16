package guardian.worldgen;

import util.*;
import guardian.world.feature.*;
import guardian.being.*;
import guardian.item.*;
import guardian.world.*;

public abstract class PopulationBuilder {

	protected GBeingFactory beingFactory;
	protected GItemFactory itemFactory;
	protected FeatureBuilder aFeatureBuilder;
	protected GMapCellFactory mapCellFactory;
	protected GWorld worldAtWork;

	public void setFactories(GBeingFactory beingFactory,
		GItemFactory itemFactory,
		FeatureBuilder aFeatureBuilder,
		GMapCellFactory mapCellFactory ){
		this.beingFactory = beingFactory;
		this.itemFactory = itemFactory;
		this.aFeatureBuilder = aFeatureBuilder;
	 	this.mapCellFactory = mapCellFactory;
	}

	public abstract void generateTowns(GWorld where);

    public final static String [] cellNames = {
		"WOODEN_FLOOR",
		"WOODEN_FLOOR",
		"PILLOW",
		"BED",
		"CLOSET",
		"CHEST",
		"TAPESTRY",
		"CLOSED_WOODEN_DOOR",
		"WOODEN_WALL",
		"TABLE",
		"CHAIR",
		"BRICK_WALL",
		"COUNTER",
		"KITCHEN_TABLE",
		"HOVEN",
		"CANDLE",
		"ATRIUM",
		"COLLECTIVE_CHAIR",
		"DIRT",
		"STATUE",
		"PASSAGE"
	};

}