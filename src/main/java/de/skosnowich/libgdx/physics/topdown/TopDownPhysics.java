package de.skosnowich.libgdx.physics.topdown;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import de.skosnowich.libgdx.gameobjects.*;
import de.skosnowich.libgdx.physics.Physics;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class TopDownPhysics implements Physics
{
	private final float FIXED_TIMESTEP = 1f / 60f;

	private float remaining;

	@Override
	public void setup()
	{
		// not needed
	}

	@Override
	public void renderDebug(Matrix4 combinedCameraMatrix)
	{
		// not needed, is done by the Stage
	}

	@Override
	public void draw(Batch batch)
	{
		// not needed, is done by the Stage
	}

	@Override
	public void doStep(float delta)
	{
		remaining += delta;

		while (remaining >= FIXED_TIMESTEP)
		{
			doSingleStep();
			remaining -= FIXED_TIMESTEP;
		}
	}

	private void doSingleStep()
	{
		for (GameObject gameObject : GameObjects.getGameObjectsByTag(Tags.DYNAMIC))
		{
			move((DynamicGameObject) gameObject);
		}
	}

	private void move(DynamicGameObject gameObject)
	{
		Vector2 velocity = gameObject.getWorldVelocity();
		if (velocity.len2() <= 0)
		{
			return;
		}
		Vector2 newPosition = gameObject.getWorldPosition().add(velocity.scl(FIXED_TIMESTEP));

		Collision collision = checkForCollision(gameObject, newPosition);

		MinimumTranslationVector mtv = collision.getMinimumTranslationVector();
		int iterationCounts = 0;
		final int maxIterations = 100;
		while (collision.isOverlaps())
		{
			// after a high number of iterations, there is probably no way to fix the collision, so don't do any move
			if (iterationCounts >= maxIterations)
			{
				newPosition.set(gameObject.getWorldPosition());
				break;
			}

			Vector2 resolveCollisionVector = mtv.normal.scl(mtv.depth);
			resolveCollisionVector.limit(velocity.len() * FIXED_TIMESTEP);

			newPosition.add(resolveCollisionVector);

			collision = checkForCollision(gameObject, newPosition);
			mtv = collision.getMinimumTranslationVector();

			iterationCounts++;
		}
		gameObject.setWorldPosition(newPosition);
	}

	private Collision checkForCollision(GameObject gameObject, Vector2 newPosition)
	{
		Collision collision = new Collision();

		Rectangle rectangle = gameObject.getBoundingRectangleWithOffset(newPosition);
		Polygon[] polygons = gameObject.getBoundingPolygonsWithOffset(newPosition);

		if (rectangle == null)
		{
			return collision;
		}

		MinimumTranslationVector mtv = new MinimumTranslationVector();

		Set<String> ignoreTags = gameObject.getIgnoreCollisionWithTagSet();

		// loop through all collidable GameObjects
		for (GameObject other : GameObjects.getGameObjectsByTag(Tags.BOUNDING))
		{
			// check if other has tags, indicating to ignore collisions with that object
			if (!other.getTags().stream().anyMatch(ignoreTags::contains))
			{
				// don't do checks on colliders belonging to this object
				if (!other.equals(gameObject))
				{
					// if bounding rectangles overlap, do more precise checks
					Rectangle otherRectangle = other.getBoundingRectangleWithOffset(other.getWorldPosition());
					if (Intersector.overlaps(rectangle, otherRectangle))
					{
						// overlap every polygon belong to this object with every polygon belonging to the other object
						Polygon[] otherPolygons = other.getBoundingPolygonsWithOffset(other.getWorldPosition());
						for (Polygon otherPolygon : otherPolygons)
						{
							for (Polygon polygon : polygons)
							{
								// if one of those polygon-pairs overlap, return the Collision with all the collision-data
								// do the extra check on the mtv, because an overlap might be detected falsely (polygons lying exactly next to each other)
								if (Intersector.overlapConvexPolygons(polygon, otherPolygon, mtv) && !MathUtils.isEqual(mtv.depth, 0))
								{
									collision.setOverlaps(true);
									collision.setOverlappingPolygon(polygon);
									collision.setOverlappingOtherPolygon(otherPolygon);
									collision.setMinimumTranslationVector(mtv);
									return collision;
								}
							}
						}
					}
				}
			}
		}

		return collision;
	}

	public Set<GameObject> getGameObjectsAtPosition(Vector2 position, float tolerance, GameObject[] ignoredGameObjects)
	{
		Polygon polygon = ShapeUtils.getCircleShape(position.x, position.y, tolerance);

		Set<GameObject> results = getGameObjectsInPolygon(polygon, ignoredGameObjects);
		return results;
	}

	public Set<GameObject> getGameObjectsInPolygon(Polygon polygon, GameObject[] ignoredGameObjects) // TODO needs testing
	{
		List<GameObject> ignoredGameObjectsSet = Arrays.asList(ignoredGameObjects);

		Rectangle rectangle = polygon.getBoundingRectangle();

		Set<GameObject> results = new HashSet<>();
		// loop through all collidable GameObjects
		for (GameObject other : GameObjects.getGameObjectsByTag(Tags.BOUNDING))
		{
			// check if other is ignored
			if (!ignoredGameObjectsSet.contains(other))
			{
				// if bounding rectangles overlap, do more precise checks
				Rectangle otherRectangle = other.getBoundingRectangleWithOffset(other.getWorldPosition());
				if (Intersector.overlaps(rectangle, otherRectangle))
				{
					// overlap polygon with every polygon belonging to the other object
					Polygon[] otherPolygons = other.getBoundingPolygonsWithOffset(other.getWorldPosition());
					for (Polygon otherPolygon : otherPolygons)
					{
						MinimumTranslationVector mtv = new MinimumTranslationVector();
						// if one of those polygon-pairs overlap, add the GameObject to the result list
						// do the extra check on the mtv, because an overlap might be detected falsely (polygons lying exactly next to each other)
						if (Intersector.overlapConvexPolygons(polygon, otherPolygon, mtv) && !MathUtils.isEqual(mtv.depth, 0))
						{
							results.add(other);
						}
					}
				}
			}
		}
		return results;
	}

	public List<RayCastHit> rayCast(Vector2 startPos, Vector2 endPos)
	{
		return rayCast(startPos, endPos, null);
	}

	public List<RayCastHit> rayCast(Vector2 startPos, Vector2 endPos, GameObject[] ignoredGameObjects)
	{
		Vector2 direction = endPos.cpy().sub(startPos);

		List<RayCastHit> hits = new ArrayList<>();

		// loop through all collidable GameObjects
		for (GameObject gameObject : GameObjects.getGameObjectsByTag(Tags.BOUNDING))
		{
			// ignore specific gameobjects
			if (ignoredGameObjects == null || !Arrays.asList(ignoredGameObjects).contains(gameObject))
			{
				// // if bounding rectangles overlap, do more precise checks
				// Rectangle otherRectangle = other.getBoundingRectangleWithOffset(other.getWorldPosition());
				// if (Intersector.intersectSegmentPolygon(startPos, endPos, otherRectangle))
				// {
				// overlap every polygon belonging to the object with the ray
				Polygon[] polygons = gameObject.getBoundingPolygonsWithOffset(gameObject.getWorldPosition());
				for (Polygon polygon : polygons)
				{
					// if one of those polygons overlap, add the rayCastResult to the hit-list
					if (Intersector.intersectSegmentPolygon(startPos, endPos, polygon))
					{
						Vector2 point = ShapeUtils.getNearestIntersectionPoint(startPos, direction, polygon);
						float distance = point.cpy().sub(startPos).len();
						RayCastHit rayCastHit = new RayCastHit(point, distance, gameObject);
						hits.add(rayCastHit);
					}
					// }
				}
			}
		}

		Collections.sort(hits, Comparator.comparing(RayCastHit::getDistance));

		return hits;
	}
}
