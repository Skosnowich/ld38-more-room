package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.PlanetBigWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class SpaceLevelOne extends SpaceLevel
{
	public SpaceLevelOne()
	{
		super(5, new Vector2(0, 0), new SpaceLevelTwo());
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team cyanTeam = new Team("Cyan", Color.CYAN, this, KI.DEFENSIVE);
		Team redTeam = new Team("Red", Color.GOLD, this, KI.DEFENSIVE);
		Team purpleTeam = new Team("Purple", Color.PURPLE, this, KI.KAMIKAZE);

		addTeam(cyanTeam);
		addTeam(redTeam);
		addTeam(purpleTeam);

		World world305_192 = new PlanetBigWorld("320:160", 320, 160, playersTeam);
		World world233_309 = new PlanetBigWorld("233:309", 233, 309, cyanTeam);
		World world65_41 = new PlanetBigWorld("65:41", 65, 41, redTeam);
		World world577_43 = new PlanetBigWorld("577:43", 577, 43, redTeam);
		World world611_196 = new PlanetBigWorld("611:196", 611, 196, purpleTeam);

		addWorld(world305_192);
		addWorld(world233_309);
		addWorld(world65_41);
		addWorld(world577_43);
		addWorld(world611_196);

		connectWorlds(world305_192, world233_309);
		connectWorlds(world305_192, world65_41);
		connectWorlds(world305_192, world577_43);

		connectWorlds(world577_43, world611_196);
	}

	@Override
	public String getStartText()
	{
		return "After many years, we discovered that there are other planets, we can populate.\nSeemingly we are also not alone in the space.\nOur people can need the room better than any other!";
	}
}
