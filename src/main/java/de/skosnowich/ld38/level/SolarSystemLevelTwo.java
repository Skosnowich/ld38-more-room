package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.BigSolarSystemWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.SmallSolarSystemWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class SolarSystemLevelTwo extends SolarSystemLevel
{
	public SolarSystemLevelTwo()
	{
		super(8, new Vector2(450, 400), null);
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team cyanTeam = new Team("Cyan", Color.CYAN, this, KI.PASSIVE);
		Team redTeam = new Team("Green", Color.GREEN, this, KI.KAMIKAZE);
		Team yellowTeam = new Team("Yellow", Color.YELLOW, this, KI.KAMIKAZE);
		Team purpleTeam = new Team("Purple", Color.PURPLE, this, KI.AGGRESSIVE);

		addTeam(cyanTeam);
		addTeam(redTeam);
		addTeam(yellowTeam);
		addTeam(purpleTeam);

		World world193_309 = new SmallSolarSystemWorld("193:309", 193, 309, cyanTeam);
		World world371_309 = new SmallSolarSystemWorld("371:309", 371, 309, cyanTeam);
		World world542_308 = new SmallSolarSystemWorld("542:308", 542, 308, cyanTeam);
		World world750_311 = new SmallSolarSystemWorld("750:311", 750, 311, cyanTeam);
		World world647_417 = new SmallSolarSystemWorld("647:417", 647, 417, cyanTeam);
		World world472_418 = new BigSolarSystemWorld("472:418", 472, 418, playersTeam);
		World world319_416 = new SmallSolarSystemWorld("319:416", 319, 416, cyanTeam);
		World world375_521 = new SmallSolarSystemWorld("375:521", 375, 521, cyanTeam);
		World world549_529 = new SmallSolarSystemWorld("549:529", 549, 529, cyanTeam);
		World world469_627 = new SmallSolarSystemWorld("469:627", 469, 627, cyanTeam);
		World world266_170 = new SmallSolarSystemWorld("266:170", 266, 170, cyanTeam);
		World world462_177 = new SmallSolarSystemWorld("462:177", 462, 177, cyanTeam);
		World world698_137 = new SmallSolarSystemWorld("698:137", 698, 137, cyanTeam);
		World world75_229 = new BigSolarSystemWorld("75:229", 75, 229, redTeam);
		World world925_233 = new BigSolarSystemWorld("925:233", 925, 233, yellowTeam);
		World world652_650 = new BigSolarSystemWorld("652:650", 652, 650, purpleTeam);

		addWorld(world193_309);
		addWorld(world371_309);
		addWorld(world542_308);
		addWorld(world750_311);

		addWorld(world647_417);
		addWorld(world472_418);
		addWorld(world319_416);

		addWorld(world375_521);
		addWorld(world549_529);

		addWorld(world469_627);

		addWorld(world266_170);
		addWorld(world462_177);
		addWorld(world698_137);
		addWorld(world75_229);
		addWorld(world925_233);
		addWorld(world652_650);

		connectWorlds(world193_309, world371_309);
		connectWorlds(world371_309, world542_308);
		connectWorlds(world542_308, world750_311);
		connectWorlds(world647_417, world472_418);
		connectWorlds(world472_418, world319_416);
		connectWorlds(world375_521, world549_529);

		connectWorlds(world469_627, world375_521);
		connectWorlds(world469_627, world549_529);

		connectWorlds(world549_529, world647_417);
		connectWorlds(world549_529, world472_418);
		connectWorlds(world375_521, world472_418);
		connectWorlds(world375_521, world319_416);

		connectWorlds(world319_416, world193_309);
		connectWorlds(world319_416, world371_309);
		connectWorlds(world472_418, world371_309);
		connectWorlds(world472_418, world542_308);
		connectWorlds(world647_417, world542_308);
		connectWorlds(world647_417, world750_311);

		connectWorlds(world266_170, world193_309);
		connectWorlds(world266_170, world371_309);
		connectWorlds(world462_177, world371_309);
		connectWorlds(world462_177, world542_308);
		connectWorlds(world698_137, world542_308);
		connectWorlds(world698_137, world750_311);

		connectWorlds(world266_170, world462_177);
		connectWorlds(world462_177, world698_137);

		connectWorlds(world75_229, world266_170);
		connectWorlds(world75_229, world193_309);
		connectWorlds(world925_233, world698_137);
		connectWorlds(world925_233, world750_311);
		connectWorlds(world652_650, world469_627);
	}

	@Override
	public String getStartText()
	{
		return "Our population keeps growing... you know what we have to do.";
	}
}
