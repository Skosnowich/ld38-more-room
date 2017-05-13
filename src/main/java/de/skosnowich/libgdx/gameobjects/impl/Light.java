package de.skosnowich.libgdx.gameobjects.impl;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import de.skosnowich.libgdx.gameobjects.GameObject;

public class Light extends GameObject
{
	private Random random = new Random();

	private Color color;
	private float radius;
	private PositionalLight light;
	private Float coneDegree;

	private float flickerTime;
	private float flickerAmount;
	private boolean flickerRandom;

	private float time;
	private float lastRadius;
	private float nextRadius;

	public Light(float x, float y, Color color, float radius, float flickerTime, float flickerAmount, boolean flickerRandom)
	{
		this(x, y, color, radius, null, flickerTime, flickerAmount, flickerRandom, false);
	}

	public Light(float x, float y, Color color, float radius, float flickerTime, float flickerAmount, boolean flickerRandom, boolean slowStart)
	{
		this(x, y, color, radius, null, flickerTime, flickerAmount, flickerRandom, slowStart);
	}

	public Light(float x, float y, Color color, float radius, Float coneDegree, float flickerTime, float flickerAmount, boolean flickerRandom)
	{
		this(x, y, color, radius, coneDegree, flickerTime, flickerAmount, flickerRandom, false);
	}

	public Light(float x, float y, Color color, float radius, Float coneDegree, float flickerTime, float flickerAmount, boolean flickerRandom,
			boolean slowStart)
	{
		super("light", x, y);

		this.color = color;
		this.radius = radius;
		this.coneDegree = coneDegree;
		this.flickerTime = flickerTime;
		this.flickerAmount = flickerAmount;
		this.flickerRandom = flickerRandom;

		if (!slowStart)
		{
			time = flickerTime;
		}
	}

	@Override
	public void setup()
	{
		super.setup();

		if (coneDegree == null)
		{
			light = new PointLight(getStage().getRayHandler(), 32, color, radius, getWorldPosition().x, getWorldPosition().y);
		}
		else
		{
			light = new ConeLight(getStage().getRayHandler(), 32, color, radius, getWorldPosition().x, getWorldPosition().y,
					getWorldRotation(), coneDegree);
		}
		light.setXray(true);
		if (flickerRandom)
		{
			nextRadius = getRandomRadius();
		}
		else
		{
			nextRadius = radius - flickerAmount;
		}
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		light.setActive(isVisible());

		if (isVisible())
		{
			light.setColor(getColor());

			OrthographicCamera cam = getStage().getCamera();
			light.setPosition(getWorldPosition().x - cam.position.x, getWorldPosition().y - cam.position.y);
			if (coneDegree != null)
			{
				((ConeLight) light).setDirection(getWorldRotation());
			}

			time += delta;

			if (time >= flickerTime)
			{
				time = 0;
				lastRadius = nextRadius;
				if (flickerRandom)
				{
					nextRadius = getRandomRadius();
				}
				else
				{
					if (lastRadius < radius)
					{
						nextRadius = radius + flickerAmount;
					}
					else
					{
						nextRadius = radius - flickerAmount;
					}
				}
			}

			light.setDistance(MathUtils.lerp(lastRadius, nextRadius, time / flickerTime));
		}
	}

	private float getRandomRadius()
	{
		return random.nextFloat() * flickerAmount - flickerAmount / 2 + radius;
	}

	@Override
	public boolean remove()
	{
		light.remove();
		light.dispose();
		return super.remove();
	}

}
