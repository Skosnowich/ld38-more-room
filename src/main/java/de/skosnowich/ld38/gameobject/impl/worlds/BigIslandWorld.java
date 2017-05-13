package de.skosnowich.ld38.gameobject.impl.worlds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class BigIslandWorld extends IslandWorld
{

	public BigIslandWorld(String name, float x, float y, Team owner)
	{
		super(name, x, y, 20, 1, owner);

		Sprite baseSprite = new Sprite(Assets.getInstance().getTexture(AssetConstants.BIG_ISLAND_TEXTURE));
		Sprite teamSprite = new Sprite(Assets.getInstance().getTexture(AssetConstants.BIG_ISLAND_TEAM_TEXTURE));
		buildSprite(baseSprite, teamSprite);

		setBoundingPolygons(new Polygon[] {
				ShapeUtils.getRectangleShape(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight() / 2) });
	}

}
