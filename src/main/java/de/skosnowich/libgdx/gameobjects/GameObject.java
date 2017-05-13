package de.skosnowich.libgdx.gameobjects;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import de.skosnowich.libgdx.physics.Physics;
import de.skosnowich.libgdx.stage.GameObjectsGameStage;
import de.skosnowich.libgdx.utils.ShapeUtils;

/**
 *
 */
public abstract class GameObject extends Group
{
	private static long lastId = 0L;

	private final long id;
	protected float animationTime;
	protected float animationTimeFactor = 1;

	protected Sprite sprite;

	private Set<String> tags;

	private Rectangle boundingRectangle;
	private Polygon[] boundingPolygons;

	private Set<String> ignoreCollisionWithTagSet;

	private boolean lockSpriteTo8Directions;

	public GameObject(String name, float x, float y, boolean lockSpriteTo8Directions)
	{
		this.lockSpriteTo8Directions = lockSpriteTo8Directions;
		id = lastId++;

		setName(name + "-" + id); // TODO changing after initialization is dangerous...
		tags = new HashSet<>();
		ignoreCollisionWithTagSet = new HashSet<>();

		setPosition(x, y);

		GameObjects.addGameObject(this);
	}

	public GameObject(String name, float x, float y)
	{
		this(name, x, y, false);
	}

	public void setup()
	{
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		animationTime += delta * animationTimeFactor;
	}

	public void setAnimationTimeFactor(float animationTimeFactor)
	{
		this.animationTimeFactor = animationTimeFactor;
	}

	public void addTag(String tag)
	{
		tags.add(tag);
		GameObjects.addTagToGameObject(this, tag);
	}

	public void removeTag(String tag)
	{
		tags.remove(tag);
		GameObjects.removeTagFromGameObject(this, tag);
	}

	@Override
	public void setPosition(float x, float y)
	{
		setPosition(x, y, Align.center);
	}

	public Vector2 getLocalPosition()
	{
		return new Vector2(getX(), getY());
	}

	public Vector2 getWorldPosition()
	{
		Vector2 worldPosition = getLocalPosition();

		Actor actor = this;
		Group parent = actor.getParent();
		while (parent != null)
		{
			float parentX = parent.getX();
			float parentY = parent.getY();
			float parentRotation = parent.getRotation();
			worldPosition.rotate(parentRotation);
			worldPosition.add(parentX, parentY);

			actor = parent;
			parent = actor.getParent();
		}

		return worldPosition;
	}

	public float getWorldRotation() // TODO needs tests
	{
		float worldRotation = getRotation();

		Actor actor = this;
		Group parent = actor.getParent();
		while (parent != null)
		{
			float parentRotation = parent.getRotation();
			worldRotation += parentRotation;

			actor = parent;
			parent = actor.getParent();
		}

		return worldRotation;
	}

	public void setWorldPosition(float x, float y)
	{
		setWorldPosition(new Vector2(x, y));
	}

	public void setWorldPosition(Vector2 position)
	{
		Group parent = getParent();
		float parentX = parent == null ? 0 : parent.getX();
		float parentY = parent == null ? 0 : parent.getY();
		float parentRotation = parent == null ? 0 : -parent.getRotation();
		Vector2 newPosition = new Vector2(position.x - parentX, position.y - parentY);
		newPosition.rotate(parentRotation);
		setPosition(newPosition.x, newPosition.y);
	}

	@Override
	public float getX()
	{
		return getX(Align.center);
	}

	@Override
	public float getY()
	{
		return getY(Align.center);
	}

	protected void updateSprite()
	{
		sprite.setPosition(getX() - sprite.getWidth() / 2, getY() - sprite.getHeight() / 2);
		if (lockSpriteTo8Directions)
		{
			int rotation = Math.round(getRotation() / 45.0f);
			rotation *= 45;
			sprite.setRotation(rotation);
		}
		else
		{
			sprite.setRotation(getRotation());
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if (sprite != null)
		{
			updateSprite();
			sprite.draw(batch);
		}
	}

	@Override
	public final void drawDebug(ShapeRenderer shapes)
	{
		if (getDebug())
		{
			super.drawDebug(shapes);

			drawCustomDebug(shapes);
		}
	}

	protected void drawCustomDebug(ShapeRenderer shapes)
	{
		if (boundingPolygons != null && boundingPolygons.length > 0)
		{
			// draw bounding-polygons
			shapes.setColor(Color.RED);
			for (Polygon polygon : getBoundingPolygonsWithOffset(getWorldPosition()))
			{
				shapes.polygon(polygon.getTransformedVertices());
			}
			shapes.setColor(Color.ORANGE);
			shapes.rect(boundingRectangle.x + getX(), boundingRectangle.y + getY(), boundingRectangle.width, boundingRectangle.height);
		}
	}

	public void setBoundingPolygons(Polygon[] boundingPolygons)
	{
		this.boundingPolygons = boundingPolygons;
		if (boundingPolygons != null && boundingPolygons.length > 0)
		{
			addTag(Tags.BOUNDING);

			boundingRectangle = new Rectangle();
			for (Polygon polygon : boundingPolygons)
			{
				boundingRectangle.merge(polygon.getBoundingRectangle());
			}
		}
	}

	public Polygon[] getBoundingPolygons()
	{
		return boundingPolygons;
	}

	public Polygon[] getBoundingPolygonsWithOffset(Vector2 offset)
	{
		if (boundingPolygons == null)
		{
			return null;
		}

		Polygon[] offsetPolygons = new Polygon[boundingPolygons.length];

		float x = offset.x;
		float y = offset.y;
		for (int i = 0; i < boundingPolygons.length; i++)
		{
			Polygon polygon = ShapeUtils.copyPolygon(boundingPolygons[i]);
			polygon.setPosition(polygon.getX() + x, polygon.getY() + y);
			offsetPolygons[i] = polygon;
		}

		return offsetPolygons;
	}

	public Rectangle getBoundingRectangleWithOffset(Vector2 offset)
	{
		if (boundingRectangle == null)
		{
			return null;
		}
		Rectangle rectangle = new Rectangle(boundingRectangle);
		rectangle.setPosition(rectangle.x + offset.x, rectangle.y + offset.y);
		return rectangle;
	}

	public Set<String> getTags()
	{
		return tags;
	}

	public Set<String> getIgnoreCollisionWithTagSet()
	{
		return ignoreCollisionWithTagSet;
	}

	public void addIgnoreCollisionWithTagSet(String tag)
	{
		ignoreCollisionWithTagSet.add(tag);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof GameObject))
		{
			return false;
		}
		GameObject other = (GameObject) obj;
		if (id != other.id)
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("GameObject [id=%s, name=%s, tags=%s, boundingRectangle=%s]", id, getName(), tags, boundingRectangle);
	}

	@Override
	public boolean remove()
	{
		GameObjects.removeGameObject(this);

		return super.remove();
	}

	@Override
	public GameObjectsGameStage<? extends Physics> getStage()
	{
		return (GameObjectsGameStage<?>) super.getStage();
	}
}
