package de.skosnowich.ld38.gameobject.impl.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.gameobjects.impl.ParticleEmitter;

public class Ship extends Unit
{
	private ParticleEmitter waterParticles;

	public Ship(World world)
	{
		super(world);
	}

	@Override
	public void setup()
	{
		super.setup();
		waterParticles = new ParticleEmitter(0, 0, AssetConstants.THRUSTER_PARTICLES, true);
		waterParticles.setColor(Color.WHITE);
		addActor(waterParticles);
	}

	@Override
	protected void buildSprite()
	{

		buildSprite(new Sprite(Assets.getInstance().getTexture(AssetConstants.SHIP_TEXTURE)),
				new Sprite(Assets.getInstance().getTexture(AssetConstants.SHIP_TEAM_TEXTURE)));
	}

	@Override
	protected void updateSprite()
	{
		super.updateSprite();

		float mod = sprite.isFlipX() ? -1 : 1;
		Vector2 offset = new Vector2(sprite.getX() - getX() - sprite.getWidth() / 2 * mod + 8, sprite.getY() - getY() + sprite.getHeight() / 2 - 3);

		offset.rotate(-getWorldRotation());

		waterParticles.setPosition(offset.x, offset.y);

		if (getWorldVelocity().x != 0 || getWorldVelocity().y != 0)
		{
			waterParticles.setVisible(true);
		}
		else
		{
			waterParticles.setVisible(false);
		}
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);

		if (!visible && waterParticles != null)
		{
			waterParticles.remove();
			waterParticles = null;
		}
	}

	@Override
	protected void playDeathSound()
	{
		float volMod = 0.5f;
		if (getTeam().isPlayer())
		{
			volMod = 1;
		}
		Sounds.getInstance().playSound(AssetConstants.TROOPS_DIE_ISLAND_SOUND, volMod * 0.4f);
	}

}
