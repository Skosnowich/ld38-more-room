package de.skosnowich.ld38.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import de.skosnowich.ld38.gameobject.Upgrade;
import de.skosnowich.ld38.level.Level;
import de.skosnowich.ld38.level.LevelOne;
import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.screen.MainMenuScreen;
import de.skosnowich.libgdx.stage.GameStage;
import de.skosnowich.libgdx.stage.UIStage;

public class MyUiStage extends UIStage
{
	private Upgrade upgrade;

	private Label moneyLabel;
	private TextButton healthButton;
	private TextButton strengthButton;
	private TextButton regButton;
	private TextButton speedButton;

	private Group gameOverGroup, victoryGroup, startGroup;
	private Label startTitle, startText;
	private Table startTable;

	public MyUiStage()
	{
		super();
		setViewport(new StretchViewport(1280, 720, getCamera()));
	}

	@Override
	public void setup()
	{
		clear();

		upgrade = Upgrade.getInstance();

		moneyLabel = new Label("Money: 9999", Assets.getInstance().getSkin());
		moneyLabel.setPosition(120, 720 - 40);
		addActor(moneyLabel);

		// Health
		String txt = "Health Level: " + (upgrade.getHealthLevel() < 10 ? " " : "") + upgrade.getHealthLevel() + " (cost: "
				+ Upgrade.getInstance().getHealthPrice() + ")";
		healthButton = new TextButton(txt, Assets.getInstance().getSkin());
		healthButton.setPosition(5, 720 - 80);
		healthButton.setWidth(300);
		healthButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				healthButton.setChecked(false);
				upgrade.buyHealth();
			}
		});
		addActor(healthButton);

		// Strength
		txt = "Strength Level: " + (upgrade.getStrengthLevel() < 10 ? " " : "") + upgrade.getStrengthLevel() + " (cost: "
				+ Upgrade.getInstance().getStrengthPrice() + ")";
		strengthButton = new TextButton(txt, Assets.getInstance().getSkin());
		strengthButton.setPosition(5, 720 - 120);
		strengthButton.setWidth(300);
		strengthButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				strengthButton.setChecked(false);
				upgrade.buyStrength();
			}
		});
		addActor(strengthButton);

		// Regeneration
		txt = "Regeneration Level: " + (upgrade.getRegenerationLevel() < 10 ? " " : "") + upgrade.getRegenerationLevel() + " (cost: "
				+ Upgrade.getInstance().getRegenerationPrice() + ")";
		regButton = new TextButton(txt, Assets.getInstance().getSkin());
		regButton.setPosition(5, 720 - 160);
		regButton.setWidth(300);
		regButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				regButton.setChecked(false);
				upgrade.buyRegeneration();
			}
		});
		addActor(regButton);

		// Speed
		txt = "Speed Level: " + (upgrade.getSpeedLevel() < 10 ? " " : "") + upgrade.getSpeedLevel() + " (cost: "
				+ Upgrade.getInstance().getSpeedPrice() + ")";
		speedButton = new TextButton(txt, Assets.getInstance().getSkin());
		speedButton.setPosition(5, 720 - 200);
		speedButton.setWidth(300);
		speedButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				speedButton.setChecked(false);
				upgrade.buySpeed();
			}
		});
		addActor(speedButton);

		/* Victory */
		victoryGroup = new Group();

		Pixmap pixmap = new Pixmap(128, 128, Format.RGBA8888);
		pixmap.setColor(0, 0, 0, .8f);
		pixmap.fill();
		Image image = new Image(new Texture(pixmap));
		image.setBounds(0, 0, getWidth(), getHeight());
		victoryGroup.addActor(image);

		Label victoryTitle = new Label("VICTORY", Assets.getInstance().getSkin(), "font32", Color.RED);
		victoryTitle.setPosition(getWidth() / 2, getHeight() / 2 + 40, Align.center);
		victoryGroup.addActor(victoryTitle);

		TextButton resumeButton = new TextButton("Next Level", Assets.getInstance().getSkin());
		resumeButton.setPosition(getWidth() / 2 - 100, getHeight() / 2 - 40, Align.center);
		resumeButton.setWidth(100);
		resumeButton.setName("resumeButton");
		resumeButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameStage<?> gameStage = Application.getInstance().getStageManager().get(GameStage.class);
				((MyGameStage) gameStage).loadNextLevel();
			}
		});
		victoryGroup.addActor(resumeButton);

		TextButton victoryExitButton = new TextButton("Exit", Assets.getInstance().getSkin());
		victoryExitButton.setPosition(getWidth() / 2 + 50, getHeight() / 2 - 40, Align.center);
		victoryExitButton.setWidth(100);
		victoryExitButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Application.getInstance().setScreen(MainMenuScreen.class);
			}
		});
		victoryGroup.addActor(victoryExitButton);

		/* Game Over */
		gameOverGroup = new Group();

		Pixmap gameOverPixmap = new Pixmap(128, 128, Format.RGBA8888);
		gameOverPixmap.setColor(0, 0, 0, .8f);
		gameOverPixmap.fill();
		Image gameOverImage = new Image(new Texture(gameOverPixmap));
		gameOverImage.setBounds(0, 0, getWidth(), getHeight());
		gameOverGroup.addActor(gameOverImage);

		Label gameOverTitle = new Label("GAME OVER", Assets.getInstance().getSkin(), "font32", Color.RED);
		gameOverTitle.setPosition(getWidth() / 2, getHeight() / 2 + 40, Align.center);
		gameOverGroup.addActor(gameOverTitle);

		TextButton restartButton = new TextButton("Restart", Assets.getInstance().getSkin());
		restartButton.setPosition(getWidth() / 2 - 100, getHeight() / 2 - 40, Align.center);
		restartButton.setWidth(100);
		restartButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameStage<?> gameStage = Application.getInstance().getStageManager().get(GameStage.class);
				((MyGameStage) gameStage).restartLevel();
			}
		});
		gameOverGroup.addActor(restartButton);

		TextButton gameoverExitButton = new TextButton("Exit", Assets.getInstance().getSkin());
		gameoverExitButton.setPosition(getWidth() / 2 + 50, getHeight() / 2 - 40, Align.center);
		gameoverExitButton.setWidth(100);
		gameoverExitButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Application.getInstance().setScreen(MainMenuScreen.class);
			}
		});
		gameOverGroup.addActor(gameoverExitButton);

		/* Start */
		startGroup = new Group();

		Pixmap startPixmap = new Pixmap(128, 128, Format.RGBA8888);
		startPixmap.setColor(0, 0, 0, .8f);
		startPixmap.fill();
		Image startImage = new Image(new Texture(startPixmap));
		startImage.setBounds(0, 0, getWidth(), getHeight());
		startGroup.addActor(startImage);

		startTitle = new Label("LEVEL 0", Assets.getInstance().getSkin(), "font32", Color.RED);
		startTitle.setPosition(getWidth() / 2, getHeight() / 2 + 320, Align.center);
		startGroup.addActor(startTitle);

		startTable = new Table();
		startText = new Label("LEVEL 0", Assets.getInstance().getSkin(), "default", Color.RED);
		startText.setWrap(true);
		startTable.setPosition(getWidth() / 2 - 200, getHeight() / 2);
		startTable.add(startText).width(400);
		startTable.pack();
		startGroup.addActor(startTable);

		TextButton startButton = new TextButton("Start", Assets.getInstance().getSkin());
		startButton.setPosition(getWidth() / 2 - 100, getHeight() / 2 - 40, Align.center);
		startButton.setWidth(100);
		startButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				startGroup.remove();
				GameStage<?> gameStage = Application.getInstance().getStageManager().get(GameStage.class);
				((MyGameStage) gameStage).setPause(false);
			}
		});
		startGroup.addActor(startButton);

		TextButton startExitButton = new TextButton("Exit", Assets.getInstance().getSkin());
		startExitButton.setPosition(getWidth() / 2 + 50, getHeight() / 2 - 40, Align.center);
		startExitButton.setWidth(100);
		startExitButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Application.getInstance().setScreen(MainMenuScreen.class);
			}
		});
		startGroup.addActor(startExitButton);
	}

	@Override
	public void act(float delta)
	{
		if (!gameOverGroup.hasParent() && !victoryGroup.hasParent() && !startGroup.hasParent())
		{
			super.act(delta);

			moneyLabel.setText("Money: " + Upgrade.getInstance().getMoney());

			String healthText = "Health Level: " + (upgrade.getHealthLevel() < 10 ? " " : "") + upgrade.getHealthLevel() + " (cost: "
					+ Upgrade.getInstance().getHealthPrice() + ")";
			healthButton.setText(healthText);

			String strengthText = "Strength Level: " + (upgrade.getStrengthLevel() < 10 ? " " : "") + upgrade.getStrengthLevel() + " (cost: "
					+ Upgrade.getInstance().getStrengthPrice() + ")";
			strengthButton.setText(strengthText);

			String regenerationText = "Regeneration Level: " + (upgrade.getRegenerationLevel() < 10 ? " " : "") + upgrade.getRegenerationLevel() + " (cost: "
					+ Upgrade.getInstance().getRegenerationPrice() + ")";
			regButton.setText(regenerationText);

			String speedText = "Speed Level: " + (upgrade.getSpeedLevel() < 10 ? " " : "") + upgrade.getSpeedLevel() + " (cost: "
					+ Upgrade.getInstance().getSpeedPrice() + ")";
			speedButton.setText(speedText);
		}
	}

	public void setGameOver(boolean on)
	{
		if (on)
		{
			addActor(gameOverGroup);
		}
		else
		{
			gameOverGroup.remove();
		}
	}

	/**
	 * setzt das VictoryFenster
	 *
	 * @param on
	 *            0=aus, 1=normaler Sieg, 2=Endsieg
	 */
	public void setVictory(int on)
	{
		if (on == 1)
		{
			addActor(victoryGroup);
		}
		else if (on == 2)
		{
			Label end = new Label(
					"We conquered the universe... but our population is still growing. The world is small.\nMaybe we can... is there a way to travel to other dimensions?",
					Assets.getInstance().getSkin(),
					"default",
					Color.RED);
			end.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
			victoryGroup.addActor(end);
			for (Actor a : victoryGroup.getChildren())
			{
				if (a.getName() != null)
				{
					a.remove();
				}
			}
			addActor(victoryGroup);
		}
		else
		{
			victoryGroup.remove();
		}
	}

	public void setStart(Level level)
	{
		if (level == null || level instanceof LevelOne)
		{
			startGroup.remove();
			return;
		}
		startTitle.setText("LEVEL " + level.getNumber());
		startText.setText(level.getStartText());
		startTable.pack();
		addActor(startGroup);
	}
}
