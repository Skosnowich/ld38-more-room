package de.skosnowich.libgdx.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.libgdx.utils.ShapeUtils;

public class ShapeUtilsTest
{

	private static final float FLOAT_DELTA = 0.0001f;

	@Test
	public void testGetRectangleShape() throws Exception
	{
		Polygon actualPolygon = ShapeUtils.getRectangleShape(9, 5, 4, 6);

		float[] expectedVertices = new float[8];
		expectedVertices[0] = 7;
		expectedVertices[1] = 2;
		expectedVertices[2] = 11;
		expectedVertices[3] = 2;
		expectedVertices[4] = 11;
		expectedVertices[5] = 8;
		expectedVertices[6] = 7;
		expectedVertices[7] = 8;
		Vector2 expectedPosition = new Vector2(9, 5);
		Vector2 expectedOrigin = new Vector2(0, 0);

		assertArrayEquals(expectedVertices, actualPolygon.getTransformedVertices(), FLOAT_DELTA);
		assertEquals(expectedPosition.x, actualPolygon.getX(), FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPolygon.getY(), FLOAT_DELTA);
		assertEquals(expectedOrigin.x, actualPolygon.getOriginX(), FLOAT_DELTA);
		assertEquals(expectedOrigin.y, actualPolygon.getOriginY(), FLOAT_DELTA);
	}

	@Test
	public void testGetRectangleShape_Nooked() throws Exception
	{
		Polygon actualPolygon = ShapeUtils.getRectangleShape(9, 5, 4, 6, 0.1f);

		float[] expectedVertices = new float[16];
		expectedVertices[0] = 7.1f;
		expectedVertices[1] = 2;
		expectedVertices[2] = 10.9f;
		expectedVertices[3] = 2;
		expectedVertices[4] = 11f;
		expectedVertices[5] = 2.1f;
		expectedVertices[6] = 11;
		expectedVertices[7] = 7.9f;
		expectedVertices[8] = 10.9f;
		expectedVertices[9] = 8;
		expectedVertices[10] = 7.1f;
		expectedVertices[11] = 8;
		expectedVertices[12] = 7;
		expectedVertices[13] = 7.9f;
		expectedVertices[14] = 7;
		expectedVertices[15] = 2.1f;
		Vector2 expectedPosition = new Vector2(9, 5);
		Vector2 expectedOrigin = new Vector2(0, 0);

		assertArrayEquals(expectedVertices, actualPolygon.getTransformedVertices(), FLOAT_DELTA);
		assertEquals(expectedPosition.x, actualPolygon.getX(), FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPolygon.getY(), FLOAT_DELTA);
		assertEquals(expectedOrigin.x, actualPolygon.getOriginX(), FLOAT_DELTA);
		assertEquals(expectedOrigin.y, actualPolygon.getOriginY(), FLOAT_DELTA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetRectangleShape_NookedNegative() throws Exception
	{
		ShapeUtils.getRectangleShape(9, 5, 4, 6, -0.1f);
	}

	@Test
	public void testGetRectangleShape_NookedNotNooked() throws Exception
	{
		Polygon actualPolygon = ShapeUtils.getRectangleShape(9, 5, 4, 6, 0.0f);

		float[] expectedVertices = new float[8];
		expectedVertices[0] = 7;
		expectedVertices[1] = 2;
		expectedVertices[2] = 11;
		expectedVertices[3] = 2;
		expectedVertices[4] = 11;
		expectedVertices[5] = 8;
		expectedVertices[6] = 7;
		expectedVertices[7] = 8;
		Vector2 expectedPosition = new Vector2(9, 5);
		Vector2 expectedOrigin = new Vector2(0, 0);

		assertArrayEquals(expectedVertices, actualPolygon.getTransformedVertices(), FLOAT_DELTA);
		assertEquals(expectedPosition.x, actualPolygon.getX(), FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPolygon.getY(), FLOAT_DELTA);
		assertEquals(expectedOrigin.x, actualPolygon.getOriginX(), FLOAT_DELTA);
		assertEquals(expectedOrigin.y, actualPolygon.getOriginY(), FLOAT_DELTA);
	}

	@Test
	public void testGetCircleShape() throws Exception
	{
		Polygon actualPolygon = ShapeUtils.getCircleShape(5, 3, 2, 8);

		float[] expectedVertices = new float[16];
		expectedVertices[0] = 7;
		expectedVertices[1] = 3;
		expectedVertices[2] = 6.4142f;
		expectedVertices[3] = 4.4142f;
		expectedVertices[4] = 5;
		expectedVertices[5] = 5;
		expectedVertices[6] = 3.5858f;
		expectedVertices[7] = 4.4142f;
		expectedVertices[8] = 3;
		expectedVertices[9] = 3;
		expectedVertices[10] = 3.5858f;
		expectedVertices[11] = 1.5858f;
		expectedVertices[12] = 5;
		expectedVertices[13] = 1;
		expectedVertices[14] = 6.4142f;
		expectedVertices[15] = 1.5858f;
		Vector2 expectedPosition = new Vector2(5, 3);
		Vector2 expectedOrigin = new Vector2(0, 0);

		assertArrayEquals(expectedVertices, actualPolygon.getTransformedVertices(), FLOAT_DELTA);
		assertEquals(expectedPosition.x, actualPolygon.getX(), FLOAT_DELTA);
		assertEquals(expectedPosition.y, actualPolygon.getY(), FLOAT_DELTA);
		assertEquals(expectedOrigin.x, actualPolygon.getOriginX(), FLOAT_DELTA);
		assertEquals(expectedOrigin.y, actualPolygon.getOriginY(), FLOAT_DELTA);
	}

	@Test
	public void testCopyPolygon() throws Exception
	{
		Polygon blueprint = ShapeUtils.getRectangleShape(4, 2, 3, 5);
		blueprint.rotate(30);
		blueprint.scale(2.5f);
		blueprint.setOrigin(2, 5);

		Polygon actualCopy = ShapeUtils.copyPolygon(blueprint);

		assertArrayEquals(blueprint.getTransformedVertices(), actualCopy.getTransformedVertices(), FLOAT_DELTA);
		assertArrayEquals(blueprint.getVertices(), actualCopy.getVertices(), FLOAT_DELTA);
		assertEquals(30, actualCopy.getRotation(), FLOAT_DELTA);
		assertEquals(3.5f, actualCopy.getScaleX(), FLOAT_DELTA);
		assertEquals(3.5f, actualCopy.getScaleY(), FLOAT_DELTA);
		assertEquals(2, actualCopy.getOriginX(), FLOAT_DELTA);
		assertEquals(5, actualCopy.getOriginY(), FLOAT_DELTA);
	}

	@Test
	public void testGetNearestIntersectionPoint_Circle() throws Exception
	{
		Vector2 origin = new Vector2(5, 5);
		Vector2 direction = new Vector2(3, 0);
		Polygon polygon = ShapeUtils.getCircleShape(9, 5, 2, 32);

		Vector2 actualIntersection = ShapeUtils.getNearestIntersectionPoint(origin, direction, polygon);

		Vector2 expectedIntersection = new Vector2(7, 5);
		assertEquals(expectedIntersection.x, actualIntersection.x, FLOAT_DELTA);
		assertEquals(expectedIntersection.y, actualIntersection.y, FLOAT_DELTA);
	}

	@Test
	public void testGetNearestIntersectionPoint_CircleNoIntersections() throws Exception
	{
		Vector2 origin = new Vector2(5, 5);
		Vector2 direction = new Vector2(1, 0);
		Polygon polygon = ShapeUtils.getCircleShape(9, 5, 2, 32);

		Vector2 actualIntersection = ShapeUtils.getNearestIntersectionPoint(origin, direction, polygon);

		assertNull(actualIntersection);
	}

	@Test
	public void testGetNearestIntersectionPoint_CircleTwoIntersections() throws Exception
	{
		Vector2 origin = new Vector2(5, 5);
		Vector2 direction = new Vector2(7, 0);
		Polygon polygon = ShapeUtils.getCircleShape(9, 5, 2, 32);

		Vector2 actualIntersection = ShapeUtils.getNearestIntersectionPoint(origin, direction, polygon);

		Vector2 expectedIntersection = new Vector2(7, 5);
		assertEquals(expectedIntersection.x, actualIntersection.x, FLOAT_DELTA);
		assertEquals(expectedIntersection.y, actualIntersection.y, FLOAT_DELTA);
	}

	@Test
	public void testGetNearestIntersectionPoint_CircleTwoIntersectionsBackwards() throws Exception
	{
		Vector2 origin = new Vector2(12, 5);
		Vector2 direction = new Vector2(-7, 0);
		Polygon polygon = ShapeUtils.getCircleShape(9, 5, 2, 32);

		Vector2 actualIntersection = ShapeUtils.getNearestIntersectionPoint(origin, direction, polygon);

		Vector2 expectedIntersection = new Vector2(11, 5);
		assertEquals(expectedIntersection.x, actualIntersection.x, FLOAT_DELTA);
		assertEquals(expectedIntersection.y, actualIntersection.y, FLOAT_DELTA);
	}
}
