package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.BigIslandWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.SmallIslandWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class IslandLevelOne extends IslandLevel
{
	public IslandLevelOne()
	{
		super(3, new Vector2(0, 350), new IslandLevelTwo());
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

		World middle = new BigIslandWorld("474:371", 474, 371, goldTeam);
		World world737_364 = new SmallIslandWorld("737:364", 737, 364, blackTeam);
		World world741_529 = new SmallIslandWorld("741:529", 741, 529, blackTeam);
		World world734_220 = new SmallIslandWorld("734:220", 734, 220, blackTeam);
		World world233_225 = new SmallIslandWorld("233:225", 233, 225, blackTeam);
		World world238_350 = new SmallIslandWorld("238:350", 238, 350, blackTeam);
		World world236_451 = new SmallIslandWorld("236:451", 236, 451, blackTeam);
		World left = new BigIslandWorld("89:348", 89, 348, playersTeam);
		World right = new BigIslandWorld("911:360", 911, 360, purpleTeam);

		addWorld(middle);
		addWorld(world737_364);
		addWorld(world741_529);
		addWorld(world734_220);
		addWorld(world233_225);
		addWorld(world238_350);
		addWorld(world236_451);
		addWorld(left);
		addWorld(right);

		connectWorlds(middle, world737_364);
		connectWorlds(middle, world741_529);
		connectWorlds(middle, world734_220);
		connectWorlds(middle, world233_225);
		connectWorlds(middle, world238_350);
		connectWorlds(middle, world236_451);
		connectWorlds(right, world737_364);
		connectWorlds(right, world741_529);
		connectWorlds(right, world734_220);
		connectWorlds(left, world233_225);
		connectWorlds(left, world238_350);
		connectWorlds(left, world236_451);
	}

	@Override
	public String getStartText()
	{
		return "Now that we have conquered the whole Island, let us conquer those other islands!\nWe need the room for our people.";
	}
}
