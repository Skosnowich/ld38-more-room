package de.skosnowich.ld38.level;

import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.ways.SpaceWay;
import de.skosnowich.ld38.gameobject.impl.ways.Way;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Sounds;

public abstract class SolarSystemLevel extends Level
{
	public SolarSystemLevel(int number, Vector2 startPoint, Level nextLevel)
	{
		super(number, startPoint, nextLevel, AssetConstants.SPACE_LEVEL_BG_TEXTURE);
	}

	@Override
	protected Way createWay(World worldFrom, World worldTo)
	{
		return new SpaceWay(worldFrom, worldTo);
	}

	@Override
	public void playMusic()
	{
		Sounds.getInstance().playMusic(AssetConstants.SPACE_MUSIC, true, 0.1f);
		Sounds.getInstance().playMusic(AssetConstants.SPACE_AMBIENT, true, 1.4f);
	}
}
