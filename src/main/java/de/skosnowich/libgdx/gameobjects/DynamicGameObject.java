package de.skosnowich.libgdx.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class DynamicGameObject extends GameObject
{
	private Animation<TextureRegion> currentAnimation;

	protected Animation<TextureRegion> idleAnimation;
	protected Animation<TextureRegion> walkAnimation;

	private Vector2 velocity;

	private float speedModifier;

	public DynamicGameObject(String name, float x, float y, float speed, boolean lockSpriteTo8Directions)
	{
		super(name, x, y, lockSpriteTo8Directions);
		addTag(Tags.DYNAMIC);

		velocity = new Vector2();
		setSpeedModifier(speed);
	}

	public DynamicGameObject(String name, float x, float y, float speed)
	{
		this(name, x, y, speed, false);
	}

	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity;
	}

	public Vector2 getWorldVelocity()
	{
		Vector2 worldVelocity = velocity.cpy();

		Actor actor = this;
		Group parent = actor.getParent();
		while (parent != null)
		{
			float parentRotation = parent.getRotation();
			worldVelocity.rotate(parentRotation);

			actor = parent;
			parent = actor.getParent();
		}

		return worldVelocity;
	}

	@Override
	protected void drawCustomDebug(ShapeRenderer shapes)
	{
		super.drawCustomDebug(shapes);

		// draw velocity
		shapes.set(ShapeType.Line);
		shapes.setColor(Color.MAGENTA);
		shapes.line(getLocalPosition(), getLocalPosition().add(velocity));
	}

	@Override
	protected void updateSprite()
	{
		super.updateSprite();

		if (walkAnimation != null && idleAnimation != null)
		{
			sprite.setTexture(currentAnimation.getKeyFrame(animationTime, true).getTexture());

			if (velocity.len2() > 0)
			{
				currentAnimation = walkAnimation;
			}
			else
			{
				currentAnimation = idleAnimation;
			}
		}
	}

	protected void moveForward()
	{
		Vector2 direction = new Vector2(1, 0).rotate(getRotation());
		setVelocity(direction.scl(getSpeedModifier()));
	}

	protected void stop()
	{
		setVelocity(Vector2.Zero);
	}

	protected void lookAt(Vector2 target)
	{
		float targetAngle = target.cpy().sub(getWorldPosition()).angle();
		setRotation(targetAngle);
	}

	public void setSpeedModifier(float speed)
	{
		speedModifier = speed;
	}

	public float getSpeedModifier()
	{
		return speedModifier;
	}

	public void setCurrentAnimation(Animation<TextureRegion> currentAnimation)
	{
		this.currentAnimation = currentAnimation;
	}

	public void setIdleAnimation(Animation<TextureRegion> idleAnimation)
	{
		this.idleAnimation = idleAnimation;
	}

	public void setWalkAnimation(Animation<TextureRegion> walkAnimation)
	{
		this.walkAnimation = walkAnimation;
	}

}
