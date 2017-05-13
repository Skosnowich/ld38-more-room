package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.PlanetBigWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.PlanetSmallWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class SpaceLevelTwo extends SpaceLevel
{
	Team redTeam, yellowTeam;

	public SpaceLevelTwo()
	{
		super(6, new Vector2(0, 300), new SolarSystemLevelOne());
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team cyanTeam = new Team("Cyan", Color.CYAN, this, KI.PASSIVE);
		redTeam = new Team("Red", Color.GREEN, this, KI.DEFENSIVE);
		yellowTeam = new Team("Yellow", Color.YELLOW, this, KI.DEFENSIVE);
		Team purpleTeam = new Team("Purple", Color.PURPLE, this, KI.AGGRESSIVE);

		addTeam(cyanTeam);
		addTeam(redTeam);
		addTeam(yellowTeam);
		addTeam(purpleTeam);

		World middle = new PlanetBigWorld("391:173", 391, 373, playersTeam);
		World world577_535 = new PlanetBigWorld("577:335", 577, 535, cyanTeam);
		World world566_226 = new PlanetBigWorld("566:26", 566, 226, yellowTeam);
		World world183_231 = new PlanetBigWorld("183:31", 183, 231, playersTeam);
		World world199_557 = new PlanetBigWorld("199:357", 199, 557, redTeam);

		World world57_552 = new PlanetSmallWorld("57:552", 57, 552, redTeam);
		World world106_448 = new PlanetSmallWorld("106:448", 106, 448, redTeam);
		World world768_669 = new PlanetSmallWorld("768:669", 768, 669, cyanTeam);
		World world881_507 = new PlanetSmallWorld("881:507", 881, 507, cyanTeam);
		World world865_178 = new PlanetSmallWorld("865:178", 865, 178, yellowTeam);
		World world641_46 = new PlanetSmallWorld("641:46", 641, 46, yellowTeam);
		World world52_133 = new PlanetSmallWorld("52:133", 52, 133, purpleTeam);
		World world286_72 = new PlanetSmallWorld("286:72", 286, 72, purpleTeam);

		addWorld(middle);
		addWorld(world577_535);
		addWorld(world566_226);
		addWorld(world183_231);
		addWorld(world199_557);

		addWorld(world57_552);
		addWorld(world106_448);
		addWorld(world768_669);
		addWorld(world881_507);
		addWorld(world865_178);
		addWorld(world641_46);
		addWorld(world52_133);
		addWorld(world286_72);

		connectWorlds(middle, world577_535);
		connectWorlds(middle, world566_226);
		connectWorlds(middle, world183_231);
		connectWorlds(middle, world199_557);

		connectWorlds(world199_557, world57_552);
		connectWorlds(world199_557, world106_448);
		connectWorlds(world57_552, world106_448);
		connectWorlds(world577_535, world768_669);
		connectWorlds(world577_535, world881_507);
		connectWorlds(world768_669, world881_507);

		connectWorlds(world566_226, world865_178);
		connectWorlds(world566_226, world641_46);
		connectWorlds(world865_178, world641_46);
		connectWorlds(world183_231, world52_133);
		connectWorlds(world183_231, world286_72);
		connectWorlds(world52_133, world286_72);
	}

	@Override
	public void act()
	{
		super.act();
		int count = 0;
		for (World w : getWorlds())
		{
			if (w.getOwner().isPlayer())
			{
				count++;
			}
		}
		if (!yellowTeam.getKi().equals(KI.AGGRESSIVE) && count >= 3)
		{
			yellowTeam.setKi(KI.AGGRESSIVE);
		}
		if (!redTeam.getKi().equals(KI.KAMIKAZE) && count >= 6)
		{
			redTeam.setKi(KI.KAMIKAZE);
		}
	}

	@Override
	public String getStartText()
	{
		return "There is a lot of space here!";
	}
}
