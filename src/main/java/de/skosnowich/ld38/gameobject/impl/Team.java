package de.skosnowich.ld38.gameobject.impl;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.ld38.level.Level;

public class Team
{
	public enum KI
	{
		/**
		 * Es passiert gar nichts.
		 */
		PASSIVE,
		/**
		 * Wenn feindliche Truppen auf dem Weg sind, werden eigene entgegen geschickt.
		 */
		DEFENSIVE,
		/**
		 * Zusätzlich zu DEFENSIVE wird angegriffen, wenn mehr als doppelt soviele Truppen in der Welt sind als in der Gegnerwelt.
		 */
		AGGRESSIVE,
		/**
		 * Zusätzlich zu AGGRESSIVE wird angegriffen, solange Truppen da sind.
		 */
		KAMIKAZE;
	}

	private final String name;
	private final Color color;
	private KI ki;
	private Set<World> myWorlds;
	private final Level level;

	public Team(String name, Color color, Level level, KI ki)
	{
		this.name = name;
		this.color = color;
		this.ki = ki;
		this.level = level;
		myWorlds = new HashSet<>();
	}

	public Team(String name, Color color)
	{
		this(name, color, null, null);
	}

	public void act()
	{
		if (isPlayer() || myWorlds.isEmpty() || ki == KI.PASSIVE)
		{
			return;
		}
		for (World world : myWorlds)
		{
			level.setNextPathfindingRequester(this);
			Array<Connection<World>> neighbors = level.getConnections(world);
			level.setNextPathfindingRequester(null);
			boolean enemyNearby = false;
			for (Connection<World> c : neighbors)
			{
				World neighbor = c.getToNode();
				if (!neighbor.getOwner().equals(this))
				{ // Feind
					enemyNearby = true;
					// Truppen entgegenschicken
					Set<Team> teams = level.getWay(world, neighbor).getTeams();
					if (!teams.contains(this) && !teams.isEmpty())
					{
						world.sendUnits(level.getPath(world, neighbor));
					}
					if (ki == KI.DEFENSIVE)
					{
						continue;
					}
					// Angreifen bei min doppelt so vielen Truppen
					if (neighbor.getUnitCount() < world.getUnitCount() / 2)
					{
						world.sendUnits(level.getPath(world, neighbor));
					}
					if (ki == KI.AGGRESSIVE)
					{
						continue;
					}
					// Angreifen, solange Truppen da sind
					if (world.getUnitCount() > 1)
					{
						world.sendUnits(level.getPath(world, neighbor));
					}
				}
			}
			if (!enemyNearby && world.isFull())
			{
				for (World destination : myWorlds)
				{
					if (!destination.isFull())
					{
						// Betrachte Nachbarn
						boolean isFront = false;
						level.setNextPathfindingRequester(this);
						Array<Connection<World>> destNeighbors = level.getConnections(destination);
						level.setNextPathfindingRequester(null);
						for (Connection<World> c : destNeighbors)
						{
							World enemy = c.getToNode();
							if (!enemy.getOwner().equals(this))
							{ // Feind
								isFront = true;
								break;
							}
						}
						if (isFront)
						{
							world.sendUnits(level.getPath(world, destination));
						}
					}
				}
			}
		}
	}

	public void addWorld(World world)
	{
		myWorlds.add(world);
	}

	public void removeWorld(World world)
	{
		myWorlds.remove(world);
	}

	public Color getColor()
	{
		return color;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Team))
		{
			return false;
		}
		Team other = (Team) obj;
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("Team [name=%s, color=%s]", name, color);
	}

	public boolean isPlayer()
	{
		return ki == null;
	}

	public void setKi(KI ki)
	{
		this.ki = ki;
	}

	public KI getKi()
	{
		return ki;
	}

	public String getName()
	{
		return name;
	}

}
