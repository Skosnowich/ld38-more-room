package de.skosnowich.libgdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

public class SideviewPhysics extends Box2DPhysics
{
	public SideviewPhysics()
	{
		super();
		world.setGravity(new Vector2(0, -9.81f));

		// FIXE muss in setup rein
	}
}
