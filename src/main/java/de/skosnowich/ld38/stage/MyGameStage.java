package de.skosnowich.ld38.stage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.skosnowich.ld38.gameobject.impl.ways.Way;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.ld38.level.Level;
import de.skosnowich.ld38.level.LevelOne;
import de.skosnowich.ld38.pathfinding.MyGraphPath;
import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.gameobjects.GameObject;
import de.skosnowich.libgdx.physics.topdown.TopDownNoCollisionsPhysics;
import de.skosnowich.libgdx.stage.GameObjectsGameStage;
import de.skosnowich.libgdx.stage.UIStage;
import de.skosnowich.libgdx.utils.ShapeUtils;

public class MyGameStage extends GameObjectsGameStage<TopDownNoCollisionsPhysics>
{
	private static final boolean DEBUG_KEYS_ACTIVATED = false; // DEBUG

	private static final int DEBUG_ON_OFF = Keys.F1;
	private static final int DEBUG_SKIP_LEVEL = Keys.F3;
	private static final int DEBUG_SELECT_ANY_WORLD = Keys.F4;
	private static final int DEBUG_GAME_OVER = Keys.F5;
	private static final int DEBUG_CREATE_TOWN = Keys.F6;

	public static final int VIEWPORT_WIDTH = 640;
	public static final int VIEWPORT_HEIGHT = 360;

	private ShapeRenderer shapeRenderer;

	private Level level;
	private Tutorial tutorial = null;

	private boolean pause = true;

	private Selection selection;
	private Rectangle selectionRectangle;
	private Set<MyGraphPath> currentPaths;

	public MyGameStage()
	{
		super();
	}

	@Override
	public void setup()
	{
		Level firstLevel = new LevelOne();
		setup(firstLevel);
	}

	private void setup(Level level)
	{
		clear();
		super.setup(new TopDownNoCollisionsPhysics(), VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		setDebug(false);

		loadLevel(level);
		// Tutorial?
		if (level instanceof LevelOne)
		{
			tutorial = new Tutorial(this);
			Application.getInstance().getStageManager().get(UIStage.class).addActor(tutorial);
		}
		else if (tutorial != null)
		{
			tutorial.remove();
			tutorial = null;
		}

		selection = new Selection();
		currentPaths = new HashSet<>();

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		((MyUiStage) Application.getInstance().getStageManager().get(UIStage.class)).setGameOver(false);
		((MyUiStage) Application.getInstance().getStageManager().get(UIStage.class)).setVictory(0);
		((MyUiStage) Application.getInstance().getStageManager().get(UIStage.class)).setStart(level);
		pause = true;
	}

	private void loadLevel(Level levelToLoad)
	{
		level = levelToLoad;
		levelToLoad.setup();
		getCamera().position.set(level.getStartPoint().x, level.getStartPoint().y, 0);
		holdCamInBoundaries();
		getCamera().update();
		Set<GameObject> gameObjects = level.getGameObjectList();
		for (GameObject gameObject : gameObjects)
		{
			addActor(gameObject);
			if (gameObject instanceof Way)
			{
				gameObject.setZIndex(0);
			}
			else if (gameObject instanceof World)
			{
				gameObject.setZIndex(Integer.MAX_VALUE);
			}
		}

		Actor backgroundImage = level.getBackgroundImage();
		addActor(backgroundImage);
		backgroundImage.setZIndex(0);

		level.initPathfinder();

		Sounds.getInstance().stopMusic();
		level.playMusic();
	}

	public void loadNextLevel()
	{
		setup(level.nextLevel());
	}

	@Override
	public void act(float delta)
	{
		if (!pause)
		{
			super.act(delta);
			level.act();

			if (isDebugKeyPressed(DEBUG_ON_OFF, true)) // DEBUG
			{
				setDebug(!isDebug());
			}

			cameraInput(delta);

			Vector2 mousePos = getMousePos();
			Set<GameObject> objectsUnderCursor = physics.getGameObjectsAtPosition(mousePos, 0.1f, new GameObject[] {});
			Set<World> worldsUnderCursor = objectsUnderCursor.stream()
					.filter(g -> g instanceof World)
					.map(g -> (World) g)
					.collect(Collectors.toSet());
			selectWorlds(mousePos, worldsUnderCursor);
			selectTargetWorld(worldsUnderCursor);

			if (level.isWon() || isDebugKeyPressed(DEBUG_SKIP_LEVEL, true)) // DEBUG
			{ // Victory
				UIStage uiStage = Application.getInstance().getStageManager().get(UIStage.class);
				if (level.nextLevel() == null)
				{
					((MyUiStage) uiStage).setVictory(2);
				}
				else
				{
					((MyUiStage) uiStage).setVictory(1);
				}
				pause = true;
			}
			if (level.isLost() || isDebugKeyPressed(DEBUG_GAME_OVER, true)) // DEBUG
			{ // Game over
				UIStage uiStage = Application.getInstance().getStageManager().get(UIStage.class);
				((MyUiStage) uiStage).setGameOver(true);
				pause = true;
			}
			if (isDebugKeyPressed(DEBUG_CREATE_TOWN, true)) // DEBUG
			{
				System.out.format("World world%s_%s = new World(\"%s:%s\", %s, %s, playersTeam);%n", (int) mousePos.x, (int) mousePos.y,
						(int) mousePos.x, (int) mousePos.y, (int) mousePos.x, (int) mousePos.y);
			}
		}
	}

	private boolean isDebugKeyPressed(int debugKey, boolean just)
	{
		if (DEBUG_KEYS_ACTIVATED)
		{
			if (just)
			{
				return isKeyJustPressed(debugKey);
			}
			return isKeyPressed(debugKey);
		}
		return false;
	}

	private void cameraInput(float delta)
	{
		int x = 0;
		int y = 0;
		if (isKeyPressed(Keys.D))
		{
			x += 1;
		}
		if (isKeyPressed(Keys.A))
		{
			x -= 1;
		}
		if (isKeyPressed(Keys.W))
		{
			y += 1;
		}
		if (isKeyPressed(Keys.S))
		{
			y -= 1;
		}

		Vector2 movementVector = new Vector2(x, y).nor().scl(200 * delta);

		if (isButtonPressed(Buttons.MIDDLE))
		{
			Vector3 currentVector = new Vector3(getMouseX(), getMouseY(), 0);
			Vector2 deltaVector = getMouseDelta();
			Vector3 lastVector = new Vector3(currentVector).add(deltaVector.x, deltaVector.y, 0);

			Vector2 unprojectedDiff = unprojectMouseCoords(currentVector).sub(unprojectMouseCoords(lastVector));
			movementVector.add(unprojectedDiff);
		}

		if (movementVector.len() > 0)
		{
			getCamera().translate(movementVector);

			holdCamInBoundaries();
			getCamera().update();
		}
	}

	private void holdCamInBoundaries()
	{
		Vector3 camPos = getCamera().position;
		Rectangle levelBoundaries = level.getLevelBoundaries();

		float leftBorder = levelBoundaries.x;
		float rightBorder = leftBorder + levelBoundaries.width;
		float bottomBorder = levelBoundaries.y;
		float topBorder = bottomBorder + levelBoundaries.height;

		float widthHalf = VIEWPORT_WIDTH / 2.0f;
		float heightHalf = VIEWPORT_HEIGHT / 2.0f;
		if (camPos.x - widthHalf < leftBorder)
		{
			camPos.x = leftBorder + widthHalf;
		}
		if (camPos.x + widthHalf > rightBorder)
		{
			camPos.x = rightBorder - widthHalf;
		}
		if (camPos.y - heightHalf < bottomBorder)
		{
			camPos.y = bottomBorder + heightHalf;
		}
		if (camPos.y + heightHalf > topBorder)
		{
			camPos.y = topBorder - heightHalf;
		}
	}

	private void selectTargetWorld(Set<World> worldsUnderCursor)
	{
		boolean rightClick = isButtonJustPressed(Buttons.RIGHT);
		currentPaths.clear();
		if (!getSelection().isEmpty())
		{
			for (World world : worldsUnderCursor)
			{
				for (World selectedWorld : getSelection())
				{
					if (world.isRevealed() && !world.equals(selectedWorld)
							|| isDebugKeyPressed(DEBUG_SELECT_ANY_WORLD, false)) // DEBUG
					{
						MyGraphPath path = level.getPath(selectedWorld, world);
						if (path.nodes.size > 0)
						{
							currentPaths.add(path);
							if (rightClick)
							{
								selectedWorld.sendUnits(path);
							}
						}
					}
				}
			}
		}
	}

	private void selectWorlds(Vector2 mousePos, Set<World> objectsUnderCursor)
	{
		if (isButtonPressed(Buttons.LEFT))
		{
			if (selectionRectangle == null)
			{
				selectionRectangle = new Rectangle(mousePos.x, mousePos.y, 0, 0);
			}
			else
			{
				float x = selectionRectangle.x;
				float y = selectionRectangle.y;

				selectionRectangle.width = mousePos.x - x;
				selectionRectangle.height = mousePos.y - y;
			}
		}
		else if (selectionRectangle != null)
		{
			if (!isKeyPressed(Keys.SHIFT_LEFT))
			{
				getSelection().clear();
			}

			Vector2 center = selectionRectangle.getCenter(new Vector2());
			Set<GameObject> objectsInRectangle = physics.getGameObjectsInPolygon(
					ShapeUtils.getRectangleShape(center.x, center.y, selectionRectangle.getWidth(), selectionRectangle.getHeight()),
					new GameObject[] {});
			objectsInRectangle.addAll(objectsUnderCursor);

			for (GameObject gameObject : objectsInRectangle)
			{
				if (gameObject instanceof World)
				{
					World world = (World) gameObject;
					if (world.getOwner().isPlayer())
					{
						getSelection().add(world);
					}
					if (isDebugKeyPressed(DEBUG_SELECT_ANY_WORLD, false)) // DEBUG
					{
						getSelection().add(world);
					}
				}
			}

			selectionRectangle = null;
		}

		if (!isDebugKeyPressed(DEBUG_SELECT_ANY_WORLD, false)) // DEBUG
		{
			Iterator<World> iterator = selection.iterator();
			while (iterator.hasNext())
			{
				World world = iterator.next();
				if (!world.getOwner().isPlayer())
				{
					world.setSelected(false);
					iterator.remove();
				}
			}
		}
	}

	private Vector2 getMousePos()
	{
		Vector3 mousePos = new Vector3(getMouseX(), getMouseY(), 0);
		return unprojectMouseCoords(mousePos);
	}

	private Vector2 unprojectMouseCoords(Vector3 mouseCoords)
	{
		Vector3 unprojectedMousePos = getCamera().unproject(mouseCoords);
		Vector2 realMousePos = new Vector2(unprojectedMousePos.x, unprojectedMousePos.y);
		return realMousePos;
	}

	@Override
	protected void customDraw()
	{
		super.customDraw();

		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin();

		if (selectionRectangle != null)
		{
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(selectionRectangle.x, selectionRectangle.y, selectionRectangle.width, selectionRectangle.height);
		}

		getBatch().begin();
		for (MyGraphPath path : currentPaths)
		{
			path.draw(getBatch());
		}
		getBatch().end();

		if (isDebug())
		{
			level.drawDebug(shapeRenderer);
			if (currentPaths.size() > 0)
			{
				for (MyGraphPath path : currentPaths)
				{
					path.drawDebug(shapeRenderer);
				}
			}
		}
		shapeRenderer.end();
	}

	@Override
	public void setDebug(boolean debug)
	{
		super.setDebug(debug);
		setDebugAll(debug);
	}

	public void restartLevel()
	{
		setup(level);
	}

	public Selection getSelection()
	{
		return selection;
	}

	public Level getLevel()
	{
		return level;
	}

	public void setPause(boolean pause)
	{
		this.pause = pause;
	}
}
