package de.skosnowich.libgdx.manager;

import com.badlogic.gdx.Screen;

import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.screen.GameScreen;
import de.skosnowich.libgdx.screen.MainMenuScreen;

/**
 * @see Application#setScreen(Class)
 */
public class ScreenManager extends AbstractCacheManager<Screen>
{

	public ScreenManager()
	{
		put(GameScreen.class, new GameScreen());
		put(MainMenuScreen.class, new MainMenuScreen());
	}

}
