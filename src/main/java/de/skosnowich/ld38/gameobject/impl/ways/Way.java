package de.skosnowich.ld38.gameobject.impl.ways;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.gameobject.MyGameObject;
import de.skosnowich.ld38.gameobject.impl.MyTags;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Unit;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.ld38.level.Discoverable;

public abstract class Way extends MyGameObject implements Discoverable
{
	private World fromNode;
	private World toNode;

	private float length;

	private boolean discovered;
	private boolean revealed;

	private Set<Unit> unitSet;

	public Way(World fromNode, World toNode)
	{
		super("Way from " + fromNode.getName() + " to " + toNode.getName(), fromNode.getX() + toNode.getX() / 2.0f, fromNode.getY() + toNode.getY() / 2.0f);
		this.fromNode = fromNode;
		this.toNode = toNode;
		length = Vector2.dst(toNode.getX(), toNode.getY(), fromNode.getX(), fromNode.getY());
		unitSet = new HashSet<>();
		addTag(MyTags.PATH);
	}

	public void addUnit(Unit unit)
	{
		unitSet.add(unit);
	}

	public void removeUnit(Unit unit)
	{
		unitSet.remove(unit);
	}

	public Set<Team> getTeams()
	{
		Set<Team> teams = new HashSet<>();
		for (Unit s : unitSet)
		{
			teams.add(s.getTeam());
		}
		return teams;
	}

	@Override
	public void discover()
	{
		discovered = true;
	}

	@Override
	public void setRevealed(boolean revealed)
	{
		this.revealed = revealed;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (fromNode.isRevealed() || toNode.isRevealed() || getTeams().stream().anyMatch(Team::isPlayer))
		{
			discover();
			setRevealed(true);
		}
		else
		{
			setRevealed(false);
		}

		if (!unitSet.isEmpty())
		{
			evaluateUnitPositions();
		}
	}

	private void evaluateUnitPositions()
	{
		Unit[] units = unitSet.toArray(new Unit[unitSet.size()]);
		float[] distances = new float[unitSet.size()];

		for (int i = 0; i < units.length; i++)
		{
			float passedDistance = Vector2.dst(units[i].getX(), units[i].getY(), fromNode.getX(), fromNode.getY());
			distances[i] = passedDistance;
		}

		for (int i = 0; i < units.length; i++)
		{
			Unit unit = units[i];
			if (!unit.isAlive())
			{
				continue;
			}
			float distance = distances[i];
			for (int j = i + 1; j < units.length; j++)
			{
				Unit otherUnit = units[j];
				float otherDistance = distances[j];
				if (MathUtils.isEqual(distance, otherDistance, 5))
				{
					if (!otherUnit.getTeam().equals(unit.getTeam()) && otherUnit.isAlive())
					{
						unit.fight(otherUnit);
						break; // immer nur einmal kämpfen
					}
				}
			}

			World currentDestination = unit.getCurrentDestination();
			int distanceFromDestination = 1;
			if (currentDestination.equals(toNode) && distance >= length - distanceFromDestination
					|| currentDestination.equals(fromNode) && distance <= distanceFromDestination)
			{
				unit.reachCurrentDestination(currentDestination);
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (discovered)
		{
			super.draw(batch, parentAlpha);
		}
	}

	@Override
	protected void drawCustomDebug(ShapeRenderer shapes)
	{
		super.drawCustomDebug(shapes);

		shapes.setColor(Color.BROWN);
		shapes.line(fromNode.getWorldPosition(), toNode.getWorldPosition());
	}

	@Override
	public boolean isRevealed()
	{
		return revealed;
	}

	@Override
	protected void updateSprite()
	{
		if (sprite != null)
		{
			sprite.setAlpha(revealed ? 1 : 0.4f);
		}
	}

	public World getFromWorld()
	{
		return fromNode;
	}

	public World getToWorld()
	{
		return toNode;
	}

}
