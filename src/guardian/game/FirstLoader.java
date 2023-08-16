
/*
 * FirstLoader.java
 *
 * Created on 2 de septiembre de 2004, 19:19
 */

package guardian.game;

import engine.*;

import guardian.ai.*;
import guardian.actions.*;
import guardian.actions.combat.*;
import guardian.world.*;
import guardian.worldgen.*;
import guardian.world.feature.*;
import guardian.being.*;
import guardian.item.*;
import guardian.ui.*;

import util.*;

import uiLayers.consoleUI.*;
import siLayers.SwingConsole.*;

public class FirstLoader implements Loader{

	public ConsoleUI ui;
	public ConsoleSI si;
	public CharAppearanceFactory appearanceFactory;

	public FractalBuilder wb;
	public FirstPopulationBuilder pb;
	public GBeingFactory beingFactory;
	public GItemFactory itemFactory;
	public GMapCellFactory mapCellFactory;

	public FeatureBuilder aFeatureBuilder;

	public Game g;

    public void load() {
	    Debug.enterMethod(this, "load");
        /* User Interface Declaration */
		ui = new ConsoleUI();

        /* System interface Declaration */
        si = new SwingConsoleSI();

        /* System Interface Setup */
        //si.setUI(ui);

        /* Load the appearances */
		CharAppearance[] appearances = loadAppearances();

        /* Appearance Factory declaration */
		appearanceFactory = new CharAppearanceFactory();

        /* Appearance Factory Setup */
		appearanceFactory.init(appearances);

		/* Actions declaration */
		GuardianAction advance = new Advance();
		GuardianAction open = new Open();
		GuardianAction get = new Get();
		GuardianAction drop = new Drop();
		GuardianAction close = new Close();
		GuardianAction equip = new Equip();
		GuardianAction shieldBash = new ShieldBash();
		GuardianAction swing = new Swing();

        /* User interface Setup */
        UserAction[] ua = new UserAction[] {
	        new UserAction(open, CharKey.O),
	        new UserAction(get, CharKey.COMMA),
	        new UserAction(drop, CharKey.D),
	        new UserAction(close, CharKey.C),
	        new UserAction(equip, CharKey.E),
	        //new UserAction(shieldBash, CharKey.S)
	        new UserAction(swing, CharKey.S)
		};

        UserCommand[] uc = new UserCommand[]{
			new UserCommand(CommandListener.QUIT, CharKey.Q)
		};

        ui.init(si, ua, uc, advance, null);

        /* AI modules Setup */

        Wanderer wanderer = new Wanderer();

		wanderer.init(new Action[] {
			advance
		}, "WANDERER");

		ActionSelector [] ais = new ActionSelector[]{
			wanderer
		};


		/* Load the definitional items */
		GItem[] items = loadTestItems(appearanceFactory);
        Material[] materials = loadTestMaterials();
		itemFactory = new GItemFactory();
        itemFactory.init(items, materials);


        /* Load the races */
		GRace[] races = loadTestRaces(appearanceFactory, materials, ais, itemFactory);


		/* Load the MapCell Definitions */
		GMapCell[] mapCells = loadTestMapCells(appearanceFactory);

		Feature [] features = loadTestFeatures();

		aFeatureBuilder = new FeatureBuilder();
		aFeatureBuilder.setFeatures(features);

		/* World Builder declaration */
		wb = new FractalBuilder();

		/* Population Builder declaration */
		pb = new FirstPopulationBuilder();

		/* Factories declaration */
		beingFactory = new GBeingFactory();

		mapCellFactory = new GMapCellFactory();

		/* Being Factory setup */
        beingFactory.init(races);

        /* Item Factory setup */


		/* MapCell Factory setup */
		mapCellFactory.init(mapCells);

		/* World Builder Setup */
		wb.init(itemFactory, mapCellFactory, beingFactory, aFeatureBuilder);

		pb.setFactories(beingFactory, itemFactory, aFeatureBuilder, mapCellFactory);

		/* Point Pool initialization */
		Point.initializePool(5000);

		/* New Game declaration */
		g = new Game();

		/* Game Setup */
		g.init(ui);

		/* Start */

		Debug.exitMethod();
    }

    public Feature[] loadTestFeatures(){
		Feature [] ret = new Feature[7];
		// House 1
		int [][] f1 = new int[][]{
			{ 8, 8, 8, 8, 8, 8, 8 },
			{ 8, 2, 2, 1, 4, 4, 8 },
			{ 8, 3, 3, 1, 1, 1, 8 },
			{ 8, 1, 1, 1, 1, 1, 8 },
			{ 8, 5, 1, 1, 1, 1, 8 },
			{ 8, 5, 1, 1, 1, 1, 8 },
			{ 8, 8, 8, 8, 7, 8, 8 }
		};
		ret[0] = new StaticFeature();
		((StaticFeature)ret[0]).set(f1, "House", "House1");

		f1 = new int[][]{
			{ 8, 8, 8, 8, 8, 8, 8 },
			{ 8, 2, 2, 1, 4, 4, 8 },
			{ 8, 3, 3, 1, 4, 4, 8 },
			{ 8, 1, 1, 1, 1, 1, 8 },
			{ 8, 1, 1, 1, 3, 2, 8 },
			{ 8, 1, 1, 1, 3, 2, 8 },
			{ 8, 8, 7, 8, 8, 8, 8 }
		};
		ret[6] = new StaticFeature();
		((StaticFeature)ret[6]).set(f1, "House", "House2");

		// Town Hall
		int [][] f2 = new int[][]{
			{ 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11},
			{ 11,  2,  2,  1, 11,  1,  1, 11,  1, 10, 10,  1, 11},
			{ 11,  3,  3,  1, 11,  1,  1, 11,  1,  9,  9,  1, 11},
			{ 11,  1,  1,  1, 11,  1,  1, 11,  1,  1,  1,  1, 11},
			{ 11,  2,  2,  1, 11,  1,  1, 11,  1, 17, 17,  1, 11},
			{ 11,  3,  3,  1, 11,  1,  1, 11,  1, 17, 17,  1, 11},
			{ 11, 11, 11,  7, 11, 11,  7, 11,  1, 17, 17,  1, 11},
			{ 11,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 11},
			{ 11,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 11},
			{ 11, 11, 11, 11, 11, 11, 11, 11, 11,  7,  7, 11, 11}
		};
		ret[1] = new StaticFeature();
		((StaticFeature)ret[1]).set(f2, "TownHall", "TownHall");

		// Church
		int [][] f3 = new int[][]{
			 { 8,  8,  8,  8,  8,  8,  8},
			 { 8, 15,  1,  1,  1, 15,  8},
			 { 8,  1, 16, 16, 16,  1,  8},
			 { 8,  1,  1,  1,  1,  1,  8},
			 { 8, 17, 17,  1, 17, 17,  8},
			 { 8, 17, 17,  1, 17, 17,  8},
			 { 8,  1,  1,  1,  1,  1,  8},
			 { 8,  1,  1,  1,  1,  1,  8},
			 { 8,  8,  8,  7,  8,  8,  8}
		};
		ret[2] = new StaticFeature();
		((StaticFeature)ret[2]).set(f3, "Temple", "SimpleTemple");

		//General Store

		int [][] f4 = new int[][]{
			{ 11, 11, 11, 11,  7, 11, 11, 11},
			{ 11,  4,  1,  1,  1,  1,  4, 11},
			{ 11,  4, 11, 12, 12, 11,  4, 11},
			{ 11,  4, 11,  1,  1, 11,  4, 11},
			{ 11, 11, 11,  7,  7, 11, 11, 11}
		};
		ret[3] = new StaticFeature();
		((StaticFeature)ret[3]).set(f4, "GeneralStore", "GeneralStore");

		//Dirty Plaza

		int [][] f5 = new int[][]{
			{ 0,  0,  0,  0, 18, 18, 18,  0,  0,  0},
			{ 0, 20, 20, 20, 20, 20, 20, 20, 20,  0},
			{ 0, 20,  0, 18, 20, 18,  0,  0, 20, 18},
			{18, 20, 18, 18, 20, 18,  0, 18, 20, 18},
			{ 0, 20, 18, 18, 20, 18,  0, 18, 20,  0},
			{ 0, 20, 18, 18, 20, 20,  0, 18, 20,  0},
			{18, 20,  0, 18, 20, 18,  0, 18, 20,  0},
			{18, 20,  0, 18, 20, 18, 18,  0, 20,  0},
			{ 0, 20, 18, 18, 20, 18, 18,  0, 20, 18},
			{ 0, 20, 20, 20, 20, 20, 20, 20, 20, 18},
			{ 0,  0, 18, 18, 20, 18, 18, 18,  0,  0}
		};
		ret[4] = new StaticFeature();
		((StaticFeature)ret[4]).set(f5, "DirtyPlaza", "DirtyPlaza");

		ret [5] = new House();
		return ret;

	}

    public GRace[] loadTestRaces(AppearanceFactory appearanceFactory, Material[] materials, ActionSelector[] ais, GItemFactory ifact){
		Debug.enterMethod(this, "loadTestRaces");

		Material flesh = null;
		for (int i=0; i<materials.length; i++){
			if (materials[i].getID().equals("FLESH")){
                flesh = materials[i];
                break;
			}
		}

		Debug.doAssert(flesh != null, "Found no flesh @ firstLoader");

		GRace[] races = new GRace[4];

        GBeing tempProtoBeing = null;
		Appearance tempAppearance = null;

	    tempProtoBeing = new GBeing();
        races[0] = new GRace("Angel", tempProtoBeing);

		tempProtoBeing.setRace(races[0]);
		tempProtoBeing.setMecStr(90);
		tempProtoBeing.setFlexStr(90);
		tempProtoBeing.setQuickness(350);
		tempProtoBeing.setSpeed(50);
		tempProtoBeing.setWeight(80);
		tempProtoBeing.setSight(5);
		tempProtoBeing.setSize(20);
		tempProtoBeing.setCombatSkill(80);

        tempProtoBeing.setActionSelector(ais[0]);

        BodyPart head = new BodyPart("Head", BodyPart.HEAD, BodyPart.HI, 0.8, tempProtoBeing, flesh,120);
		BodyPart tors = new BodyPart("Torso", BodyPart.TORSO, BodyPart.MED, 0.7, tempProtoBeing, flesh, 300);
		BodyPart rwin = new BodyPart("Left Wing", BodyPart.WING, BodyPart.HI, 0.01, tempProtoBeing, flesh, 50);
		BodyPart lwin = new BodyPart("Right Wing", BodyPart.WING, BodyPart.HI, 0.01, tempProtoBeing, flesh, 50);
		BodyPart lArm = new BodyPart("Left Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh, 100);
		BodyPart rArm = new BodyPart("Right Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh, 100);
		BodyPart lFoo = new BodyPart("Left Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh,100);
		BodyPart rFoo = new BodyPart("Right Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh,100);

		EquipmentSlot armor = new EquipmentSlot("Armor", tors);
		EquipmentSlot backPack = new EquipmentSlot("Backpack", tors);
		EquipmentSlot weapon = new EquipmentSlot("Weapon", rArm);
		EquipmentSlot shield = new EquipmentSlot("Shield", lArm);

		VBodyPart bps = new VBodyPart(new BodyPart[] {
			head, tors, rwin, lwin, lArm, rArm, lFoo, rFoo });

		tempProtoBeing.setBodyParts(bps);
		tempProtoBeing.setSlots(armor, backPack, weapon, shield);


		/*Role r1 = new Role("FIGHTER");
		r1.addChance(head, 0.4, "HELMET");*/

		try{
			tempProtoBeing.setAppearance(appearanceFactory.createAppearance("ANGEL"));
		}
		catch (Exception e) {
			Debug.doAssert(false, e.getMessage());
		}

		tempProtoBeing = new GBeing();
        races[1] = new GRace("Human", tempProtoBeing);

		tempProtoBeing.setRace(races[1]);
		tempProtoBeing.setMecStr(20);
		tempProtoBeing.setFlexStr(47);
		tempProtoBeing.setQuickness(44);
		tempProtoBeing.setSpeed(44);
		tempProtoBeing.setSight(5);
		tempProtoBeing.setWeight(48);
        tempProtoBeing.setSize(41);
		tempProtoBeing.setCombatSkill(50);

		tempProtoBeing.setActionSelector(ais[0]);

        head = new BodyPart("Head", BodyPart.HEAD, BodyPart.HI, 0.8, tempProtoBeing, flesh, 50);
		tors = new BodyPart("Torso", BodyPart.TORSO, BodyPart.MED, 0.7, tempProtoBeing, flesh, 70);
		lArm = new BodyPart("Left Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh, 50);
		rArm = new BodyPart("Right Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh,50);
		lFoo = new BodyPart("Left Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh, 60);
		rFoo = new BodyPart("Right Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh, 60);

		backPack = new EquipmentSlot("Backpack", tors);
		weapon = new EquipmentSlot("Weapon", rArm);
		shield = new EquipmentSlot("Shield", lArm);
		armor = new EquipmentSlot("Armor", tors);

		bps = new VBodyPart(new BodyPart[] {
			head, tors, lArm, rArm, lFoo, rFoo });

		tempProtoBeing.setBodyParts(bps);
		tempProtoBeing.setSlots(armor, backPack, weapon, shield);

		try{
			tempProtoBeing.setCorpse(ifact.createMaterialItem("HMN-CRP", "FLESH"));
			tempProtoBeing.setAppearance(appearanceFactory.createAppearance("HUMAN"));
		}
		catch (Exception e) {
			Debug.doAssert(false, e.getMessage());
		}

		tempProtoBeing = new GBeing();
        races[2] = new GRace("Wolf", tempProtoBeing);

		tempProtoBeing.setRace(races[2]);
		tempProtoBeing.setMecStr(10);
		tempProtoBeing.setFlexStr(40);
		tempProtoBeing.setQuickness(40);
		tempProtoBeing.setSpeed(56);
		tempProtoBeing.setSight(9);
		tempProtoBeing.setWeight(34);
        tempProtoBeing.setSize(34);
		tempProtoBeing.setCombatSkill(30);

		tempProtoBeing.setActionSelector(ais[0]);

        head = new BodyPart("Head", BodyPart.HEAD, BodyPart.HI, 0.8, tempProtoBeing, flesh, 50);
		tors = new BodyPart("Torso", BodyPart.TORSO, BodyPart.MED, 0.7, tempProtoBeing, flesh, 70);
		lArm = new BodyPart("Left Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh, 50);
		rArm = new BodyPart("Right Arm", BodyPart.ARM, BodyPart.MED, 0.3, tempProtoBeing, flesh,50);
		lFoo = new BodyPart("Left Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh, 60);
		rFoo = new BodyPart("Right Foot", BodyPart.FOOT, BodyPart.LOW, 0.2, tempProtoBeing, flesh, 60);

		bps = new VBodyPart(new BodyPart[] {
			head, tors, lArm, rArm, lFoo, rFoo });

		tempProtoBeing.setBodyParts(bps);

		try{
			tempProtoBeing.setCorpse(ifact.createMaterialItem("WOLF-CORPSE", "FLESH"));
			tempProtoBeing.setAppearance(appearanceFactory.createAppearance("WOLF"));
		}
		catch (Exception e) {
			Debug.doAssert(false, e.getMessage());
		}

		Debug.exitMethod(races);
		return races;
	}

	public Material[] loadTestMaterials(){
		Material wood = new Material("WOOD", "wooden");
		Material iron = new Material("IRON", "iron");
		Material flesh = new Material("FLESH", "flesh");
		return new Material[]{
			wood, iron, flesh
		};
	}

	public GItem[] loadTestItems(AppearanceFactory appearanceFactory){
		Debug.enterMethod(this, "loadTestItems", appearanceFactory);


		GItem sword = new GItem("VNK-SWD", "Sword of the Varnak");
		GItem humanCorpse = new GItem("HMN-CRP", "A Human Corpse");
		GItem wolfCorpse = new GItem("WOLF-CORPSE", "A Wolf Corpse");
		GItem shield = new GItem("AMA-SWD", "Shield of Amaer");
		GItem ring = new GItem("BNB-RNG","Ring of the BoneBreaker");
		GItem potion = new GItem("MGT-PTN", "Potion of Might");
		GItem scroll = new GItem("ANS-SCR", "Scroll of Angel Summoning");
		GItem backPack = new GItem("MAG-CON", "Magic Back Pack");

		try {
			backPack.setAppearance(appearanceFactory.createAppearance("BACKPACK"));
			sword.setAppearance(appearanceFactory.createAppearance("SWORD"));
			humanCorpse.setAppearance(appearanceFactory.createAppearance("HUMANCORPSE"));
			wolfCorpse.setAppearance(appearanceFactory.createAppearance("WOLF-CORPSE"));
			shield.setAppearance(appearanceFactory.createAppearance("SHIELD"));
			ring.setAppearance(appearanceFactory.createAppearance("RING"));
			potion.setAppearance(appearanceFactory.createAppearance("BLUE_POTION"));
			scroll.setAppearance(appearanceFactory.createAppearance("CYAN_POTION"));
		} catch (EngineException ee){
			Debug.say(ee);
		}

		/*backPack.setItemType(GItem.CONTAINER);
        sword.setItemType(GItem.SWORD);
		shield.setItemType(GItem.SHIELD);
		ring.setItemType(GItem.RING);
		potion.setItemType(GItem.POTION);
		scroll.setItemType(GItem.SCROLL);*/

		GItem[] items = new GItem[] {
			sword, shield, ring, potion, scroll, backPack, humanCorpse, wolfCorpse
		};

		backPack.setAllowedBodyParts(new String[]{BodyPart.BACK});
		backPack.setVolumeCapacity(50);
		backPack.setWeightCapacity(50);
		backPack.setBodyPartCoverage(40);

		sword.setAllowedBodyParts(new String[] {BodyPart.HAND});
		sword.setBodyPartCoverage(3);
		sword.setVolume(5);
		sword.setBladeSize(60);
		sword.setBladeSharpness(40);
		sword.setWeight(24);

		shield.setAllowedBodyParts(new String[] {BodyPart.HAND});
		shield.setBodyPartCoverage(15);
		shield.setVolume(15);

		ring.setAllowedBodyParts(new String[] {BodyPart.FINGER});
		ring.setBodyPartCoverage(1);
		ring.setVolume(2);

		potion.setBodyPartCoverage(3);
		potion.setVolume(10);

		scroll.setBodyPartCoverage(1);
		scroll.setVolume(2);

		Debug.exitMethod(items);
		return items;

	}

	public GMapCell[] loadTestMapCells(AppearanceFactory appearanceFactory){
		Debug.enterMethod(this, "loadTestMapCells", appearanceFactory);

        int cellNum = 40;
        GMapCell[] definitions = new GMapCell [ cellNum + 1];
        try {
	        for (int i=0; i<= cellNum; i++){
	            switch (i){
	                case 0:
	                    definitions[i] = new GMapCell("ICE",
						appearanceFactory.createAppearance("ICE"),
						"Ice", "",
						GMapCell.C_GRASS, 0, null);
                    break;
					case 1:
	                    definitions[i] = new GMapCell("TUNDRA",
						appearanceFactory.createAppearance("TUNDRA"),
						"Tundra", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 2:
	                    definitions[i] = new GMapCell("OCEANIC",
						appearanceFactory.createAppearance("OCEANIC"),
						"Oceanic Vegetation", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 3:
	                    definitions[i] = new GMapCell("PRAIRIE",
						appearanceFactory.createAppearance("PRAIRIE"),
						"Prairie", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 4:
	                    definitions[i] = new GMapCell("SEA",
						appearanceFactory.createAppearance("SEA"),
						"Prairie", "",
						GMapCell.C_OCEAN, 0, null);
					break;
					case 5:
	                    definitions[i] = new GMapCell("TAIGA",
						appearanceFactory.createAppearance("TAIGA"),
						"Taig'a", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 6:
	                    definitions[i] = new GMapCell("MEDIT",
						appearanceFactory.createAppearance("MEDIT"),
						"Mediterraneum Vegetation", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 7:
	                    definitions[i] = new GMapCell("COAST",
						appearanceFactory.createAppearance("COAST"),
						"Coastal Oceanic", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 8:
	                    definitions[i] = new GMapCell("ASDFOREST",
						appearanceFactory.createAppearance("ASDFOREST"),
						"Asiduous Forest", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 9:
	                    definitions[i] = new GMapCell("SAVANNA",
						appearanceFactory.createAppearance("SAVANNA"),
						"Savanna", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 10:
	                    definitions[i] = new GMapCell("PLUVI",
						appearanceFactory.createAppearance("PLUVI"),
						"Pluviforest", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 11:
	                    definitions[i] = new GMapCell("STEEPE",
						appearanceFactory.createAppearance("STEEPE"),
						"Steepe", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 12:
	                    definitions[i] = new GMapCell("DESERT",
						appearanceFactory.createAppearance("DESERT"),
						"Desert", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 13:
	                    definitions[i] = new GMapCell("TROPICAL",
						appearanceFactory.createAppearance("TROPICAL"),
						"Tropical Forest", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 14:
	                    definitions[i] = new GMapCell("PLATEAU",
						appearanceFactory.createAppearance("PLATEAU"),
						"Tropical Plateau", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 15:
	                    definitions[i] = new GMapCell("DRYFOREST",
						appearanceFactory.createAppearance("DRYFOREST"),
						"Dry Forest", "",
						GMapCell.C_GRASS, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 16:
	                    definitions[i] = new GMapCell("HILLS",
						appearanceFactory.createAppearance("HILLS"),
						"Hills", "",
						GMapCell.C_GRASS, 0, null);
					break;
					case 17:
	                    definitions[i] = new GMapCell("ICEMNTN",
						appearanceFactory.createAppearance("ICEMNTN"),
						"Ice Mountain", "",
						GMapCell.C_MOUNTAIN, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;
					case 18:
	                    definitions[i] = new GMapCell("RCKMNTN",
						appearanceFactory.createAppearance("RCKMNTN"),
						"Rocky Mountain", "",
						GMapCell.C_MOUNTAIN, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;
					case 19:
	                    definitions[i] = new GMapCell("DEEPSEA",
						appearanceFactory.createAppearance("DEEPSEA"),
						"Deep Sea", "",
						GMapCell.C_OCEAN, 0, null);
						definitions[i].setWater(true);
					break;
					case 20:
	                    definitions[i] = new GMapCell("WOODEN_FLOOR",
						appearanceFactory.createAppearance("WOODEN_FLOOR"),
						"WOODEN_FLOOR", "Found in small human settlements",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 21:
	                    definitions[i] = new GMapCell("PILLOW",
						appearanceFactory.createAppearance("PILLOW"),
						"PILLOW", "Humans rest their heads here",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 22:
	                    definitions[i] = new GMapCell("BED",
	                    appearanceFactory.createAppearance("BED"),
						"BED", "Resting place of humans",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 23:
	                    definitions[i] = new GMapCell("CLOSET",
						appearanceFactory.createAppearance("CLOSET"),
						"CLOSET", "Storage",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 24:
	                    definitions[i] = new GMapCell("CHEST",
						appearanceFactory.createAppearance("CHEST"),
						"CHEST", "Safe Storage",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setOpaque(true);
					break;
					case 25:
	                    definitions[i] = new GMapCell("TAPESTRY",
						appearanceFactory.createAppearance("TAPESTRY"),
						"TAPESTRY", "Adornment for houses",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 26:
	                    definitions[i] = new GMapCell("CLOSED_WOODEN_DOOR",
						appearanceFactory.createAppearance("CLOSED_WOODEN_DOOR"),
						"Closed Wooden Door", "You can try to Open it",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;
					case 27:
	                    definitions[i] = new GMapCell("WOODEN_WALL",
						appearanceFactory.createAppearance("WOODEN_WALL"),
						"WOODEN_WALL", "A simple barrier used for houses",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;
					case 28:
	                    definitions[i] = new GMapCell("TABLE",
						appearanceFactory.createAppearance("TABLE"),
						"TABLE", "A Wooden Table usable for studying and eating",
						GMapCell.C_INFERTILE, 0, null);
					break;

					case 29:
	                    definitions[i] = new GMapCell("CHAIR",
						appearanceFactory.createAppearance("CHAIR"),
						"CHAIR", "Storage",
						GMapCell.C_INFERTILE, 0, null);
					break;

					case 30:
	                    definitions[i] = new GMapCell("BRICK_WALL",
						appearanceFactory.createAppearance("BRICK_WALL"),
						"BRICK_WALL", "A strong barrier",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;

					case 31:
	                    definitions[i] = new GMapCell("COUNTER",
						appearanceFactory.createAppearance("COUNTER"),
						"COUNTER", "Keeps customers at bay",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setSolid(true);
					break;

					case 32:
	                    definitions[i] = new GMapCell("KITCHEN_TABLE",
						appearanceFactory.createAppearance("KITCHEN_TABLE"),
						"KITCHEN TABLE", "Used to butch and hearse",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setSolid(true);
					break;


					case 33:
	                    definitions[i] = new GMapCell("HOVEN",
						appearanceFactory.createAppearance("HOVEN"),
						"Hoven", "Cooks things",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setSolid(true);
					break;

					case 34:
	                    definitions[i] = new GMapCell("CANDLE",
						appearanceFactory.createAppearance("CANDLE"),
						"Candle", "Lights and adorns",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setSolid(true);
					break;

					case 35:
	                    definitions[i] = new GMapCell("ATRIUM",
						appearanceFactory.createAppearance("ATRIUM"),
						"Atrium", "Preachers use it",
						GMapCell.C_INFERTILE, 0, null);
					break;

					case 36:
	                    definitions[i] = new GMapCell("COLLECTIVE_CHAIR",
						appearanceFactory.createAppearance("COLLECTIVE_CHAIR"),
						"COLLECTIVE CHAIR", "More than 2 can sit here",
						GMapCell.C_INFERTILE, 0, null);
					break;

					case 37:
	                    definitions[i] = new GMapCell("DIRT",
						appearanceFactory.createAppearance("DIRT"),
						"DIRT", "Patch of dirt seen in some plazas",
						GMapCell.C_INFERTILE, 0, null);
					break;

					case 38:
	                    definitions[i] = new GMapCell("STATUE",
						appearanceFactory.createAppearance("STATUE"),
						"STATUE", "A statue dedicated to the founder of the town",
						GMapCell.C_INFERTILE, 0, null);
						definitions[i].setOpaque(true);
						definitions[i].setSolid(true);
					break;

					case 39:
	                    definitions[i] = new GMapCell("PASSAGE",
						appearanceFactory.createAppearance("PASSAGE"),
						"PASSAGE", "Made for people to pass...",
						GMapCell.C_INFERTILE, 0, null);
					break;
					case 40:
	                    definitions[i] = new GMapCell("OPEN_WOODEN_DOOR",
						appearanceFactory.createAppearance("OPEN_WOODEN_DOOR"),
						"Open Wooden Door", "You can try to Close it",
						GMapCell.C_INFERTILE, 0, null);

					break;
	            }
			}
		}catch (EngineException e) {
			Debug.doAssert(false, "Load error "+e);

		}
		definitions[26].addTransformation(new Property("OPEN", definitions[40]));
		definitions[40].addTransformation(new Property("CLOSE", definitions[26]));
		Debug.exitMethod();
		return definitions;
    }

    public CharAppearance[] loadAppearances(){
	    Debug.enterMethod(this, "loadAppearances");
		Debug.say("Loading Test Appearances from code");
		CharAppearance[] tapp = new CharAppearance [] {
			new CharAppearance("ICE", '.', Color.WHITE),
			new CharAppearance("TUNDRA", '.', Color.CYAN),
			new CharAppearance("OCEANIC", '.', new Color(0,200,255)),
			new CharAppearance("PRAIRIE", '.', Color.GREEN),
			new CharAppearance("SEA", '~', Color.BLUE),
			new CharAppearance("TAIGA", 't', new Color(0,200,255)),
			new CharAppearance("MEDIT", 't', new Color(0,100,180)),
			new CharAppearance("COAST", 't', new Color(80,100,255)),
			new CharAppearance("ASDFOREST", 't', new Color(180,200,80)),
			new CharAppearance("SAVANNA", '.', new Color(255,200,0)),
			new CharAppearance("PLUVI", '&', new Color(0,100,0)),
			new CharAppearance("STEEPE", '.', new Color(200,150,100)),
			new CharAppearance("DESERT", '~', new Color(250,200,150)),
			new CharAppearance("TROPICAL", '&', new Color(0,100,80)),
			new CharAppearance("PLATEAU", '.', new Color(250,100,100)),
			new CharAppearance("DRYFOREST", 't', new Color(250,100,100)),
			new CharAppearance("HILLS", '^', new Color(0,180,0)),
			new CharAppearance("ICEMNTN", '^', Color.CYAN),
			new CharAppearance("RCKMNTN", '^', Color.BROWN),
			new CharAppearance("DEEPSEA", '~', new Color(0,0,80)),
			new CharAppearance("WOODEN_FLOOR", '.', Color.BROWN),
			new CharAppearance("PILLOW", 'O', Color.BLUE),
			new CharAppearance("BED", 'U', Color.BLUE),
			new CharAppearance("CLOSET", 'H', new Color(200, 50, 50)),
			new CharAppearance("CHEST", '&', Color.YELLOW),
			new CharAppearance("TAPESTRY", '*', Color.RED),
			new CharAppearance("OPEN_WOODEN_DOOR", '\'', Color.BROWN),
			new CharAppearance("CLOSED_WOODEN_DOOR", '/', Color.BROWN),
			new CharAppearance("WOODEN_WALL", '#', Color.BROWN),
			new CharAppearance("TABLE", '+', Color.BROWN),
			new CharAppearance("CHAIR", '-', Color.BROWN),
			new CharAppearance("BRICK_WALL", '#', Color.GRAY),
			new CharAppearance("COUNTER", '=', Color.BROWN),
			new CharAppearance("KITCHEN_TABLE", '+', Color.BROWN),
			new CharAppearance("HOVEN", 'o', Color.RED),
			new CharAppearance("CANDLE", 'i', Color.ORANGE),
			new CharAppearance("ATRIUM", ',', Color.YELLOW),
			new CharAppearance("COLLECTIVE_CHAIR", '=', Color.BROWN),
			new CharAppearance("DIRT", '.', Color.BROWN),
			new CharAppearance("STATUE", '@', Color.GRAY),
			new CharAppearance("BACKPACK", 'b', Color.GRAY),
			new CharAppearance("PASSAGE", ';', Color.GRAY),

			/*
			new CharAppearance("WALL", '#', Color.GRAY),
			new CharAppearance("CLOSEDDOOR", '/', Color.ORANGE),
			new CharAppearance("OPENDOOR", '\'', Color.ORANGE),
			new CharAppearance("SMALLRIVER", '=', Color.BLUE),
			new CharAppearance("HOUSEFLOOR", '.', Color.ORANGE),
			new CharAppearance("DIRT", '.', Color.ORANGE),
			new CharAppearance("TREE", '&', Color.GREEN),
			new CharAppearance("CAVEWALL", '%', Color.GRAY),
			new CharAppearance("SLOPEDOWN", 'v', Color.GRAY),
			new CharAppearance("SLOPEUP", '^', Color.GRAY),
			new CharAppearance("HOLE", ' ', Color.GRAY),
			*/

			new CharAppearance("ANGEL", '@', Color.CYAN),
			new CharAppearance("HUMAN", '@', Color.GREEN),
			new CharAppearance("ORC", 'o', Color.GREEN),
			new CharAppearance("RAT", 'r', Color.BROWN),
			new CharAppearance("WOLF", 'w', Color.BROWN),

			new CharAppearance("SWORD", '/', Color.BLUE),
			new CharAppearance("SHIELD", '[', Color.GREEN),
			new CharAppearance("RING", '=', Color.WHITE),
			new CharAppearance("BLUE_POTION", '!', Color.BLUE),
			new CharAppearance("CYAN_POTION", '!', Color.CYAN),
			new CharAppearance("RED_POTION", '!', Color.RED),
			new CharAppearance("SCROLL", '?', Color.WHITE),
			new CharAppearance("HUMANCORPSE", '%', Color.GRAY),
			new CharAppearance("WOLF-CORPSE", '%', Color.BROWN)
			};
		Debug.exitMethod();
		return tapp;
	}


}