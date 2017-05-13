package de.skosnowich.ld38.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

import de.skosnowich.ld38.gameobject.impl.worlds.World;

public class ManhattanHeuristic implements Heuristic<World>
{

	@Override
	public float estimate(World node, World endNode)
	{
		return endNode.getWorldPosition().sub(node.getWorldPosition()).len();
	}

}
