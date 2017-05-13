package de.skosnowich.libgdx.physics.topdown;

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;

class Collision
{

	private boolean overlaps;
	private Polygon overlappingPolygon;
	private Polygon overlappingOtherPolygon;
	private MinimumTranslationVector minimumTranslationVector;

	public void setOverlaps(boolean overlaps)
	{
		this.overlaps = overlaps;
	}

	public boolean isOverlaps()
	{
		return overlaps;
	}

	public void setOverlappingPolygon(Polygon overlappingPolygon)
	{
		this.overlappingPolygon = overlappingPolygon;
	}

	public Polygon getOverlappingPolygon()
	{
		return overlappingPolygon;
	}

	public void setOverlappingOtherPolygon(Polygon overlappingOtherPolygon)
	{
		this.overlappingOtherPolygon = overlappingOtherPolygon;
	}

	public Polygon getOverlappingOtherPolygon()
	{
		return overlappingOtherPolygon;
	}

	public MinimumTranslationVector getMinimumTranslationVector()
	{
		return minimumTranslationVector;
	}

	public void setMinimumTranslationVector(MinimumTranslationVector minimumTranslationVector)
	{
		this.minimumTranslationVector = minimumTranslationVector;
	}

	@Override
	public String toString()
	{
		return String.format("Collision [overlaps=%s, overlappingPolygon=%s, overlappingOtherPolygon=%s, minimumTranslationVector=%s]", overlaps,
				overlappingPolygon, overlappingOtherPolygon, minimumTranslationVector);
	}

}
