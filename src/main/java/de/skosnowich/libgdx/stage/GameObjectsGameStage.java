package de.skosnowich.libgdx.stage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import de.skosnowich.libgdx.gameobjects.GameObject;
import de.skosnowich.libgdx.gameobjects.GameObjects;
import de.skosnowich.libgdx.physics.Physics;

public abstract class GameObjectsGameStage<T extends Physics> extends GameStage<T>
{
	private RayHandler rayHandler;

	@Override
	protected void setup(T physics, float viewportWidth, float viewportHeight)
	{
		super.setup(physics, viewportWidth, viewportHeight);

		World world = new World(new Vector2(0, 0), false);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0, 1f);
		rayHandler.setCombinedMatrix(getCamera().combined, 0, 0, viewportWidth, viewportHeight);

		setRayHandler(rayHandler);

		GameObjects.init();
	};

	public void addActor(GameObject gameObject)
	{
		super.addActor(gameObject);
		gameObject.setup();
	}

	@Override
	public void draw()
	{
		super.draw();

		if (rayHandler != null)
		{
			rayHandler.updateAndRender();
		}
	}

	public RayHandler getRayHandler()
	{
		return rayHandler;
	}

	public void setRayHandler(RayHandler rayHandler)
	{
		this.rayHandler = rayHandler;
	}
}
