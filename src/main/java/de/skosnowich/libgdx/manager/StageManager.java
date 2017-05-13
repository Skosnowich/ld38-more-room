package de.skosnowich.libgdx.manager;

import com.badlogic.gdx.scenes.scene2d.Stage;

import de.skosnowich.libgdx.stage.MainMenuStage;
import de.skosnowich.libgdx.stage.SettingsStage;
import de.skosnowich.libgdx.stage.UIStage;

public class StageManager extends AbstractCacheManager<Stage>
{

	public StageManager()
	{
		super();
		put(UIStage.class, new UIStage());
		put(MainMenuStage.class, new MainMenuStage());
		put(SettingsStage.class, new SettingsStage());
	}

}
