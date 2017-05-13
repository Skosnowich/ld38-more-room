package de.skosnowich.ld38.gameobject.impl.worlds;

import java.util.Stack;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.skosnowich.ld38.gameobject.MyGameObject;
import de.skosnowich.ld38.gameobject.impl.MyTags;
import de.skosnowich.ld38.gameobject.impl.Team;
import de.skosnowich.ld38.gameobject.impl.units.Unit;
import de.skosnowich.ld38.level.Discoverable;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.physics.graphics.CompositeSprite;

public abstract class World extends MyGameObject implements Discoverable
{
	private Sprite baseSprite;
	private Sprite teamSprite;

	private Sprite outlineSprite;

	private final float increaseTimeNeeded;
	private float increaseTimer;
	private float decreaseTimer;
	private final int size;
	private final Stack<Unit> units;
	private Team owner;
	private final Label label;

	private boolean discovered;

	private boolean selected;
	private boolean revealed;

	/**
	 * Erstellt eine Welt
	 *
	 * @param name
	 * @param x
	 *            pos
	 * @param y
	 *            pos
	 * @param size
	 *            größe der Welt, damit gleichzeitig auch max {@link Unit} Anzahl
	 * @param increaseTimeNeeded
	 *            Zeit für eine neu Truppe in Sekunden
	 * @param owner
	 */
	public World(String name, float x, float y, int size, float increaseTimeNeeded, Team owner)
	{
		super(name, x, y);
		this.size = size;
		this.increaseTimeNeeded = increaseTimeNeeded;
		this.owner = owner;
		owner.addWorld(this);
		addTag(MyTags.WORLD);
		units = new Stack<>();
		increaseTimer = 0;
		decreaseTimer = 0;

		label = new Label("0/0", Assets.getInstance().getSkin(), "font8");

		// DEBUG
		for (int i = 0; i < 5; i++)
		{
			addNewUnit();
		}

	}

	@Override
	public void setup()
	{
		super.setup();

		addActor(label);
	}

	protected void buildSprite(Sprite base, Sprite team)
	{
		baseSprite = base;
		teamSprite = team;

		CompositeSprite compositeSprite = new CompositeSprite(base);
		compositeSprite.addSprite(team);

		sprite = compositeSprite;

		label.setPosition(5 + sprite.getWidth() / 2, -label.getHeight() / 2);

		// create outline sprite
		TextureData textureData = baseSprite.getTexture().getTextureData();
		if (!textureData.isPrepared())
		{
			textureData.prepare();
		}
		int outlineThickness = 4;
		Pixmap spritePixmap = textureData.consumePixmap();
		Pixmap outlinePixmap = new Pixmap(spritePixmap.getWidth() + outlineThickness, spritePixmap.getHeight() + outlineThickness, spritePixmap.getFormat());

		for (int x = -outlineThickness / 2; x < spritePixmap.getWidth() + outlineThickness / 2 - 1; x++)
		{
			for (int y = -outlineThickness / 2; y < spritePixmap.getHeight() + outlineThickness / 2 - 1; y++)
			{
				float a = getA(spritePixmap, x, y);

				if (a == 0)
				{
					boolean oneFilled = false;
					for (int i = -outlineThickness / 2; i <= outlineThickness / 2 && !oneFilled; i++)
					{
						if (i != 0)
						{
							float h = getA(spritePixmap, x + i, y);
							float v = getA(spritePixmap, x, y + i);

							oneFilled = h > 0 || v > 0;
							if (oneFilled)
							{
								outlinePixmap.drawPixel(x + outlineThickness / 2, y + outlineThickness / 2, Color.rgba8888(1, 1, 1, 1));
							}
						}
					}
				}
			}
		}

		outlineSprite = new Sprite(new Texture(outlinePixmap));
		outlineSprite.setPosition(getX() - outlineSprite.getWidth() / 2, getY() - outlineSprite.getHeight() / 2);
		outlineSprite.setColor(Color.PINK);

		outlinePixmap.dispose();
		spritePixmap.dispose();
	}

	private float getA(Pixmap spritePixmap, int x, int y)
	{
		if (x < 0 || x >= spritePixmap.getWidth() || y < 0 || y >= spritePixmap.getHeight())
		{
			return 0;
		}
		return new Color(spritePixmap.getPixel(x, y)).a;
	}

	public float getSize()
	{
		return size;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (getUnitCount() < size)
		{
			increaseTimer += delta;

			if (increaseTimer > increaseTimeNeeded)
			{
				increaseTimer = 0;
				addNewUnit();
			}
		}
		else if (getUnitCount() > size)
		{
			decreaseTimer += delta;

			float decreaseTimeNeeded = 2 * increaseTimeNeeded * size / getUnitCount();
			if (decreaseTimer > decreaseTimeNeeded)
			{
				decreaseTimer = 0;
				getUnit().remove();
			}
		}
		else
		{
			// Timer zurücksetzen, da ansonsten übriggebliebene Zeit genutzt wird
			increaseTimer = 0;
			decreaseTimer = 0;
		}

		label.setVisible(revealed);
	}

	private void updateLabel()
	{
		label.setText(getUnitCount() + "/" + size);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (discovered)
		{
			super.draw(batch, parentAlpha);
		}
		if (selected)
		{
			outlineSprite.draw(batch, parentAlpha);
		}
	}

	@Override
	protected void drawCustomDebug(ShapeRenderer shapes)
	{
		super.drawCustomDebug(shapes);

		shapes.setColor(owner.getColor());
		shapes.circle(getX(), getY(), size);
		if (selected)
		{
			shapes.setColor(Color.YELLOW);
			shapes.circle(getX(), getY(), size + 2);
			shapes.circle(getX(), getY(), size + 1.5f);
		}
	}

	public void sendUnits(GraphPath<World> path)
	{
		if (path.getCount() <= 0)
		{
			return;
		}
		int c = getUnitCount() / 2;
		if (c <= 0)
		{
			return;
		}

		float factor = 1f / c;
		if (owner.isPlayer())
		{
			playSendTroopsSound();
		}
		while (c > 0)
		{
			Unit s = sendUnitOut();
			s.move(factor * (c - 1), path);
			c--;
		}
	}

	protected abstract void playSendTroopsSound();

	public abstract void addNewUnit();

	public void addUnit(Unit s)
	{
		s.setVisible(false);
		s.stop();
		units.push(s);
		updateLabel();
	}

	public Unit sendUnitOut()
	{
		Unit s = getUnit();
		if (s != null)
		{
			s.setVisible(true);
			getStage().addActor(s);
		}
		return s;
	}

	private Unit getUnit()
	{
		if (getUnitCount() <= 0)
		{
			return null;
		}
		Unit s = units.pop();
		updateLabel();
		return s;
	}

	public int getUnitCount()
	{
		return units.size();
	}

	public boolean isFull()
	{
		return units.size() >= size;
	}

	public void setOwner(Team team)
	{
		owner = team;
	}

	public Team getOwner()
	{
		return owner;
	}

	@Override
	public String toString()
	{
		return String.format("World [name=%s]", getName());
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
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
	public boolean isRevealed()
	{
		return revealed;
	}

	@Override
	protected void updateSprite()
	{
		super.updateSprite();

		sprite.setAlpha(revealed ? 1 : 0.4f);
		teamSprite.setColor(getOwner().getColor());
		teamSprite.setAlpha(revealed ? 1 : 0);
	}

	public abstract void playWorldLostSound();

	public abstract void playWorldCapturedSound();
}
