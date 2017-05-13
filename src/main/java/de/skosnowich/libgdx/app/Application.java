package de.skosnowich.libgdx.app;

import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.skosnowich.libgdx.manager.ScreenManager;
import de.skosnowich.libgdx.manager.StageManager;

public class Application extends Game
{
	private static Application instance;

	private static String applicationTitle = null;
	private static String applicationVersion = null;

	private ScreenManager screenManager;
	private StageManager stageManager;

	public static Application getInstance()
	{
		return instance;
	}

	public static String getVersion()
	{
		return applicationVersion;
	}

	public static String getTitle()
	{
		return applicationTitle;
	}

	public Application()
	{
		instance = this;
	}

	/**
	 * start the application with default configuration.
	 *
	 * @throws IOException
	 */
	public void start() throws IOException
	{
		start(createConfig());
	}

	/**
	 * start the application with given configuration.
	 *
	 * @param configuration
	 * @throws IOException
	 */
	public void start(LwjglApplicationConfiguration configuration) throws IOException
	{
		loadProjectProperties();

		new LwjglApplication(instance, configuration);
	}

	/**
	 * Create the config needed to run the application
	 *
	 * @return config
	 */
	protected LwjglApplicationConfiguration createConfig()
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		return config;
	}

	/**
	 * Load the title and version of the application from Maven-properties
	 *
	 * @throws IOException
	 */
	protected void loadProjectProperties() throws IOException
	{
		final Properties properties = new Properties();
		properties.load(Application.class.getClassLoader().getResourceAsStream("project.properties"));
		applicationVersion = properties.getProperty("version");
		applicationTitle = properties.getProperty("name");
	}

	@Override
	public void create()
	{
		screenManager = new ScreenManager();
		stageManager = new StageManager();
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
	}

	@Override
	public void render()
	{
		super.render();

		Gdx.graphics.setTitle(applicationTitle + " - FPS: " + Gdx.graphics.getFramesPerSecond());
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
	public void dispose()
	{
	}

	@Override
	public void setScreen(Screen screen)
	{
		throw new IllegalAccessError("use setScreen(Class<? extends Screen> screenType) instead");
	}

	/**
	 * Instead of an instance of a {@link Screen} the type of the {@link Screen} is demanded
	 *
	 * @see com.badlogic.gdx.Game#setScreen(com.badlogic.gdx.Screen)
	 *
	 * @param screenType
	 */
	public void setScreen(Class<? extends Screen> screenType)
	{
		Screen screenInstance = screenManager.get(screenType);
		super.setScreen(screenInstance);
	}

	public ScreenManager getScreenManager()
	{
		return screenManager;
	}

	public StageManager getStageManager()
	{
		return stageManager;
	}
}
