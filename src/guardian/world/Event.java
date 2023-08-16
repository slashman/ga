package guardian.world;

import engine.*;

import util.*;

public class Event {
	private String textDescriptionYou;
	private String textDescriptionOther;
	private Actor source;
	private Action action;
	private VActor targets;
	private Point position;
	private GTime time;

	public Event(GTime pTime, String pTextDescriptionYou, String pTextDescriptionOther, Actor pSource, Action pAction, VActor pTargets, Point pPosition){
		time = pTime;
		source = pSource;
		action = pAction;
		targets = pTargets;
		position = pPosition;
		textDescriptionYou = pTextDescriptionYou;
		textDescriptionOther = pTextDescriptionOther;
	}

	public String getDescriptionYou(){
		return /*time + " - " + */textDescriptionYou;
	}

	public String getDescriptionOther(){
		return /*time + " - " + */textDescriptionOther;
	}

	public String makeDescription(){
		return source + "" + action + "" + targets;
	}
	public GTime getTime() {
		return time;
	}

	public Actor getSource() {
		return source;
	}

	public String toString(){
		//return makeDescription();
		return getDescriptionOther() + " at " + position +" on " + time;
	}

	public Point getPosition() {
		return position;
	}
}