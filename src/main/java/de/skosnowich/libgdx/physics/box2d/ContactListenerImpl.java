package de.skosnowich.libgdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;

public class ContactListenerImpl implements ContactListener
{
	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
	}

	@Override
	public void endContact(Contact contact)
	{
		Object userDataA = contact.getFixtureA().getUserData();
		Object userDataB = contact.getFixtureB().getUserData();

		if (userDataA != null && userDataA instanceof PhysicsActor &&
				userDataB != null && userDataB instanceof PhysicsActor)
		{
			((PhysicsActor) userDataA).endContact((PhysicsActor) userDataB, contact.getFixtureA(), contact.getFixtureB());
			((PhysicsActor) userDataB).endContact((PhysicsActor) userDataA, contact.getFixtureB(), contact.getFixtureA());
		}
	}

	@Override
	public void beginContact(Contact contact)
	{
		Object userDataA = contact.getFixtureA().getUserData();
		Object userDataB = contact.getFixtureB().getUserData();

		if (userDataA != null && userDataA instanceof PhysicsActor &&
				userDataB != null && userDataB instanceof PhysicsActor)
		{
			((PhysicsActor) userDataA).beginContact((PhysicsActor) userDataB, contact.getFixtureA(), contact.getFixtureB());
			((PhysicsActor) userDataB).beginContact((PhysicsActor) userDataA, contact.getFixtureB(), contact.getFixtureA());
		}
	}
}
