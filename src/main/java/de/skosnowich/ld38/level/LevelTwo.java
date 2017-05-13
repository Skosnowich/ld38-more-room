package de.skosnowich.ld38.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.FarmWorld;
import de.skosnowich.ld38.gameobject.impl.worlds.TownWorld;
import de.skosnowich.ld38.stage.MyGameStage;

public class LevelTwo extends MedievalLevel
{
	public LevelTwo()
	{
		super(2, new Vector2(275 + MyGameStage.VIEWPORT_WIDTH / 2.0f, 780 - MyGameStage.VIEWPORT_HEIGHT / 2.0f), new IslandLevelOne(),
				AssetConstants.LEVEL_TWO_BG_TEXTURE);
	}

	@Override
	public void setup()
	{
		super.setup();
		Team playersTeam = new Team("Player's Team", Color.RED);
		Team farmersTeam = new Team("Farmers cyan", Color.CYAN, this, KI.DEFENSIVE);
		Team redKingdomTeam = new Team("Farmers red ", Color.GOLD, this, KI.DEFENSIVE);
		Team purpleKingdomTeam = new Team("Farmers purple", Color.PURPLE, this, KI.DEFENSIVE);

		TownWorld northforkenTown = new TownWorld("Northforken", 171, 103, purpleKingdomTeam);
		TownWorld southforkenTown = new TownWorld("Southforken", 146, 255, purpleKingdomTeam);
		TownWorld bridgescrossingTown = new TownWorld("Bridgescrossing", 318, 203, purpleKingdomTeam);
		TownWorld rivertonTown = new TownWorld("City of Riverton", 480, 205, redKingdomTeam);
		TownWorld forestendingTown = new TownWorld("City of Forestending", 401, 346, redKingdomTeam);
		TownWorld forestspoutTown = new TownWorld("City of Forestspout", 196, 466, redKingdomTeam);
		TownWorld dontknowiaTown = new TownWorld("City of Dontknowia", 255, 590, playersTeam);
		FarmWorld oguldsFarm = new FarmWorld("Oguld's Farm", 186, 670, farmersTeam);
		FarmWorld tangornsFarm = new FarmWorld("Tangorn's Farm", 297, 685, farmersTeam);

		addTeam(farmersTeam);
		addTeam(redKingdomTeam);
		addTeam(purpleKingdomTeam);

		addWorld(northforkenTown);
		addWorld(southforkenTown);
		addWorld(bridgescrossingTown);
		addWorld(rivertonTown);
		addWorld(forestendingTown);
		addWorld(forestspoutTown);
		addWorld(dontknowiaTown);
		addWorld(oguldsFarm);
		addWorld(tangornsFarm);

		connectWorlds(bridgescrossingTown, northforkenTown);
		connectWorlds(bridgescrossingTown, southforkenTown);

		connectWorlds(forestendingTown, bridgescrossingTown);

		connectWorlds(forestendingTown, rivertonTown);
		connectWorlds(forestspoutTown, forestendingTown);
		connectWorlds(dontknowiaTown, forestspoutTown);

		connectWorlds(dontknowiaTown, oguldsFarm);
		connectWorlds(dontknowiaTown, tangornsFarm);
	}

	@Override
	public String getStartText()
	{
		return "Let us take the island, our nation grows bigger every day. We need the place!\n\nTwo hints:\n1) you can move the camera with W,S,A,D or holding down the middle mouse button.\n2) you see only towns (farms etc.), you're neighbor of.";
	}
}
