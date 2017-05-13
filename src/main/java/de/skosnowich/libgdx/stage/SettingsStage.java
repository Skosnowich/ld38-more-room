package de.skosnowich.libgdx.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.skosnowich.libgdx.app.Application;
import de.skosnowich.libgdx.assets.Assets;
import de.skosnowich.libgdx.assets.Sounds;
import de.skosnowich.libgdx.screen.MainMenuScreen;
import de.skosnowich.libgdx.ui.ActionSlider;
import de.skosnowich.libgdx.ui.ActionTextButton;

public class SettingsStage extends Stage
{

	private Label masterVolumeLabel;
	private Label soundVolumeLabel;
	private Label musicVolumeLabel;

	public SettingsStage()
	{
		super();
		Skin skin = Assets.getInstance().getSkin();

		Vector2 screenCenter = new Vector2(getViewport().getScreenWidth() / 2, getViewport().getScreenHeight() / 2);

		Label titleLabel = new Label("Settings", skin);
		titleLabel.setPosition(screenCenter.x, screenCenter.y + 50, Align.center);
		addActor(titleLabel);

		// Master Volume
		masterVolumeLabel = new Label("Master Volume: " + (int) (Sounds.getInstance().getMasterVolume() * 100), skin);
		masterVolumeLabel.setPosition(screenCenter.x - 10, screenCenter.y, Align.right);
		addActor(masterVolumeLabel);

		ActionSlider masterVolumeSlider = new ActionSlider(0.f, 100.f, 1.f, (Sounds.getInstance().getMasterVolume() * 100), false,
				(value) -> setMasterVolume(value.intValue()));
		masterVolumeSlider.setPosition(screenCenter.x + 10, screenCenter.y, Align.left);
		addActor(masterVolumeSlider);

		// Sound Volume
		soundVolumeLabel = new Label("Sound Volume: " + (int) (Sounds.getInstance().getSoundVolume() * 100), skin);
		soundVolumeLabel.setPosition(screenCenter.x - 10, screenCenter.y - 40, Align.right);
		addActor(soundVolumeLabel);

		ActionSlider soundVolumeSlider = new ActionSlider(0.f, 100.f, 1.f, (Sounds.getInstance().getSoundVolume() * 100), false,
				(value) -> setSoundVolume(value.intValue()));
		soundVolumeSlider.setPosition(screenCenter.x + 10, screenCenter.y - 40, Align.left);
		addActor(soundVolumeSlider);

		// Music Volume
		musicVolumeLabel = new Label("Music Volume: " + (int) (Sounds.getInstance().getMusicVolume() * 100), skin);
		musicVolumeLabel.setPosition(screenCenter.x - 10, screenCenter.y - 80, Align.right);
		addActor(musicVolumeLabel);

		ActionSlider musicVolumeSlider = new ActionSlider(0.f, 100.f, 1.f, (Sounds.getInstance().getMusicVolume() * 100), false,
				(value) -> setMusicVolume(value.intValue()));
		musicVolumeSlider.setPosition(screenCenter.x + 10, screenCenter.y - 80, Align.left);
		addActor(musicVolumeSlider);

		// Resolution
		Label resolutionLabel = new Label("Resolution:", skin);
		resolutionLabel.setPosition(screenCenter.x - 10, screenCenter.y - 120, Align.right);
		addActor(resolutionLabel);

		SelectBox<String> resolutionBox = new SelectBox<>(skin);
		java.util.List<Integer[]> resolutions = findResolutions();

		String resolutionStrings[] = new String[resolutions.size()];
		int resolutionValues[][] = new int[resolutions.size()][2];
		for (int i = 0; i < resolutions.size(); i++)
		{
			Integer[] resolution = resolutions.get(i);
			int width = resolution[0];
			int height = resolution[1];
			resolutionStrings[i] = String.format("%s : %s", width, height);
			resolutionValues[i][0] = width;
			resolutionValues[i][1] = height;
		}
		resolutionBox.setItems(resolutionStrings);
		String currentResolution = String.format("%s : %s", getViewport().getScreenWidth(), getViewport().getScreenHeight());
		int currentSelection = 0;
		for (int i = 0; i < resolutionStrings.length; i++)
		{
			if (currentResolution.equals(resolutionStrings[i]))
			{
				currentSelection = i;
			}
		}
		resolutionBox.setSelected(resolutionStrings[currentSelection]);
		resolutionBox.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				int val[] = resolutionValues[resolutionBox.getSelectedIndex()];
				Gdx.graphics.setWindowedMode(val[0], val[1]);
			}

		});
		resolutionBox.setWidth(120);
		resolutionBox.setPosition(screenCenter.x + 10, screenCenter.y - 120, Align.left);
		addActor(resolutionBox);

		// Back
		TextButton exitGameButton = new ActionTextButton("Back", skin, () -> gotoMainMenuStage());
		exitGameButton.setPosition(screenCenter.x, screenCenter.y - 160, Align.center);
		addActor(exitGameButton);
	}

	protected java.util.List<Integer[]> findResolutions()
	{
		java.util.List<Integer[]> resolutions = new ArrayList<>();
		resolutions.add(new Integer[] { 640, 360 });
		resolutions.add(new Integer[] { 896, 504 });
		resolutions.add(new Integer[] { 1280, 720 });
		for (DisplayMode displayMode : Gdx.graphics.getDisplayModes())
		{
			if (1.0f * displayMode.height / displayMode.width == 9 / 16.0f)
			{
				Integer[] resolution = new Integer[] { displayMode.width, displayMode.height };
				if (resolutions.stream().noneMatch(arr -> arr[0].equals(resolution[0]) && arr[1].equals(resolution[1])))
				{
					resolutions.add(resolution);
				}
			}
		}
		Collections.sort(resolutions, Comparator.comparingInt(arr -> arr[0]));
		return resolutions;
	}

	private void gotoMainMenuStage()
	{
		Application.getInstance().getScreenManager().get(MainMenuScreen.class)
				.setCurrentStage(Application.getInstance().getStageManager().get(MainMenuStage.class));
	}

	private void setMasterVolume(int value)
	{
		masterVolumeLabel.setText("Master Volume: " + value);
		Sounds.getInstance().setMasterVolume(value / 100.f);
	}

	private void setMusicVolume(int value)
	{
		musicVolumeLabel.setText("Music Volume: " + value);
		Sounds.getInstance().setMusicVolume(value / 100.f);
	}

	private void setSoundVolume(int value)
	{
		soundVolumeLabel.setText("Sound Volume: " + value);
		Sounds.getInstance().setSoundVolume(value / 100.f);
	}
}
