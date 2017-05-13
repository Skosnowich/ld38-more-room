package de.skosnowich.ld38.gameobject.impl.units;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import de.skosnowich.ld38.AssetConstants;
import de.skosnowich.ld38.gameobject.MyDynamicGameObject;
import de.skosnowich.ld38.gameobject.Upgrade;
import de.skosnowich.ld38.gameobject.impl.MyTags;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.ways.Way;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.libgdx.gameobjects.impl.ParticleEmitter;
import de.skosnowich.libgdx.physics.graphics.CompositeSprite;
import de.skosnowich.libgdx.utils.ShapeUtils;

public abstract class Unit extends MyDynamicGameObject
{

	private static final float MIN_AMPLITUDE = 2.5f;
	private static final float MAX_AMPLITUDE = 5f;

	private boolean revealed;

	private float startOffset;
	private final Team team;

	private GraphPath<World> path;
	private int currentDestination;
	private Vector2 generalDirectionVertical;
	private float theta, amplitude;
	private Way currentWay = null;

	private float maxHealth, health, strength, regeneration, regenerationTimer;

	private Sprite baseSprite;
	private Sprite teamSprite;

	public Unit(World world)
	{
		super("unit", world.getWorldPosition().x, world.getWorldPosition().y, 20);
		addTag(MyTags.UNIT);
		team = world.getOwner();
		generalDirectionVertical = new Vector2();
		regenerationTimer = 0;
		amplitude = MathUtils.random(MIN_AMPLITUDE, MAX_AMPLITUDE);
		move(0, null);

		if (team.isPlayer())
		{
			health = maxHealth = Upgrade.getInstance().getHealth();
			strength = Upgrade.getInstance().getStrength();
			regeneration = Upgrade.getInstance().getRegeneration();
			setSpeedModifier(Upgrade.getInstance().getSpeed());
		}
		else
		{
			health = maxHealth = 100;
			strength = 100;
			regeneration = 0;
		}

		buildSprite();
		setBoundingPolygons(new Polygon[] { ShapeUtils.getCircleShape(0, 0, 10) });
	}

	protected abstract void buildSprite();

	protected void buildSprite(Sprite base, Sprite teamOverlay)
	{
		baseSprite = base;
		teamSprite = teamOverlay;

		teamOverlay.setColor(team.getColor());

		CompositeSprite compositeSprite = new CompositeSprite(base);
		compositeSprite.addSprite(teamOverlay);

		sprite = compositeSprite;
	}

	public void move(float offset, GraphPath<World> p)
	{
		currentDestination = 0;
		startOffset = offset;
		path = p;
		theta = MathUtils.random(0, MathUtils.PI2);

		// Upgrades?
		if (team.isPlayer())
		{
			maxHealth = Upgrade.getInstance().getHealth();
			strength = Upgrade.getInstance().getStrength();
			regeneration = Upgrade.getInstance().getRegeneration();
			setSpeedModifier(Upgrade.getInstance().getSpeed());
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (revealed)
		{
			super.draw(batch, parentAlpha);
		}
	}

	@Override
	protected void drawCustomDebug(ShapeRenderer shapes)
	{
		super.drawCustomDebug(shapes);

		shapes.setColor(team.getColor());
		shapes.circle(getX(), getY(), 2f);
	}

	@Override
	public void act(float delta)
	{
		checkRevealation();

		// Regeneration
		if (health < maxHealth)
		{
			regenerationTimer += delta;
			if (regenerationTimer > 1)
			{
				regenerationTimer = 0;
				health += regeneration;
				if (health > maxHealth)
				{
					health = maxHealth;
				}
			}
		}

		if (isVisible() && isAlive())
		{
			super.act(delta);
			if (startOffset > 0)
			{
				startOffset -= delta;
			}
			else
			{
				// sin/cos
				theta += 0.05f;
				theta %= MathUtils.PI2;

				if (path != null && currentDestination == 0)
				{
					reachCurrentDestination(getCurrentDestination());
				}
			}
		}
	}

	public void reachCurrentDestination(World world)
	{
		if (!world.getOwner().equals(team))
		{
			attack(world);
		}
		else
		{
			if (currentWay != null)
			{
				currentWay.removeUnit(this);
				currentWay = null;
			}
			currentDestination++;
			if (currentDestination >= path.getCount())
			{ // Ziel erreicht
				path = null;
				if (world.getOwner().equals(team))
				{
					world.addUnit(this);
				}
				else
				{
					System.err.println("shouldn't be possible " + this);
				}
			}
			else
			{
				lookAt(path.get(currentDestination).getWorldPosition());
				moveForward();
				generalDirectionVertical = getWorldVelocity().cpy().rotate90(1).nor();
				currentWay = getStage().getLevel().getWay(path.get(currentDestination), world);
				currentWay.addUnit(this);
			}
		}
	}

	@Override
	protected void updateSprite()
	{
		float yOffset = MathUtils.sin(theta) * amplitude;
		Vector2 spritePosition = new Vector2(getX() - sprite.getWidth() / 2, getY() - sprite.getHeight() / 2).add(generalDirectionVertical.cpy().scl(yOffset));
		sprite.setPosition(spritePosition.x, spritePosition.y);

		if (getWorldVelocity().x > 0.1f)
		{
			sprite.setFlip(false, false);
		}
		else if (getWorldVelocity().x < -0.1f)
		{
			sprite.setFlip(true, false);
		}
		else
		{
			// +0.2f damit etwas in die Zukunft geschaut wird -> drehen sich früher um
			if (MathUtils.cos(theta + 0.2f) < 0)
			{
				sprite.setFlip(true, false);
			}
			else
			{
				sprite.setFlip(false, false);
			}
		}
	}

	private void checkRevealation()
	{
		if (path != null)
		{
			World comingFrom = currentDestination > 0 ? path.get(currentDestination - 1) : null;
			World destination = currentDestination < path.getCount() ? path.get(currentDestination) : null; // TODO IndexOutOfBoundsException hier.. wie??

			if (destination != null && destination.isRevealed() || comingFrom != null && comingFrom.isRevealed()
					|| currentWay != null && currentWay.isRevealed())
			{
				revealed = true;
			}
			else
			{
				revealed = false;
			}
		}
	}

	public boolean isAlive()
	{
		return health > 0;
	}

	/**
	 * Dieser Soldat kämpft gegen den übergebenen bis zum Tod.
	 *
	 * @param enemy
	 * @return -1= Feind hat gewonnen, 0= beide Tot, 1=Sieg
	 */
	public int fight(Unit enemy)
	{
		int ret = 0;
		boolean loop = true;
		while (loop)
		{
			float ownPower = getPower();
			if (harm(enemy.getPower()))
			{
				loop = false;
				ret--;
			}
			if (enemy.harm(ownPower))
			{
				loop = false;
				ret++;
			}
		}
		if (ret != -1 && team.isPlayer())
		{
			Upgrade.getInstance().addMoney(1);
		}
		return ret;
	}

	public void attack(World world)
	{
		boolean loop = true;
		while (loop)
		{
			Unit defender = world.sendUnitOut();
			if (defender == null)
			{
				team.addWorld(world);
				Team oldOwner = world.getOwner();
				oldOwner.removeWorld(world);
				world.setOwner(team);
				if (team.isPlayer())
				{
					world.playWorldCapturedSound();
				}
				else if (oldOwner.isPlayer())
				{
					world.playWorldLostSound();
				}
				world.addUnit(this);
				loop = false;
				if (team.isPlayer())
				{
					Upgrade.getInstance().addMoney((int) world.getSize());
				}
			}
			else
			{
				int result = fight(defender);
				if (result != 1)
				{ // Leider gestorben
					loop = false;
				}
				if (result == -1)
				{ // andere Soldat hat überlebt -> wieder in die Welt.
					world.addUnit(defender);
				}
			}
		}
	}

	/**
	 * Verletzt den Soldaten um den gegebenen Wert. Wenn er dabei stirbt, wird er entfernt und true zurückgegeben.
	 *
	 * @param value
	 * @return true, wenn Soldat gestorben.
	 */
	public boolean harm(float value)
	{
		health -= value;
		if (!isAlive())
		{
			float particleX = getX();
			float particleY = getY();
			if (currentWay != null)
			{
				particleX = sprite.getX() + sprite.getWidth() / 2;
				particleY = sprite.getY() + sprite.getHeight() / 2;
			}
			if (revealed)
			{
				playDeathSound();
			}
			ParticleEmitter pe = new ParticleEmitter(particleX, particleY,
					AssetConstants.DIE_PARTICLES, false);
			pe.setColor(team.getColor());
			if (getStage() != null)
			{ // TODO: warum??
				getStage().addActor(pe);
			}
			if (currentWay != null)
			{
				currentWay.removeUnit(this);
			}
			remove();
			return true;
		}
		return false;
	}

	protected abstract void playDeathSound();

	public float getPower()
	{
		float res = strength * (health / maxHealth);
		return res < 1 ? 1 : res;
	}

	public Team getTeam()
	{
		return team;
	}

	@Override
	public void stop()
	{
		super.stop();
	}

	public World getCurrentDestination()
	{
		return path != null ? path.get(currentDestination) : null;
	}
}
