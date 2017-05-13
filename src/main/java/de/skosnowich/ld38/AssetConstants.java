package de.skosnowich.ld38;

import de.skosnowich.libgdx.assets.AssetsType;
import de.skosnowich.libgdx.assets.LoadAsset;

public class AssetConstants
{
	// Level Backgrounds
	@LoadAsset(AssetsType.TEXTURE)
	public static final String LEVEL_ONE_BG_TEXTURE = "backgrounds/level-one";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String LEVEL_TWO_BG_TEXTURE = "backgrounds/level-two";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String SPACE_LEVEL_BG_TEXTURE = "backgrounds/space-level-background";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String ISLAND_LEVEL_BG_TEXTURE = "backgrounds/island-level-background";

	// Worlds
	@LoadAsset(AssetsType.TEXTURE)
	public static final String TOWN_TEXTURE = "town";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String TOWN_TEAM_TEXTURE = "town-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String FARM_TEXTURE = "farm";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String FARM_TEAM_TEXTURE = "farm-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String SMALL_ISLAND_TEXTURE = "small-island";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SMALL_ISLAND_TEAM_TEXTURE = "small-island-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String BIG_ISLAND_TEXTURE = "big-island";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String BIG_ISLAND_TEAM_TEXTURE = "big-island-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String PLANET_BIG_TEXTURE = "planet-big";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String PLANET_BIG_TEAM_TEXTURE = "planet-big";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String PLANET_SMALL_TEXTURE = "planet-small";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String PLANET_SMALL_TEAM_TEXTURE = "planet-small-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String SMALL_SOLARSYSTEM_TEXTURE = "small-solarsystem";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SMALL_SOLARSYSTEM_TEAM_TEXTURE = "small-solarsystem-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String BIG_SOLARSYSTEM_TEXTURE = "big-solarsystem";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String BIG_SOLARSYSTEM_TEAM_TEXTURE = "big-solarsystem-team";

	// Units
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SOLDIER_TEXTURE_0 = "soldier_0";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SOLDIER_TEXTURE_1 = "soldier_1";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SOLDIER_TEXTURE_2 = "soldier_2";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SOLDIER_TEXTURE_3 = "soldier_3";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SOLDIER_TEAM_TEXTURE = "soldier-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String SHIP_TEXTURE = "ship";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SHIP_TEAM_TEXTURE = "ship-team";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String SPACESHIP_TEXTURE = "spaceship";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SPACESHIP_TEAM_TEXTURE = "spaceship-team";

	// Arrows
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_FRIENDLY_START_TEXTURE = "arrow-friendly-start";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_FRIENDLY_MIDDLE_TEXTURE = "arrow-friendly-middle";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_FRIENDLY_END_TEXTURE = "arrow-friendly-end";

	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_HOSTILE_START_TEXTURE = "arrow-hostile-start";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_HOSTILE_MIDDLE_TEXTURE = "arrow-hostile-middle";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ARROW_HOSTILE_END_TEXTURE = "arrow-hostile-end";

	// Road
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ROAD_1_TEXTURE = "road-tile-1";
	@LoadAsset(AssetsType.TEXTURE)
	public static final String ROAD_2_TEXTURE = "road-tile-2";

	public static final String[] ROAD_TEXTURES = new String[] { ROAD_1_TEXTURE, ROAD_2_TEXTURE };

	// Spaceway
	@LoadAsset(AssetsType.TEXTURE)
	public static final String SPACEWAY_1_TEXTURE = "spaceway-tile-1";

	public static final String[] SPACEWAY_TEXTURES = new String[] { SPACEWAY_1_TEXTURE };

	// Particles
	@LoadAsset(AssetsType.PARTICLES)
	public static final String THRUSTER_PARTICLES = "thruster";

	@LoadAsset(AssetsType.PARTICLES)
	public static final String DIE_PARTICLES = "die";

	// Sounds
	@LoadAsset(AssetsType.SOUND)
	public static final String WORLD_CAPTURED_MEDIEVAL_SOUND = "world_captured_medieval";
	@LoadAsset(AssetsType.SOUND)
	public static final String WORLD_LOST_MEDIEVAL_SOUND = "world_lost_medieval";
	@LoadAsset(AssetsType.SOUND)
	public static final String WORLD_CAPTURED_SPACE_SOUND = "world_captured_space";
	@LoadAsset(AssetsType.SOUND)
	public static final String WORLD_LOST_SPACE_SOUND = "world_lost_space";

	@LoadAsset(AssetsType.SOUND)
	public static final String SEND_TROOPS_MEDIEVAL_SOUND = "send_troops_medieval";
	@LoadAsset(AssetsType.SOUND)
	public static final String SEND_TROOPS_SPACE_SOUND = "send_troops_space";
	@LoadAsset(AssetsType.SOUND)
	public static final String SEND_TROOPS_ISLAND_SOUND = "send_troops_island";

	@LoadAsset(AssetsType.SOUND)
	public static final String TROOPS_DIE_SPACE_SOUND = "troops_die_space";
	@LoadAsset(AssetsType.SOUND)
	public static final String TROOPS_DIE_MEDIEVAL_SOUND = "troops_die_medieval";
	@LoadAsset(AssetsType.SOUND)
	public static final String TROOPS_DIE_ISLAND_SOUND = "troops_die_island";

	// Music & Ambient
	@LoadAsset(AssetsType.MUSIC)
	public static final String MEDIEVAL_MUSIC = "medieval";

	@LoadAsset(AssetsType.MUSIC)
	public static final String MEDIEVAL_AMBIENT = "medieval_ambient";

	@LoadAsset(AssetsType.MUSIC)
	public static final String SPACE_MUSIC = "space";

	@LoadAsset(AssetsType.MUSIC)
	public static final String SPACE_AMBIENT = "space_ambient";

	@LoadAsset(AssetsType.MUSIC)
	public static final String ISLAND_AMBIENT = "island_ambient";

}
