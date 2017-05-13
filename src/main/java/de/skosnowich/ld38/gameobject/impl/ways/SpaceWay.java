package de.skosnowich.ld38.gameobject.impl.ways;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.physics.graphics.CompositeSprite;

public class SpaceWay extends Way
{

	public SpaceWay(World fromNode, World toNode)
	{
		super(fromNode, toNode);

		Vector2 fromPos = fromNode.getWorldPosition();
		Vector2 toPos = toNode.getWorldPosition();
		Vector2 direction = toPos.cpy().sub(fromPos);
		Vector2 orthoDirection = direction.cpy().rotate90(1).nor();

		CompositeSprite compoSprite = null;

		float pathLength = direction.len();
		float stepSize = 40;
		float startOffset = 40;
		float endOffset = 40;
		float variance = 0;
		Random random = new Random();
		for (float i = startOffset; i < pathLength - endOffset; i += stepSize)
		{
			Vector2 pos = fromPos.cpy().add(direction.cpy().scl(i / pathLength));
			pos.add(orthoDirection.cpy().scl(random.nextFloat() * variance * 2 - variance));
			Sprite wayTile = new Sprite(
					Assets.getInstance().getTexture(AssetConstants.SPACEWAY_TEXTURES[random.nextInt(AssetConstants.SPACEWAY_TEXTURES.length)]));
			wayTile.setPosition(pos.x - wayTile.getWidth() / 2, pos.y - wayTile.getHeight() / 2);
			wayTile.rotate(direction.angle() + 90);
			if (compoSprite == null)
			{
				compoSprite = new CompositeSprite(wayTile);
			}
			else
			{
				compoSprite.addSprite(wayTile);
			}
		}

		sprite = compoSprite;
	}

}
