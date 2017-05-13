package de.skosnowich.ld38.stage;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import de.skosnowich.ld38.gameobject.Upgrade;
import de.skosnowich.ld38.gameobject.impl.Team.KI;
import de.skosnowich.ld38.gameobject.impl.worlds.World;
import de.skosnowich.ld38.level.LevelOne;
import de.skosnowich.libgdx.assets.Assets;

public class Tutorial extends Group
{
	private MyGameStage stage;
	private int step = 0;
	private Label textLabel;
	private TextButton textButton, nextButton;
	Table table;

	public Tutorial(MyGameStage stage)
	{
		super();
		this.stage = stage;
		table = new Table();
		table.setPosition(50, 100);
		Pixmap pm1 = new Pixmap(1, 1, Format.RGB888);
		pm1.setColor(Color.GRAY);
		pm1.fill();
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

		textLabel = new Label("Hi, this is a Tutorial. Do you want to skip it?", Assets.getInstance().getSkin());
		textLabel.setAlignment(Align.center);
		textLabel.setWrap(true);
		table.add(textLabel).width(200f);
		table.row();

		// Buttons
		textButton = new TextButton("Skip", Assets.getInstance().getSkin());
		textButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				textButton.setChecked(false);
				remove();
				((LevelOne) (stage.getLevel())).setTutorialState(2);
				stage.getLevel().getTeam("Farmers").setKi(KI.DEFENSIVE);
				stage.setPause(false);
			}
		});
		nextButton = new TextButton("Continue", Assets.getInstance().getSkin());
		nextButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				nextButton.setChecked(false);
				step++;
			}
		});
		table.add(textButton).padTop(10);
		table.add(nextButton).padTop(10);
		table.pack();
		addActor(table);
	}

	@Override
	public void act(float delta)
	{
		switch (step)
		{
		case 1:
			stage.setPause(false);
			textButton.setVisible(false);
			textLabel.setText(
					"My Lord, we are the red nation. Currently we own the town in the middle and the northwestern farm. We do need more food for our people, because our population is growing.");
			table.pack();
			step++;
			break;
		case 3:
			textLabel.setText(
					"We train new soldiers continuously as you can see. Our count of soldiers is increasing. In towns we can train soldiers faster than on farms, also a city can hold more of them.");
			table.pack();
			step++;
			break;
		case 5:
			nextButton.setVisible(false);
			textLabel.setText(
					"Our goal is to take the golden farms. With the left mouse button you can select our town or our farm. To select more than one, you can drag a box holding the left mouse button or clicking shift + left mouse button. Now select our town.");
			table.pack();
			step++;
			break;
		case 6:
			Iterator<World> it = stage.getSelection().iterator();
			while (it.hasNext())
			{
				if (it.next().getTags().contains("target1"))
				{
					step++;
				}
			}
			break;
		case 7:
			nextButton.setVisible(true);
			step++;
			break;
		case 9:
			nextButton.setVisible(false);
			textLabel.setText(
					"Now, if you hover over the northeastern farm, you will see a red arrow. This indicates, that we're going to attack. Clicking the right mouse button, you send half of our current soldiers of our city to attack. If at least one of our soldiers reach the enemy farm and there is no enemy soldier to defend, we conquered that farm. Now let us take the northeastern farm!");
			table.pack();
			step++;
			break;
		case 10:
			for (World w : stage.getLevel().getWorlds())
			{
				if (w.getTags().contains("target2") && w.getOwner().isPlayer())
				{
					step++;
				}
			}
			break;
		case 11:
			stage.getLevel().getTeam("Farmers").setKi(KI.DEFENSIVE);
			((LevelOne) (stage.getLevel())).setTutorialState(1);
			textLabel.setText(
					"Well done! Looks like you're a descendant of Alexander the Great. Now it shouldn't be a problem to conquer the southern farm, right?");
			table.pack();
			step++;
			break;
		case 12:
			for (World w : stage.getLevel().getWorlds())
			{
				if (w.getTags().contains("target3") && w.getOwner().isPlayer())
				{
					step++;
				}
			}
			break;
		case 13:
			nextButton.setVisible(true);
			textLabel.setText(
					"Ah, faster than I expected. As you saw, the enemy sent troops against you, as he noticed your approach. When soldiers meet, they fight on open street until death.");
			table.pack();
			step++;
			break;
		case 15:
			nextButton.setVisible(false);
			textLabel.setText(
					"On the left side of the screen, you see an upgrade window. For every kill and every conquer, we receive money. Now that we've earned a bit, buy us an health-upgrade for our soldiers.");
			table.pack();
			step++;
			break;
		case 16:
			if (Upgrade.getInstance().getHealthLevel() > 0)
			{
				step++;
			}
			break;
		case 17:
			nextButton.setVisible(true);
			step++;
			break;
		case 19:
			nextButton.setVisible(false);
			((LevelOne) (stage.getLevel())).setTutorialState(2);
			stage.getLevel().getTeam("Farmers").setKi(KI.KAMIKAZE);
			textLabel.setText(
					"Oh, it looks like the farmers became a little angry about our campaign, but we need the food for our people... Destroy them!");
			table.pack();
			step++;
			break;
		default:
			break;
		}
	}
}
