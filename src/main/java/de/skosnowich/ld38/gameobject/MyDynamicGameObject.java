package de.skosnowich.ld38.gameobject;

import de.skosnowich.ld38.stage.MyGameStage;
import de.skosnowich.libgdx.gameobjects.DynamicGameObject;
import de.skosnowich.libgdx.gameobjects.Tags;

public class MyDynamicGameObject extends DynamicGameObject
{

	public MyDynamicGameObject(String name, float x, float y, float speed)
	{
		super(name, x, y, speed);
	}

	public MyDynamicGameObject(String name, float x, float y, float speed, boolean lockSpriteTo8Directions)
	{
		super(name, x, y, speed, lockSpriteTo8Directions);
	}

	@Override
	public MyGameStage getStage()
	{
		return (MyGameStage) super.getStage();
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if (visible)
		{
			addTag(Tags.DYNAMIC);
			if (getBoundingPolygons() != null && getBoundingPolygons().length > 0)
			{
				addTag(Tags.BOUNDING);
			}
		}
		else
		{
			removeTag(Tags.DYNAMIC);
			if (getBoundingPolygons() != null && getBoundingPolygons().length > 0)
			{
				removeTag(Tags.BOUNDING);
			}
		}
	}

}
