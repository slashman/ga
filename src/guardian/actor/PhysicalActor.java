package guardian.actor;

import util.Point;
import engine.*;
import guardian.ui.*;

public abstract class PhysicalActor extends Actor{
	protected Point position;
	private Appearance aAppearance;

	public Appearance getAppearance() {
		return aAppearance;
	}

	public void setAppearance(Appearance value) {
		aAppearance = value;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point value) {
		position = value;
	}

	public void setPosition(int x, int y, int z) {
		position = new Point(x,y,z);
	}
}