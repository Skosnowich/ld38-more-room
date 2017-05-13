package de.skosnowich.ld38.level;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.FarmWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.TownWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class LevelOne extends MedievalLevel
{
	private int tutorialState = 0;

	public LevelOne()
	{
		super(1, new Vector2(0, 0), new LevelTwo(), AssetConstants.LEVEL_ONE_BG_TEXTURE);
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team farmersTeam = new Team("Farmers", Color.GOLD, this, KI.PASSIVE);

		TownWorld startTown = new TownWorld("City of Dontknowia", 320, 180, playersTeam);
		startTown.addTag("target1");
		startTown.addTag("tutorialState0");
		FarmWorld northWestFarm = new FarmWorld("Odan's Farm", 216, 267, playersTeam);
		northWestFarm.addTag("tutorialState0");
		FarmWorld northEastFarm = new FarmWorld("Olaf's Farm", 424, 276, farmersTeam);
		northEastFarm.addTag("target2");
		northEastFarm.addTag("tutorialState0");
		FarmWorld southernFarm = new FarmWorld("Brunhild's Farm", 330, 31, farmersTeam);
		southernFarm.addTag("target3");
		southernFarm.addTag("tutorialState1");
		FarmWorld southWestFarm = new FarmWorld("Egil's Farm", 212, 41, farmersTeam);
		southWestFarm.addTag("tutorialState2");

		addTeam(farmersTeam);

		addWorld(startTown);
		addWorld(northEastFarm);
		addWorld(southernFarm);
		addWorld(southWestFarm);
		addWorld(northWestFarm);

		connectWorlds(startTown, northEastFarm);
		connectWorlds(startTown, northWestFarm);
		connectWorlds(startTown, southernFarm);
		connectWorlds(startTown, southWestFarm);
		connectWorlds(southernFarm, southWestFarm);
	}

	@Override
	public Array<Connection<World>> getConnections(World originWorld)
	{
		Array<Connection<World>> all = super.getConnections(originWorld);
		Array<Connection<World>> ret = new Array<>();
		for (Connection<World> con : all)
		{
			for (int i = 0; i <= tutorialState; i++)
			{
				if (con.getFromNode().getTags().contains("tutorialState" + i))
				{
					for (int j = 0; j <= tutorialState; j++)
					{
						if (con.getToNode().getTags().contains("tutorialState" + j))
						{
							ret.add(con);
							break;
						}
					}
					break;
				}
			}
		}
		return ret;
	}

	public void setTutorialState(int tutorialState)
	{
		this.tutorialState = tutorialState;
	}

	@Override
	public String getStartText()
	{
		return "";
	}
}
