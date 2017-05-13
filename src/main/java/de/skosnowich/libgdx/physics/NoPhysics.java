package de.skosnowich.libgdx.physics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

public class NoPhysics implements Physics
{
	@Override
	public void doStep(float delta)
	{
	}

	@Override
	public void renderDebug(Matrix4 combinedCameraMatrix)
	{
	}

	@Override
	public void setup()
	{
	}

	@Override
	public void draw(Batch batch)
	{
	}
}
