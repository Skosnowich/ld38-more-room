package de.skosnowich.ld38.gameobject.impl.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.assets.Sounds;

public class Soldier extends Unit
{
	private Animation<Texture> animation;

	public Soldier(World world)
	{
		super(world);

		Array<Texture> keyFrames = new Array<>(4);
		keyFrames.add(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEXTURE_0));
		keyFrames.add(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEXTURE_1));
		keyFrames.add(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEXTURE_2));
		keyFrames.add(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEXTURE_3));
		animation = new Animation<>(0.125f, keyFrames, PlayMode.LOOP);
	}

	@Override
	protected void buildSprite()
	{
		buildSprite(new Sprite(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEXTURE_1)),
				new Sprite(Assets.getInstance().getTexture(AssetConstants.SOLDIER_TEAM_TEXTURE)));
	}

	@Override
	protected void updateSprite()
	{
		super.updateSprite();

		sprite.setTexture(animation.getKeyFrame(animationTime));
	}

	@Override
	protected void playDeathSound()
	{
		float volMod = 0.5f;
		if (getTeam().isPlayer())
		{
			volMod = 1;
		}
		Sounds.getInstance().playSound(AssetConstants.TROOPS_DIE_MEDIEVAL_SOUND, volMod * 0.2f);
	}

}
