package de.skosnowich.libgdx.ui;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.skosnowich.libgdx.assets.Assets;

public class ActionSlider extends Slider
{

	public ActionSlider(float min, float max, float stepSize, boolean vertical, SliderStyle style, Consumer<Float> action)
	{
		super(min, max, stepSize, vertical, style);
		setValue(getMaxValue());
		setAction(action);
	}

	public ActionSlider(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName, Consumer<Float> action)
	{
		super(min, max, stepSize, vertical, skin, styleName);
		setValue(getMaxValue());
		setAction(action);
	}

	public ActionSlider(float min, float max, float stepSize, boolean vertical, Skin skin, Consumer<Float> action)
	{
		super(min, max, stepSize, vertical, skin);
		setValue(getMaxValue());
		setAction(action);
	}

	public ActionSlider(float min, float max, float stepSize, float actual, boolean vertical, Consumer<Float> action)
	{
		super(min, max, stepSize, vertical, Assets.getInstance().getSkin());
		setValue(actual);
		setAction(action);
	}

	private void setAction(Consumer<Float> action)
	{
		addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				action.accept(getValue());
			}
		});
	}
}
