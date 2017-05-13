package de.skosnowich.libgdx.physics.box2d;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class PhysicsActor
{
	protected Sprite sprite;

	protected Body body;
	protected Map<Fixture, FixtureDef> fixtureMap;

	private String name;

	public PhysicsActor(String name)
	{
		this.name = name;
		fixtureMap = new HashMap<>();
	}

	public Sprite getSprite()
	{
		return sprite;
	}

	public Body getBody()
	{
		return body;
	}

	public void setBody(Body body)
	{
		this.body = body;
	}

	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
	}

	public void beginContact(PhysicsActor other, Fixture ownFixture, Fixture otherFixture)
	{
	}

	public void endContact(PhysicsActor other, Fixture ownFixture, Fixture otherFixture)
	{
	}

	public String getName()
	{
		return name;
	}

	public void act(float delta)
	{

	}

	public void putFixture(Fixture fixture, FixtureDef fixtureDef)
	{
		fixtureMap.put(fixture, fixtureDef);
	}

	public void updateSprite()
	{
		sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}

	public void draw(Batch batch)
	{
		if (sprite != null)
		{
			updateSprite();
			sprite.draw(batch);
		}
	}

	@Override
	public String toString()
	{
		return String.format("PhysicsActor [name=%s, body.position=%s]", name, body.getPosition());
	}

}
