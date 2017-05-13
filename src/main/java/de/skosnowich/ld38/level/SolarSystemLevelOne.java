package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.BigSolarSystemWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.SmallSolarSystemWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class SolarSystemLevelOne extends SolarSystemLevel
{
	public SolarSystemLevelOne()
	{
		super(7, new Vector2(0, 450), new SolarSystemLevelTwo());
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team cyanTeam = new Team("Cyan", Color.CYAN, this, KI.PASSIVE);
		Team redTeam = new Team("Red", Color.GREEN, this, KI.AGGRESSIVE);
		Team yellowTeam = new Team("Yellow", Color.YELLOW, this, KI.AGGRESSIVE);
		Team purpleTeam = new Team("Purple", Color.PURPLE, this, KI.KAMIKAZE);

		addTeam(cyanTeam);
		addTeam(redTeam);
		addTeam(yellowTeam);
		addTeam(purpleTeam);

		World world344_519 = new SmallSolarSystemWorld("344:519", 344, 519, cyanTeam);
		World world535_522 = new SmallSolarSystemWorld("535:522", 535, 522, cyanTeam);
		World world747_528 = new SmallSolarSystemWorld("747:528", 747, 528, cyanTeam);
		World world739_355 = new SmallSolarSystemWorld("739:355", 739, 355, cyanTeam);
		World world531_362 = new SmallSolarSystemWorld("531:362", 531, 362, cyanTeam);
		World world249_356 = new SmallSolarSystemWorld("249:356", 249, 356, cyanTeam);
		World world264_208 = new SmallSolarSystemWorld("264:208", 264, 208, cyanTeam);
		World world505_184 = new SmallSolarSystemWorld("505:184", 505, 184, cyanTeam);
		World world697_213 = new SmallSolarSystemWorld("697:213", 697, 213, cyanTeam);

		World world541_698 = new BigSolarSystemWorld("541:698", 541, 698, redTeam);
		World world906_352 = new BigSolarSystemWorld("906:352", 906, 352, purpleTeam);
		World world481_31 = new BigSolarSystemWorld("481:31", 481, 31, yellowTeam);
		World world78_411 = new BigSolarSystemWorld("78:411", 78, 411, playersTeam);

		addWorld(world344_519);
		addWorld(world535_522);
		addWorld(world747_528);
		addWorld(world739_355);
		addWorld(world531_362);
		addWorld(world249_356);
		addWorld(world264_208);
		addWorld(world505_184);
		addWorld(world697_213);

		addWorld(world541_698);
		addWorld(world906_352);
		addWorld(world481_31);
		addWorld(world78_411);

		connectWorlds(world344_519, world535_522);
		connectWorlds(world535_522, world747_528);
		connectWorlds(world739_355, world531_362);
		connectWorlds(world531_362, world249_356);
		connectWorlds(world264_208, world505_184);
		connectWorlds(world505_184, world697_213);

		connectWorlds(world344_519, world249_356);
		connectWorlds(world249_356, world264_208);
		connectWorlds(world535_522, world531_362);
		connectWorlds(world531_362, world505_184);
		connectWorlds(world747_528, world739_355);
		connectWorlds(world739_355, world697_213);

		connectWorlds(world541_698, world535_522);
		connectWorlds(world906_352, world739_355);
		connectWorlds(world481_31, world505_184);
		connectWorlds(world78_411, world249_356);
	}

	@Override
	public String getStartText()
	{
		return "There are other solar systems? Well, we need them, so we have enough room!";
	}
}
