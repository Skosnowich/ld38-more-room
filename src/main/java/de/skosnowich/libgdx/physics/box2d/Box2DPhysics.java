package de.skosnowich.libgdx.physics.box2d;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.libgdx.physics.Physics;

public class Box2DPhysics implements Physics
{

	public World world;
	private Box2DDebugRenderer debugRenderer;
	private float accumulator = 0;

	private Stack<Body> bodiesMarkedForDeletion;

	public void setup()
	{
		Box2D.init();

		bodiesMarkedForDeletion = new Stack<>();

		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();

		world.setContactListener(new ContactListenerImpl());
	}

	public void doStep(float delta)
	{
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		float fixedStep = 1 / 60f;
		while (accumulator >= fixedStep)
		{
			world.step(fixedStep, 6, 2);
			accumulator -= fixedStep;
		}

		// act all Actors
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for (Body b : bodies)
		{
			Object userData = b.getUserData();
			if (userData != null && userData instanceof PhysicsActor)
			{
				((PhysicsActor) userData).act(delta);
			}
		}

		while (!bodiesMarkedForDeletion.isEmpty())
		{
			world.destroyBody(bodiesMarkedForDeletion.pop());
		}

	}

	public void renderDebug(Matrix4 combinedCameraMatrix)
	{
		debugRenderer.setDrawVelocities(true);
		debugRenderer.render(world, combinedCameraMatrix);
	}

	/**
	 * Creates a simple {@link PhysicsActor} with a boundingBox as big as the sprite
	 *
	 * @param physicsActor
	 * @param x
	 *            x-Coordinate in metres
	 * @param y
	 *            y-Coordinate in metres
	 * @param width
	 *            width in metres
	 * @param height
	 *            height in metres
	 * @param bodyType
	 * @return
	 */
	public void addPhysicsActor(PhysicsActor physicsActor, float x, float y, float width, float height, BodyType bodyType)
	{
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width / 2.0f, height / 2.0f);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(x, y);
		bodyDef.fixedRotation = true;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxShape;
		fixtureDef.density = .5f;

		addPhysicsActor(physicsActor, bodyDef, fixtureDef);
	}

	public void addPhysicsActor(PhysicsActor physicsActor, BodyDef bodyDef, FixtureDef... fixtureDefs)
	{
		Sprite sprite = physicsActor.getSprite();
		if (sprite != null)
		{
			physicsActor.setSprite(sprite);
			sprite.setOrigin(sprite.getWidth() / 2.0f, sprite.getHeight() / 2.0f);
		}

		Body body = world.createBody(bodyDef);

		for (FixtureDef fixtureDef : fixtureDefs)
		{
			Fixture fixture = body.createFixture(fixtureDef);
			fixture.setUserData(physicsActor);
			physicsActor.putFixture(fixture, fixtureDef);

			fixtureDef.shape.dispose();
		}

		physicsActor.setBody(body);

		body.setUserData(physicsActor);
	}

	public void draw(Batch batch)
	{
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		for (Body body : bodies)
		{
			Object userData = body.getUserData();
			if (userData != null && userData instanceof PhysicsActor)
			{
				PhysicsActor actor = (PhysicsActor) userData;
				actor.draw(batch);
			}
		}
	}

	public void destroyPhysicsActor(PhysicsActor actor)
	{
		bodiesMarkedForDeletion.push(actor.getBody());
	}

}
