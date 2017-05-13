package de.skosnowich.ld38.stage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class Selection implements Iterable<World>
{

	private Set<World> selectedWorlds;

	public Selection()
	{
		selectedWorlds = new HashSet<>();
	}

	public void add(World world)
	{
		selectedWorlds.add(world);
		world.setSelected(true);
	}

	@Override
	public String toString()
	{
		return String.format("Selection [selectedWorlds=%s]", selectedWorlds);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((selectedWorlds == null) ? 0 : selectedWorlds.hashCode());
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
		if (!(obj instanceof Selection))
		{
			return false;
		}
		Selection other = (Selection) obj;
		if (selectedWorlds == null)
		{
			if (other.selectedWorlds != null)
			{
				return false;
			}
		}
		else if (!selectedWorlds.equals(other.selectedWorlds))
		{
			return false;
		}
		return true;
	}

	public void clear()
	{
		for (World world : selectedWorlds)
		{
			world.setSelected(false);
		}
		selectedWorlds.clear();
	}

	public boolean isEmpty()
	{
		return selectedWorlds.isEmpty();
	}

	@Override
	public Iterator<World> iterator()
	{
		return selectedWorlds.iterator();
	}

}
