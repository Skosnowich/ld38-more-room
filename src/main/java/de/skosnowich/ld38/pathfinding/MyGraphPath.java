package de.skosnowich.ld38.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Assets;

public class MyGraphPath extends DefaultGraphPath<World>
{

	private Array<Sprite> sprites;

	@Override
	public String toString()
	{
		return String.format("MyGraphPath [nodes=%s]", nodes);
	}

	public void draw(Batch batch)
	{
		for (Sprite sprite : sprites)
		{
			sprite.draw(batch);
		}
	}

	public void drawDebug(ShapeRenderer debugShapes)
	{
		debugShapes.setColor(Color.MAGENTA);
		for (int i = 0; i < nodes.size - 1; i++)
		{
			debugShapes.line(nodes.get(i).getWorldPosition(), nodes.get(i + 1).getWorldPosition());
		}

	}

	public void generateSprite()
	{
		sprites = new Array<>(Math.max(0, 3 * (nodes.size - 1)));

		if (nodes.size > 1)
		{
			String middleTexture = AssetConstants.ARROW_HOSTILE_MIDDLE_TEXTURE;
			String startTexture = AssetConstants.ARROW_HOSTILE_START_TEXTURE;
			String endTexture = AssetConstants.ARROW_HOSTILE_END_TEXTURE;
			if (nodes.get(0).getOwner().equals(nodes.get(nodes.size - 1).getOwner()))
			{
				middleTexture = AssetConstants.ARROW_FRIENDLY_MIDDLE_TEXTURE;
				startTexture = AssetConstants.ARROW_FRIENDLY_START_TEXTURE;
				endTexture = AssetConstants.ARROW_FRIENDLY_END_TEXTURE;
			}

			for (int i = 0; i < nodes.size - 1; i++)
			{
				Vector2 from = nodes.get(i).getWorldPosition();
				Vector2 to = nodes.get(i + 1).getWorldPosition();

				Vector2 direction = to.cpy().sub(from);
				Vector2 middle = direction.cpy().scl(0.5f).add(from);

				Sprite arrowMiddle = new Sprite(Assets.getInstance().getTexture(middleTexture));
				arrowMiddle.setSize(direction.len() * 0.8f, arrowMiddle.getRegionHeight());

				float midArrowWidthHalf = arrowMiddle.getWidth() / 2;
				float midArrowHeightHalf = arrowMiddle.getHeight() / 2;
				arrowMiddle.setOrigin(midArrowWidthHalf, midArrowHeightHalf);
				arrowMiddle.setPosition(middle.x - midArrowWidthHalf, middle.y - midArrowHeightHalf);

				Sprite arrowStart = new Sprite(Assets.getInstance().getTexture(startTexture));
				arrowStart.setPosition(middle.x - midArrowWidthHalf - arrowStart.getWidth(), middle.y - arrowStart.getHeight() / 2);
				arrowStart.setOrigin(midArrowWidthHalf + arrowStart.getWidth(), midArrowHeightHalf);

				Sprite arrowEnd = new Sprite(Assets.getInstance().getTexture(endTexture));
				arrowEnd.setPosition(middle.x + midArrowWidthHalf, middle.y - arrowEnd.getHeight() / 2);
				arrowEnd.setOrigin(-midArrowWidthHalf, midArrowHeightHalf);

				arrowMiddle.rotate(direction.angle());
				arrowStart.rotate(direction.angle());
				arrowEnd.rotate(direction.angle());

				sprites.add(arrowEnd);
				sprites.add(arrowMiddle);
				sprites.add(arrowStart);
				float alpha = 0.7f;
				arrowEnd.setAlpha(alpha);
				arrowMiddle.setAlpha(alpha);
				arrowStart.setAlpha(alpha);
			}
		}
	}
}
