package de.skosnowich.ld38.level;

import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.ways.MedievalWay;
import de.skosnowich.ld38.gameobject.impl.ways.Way;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Sounds;

public abstract class MedievalLevel extends Level
{

	public MedievalLevel(int number, Vector2 startPoint, Level nextLevel, String backgroundTextureName)
	{
		super(number, startPoint, nextLevel, backgroundTextureName);
	}

	@Override
	protected Way createWay(World worldFrom, World worldTo)
	{
		return new MedievalWay(worldFrom, worldTo);
	}

	@Override
	public void playMusic()
	{
		Sounds.getInstance().playMusic(AssetConstants.MEDIEVAL_MUSIC, true, 0.1f);
		Sounds.getInstance().playMusic(AssetConstants.MEDIEVAL_AMBIENT, true, 1.4f);
	}

}
