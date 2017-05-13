package de.skosnowich.libgdx.stage;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.skosnowich.libgdx.physics.Physics;

public abstract class GameStage<T extends Physics> extends Stage
{
	private boolean debug = false;

	protected T physics;

	private Set<Integer> keyPressed;
	private Set<Integer> keyJustPressed;

	public abstract void setup();

	protected void setup(T physics, float viewportWidth, float viewportHeight)
	{
		this.physics = physics;
		keyPressed = new HashSet<>();
		keyJustPressed = new HashSet<>();
		physics.setup();

		OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
		getViewport().setCamera(camera);
		camera.translate(camera.viewportWidth / 2.0f, camera.viewportHeight / 2.0f);
	};

	@Override
	public void act(float delta)
	{
		super.act(delta);

		physics.doStep(delta);
	}

	@Override
	public void draw()
	{
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		customBackgroundDraw();

		super.draw();

		customDraw();

		getBatch().begin();
		physics.draw(getBatch());
		getBatch().end();

		if (debug)
		{
			physics.renderDebug(getCamera().combined);
		}
	}

	protected void customBackgroundDraw()
	{
	}

	protected void customDraw()
	{
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public T getPhysics()
	{
		return physics;
	}

	@Override
	public OrthographicCamera getCamera()
	{
		return (OrthographicCamera) super.getCamera();
	}

	public boolean isDebug()
	{
		return debug;
	}

	@Override
	public boolean keyDown(int key)
	{
		keyPressed.add(key);
		return true;
	}

	@Override
	public boolean keyUp(int key)
	{
		keyPressed.remove(key);
		keyJustPressed.add(key);
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		keyPressed.add(button);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		keyPressed.remove(button);
		keyJustPressed.add(button);
		return true;
	}

	public boolean isKeyPressed(int key)
	{
		return keyPressed.contains(key);
	}

	public boolean isKeyJustPressed(int key)
	{
		return keyJustPressed.remove(key);
	}

	public boolean isButtonPressed(int button)
	{
		return keyPressed.contains(button);
	}

	public boolean isButtonJustPressed(int key)
	{
		return keyJustPressed.remove(key);
	}

	public int getMouseX()
	{
		return Gdx.input.getX();
	}

	public int getMouseY()
	{
		return Gdx.input.getY();
	}

	public Vector2 getMouseDelta()
	{
		return new Vector2(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
	}
}
