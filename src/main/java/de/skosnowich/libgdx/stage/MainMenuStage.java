
package de.skosnowich.libgdx.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.screen.GameScreen;
import de.skosnowich.libgdx.screen.MainMenuScreen;
import de.skosnowich.libgdx.ui.ActionTextButton;

public class MainMenuStage extends Stage
{

	public MainMenuStage()
	{
		super();
		Skin skin = Assets.getInstance().getSkin();

		Texture backgroundTexture = getBackgroundTexture();
		if (backgroundTexture != null)
		{
			Image bg = new Image(backgroundTexture);
			bg.setBounds(0, 0, getWidth(), getHeight());
			addActor(bg);
		}

		Vector2 screenCenter = new Vector2(getViewport().getScreenWidth() / 2, getViewport().getScreenHeight() / 2);

		Label titleLabel = new Label(Application.getTitle(), skin, "font32", Color.WHITE);
		titleLabel.setPosition(screenCenter.x, screenCenter.y + 50, Align.center);
		addActor(titleLabel);

		TextButton startGameButton = new ActionTextButton("Start Game", () -> startGame());
		startGameButton.setPosition(screenCenter.x, screenCenter.y, Align.center);
		addActor(startGameButton);

		TextButton settingsGameButton = new ActionTextButton("Settings", () -> gotoSettingsStage());
		settingsGameButton.setPosition(screenCenter.x, screenCenter.y - 40, Align.center);
		addActor(settingsGameButton);

		TextButton exitGameButton = new ActionTextButton("Exit Game", () -> Gdx.app.exit());
		exitGameButton.setPosition(screenCenter.x, screenCenter.y - 80, Align.center);
		addActor(exitGameButton);

		Label versionLabel = new Label("Version: " + Application.getVersion(), skin);
		addActor(versionLabel);
	}

	protected Texture getBackgroundTexture()
	{
		return null;
	}

	private void gotoSettingsStage()
	{
		Application.getInstance().getScreenManager().get(MainMenuScreen.class)
				.setCurrentStage(Application.getInstance().getStageManager().get(SettingsStage.class));
	}

	protected void startGame()
	{
		Application.getInstance().setScreen(GameScreen.class);
	}

}
