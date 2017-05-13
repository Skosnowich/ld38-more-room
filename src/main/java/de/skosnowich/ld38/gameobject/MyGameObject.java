package de.skosnowich.ld38.gameobject;

import de.skosnowich.ld38.stage.MyGameStage;
import de.skosnowich.libgdx.gameobjects.GameObject;

public class MyGameObject extends GameObject
{
	public MyGameObject(String name, float x, float y)
	{
		super(name, x, y);
	}

	public MyGameObject(String name, float x, float y, boolean lockSpriteTo8Directions)
	{
		super(name, x, y, lockSpriteTo8Directions);
	}

	@Override
	public MyGameStage getStage()
	{
		return (MyGameStage) super.getStage();
	}

}
