package de.skosnowich.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.stage.MainMenuStage;
import de.skosnowich.libgdx.stage.SettingsStage;

public class MainMenuScreen implements Screen
{
	protected Stage currentStage;

	@Override
	public void show()
	{
		setCurrentStage(Application.getInstance().getStageManager().get(MainMenuStage.class));
	}

	public void setCurrentStage(Stage stage)
	{
		if (currentStage != null)
		{
			stage.getViewport().update(currentStage.getViewport().getScreenWidth(), currentStage.getViewport().getScreenHeight(), true);
		}
		currentStage = stage;
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		currentStage.act(delta);
		currentStage.draw();

		// back to TitleSceen or Exit at ESC
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			if (currentStage instanceof MainMenuStage)
			{
				Gdx.app.exit();
			}
			else
			{
				Application.getInstance().setScreen(MainMenuScreen.class);
			}
		}
	}

	@Override
	public void resize(int width, int height)
	{
		currentStage.getViewport().update(width, height, true);
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
	}
}
