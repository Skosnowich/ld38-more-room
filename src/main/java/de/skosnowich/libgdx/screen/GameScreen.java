package de.skosnowich.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.stage.GameStage;
import de.skosnowich.libgdx.stage.UIStage;

public class GameScreen implements Screen
{
	protected GameStage gameStage;
	protected UIStage uiStage;

	@Override
	public void show()
	{
		gameStage = Application.getInstance().getStageManager().get(GameStage.class);
		uiStage = Application.getInstance().getStageManager().get(UIStage.class);
		Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, gameStage));

		uiStage.clear();
		gameStage.clear();

		uiStage.setup();
		gameStage.setup();
	}

	@Override
	public void render(float delta)
	{
		delta = Math.min(delta, 0.1f); // cap delta so there is no tunneling through external influences

		gameStage.act(delta);
		uiStage.act(delta);

		gameStage.draw();
		uiStage.draw();

		// back to TitleSceen at ESC
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			Application.getInstance().setScreen(MainMenuScreen.class);
		}
	}

	@Override
	public void resize(int width, int height)
	{
		uiStage.getViewport().update(width, height, true);
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void hide()
	{
	}

	@Override
	public void dispose()
	{
		gameStage.dispose();
		uiStage.dispose();
	}

}
