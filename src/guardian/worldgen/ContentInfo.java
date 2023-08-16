package guardian.worldgen;

import util.*;

public class ContentInfo {
	private ContentDefinition contentDefinition;
	private Point position;

	public ContentDefinition getContentDefinition() {
		return contentDefinition;
	}

	public Point getPosition() {
		return position;
	}

	public ContentInfo(Point pPosition, ContentDefinition pContent){
		position = pPosition;
		contentDefinition = pContent;
	}
}