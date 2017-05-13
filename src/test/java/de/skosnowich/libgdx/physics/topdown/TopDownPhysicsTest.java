package de.skosnowich.libgdx.physics.topdown;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.libgdx.gameobjects.DynamicGameObject;
import de.skosnowich.libgdx.gameobjects.GameObject;
import de.skosnowich.libgdx.gameobjects.GameObjects;
import de.skosnowich.libgdx.gameobjects.impl.TestDynamicGameObject;
import de.skosnowich.libgdx.gameobjects.impl.TestGameObject;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class TopDownPhysicsTest
{
	private static final float FLOAT_DELTA = 0.0005f;

	private TopDownPhysics physics;
	private DynamicGameObject gameObject;

	@Before
	public void beforeTest()
	{
		GameObjects.init();

		gameObject = new TestDynamicGameObject(0, 0);
		physics = new TopDownPhysics();
		physics.setup();
	}

	@Test
	public void testMove() throws Exception
	{
		gameObject.setWorldPosition(5, 5);
		Vector2 velocity = new Vector2(2, -1);
		gameObject.setVelocity(velocity);

		physics.doStep(0.2f);

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(5.4f, 4.8f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_Child() throws Exception
	{
		gameObject.setWorldPosition(5, 5);
		TestDynamicGameObject child = new TestDynamicGameObject(1, 3);
		Vector2 velocity = new Vector2(2, -1);
		child.setVelocity(velocity);
		gameObject.addActor(child);

		physics.doStep(0.2f);

		Vector2 actualPosition = child.getWorldPosition();
		Vector2 expectedPosition = new Vector2(6.4f, 7.8f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_ChildParentRotated() throws Exception
	{
		gameObject.setWorldPosition(5, 5);
		gameObject.setRotation(90);
		TestDynamicGameObject child = new TestDynamicGameObject(1, 3);
		Vector2 velocity = new Vector2(2, -1);
		child.setVelocity(velocity);
		gameObject.addActor(child);

		physics.doStep(0.2f);

		Vector2 actualPosition = child.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.2f, 6.4f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_MultipleSteps() throws Exception
	{
		gameObject.setWorldPosition(5, 5);
		Vector2 velocity = new Vector2(2, -1);
		gameObject.setVelocity(velocity);

		physics.doStep(0.2f);
		physics.doStep(0.25f);
		physics.doStep(0.15f);

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(6.2f, 4.4f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_Collision() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		Vector2 velocity = new Vector2(-2, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.5f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_CollisionCorner() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(1.5f, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 6) });
		GameObject obstacle2 = new TestDynamicGameObject(3, 1.5f);
		obstacle2.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 4, 1) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		Vector2 velocity = new Vector2(-2, -2);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.5f, 2.5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_BigTimeSteps() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		Vector2 velocity = new Vector2(-2, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 3; i++)
		{
			physics.doStep(5.00f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.5f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_HighVelocity() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		Vector2 velocity = new Vector2(-3f, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.5f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	@Ignore // can happen, needs to be considered in game design...
	public void testMove_EnormousVelocity() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, .5f, 4) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		Vector2 velocity = new Vector2(-200f, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.25f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_CollisionDiagonalMovement() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(4, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 1f) });
		Vector2 velocity = new Vector2(-1, -1);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		float expectedXPosition = 3f;
		float expectedLessThanYPosition = 3.9f;
		assertEquals(expectedXPosition, actualPosition.x, FLOAT_DELTA);
		assertTrue(expectedLessThanYPosition > actualPosition.y);
	}

	@Test(timeout = 1000)
	public void testMove_CollisionTwoCirclesStuckOnWalls() throws Exception
	{
		DynamicGameObject circle1 = new TestDynamicGameObject(2.5f, 1);
		circle1.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, .5f) });
		DynamicGameObject circle2 = new TestDynamicGameObject(2.5f, 2);
		circle2.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, .5f) });

		GameObject wall = new TestGameObject(2.5f, 0);
		wall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 5, 1) });
		GameObject leftWall = new TestGameObject(1.5f, 2);
		leftWall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 4) });
		GameObject rightWall = new TestGameObject(3.5f, 2);
		rightWall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 4) });

		circle1.setVelocity(new Vector2(0, 1));
		circle2.setVelocity(new Vector2(0, -1));

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition1 = circle1.getWorldPosition();
		Vector2 expectedPosition1 = new Vector2(2.5f, 1);
		assertEquals(expectedPosition1.x, actualPosition1.x, FLOAT_DELTA);
		assertEquals(expectedPosition1.y, actualPosition1.y, FLOAT_DELTA);

		Vector2 actualPosition2 = circle2.getWorldPosition();
		Vector2 expectedPosition2 = new Vector2(2.5f, 2);
		assertEquals(expectedPosition2.x, actualPosition2.x, FLOAT_DELTA);
		assertEquals(expectedPosition2.y, actualPosition2.y, FLOAT_DELTA);
	}

	@Test(timeout = 1000)
	public void testMove_CollisionThreeCirclesStuckOnWalls() throws Exception
	{
		DynamicGameObject circle1 = new TestDynamicGameObject(2.5f, 1);
		circle1.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, .5f) });
		DynamicGameObject circle2 = new TestDynamicGameObject(2.01f, 1.84f);
		circle2.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, .5f) });
		DynamicGameObject circle3 = new TestDynamicGameObject(2.99f, 1.84f);
		circle3.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, .5f) });

		GameObject wall = new TestGameObject(2.5f, 0);
		wall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 5, 1) });
		GameObject leftWall = new TestGameObject(1f, 2);
		leftWall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 4) });
		GameObject rightWall = new TestGameObject(4f, 2);
		rightWall.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 4) });

		circle1.setVelocity(new Vector2(0, 1));
		circle2.setVelocity(new Vector2(1, -1));
		circle3.setVelocity(new Vector2(-1, -1));

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition1 = circle1.getWorldPosition();
		Vector2 expectedPosition1 = new Vector2(2.5f, 1);
		assertEquals(expectedPosition1.x, actualPosition1.x, FLOAT_DELTA);
		assertEquals(expectedPosition1.y, actualPosition1.y, FLOAT_DELTA);

		Vector2 actualPosition2 = circle2.getWorldPosition();
		Vector2 expectedPosition2 = new Vector2(2.01f, 1.84f);
		assertEquals(expectedPosition2.x, actualPosition2.x, FLOAT_DELTA);
		assertEquals(expectedPosition2.y, actualPosition2.y, FLOAT_DELTA);

		Vector2 actualPosition3 = circle3.getWorldPosition();
		Vector2 expectedPosition3 = new Vector2(2.99f, 1.84f);
		assertEquals(expectedPosition3.x, actualPosition3.x, FLOAT_DELTA);
		assertEquals(expectedPosition3.y, actualPosition3.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_CollisionRectangle() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 0.5f, 2) });
		Vector2 velocity = new Vector2(-2, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.25f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_CollisionRotatedRectangle() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });

		gameObject.setWorldPosition(5, 5);
		Polygon rectangleShape = ShapeUtils.getRectangleShape(0, 0, 1, 1);
		rectangleShape.rotate(45);
		gameObject.setBoundingPolygons(new Polygon[] { rectangleShape });
		Vector2 velocity = new Vector2(-2, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 40; i++)
		{
			physics.doStep(0.05f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(2.7071067811865475f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testMove_IgnoreCollision() throws Exception
	{
		GameObject obstacle = new TestDynamicGameObject(2, 5);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(-0.5f, 0, 1, 4) });
		obstacle.addTag("ignoreme");

		gameObject.setWorldPosition(5, 5);
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 0.5f) });
		gameObject.addIgnoreCollisionWithTagSet("ignoreme");
		Vector2 velocity = new Vector2(-2, 0);
		gameObject.setVelocity(velocity);

		for (int i = 0; i < 20; i++)
		{
			physics.doStep(0.1f);
		}

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(1f, 5f);
		assertEquals(expectedPosition.x, actualPosition.x, FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPosition.y, FLOAT_DELTA);
	}

	@Test
	public void testRayCast() throws Exception
	{
		GameObject obstacle = new TestGameObject(1.5f, 5.5f);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 1) });

		Vector2 startPos = new Vector2(6, 2);
		Vector2 endPos = new Vector2(0, 6);
		List<RayCastHit> actualRayCastHits = physics.rayCast(startPos, endPos);

		assertEquals(1, actualRayCastHits.size());
		RayCastHit actualRayCastHit = actualRayCastHits.get(0);
		assertEquals(new Vector2(1.5f, 5), actualRayCastHit.getPoint());
		assertEquals(5.408326913195984f, actualRayCastHit.getDistance(), FLOAT_DELTA);
		assertEquals(obstacle, actualRayCastHit.getGameObject());

	}

	@Test
	public void testRayCast_NoHits() throws Exception
	{
		GameObject obstacle = new TestGameObject(1.5f, 5.5f);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 1) });

		Vector2 startPos = new Vector2(6, 2);
		Vector2 endPos = new Vector2(0, 2);
		List<RayCastHit> actualRayCastResults = physics.rayCast(startPos, endPos);

		assertEquals(0, actualRayCastResults.size());
	}

	@Test
	public void testRayCast_MultipleHits() throws Exception
	{
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(.5f, 1.5f, 1, 1) });

		GameObject obstacle = new TestGameObject(1.5f, 5.5f);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 1) });

		Vector2 startPos = new Vector2(0, -1);
		Vector2 endPos = new Vector2(2, 7);
		List<RayCastHit> actualRayCastHits = physics.rayCast(startPos, endPos);

		assertEquals(2, actualRayCastHits.size());

		RayCastHit actualRayCastHitFirst = actualRayCastHits.get(0);
		assertEquals(new Vector2(0.5f, 1), actualRayCastHitFirst.getPoint());
		assertEquals(2.06155281280883f, actualRayCastHitFirst.getDistance(), FLOAT_DELTA);
		assertEquals(gameObject, actualRayCastHitFirst.getGameObject());

		RayCastHit actualRayCastHitSecond = actualRayCastHits.get(1);
		assertEquals(new Vector2(1.5f, 5), actualRayCastHitSecond.getPoint());
		assertEquals(6.184658438426491f, actualRayCastHitSecond.getDistance(), FLOAT_DELTA);
		assertEquals(obstacle, actualRayCastHitSecond.getGameObject());
	}

	@Test
	public void testRayCast_IgnoreGameObjects() throws Exception
	{
		gameObject.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(.5f, 1.5f, 1, 1) });

		GameObject obstacle = new TestGameObject(1.5f, 5.5f);
		obstacle.setBoundingPolygons(new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 1, 1) });

		Vector2 startPos = new Vector2(0, -1);
		Vector2 endPos = new Vector2(2, 7);
		List<RayCastHit> actualRayCastHits = physics.rayCast(startPos, endPos, new GameObject[] { gameObject });

		assertEquals(1, actualRayCastHits.size());

		RayCastHit actualRayCastHit = actualRayCastHits.get(0);
		assertEquals(new Vector2(1.5f, 5), actualRayCastHit.getPoint());
		assertEquals(6.184658438426491f, actualRayCastHit.getDistance(), FLOAT_DELTA);
		assertEquals(obstacle, actualRayCastHit.getGameObject());
	}
}
