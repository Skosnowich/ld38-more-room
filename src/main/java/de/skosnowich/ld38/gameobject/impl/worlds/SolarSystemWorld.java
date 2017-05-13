package de.skosnowich.ld38.gameobject.impl.worlds;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Spaceship;
import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.gameobjects.impl.Light;

public class SolarSystemWorld extends World
{
	private Light light;

	public SolarSystemWorld(String name, float x, float y, int size, float increaseTimeNeeded, Team owner)
	{
		super(name, x, y, size, increaseTimeNeeded, owner);
	}

	@Override
	public void setup()
	{
		super.setup();

		light = new Light(0, 0, getOwner().getColor(), getSize() * 6, 2, getSize(), true);
		addActor(light);
		light.setup();
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

	@Override
	public void act(float delta)
	{
		super.act(delta);

		light.setColor(getOwner().getColor());
		light.setVisible(isRevealed());
	}
}
