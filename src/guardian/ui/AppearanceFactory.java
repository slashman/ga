package guardian.ui;

import util.EngineException;

public interface AppearanceFactory {

	public Appearance createAppearance(String desc) throws EngineException;

}