package de.skosnowich.libgdx.physics.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class CompositeSprite extends Sprite
{
	private Array<Sprite> additionalSprites;

	public CompositeSprite(Sprite sprite)
	{
		super(sprite);
		additionalSprites = new Array<>();
	}

	public void addSprite(Sprite sprite)
	{
		additionalSprites.add(sprite);
	}

	@Override
	public void draw(Batch batch)
	{
		super.draw(batch);

		for (Sprite sprite : additionalSprites)
		{
			sprite.draw(batch);
		}
	}

	@Override
	public void draw(Batch batch, float alphaModulation)
	{
		super.draw(batch, alphaModulation);

		for (Sprite sprite : additionalSprites)
		{
			sprite.draw(batch, alphaModulation);
		}
	}

	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		for (Sprite sprite : additionalSprites)
		{
			sprite.setPosition(x, y);
		}
	}

	@Override
	public void setRotation(float degrees)
	{
		super.setRotation(degrees);
		for (Sprite sprite : additionalSprites)
		{
			sprite.setRotation(degrees);
		}
	}

	@Override
	public void setFlip(boolean x, boolean y)
	{
		super.setFlip(x, y);
		for (Sprite sprite : additionalSprites)
		{
			sprite.setFlip(x, y);
		}
	}

	@Override
	public void setAlpha(float a)
	{
		super.setAlpha(a);
		for (Sprite sprite : additionalSprites)
		{
			sprite.setAlpha(a);
		}
	}
}
