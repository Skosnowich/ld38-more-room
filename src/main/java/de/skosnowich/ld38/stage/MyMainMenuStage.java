package de.skosnowich.ld38.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.stage.MainMenuStage;

public class MyMainMenuStage extends MainMenuStage
{
	public MyMainMenuStage()
	{
		super();

		Label creditsLabel = new Label("Created by: Matjes and Skosnowich", Assets.getInstance().getSkin());
		creditsLabel.setPosition(getWidth() - 10, 0, Align.bottomRight);
		addActor(creditsLabel);
	}
}
