package de.skosnowich.ld38.gameobject.impl.worlds;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Ship;
import de.skosnowich.libgdx.assets.Sounds;

public abstract class IslandWorld extends World
{

	public IslandWorld(String name, float x, float y, int size, float increaseTimeNeeded, Team owner)
	{
		super(name, x, y, size, increaseTimeNeeded, owner);
	}

	@Override
	public void addNewUnit()
	{
		addUnit(new Ship(this));
	}

	@Override
	public void playWorldCapturedSound()
	{
		Sounds.getInstance().playSound(AssetConstants.WORLD_CAPTURED_MEDIEVAL_SOUND, 0.4f);
	}

	@Override
	public void playWorldLostSound()
	{
		Sounds.getInstance().playSound(AssetConstants.WORLD_LOST_MEDIEVAL_SOUND, 0.7f);
	}

	@Override
	protected void playSendTroopsSound()
	{
		Sounds.getInstance().playSound(AssetConstants.SEND_TROOPS_ISLAND_SOUND, 0.7f, false);
	}
}
