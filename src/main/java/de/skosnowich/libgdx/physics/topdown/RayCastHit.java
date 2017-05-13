package de.skosnowich.libgdx.physics.topdown;

import com.badlogic.gdx.math.Vector2;

import de.skosnowich.libgdx.gameobjects.GameObject;

public class RayCastHit
{
	private Vector2 point;
	private float distance;
	private GameObject gameObject;

	public RayCastHit(Vector2 point, float distance, GameObject gameObject)
	{
		super();
		this.point = point;
		this.distance = distance;
		this.gameObject = gameObject;
	}

	public Vector2 getPoint()
	{
		return point;
	}

	public float getDistance()
	{
		return distance;
	}

	public GameObject getGameObject()
	{
		return gameObject;
	}

	@Override
	public String toString()
	{
		return String.format("RayCastResult [point=%s, distance=%s, gameObject=%s]", point, distance, gameObject);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(distance);
		result = prime * result + ((gameObject == null) ? 0 : gameObject.hashCode());
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof RayCastHit))
		{
			return false;
		}
		RayCastHit other = (RayCastHit) obj;
		if (Float.floatToIntBits(distance) != Float.floatToIntBits(other.distance))
		{
			return false;
		}
		if (gameObject == null)
		{
			if (other.gameObject != null)
			{
				return false;
			}
		}
		else if (!gameObject.equals(other.gameObject))
		{
			return false;
		}
		if (point == null)
		{
			if (other.point != null)
			{
				return false;
			}
		}
		else if (!point.equals(other.point))
		{
			return false;
		}
		return true;
	}

}
