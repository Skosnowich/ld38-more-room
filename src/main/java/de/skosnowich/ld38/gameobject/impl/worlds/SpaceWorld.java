package de.skosnowich.ld38.gameobject.impl.worlds;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Spaceship;
import de.skosnowich.libgdx.assets.Sounds;

public abstract class SpaceWorld extends World
{
	public SpaceWorld(String name, float x, float y, int size, float increaseTimeNeeded, Team owner)
	{
		super(name, x, y, size, increaseTimeNeeded, owner);
	}

	@Override
	public void addNewUnit()
	{
		addUnit(new Spaceship(this));
	}

	@Override
	public void playWorldCapturedSound()
	{
		Sounds.getInstance().playSound(AssetConstants.WORLD_CAPTURED_SPACE_SOUND, 0.7f);
	}

	@Override
	public void playWorldLostSound()
	{
		Sounds.getInstance().playSound(AssetConstants.WORLD_LOST_SPACE_SOUND, 0.2f);
	}

	@Override
	protected void playSendTroopsSound()
	{
		Sounds.getInstance().playSound(AssetConstants.SEND_TROOPS_SPACE_SOUND, 0.1f, false);
	}
}
