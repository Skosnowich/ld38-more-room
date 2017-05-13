package de.skosnowich.ld38.gameobject.impl.worlds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.gameobjects.impl.Light;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class PlanetBigWorld extends SpaceWorld
{
	private Light light;

	public PlanetBigWorld(String name, float x, float y, Team owner)
	{
		super(name, x, y, 20, 1, owner);

		Sprite baseSprite = new Sprite(Assets.getInstance().getTexture(AssetConstants.PLANET_BIG_TEXTURE));
		Sprite teamSprite = new Sprite(Assets.getInstance().getTexture(AssetConstants.PLANET_BIG_TEAM_TEXTURE));
		buildSprite(baseSprite, teamSprite);

		setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(sprite.getX(), sprite.getY(), sprite.getWidth() / 2) });
	}

	@Override
	public void setup()
	{
		super.setup();

		light = new Light(0, 0, getOwner().getColor(), 120, 2, getSize(), true);
		addActor(light);
		light.setup();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		light.setColor(getOwner().getColor());
		light.setVisible(isRevealed());
	}
}
