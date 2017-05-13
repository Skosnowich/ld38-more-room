package de.skosnowich.libgdx.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class ShapeUtils
{
	private static final int CIRCLE_SEGMENTS = 32;

	static Polygon getRectangleShape(float centerX, float centerY, float width, float height, float cornerDistance)
	{
		if (cornerDistance < 0)
		{
			throw new IllegalArgumentException("cornerDistance must be >= 0");
		}
		boolean nooked = cornerDistance > 0;

		float leftSide = -width / 2.0f;
		float bottomSide = -height / 2.0f;
		float rightSide = width / 2.0f;
		float topSide = height / 2.0f;

		List<Float> verticeList = new ArrayList<>();
		verticeList.add(leftSide + cornerDistance); // bottom-left-x
		verticeList.add(bottomSide); // bottom-left-y
		verticeList.add(rightSide - cornerDistance); // bottom-right-x
		verticeList.add(bottomSide); // bottom-right-y
		if (nooked)
		{
			verticeList.add(rightSide); // bottom-right-x
			verticeList.add(bottomSide + cornerDistance); // bottom-right-y
			verticeList.add(rightSide); // top-right-x
			verticeList.add(topSide - cornerDistance); // top-right-y
		}
		verticeList.add(rightSide - cornerDistance); // top-right-x
		verticeList.add(topSide); // top-right-y
		verticeList.add(leftSide + cornerDistance); // top-left-x
		verticeList.add(topSide); // top-left-y
		if (nooked)
		{
			verticeList.add(leftSide); // top-left-x
			verticeList.add(topSide - cornerDistance); // top-left-y
			verticeList.add(leftSide); // bottom-left-x
			verticeList.add(bottomSide + cornerDistance); // bottom-left-y
		}

		float[] vertices = new float[verticeList.size()];
		for (int i = 0; i < vertices.length; i++)
		{
			vertices[i] = verticeList.get(i);
		}
		Polygon polygon = new Polygon(vertices);
		polygon.setOrigin(0, 0);
		polygon.setPosition(centerX, centerY);

		return polygon;
	}

	public static Polygon getRectangleShape(float centerX, float centerY, float width, float height)
	{
		return getRectangleShape(centerX, centerY, width, height, 0);
	}

	public static Polygon getCircleShape(float centerX, float centerY, float radius)
	{
		return getCircleShape(centerX, centerY, radius, CIRCLE_SEGMENTS);
	}

	static Polygon getCircleShape(float centerX, float centerY, float radius, int segments)
	{
		float[] vertices = new float[segments * 2];

		double angleSteps = 2 * Math.PI / segments;

		for (int i = 0; i < vertices.length; i += 2)
		{
			float angle = (float) (i / 2 * angleSteps);
			vertices[i] = (float) (radius * Math.cos(angle));
			vertices[i + 1] = (float) (radius * Math.sin(angle));
		}

		Polygon polygon = new Polygon(vertices);
		polygon.setOrigin(0, 0);
		polygon.setPosition(centerX, centerY);

		return polygon;
	}

	public static Polygon copyPolygon(Polygon polygon)
	{
		Polygon copy = new Polygon(polygon.getVertices());

		copy.setPosition(polygon.getX(), polygon.getY());
		copy.setScale(polygon.getScaleX(), polygon.getScaleY());
		copy.setOrigin(polygon.getOriginX(), polygon.getOriginY());
		copy.setRotation(polygon.getRotation());

		return copy;
	}

	public static Vector2 getNearestIntersectionPoint(Vector2 origin, Vector2 direction, Polygon polygon)
	{
		List<Vector2> intersections = new ArrayList<>();
		Vector2 destination = origin.cpy().add(direction);

		intersections.addAll(polygonSegmentIntersections(origin, destination, polygon));

		if (intersections.size() == 0)
		{
			return null;
		}
		else if (intersections.size() == 1)
		{
			return intersections.get(0);
		}
		else
		{
			float lowestDistance = Float.MAX_VALUE;
			Vector2 nearestIntersection = new Vector2();

			for (Vector2 i : intersections)
			{
				float distance = Vector2.dst2(origin.x, origin.y, i.x, i.y);

				if (distance < lowestDistance)
				{
					lowestDistance = distance;
					nearestIntersection = i;
				}
			}

			return nearestIntersection;
		}
	}

	private static List<Vector2> polygonSegmentIntersections(Vector2 origin, Vector2 destination, Polygon polygon)
	{
		Vector2 intersection = new Vector2();
		Vector2 startOther = new Vector2();
		Vector2 endOther = new Vector2();

		List<Vector2> intersections = new ArrayList<>();

		float[] vertices = polygon.getTransformedVertices();
		for (int i = 0; i < vertices.length; i += 2)
		{
			startOther.set(vertices[i], vertices[i + 1]);
			if (i + 2 == vertices.length)
			{
				endOther.set(vertices[0], vertices[1]);
			}
			else
			{
				endOther.set(vertices[i + 2], vertices[i + 3]);
			}
			if (Intersector.intersectSegments(origin, destination, startOther, endOther, intersection))
			{
				intersections.add(intersection.cpy());
			}
		}
		return intersections;
	}
}
