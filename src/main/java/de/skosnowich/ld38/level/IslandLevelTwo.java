package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.BigIslandWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.SmallIslandWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class IslandLevelTwo extends IslandLevel
{
	public IslandLevelTwo()
	{
		super(4, new Vector2(400, 550), new SpaceLevelOne());
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team blackTeam = new Team("Black", Color.BLACK, this, KI.PASSIVE);
		Team goldTeam = new Team("Gold", Color.GOLD, this, KI.DEFENSIVE);
		Team purpleTeam = new Team("Purple", Color.PURPLE, this, KI.KAMIKAZE);

		addTeam(blackTeam);
		addTeam(goldTeam);
		addTeam(purpleTeam);

		World world804_383 = new SmallIslandWorld("804:533", 804, 383, blackTeam);
		World world730_233 = new SmallIslandWorld("730:383", 730, 233, blackTeam);
		World world537_142 = new SmallIslandWorld("537:292", 537, 142, blackTeam);
		World world361_218 = new SmallIslandWorld("361:368", 361, 218, blackTeam);
		World world227_380 = new SmallIslandWorld("227:530", 227, 380, blackTeam);
		World world506_590 = new SmallIslandWorld("506:695", 506, 590, blackTeam);
		World world345_549 = new SmallIslandWorld("345:699", 345, 549, blackTeam);
		World world691_545 = new SmallIslandWorld("691:695", 691, 545, blackTeam);
		World world382_683 = new BigIslandWorld("382:683", 382, 683, playersTeam);
		World world904_261 = new BigIslandWorld("904:261", 904, 261, purpleTeam);
		World world128_217 = new BigIslandWorld("128:217", 128, 217, goldTeam);

		addWorld(world804_383);
		addWorld(world730_233);
		addWorld(world537_142);
		addWorld(world361_218);
		addWorld(world227_380);
		addWorld(world506_590);
		addWorld(world345_549);
		addWorld(world691_545);
		addWorld(world382_683);
		addWorld(world904_261);
		addWorld(world128_217);

		connectWorlds(world804_383, world730_233);
		connectWorlds(world730_233, world537_142);
		connectWorlds(world537_142, world361_218);
		connectWorlds(world361_218, world227_380);
		connectWorlds(world227_380, world345_549);
		connectWorlds(world506_590, world345_549);
		connectWorlds(world506_590, world691_545);
		connectWorlds(world691_545, world804_383);

		connectWorlds(world382_683, world345_549);
		connectWorlds(world382_683, world506_590);
		connectWorlds(world904_261, world804_383);
		connectWorlds(world904_261, world730_233);
		connectWorlds(world128_217, world361_218);
		connectWorlds(world128_217, world227_380);
	}

	@Override
	public String getStartText()
	{
		return "We want the other side of the planet!";
	}
}
