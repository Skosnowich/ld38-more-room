package de.skosnowich.libgdx.gameobjects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.libgdx.gameobjects.impl.TestDynamicGameObject;
import de.skosnowich.libgdx.gameobjects.impl.TestGameObject;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class GameObjectTest
{

	private GameObject gameObject;

	@Before
	public void beforeTest()
	{
		GameObjects.init();

		gameObject = new TestDynamicGameObject(0, 0);
	}

	@Test
	public void testGetBoundingPolygonsWithOffset_Rectangle() throws Exception
	{
		Polygon[] boundingShapes = new Polygon[] { ShapeUtils.getRectangleShape(0, 0, 2, 2) };
		gameObject.setBoundingPolygons(boundingShapes);

		Vector2 offset = new Vector2(3, 5);
		Polygon[] actualShapes = gameObject.getBoundingPolygonsWithOffset(offset);

		assertEquals(1, actualShapes.length);
		Polygon expectedShape = ShapeUtils.getRectangleShape(3, 5, 2, 2);
		assertArrayEquals(expectedShape.getTransformedVertices(), actualShapes[0].getTransformedVertices(), 0.001f);
	}

	@Test
	public void testGetBoundingPolygonsWithOffset_Circle() throws Exception
	{
		Polygon[] boundingShapes = new Polygon[] { ShapeUtils.getCircleShape(-3, -3, 5) };
		gameObject.setBoundingPolygons(boundingShapes);

		Vector2 offset = new Vector2(1, 7);
		Polygon[] actualShapes = gameObject.getBoundingPolygonsWithOffset(offset);

		assertEquals(1, actualShapes.length);
		Polygon expectedShape = ShapeUtils.getCircleShape(-2, 4, 5);
		assertArrayEquals(expectedShape.getTransformedVertices(), actualShapes[0].getTransformedVertices(), 0.001f);
	}

	@Test
	public void testGetBoundingPolygonsWithOffset_Multiple() throws Exception
	{
		Polygon[] boundingShapes = new Polygon[] { ShapeUtils.getRectangleShape(-7, -6.5f, 6, 3), ShapeUtils.getCircleShape(5, 2, 4) };
		gameObject.setBoundingPolygons(boundingShapes);

		Vector2 offset = new Vector2(-3, -5);
		Polygon[] actualShapes = gameObject.getBoundingPolygonsWithOffset(offset);

		assertEquals(2, actualShapes.length);
		Polygon expectedRectangle = ShapeUtils.getRectangleShape(-10, -11.5f, 6, 3);
		assertArrayEquals(expectedRectangle.getTransformedVertices(), actualShapes[0].getTransformedVertices(), 0.001f);
		Polygon expectedCircle = ShapeUtils.getCircleShape(2, -3, 4);
		assertArrayEquals(expectedCircle.getTransformedVertices(), actualShapes[1].getTransformedVertices(), 0.001f);
	}

	@Test
	public void testSetWorldPosition()
	{
		gameObject.setWorldPosition(23, 10);

		Vector2 actualPosition = new Vector2(gameObject.getX(), gameObject.getY());
		Vector2 expectedPosition = new Vector2(23, 10);
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testSetWorldPosition_GetWorldPosition()
	{
		gameObject.setWorldPosition(23, 10);

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(23, 10);
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testSetWorldPosition_Child()
	{
		gameObject.setWorldPosition(2, 3);
		TestGameObject child = new TestGameObject(5, 3);
		gameObject.addActor(child);

		child.setWorldPosition(4, 6);

		Vector2 actualPosition = new Vector2(child.getX(), child.getY());
		Vector2 expectedPosition = new Vector2(2, 3);
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testSetWorldPosition_ChildGetWorldPosition()
	{
		gameObject.setWorldPosition(2, 3);
		TestGameObject child = new TestGameObject(5, 3);
		gameObject.addActor(child);

		child.setWorldPosition(4, 6);

		Vector2 actualPosition = child.getWorldPosition();
		Vector2 expectedPosition = new Vector2(4, 6);
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testGetWorldPosition()
	{
		gameObject.setPosition(46, 20);

		Vector2 actualPosition = gameObject.getWorldPosition();
		Vector2 expectedPosition = new Vector2(46, 20);
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	public void testGetWorldPosition_Child()
	{
		gameObject.setWorldPosition(2, 3);
		TestGameObject child = new TestGameObject(5, 3);
		gameObject.addActor(child);

		Vector2 actualPosition = child.getWorldPosition();
		Vector2 expectedPosition = new Vector2(7, 6);
		assertEquals(expectedPosition, actualPosition);
	}

}
