package de.skosnowich.libgdx.gameobjects.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.gameobjects.GameObject;

public class ParticleEmitter extends GameObject
{

	private ParticleEffect particleEffect;
	private boolean looping;

	public ParticleEmitter(float x, float y, String particleName, boolean looping)
	{
		super("particle-emitter", x, y);
		this.looping = looping;

		particleEffect = new ParticleEffect(Assets.getInstance().getParticle(particleName));
		particleEffect.setPosition(x, y);
		particleEffect.start();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		particleEffect.update(delta);
		if (particleEffect.isComplete())
		{
			if (looping)
			{
				particleEffect.reset();
			}
			else
			{
				remove();
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		particleEffect.draw(batch);
	}

	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		if (particleEffect != null)
		{
			particleEffect.setPosition(x, y);
		}
	}

	@Override
	public boolean remove()
	{
		if (particleEffect != null)
		{
			particleEffect.dispose();
		}
		return super.remove();
	}

	@Override
	public void setColor(Color color)
	{
		setColor(color.r, color.g, color.b, color.a);
	}

	@Override
	public void setColor(float r, float g, float b, float a)
	{
		super.setColor(r, g, b, a);

		for (com.badlogic.gdx.graphics.g2d.ParticleEmitter particleEmitter : particleEffect.getEmitters())
		{
			particleEmitter.getTint().setColors(new float[] { r, g, b });
		}
	}
}
