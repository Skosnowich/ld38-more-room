package de.skosnowich.libgdx.assets;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;

public class Sounds
{
	/** Die Singleton instance */
	private static Sounds instance;

	/** Die Volumes */
	private float masterVolume = 0.5f;
	private float soundVolume = 1.f;
	private float musicVolume = 1.f;

	private Map<Music, Float> currentMusic;

	/**
	 * Konstruktor
	 */
	private Sounds()
	{
		currentMusic = new HashMap<>();
	}

	/**
	 * Gibt immer die gleiche Instanz der Klasse {@link Sounds} zurück (Singleton-Pattern)
	 *
	 * @return Instanz der Klasse {@link Sounds}
	 */
	public static Sounds getInstance()
	{
		if (instance == null)
		{
			instance = new Sounds();
		}
		return instance;
	}

	/**
	 * Spielt den übergebenen Sound ab.
	 *
	 * @param name
	 *            Name das Sounds
	 */
	public void playSound(String name)
	{
		playSound(name, 1);
	}

	/**
	 * Spielt den übergebenen Sound ab.
	 *
	 * @param name
	 *            Name das Sounds
	 * @param volume
	 *            Lautstärkefaktor, Standard ist 1
	 */
	public void playSound(String name, float volume)
	{
		playSound(name, volume, true);
	}

	public void playSound(String name, float volume, boolean repetitionAllowed)
	{
		Sound sound = Assets.getInstance().getSound(name);
		if (!repetitionAllowed)
		{
			sound.stop();
		}
		sound.play(masterVolume * soundVolume * volume);
	}

	public void playMusic(String name, boolean looping)
	{
		playMusic(name, looping, null);
	}

	public void playMusic(String name, boolean looping, float volume)
	{
		playMusic(name, looping, volume, null);
	}

	public void playMusic(String name, boolean looping, OnCompletionListener onCompletionListener)
	{
		playMusic(name, looping, 1, onCompletionListener);
	}

	public void playMusic(String name, boolean looping, float volume, OnCompletionListener onCompletionListener)
	{
		Music music = Assets.getInstance().getMusic(name);
		music.setVolume(volume * masterVolume * musicVolume);
		music.setLooping(looping);
		music.play();
		if (onCompletionListener != null)
		{
			music.setOnCompletionListener(onCompletionListener);
		}
		currentMusic.put(music, volume);
	}

	public void pauseMusic(String name)
	{
		Music music = Assets.getInstance().getMusic(name);
		music.pause();
	}

	public void unpauseMusic(String name)
	{
		Music music = Assets.getInstance().getMusic(name);
		music.play();
	}

	public void stopMusic(String name)
	{
		Music music = Assets.getInstance().getMusic(name);
		music.stop();
		currentMusic.remove(music);
	}

	public void stopMusic()
	{
		currentMusic.forEach((music, volume) -> music.stop());
		currentMusic.clear();
	}

	public void changeVolumeOnCurrentMusic()
	{
		currentMusic.forEach((music, volume) -> music.setVolume(volume * masterVolume * musicVolume));
	}

	public void setMasterVolume(float masterVolume)
	{
		this.masterVolume = masterVolume;
		changeVolumeOnCurrentMusic();
	}

	public void setSoundVolume(float soundVolume)
	{
		this.soundVolume = soundVolume;
	}

	public void setMusicVolume(float musicVolume)
	{
		this.musicVolume = musicVolume;
		changeVolumeOnCurrentMusic();
	}

	public float getMasterVolume()
	{
		return masterVolume;
	}

	public float getSoundVolume()
	{
		return soundVolume;
	}

	public float getMusicVolume()
	{
		return musicVolume;
	}
}
