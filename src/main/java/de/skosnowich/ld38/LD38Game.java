package de.skosnowich.ld38;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.skosnowich.ld38.screen.MyGameScreen;
import de.skosnowich.ld38.stage.MyGameStage;
import de.skosnowich.ld38.stage.MyMainMenuStage;
import de.skosnowich.ld38.stage.MyUiStage;
import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.screen.GameScreen;
import de.skosnowich.libgdx.screen.MainMenuScreen;
import de.skosnowich.libgdx.stage.GameStage;
import de.skosnowich.libgdx.stage.MainMenuStage;
import de.skosnowich.libgdx.stage.UIStage;

public class LD38Game extends Application
{

	public static void main(String[] args) throws IOException
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		if (args.length >= 2)
		{
			config.width = Integer.parseInt(args[0]);
			config.height = Integer.parseInt(args[1]);
		}
		else
		{
			config.width = 1280;
			config.height = 720;
		}

		config.resizable = false;

		LD38Game application = new LD38Game();

		application.start(config);
	}

	@Override
	public void create()
	{
		super.create();
		getScreenManager().put(GameScreen.class, new MyGameScreen());
		getStageManager().put(MainMenuStage.class, new MyMainMenuStage());
		getStageManager().put(GameStage.class, new MyGameStage());
		getStageManager().put(UIStage.class, new MyUiStage());

		Assets assets = Assets.getInstance();

		assets.addAssetsFromClass(AssetConstants.class);

		assets.finishLoading();

		setScreen(MainMenuScreen.class);
	}
}
