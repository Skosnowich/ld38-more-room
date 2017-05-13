package de.skosnowich.ld38.level;

import java.util.*;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.ld38.gameobject.Upgrade;
import de.skosnowich.ld38.gameobject.impl.MyTags;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Unit;
import de.skosnowich.ld38.gameobject.impl.ways.Way;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.ld38.pathfinding.ManhattanHeuristic;
import de.skosnowich.ld38.pathfinding.MyGraphPath;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.gameobjects.GameObject;
import de.skosnowich.libgdx.gameobjects.GameObjects;

public abstract class Level implements IndexedGraph<World>
{
	private List<World> worldList;
	protected List<Team> teamList;

	private Set<Way> waySet;
	private Map<World, Array<Way>> pathMap;

	private final ManhattanHeuristic HEURISTIC = new ManhattanHeuristic();
	private IndexedAStarPathFinder<World> pathfinder;

	private Rectangle levelBoundaries;
	private Vector2 startPoint;
	private Texture backgroundTexture;

	private final int number;
	private Level nextLevel;

	private Team nextPathfindingRequester;

	public Level(int number, Vector2 startPoint, Level nextLevel, String backgroundTextureName)
	{
		this.number = number;
		this.startPoint = startPoint;
		this.nextLevel = nextLevel;
		backgroundTexture = Assets.getInstance().getTexture(backgroundTextureName);
		levelBoundaries = new Rectangle(0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
	}

	public void setup()
	{
		worldList = new ArrayList<>();
		teamList = new ArrayList<>();
		waySet = new HashSet<>();
		pathMap = new HashMap<>();
		Upgrade.getInstance().reset();
	}

	public Way getWay(World fromWorld, World toWorld)
	{
		for (Way r : waySet)
		{
			if ((r.getFromWorld().equals(fromWorld) && r.getToWorld().equals(toWorld))
					|| (r.getFromWorld().equals(toWorld) && r.getToWorld().equals(fromWorld)))
			{
				return r;
			}
		}
		return null;
	}

	protected void addWorld(World world)
	{
		worldList.add(world);
		pathMap.put(world, new Array<>());
	}

	protected void addTeam(Team team)
	{
		teamList.add(team);
	}

	protected void connectWorlds(World worldFrom, World worldTo)
	{
		Way path = createWay(worldFrom, worldTo);
		waySet.add(path);

		Array<Way> worldAPaths = pathMap.get(worldFrom);
		Array<Way> worldBPaths = pathMap.get(worldTo);
		worldAPaths.add(path);
		worldBPaths.add(path);
	}

	protected abstract Way createWay(World worldFrom, World worldTo);

	public abstract String getStartText();

	@Override
	public int getIndex(World node)
	{
		return worldList.indexOf(node);
	}

	@Override
	public int getNodeCount()
	{
		return worldList.size();
	}

	@Override
	public Array<Connection<World>> getConnections(World originWorld)
	{
		Objects.requireNonNull(nextPathfindingRequester);

		Array<Connection<World>> connections = new Array<>();
		Array<Way> paths = pathMap.get(originWorld);
		for (Way path : paths)
		{
			World fromWorld = path.getFromWorld();
			World toWorld = path.getToWorld();
			if (toWorld.equals(originWorld))
			{
				fromWorld = path.getToWorld();
				toWorld = path.getFromWorld();
			}

			if (fromWorld.getOwner().equals(nextPathfindingRequester))
			{
				connections.add(new DefaultConnection<>(originWorld, toWorld));
			}
		}

		return connections;
	}

	public MyGraphPath getPath(World fromWorld, World toWorld)
	{
		MyGraphPath path = new MyGraphPath();
		setNextPathfindingRequester(fromWorld.getOwner());
		pathfinder.searchNodePath(fromWorld, toWorld, HEURISTIC, path);
		setNextPathfindingRequester(null);
		path.generateSprite();
		return path;
	}

	public Set<GameObject> getGameObjectList()
	{
		Set<GameObject> gameObjectSet = new HashSet<>(worldList);
		for (Way path : waySet)
		{
			gameObjectSet.add(path);
		}
		return gameObjectSet;
	}

	public Vector2 getStartPoint()
	{
		return startPoint;
	}

	public boolean isWon()
	{
		// worlds
		for (World world : worldList)
		{
			if (!world.getOwner().isPlayer())
			{
				return false;
			}
		}
		// Soldiers
		for (GameObject o : GameObjects.getGameObjectsByTag(MyTags.UNIT))
		{
			if (o instanceof Unit)
			{
				if (!((Unit) o).getTeam().isPlayer())
				{
					return false;
				}
			}
		}
		return true;
	}

	public boolean isLost()
	{
		// worlds
		for (World world : worldList)
		{
			if (world.getOwner().isPlayer())
			{
				return false;
			}
		}
		// Soldiers
		for (GameObject o : GameObjects.getGameObjectsByTag(MyTags.UNIT))
		{
			if (o instanceof Unit)
			{
				if (((Unit) o).getTeam().isPlayer())
				{
					return false;
				}
			}
		}
		return true;
	}

	public Level nextLevel()
	{
		return nextLevel;
	}

	public void initPathfinder()
	{
		pathfinder = new IndexedAStarPathFinder<>(this);
	}

	public Rectangle getLevelBoundaries()
	{
		return levelBoundaries;
	}

	public void act()
	{
		Set<Discoverable> revealedObjects = new HashSet<>();
		for (Way path : waySet)
		{
			World from = path.getFromWorld();
			World to = path.getToWorld();

			from.setRevealed(false);
			to.setRevealed(false);

			boolean fromRevealed = from.getOwner().isPlayer();
			boolean toRevealed = to.getOwner().isPlayer();

			if (fromRevealed || toRevealed)
			{
				from.discover();
				to.discover();

				revealedObjects.add(from);
				revealedObjects.add(to);
			}
		}
		revealedObjects.forEach(d -> d.setRevealed(true));

		for (Team t : teamList)
		{
			t.act();
		}
	}

	public void drawDebug(ShapeRenderer debugShapes)
	{
		debugShapes.setColor(Color.YELLOW);
		debugShapes.rect(levelBoundaries.x, levelBoundaries.y, levelBoundaries.width, levelBoundaries.height);
	}

	public void setNextPathfindingRequester(Team nextPathfindingRequester)
	{
		this.nextPathfindingRequester = nextPathfindingRequester;
	}

	public Actor getBackgroundImage()
	{
		Sprite sprite = new Sprite(backgroundTexture);
		sprite.setPosition(levelBoundaries.x, levelBoundaries.y);

		Actor actor = new Actor()
		{
			@Override
			public void draw(Batch batch, float parentAlpha)
			{
				super.draw(batch, parentAlpha);
				sprite.draw(batch, parentAlpha);
			}
		};
		return actor;
	}

	public List<World> getWorlds()
	{
		return worldList;
	}

	public Team getTeam(String name)
	{
		for (Team t : teamList)
		{
			if (t.getName().equals(name))
			{
				return t;
			}
		}
		return null;
	}

	public abstract void playMusic();

	public int getNumber()
	{
		return number;
	}
}
