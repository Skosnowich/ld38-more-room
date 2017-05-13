package de.skosnowich.ld38.gameobject.impl.units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.gameobjects.impl.ParticleEmitter;

public class Spaceship extends Unit
{
	private ParticleEmitter thrusterParticles;

	public Spaceship(World world)
	{
		super(world);
	}

	@Override
	public void setup()
	{
		super.setup();
		thrusterParticles = new ParticleEmitter(0, 0, AssetConstants.THRUSTER_PARTICLES, true);
		addActor(thrusterParticles);
	}

	@Override
	protected void buildSprite()
	{

		buildSprite(new Sprite(Assets.getInstance().getTexture(AssetConstants.SPACESHIP_TEXTURE)),
				new Sprite(Assets.getInstance().getTexture(AssetConstants.SPACESHIP_TEAM_TEXTURE)));
	}

	@Override
	protected void updateSprite()
	{
		super.updateSprite();

		float mod = sprite.isFlipX() ? -1 : 1;
		Vector2 offset = new Vector2(sprite.getX() - getX() - sprite.getWidth() / 2 * mod + 8, sprite.getY() - getY() + sprite.getHeight() / 2);

		offset.rotate(-getWorldRotation());

		thrusterParticles.setPosition(offset.x, offset.y);

		if (getWorldVelocity().x != 0 || getWorldVelocity().y != 0)
		{
			thrusterParticles.setVisible(true);
		}
		else
		{
			thrusterParticles.setVisible(false);
		}
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);

		if (!visible && thrusterParticles != null)
		{
			thrusterParticles.remove();
			thrusterParticles = null;
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
		Sounds.getInstance().playSound(AssetConstants.TROOPS_DIE_SPACE_SOUND, volMod * 0.2f);
	}

}
