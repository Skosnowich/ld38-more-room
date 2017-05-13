package de.skosnowich.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.skosnowich.libgdx.assets.Assets;

public class ActionTextButton extends TextButton
{

	public ActionTextButton(String text, Skin skin, Runnable action)
	{
		super(text, skin);
		setAction(action);
	}

	public ActionTextButton(String text, TextButtonStyle textButtonStyle, Runnable action)
	{
		super(text, textButtonStyle);
		setAction(action);
	}

	public ActionTextButton(String text, Skin skin, String styleName, Runnable action)
	{
		super(text, skin, styleName);
		setAction(action);
	}

	public ActionTextButton(String text, Runnable action)
	{
		super(text, Assets.getInstance().getSkin());
		setAction(action);
	}

	private void setAction(Runnable action)
	{
		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				action.run();
			}
		});
	}
}
