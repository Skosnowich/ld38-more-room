package de.skosnowich.ld38.screen;

import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.screen.GameScreen;

public class MyGameScreen extends GameScreen
{

	public MyGameScreen()
	{
		super();
	}

	@Override
	public void hide()
	{
		super.hide();
		Sounds.getInstance().stopMusic();
	}
}
