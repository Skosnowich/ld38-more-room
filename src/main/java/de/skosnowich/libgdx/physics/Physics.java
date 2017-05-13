package de.skosnowich.libgdx.physics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

public interface Physics
{

	void setup();

	void doStep(float delta);

	void draw(Batch batch);

	void renderDebug(Matrix4 combined);

}
