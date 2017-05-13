package de.skosnowich.libgdx.assets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Assets
{

	/** Die Singleton instance */
	private static Assets instance;
	/** Der AssetManager */
	private final AssetManager manager;

	/** HashMap mit allen verfügbaren Shadern (Cache) */
	private Map<String, ShaderProgram> shaderMap;

	/** Allgemeiner Skin für ui */
	private final Skin skin;

	private Assets()
	{
		manager = new AssetManager();

		manager.setLoader(TiledMap.class, ".tmx", new TmxMapLoader());
		ParticleEffectLoader particleEffectLoader = new ParticleEffectLoader(manager.getFileHandleResolver());
		manager.setLoader(ParticleEffect.class, ".p", particleEffectLoader);

		// Skin
		FileHandle fh = Gdx.files.internal("ui/uiskin.json");
		if (fh.exists())
		{
			skin = new Skin(fh);
		}
		else
		{
			skin = new Skin();
			createSkin();
		}

		shaderMap = new HashMap<>();
		ShaderProgram.pedantic = false;
		loadShader(null);
	}

	private void createSkin()
	{
		final String DEFAULT = "default";
		final String WHITE_TEXTURE = "WHITE_TEXTURE";

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add(WHITE_TEXTURE, new Texture(pixmap));
		pixmap.dispose();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Grand9K_Pixel.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = Color.WHITE;
		BitmapFont font16 = generator.generateFont(parameter);

		parameter.size = 32;
		BitmapFont font32 = generator.generateFont(parameter);

		parameter.size = 8;
		BitmapFont font8 = generator.generateFont(parameter);
		generator.dispose();

		// Store the default libgdx font under the name "default".
		skin.add(DEFAULT, font16);
		skin.add("font32", font32);
		skin.add("font8", font8);

		// Configure a TextButtonStyle and name it "default"
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		Drawable buttonBackground = skin.newDrawable(WHITE_TEXTURE, Color.DARK_GRAY);
		buttonBackground.setTopHeight(5);
		buttonBackground.setBottomHeight(5);
		buttonBackground.setLeftWidth(5);
		buttonBackground.setRightWidth(5);
		Drawable buttonOver = skin.newDrawable(WHITE_TEXTURE, Color.LIGHT_GRAY);
		buttonOver.setTopHeight(5);
		buttonOver.setBottomHeight(5);
		buttonOver.setLeftWidth(5);
		buttonOver.setRightWidth(5);
		textButtonStyle.up = buttonBackground;
		textButtonStyle.down = buttonBackground;
		textButtonStyle.checked = buttonBackground;
		textButtonStyle.over = buttonOver;
		textButtonStyle.font = skin.getFont(DEFAULT);
		skin.add(DEFAULT, textButtonStyle);

		// Configure a TextFieldStyle and name it "default"
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = buttonBackground;
		textFieldStyle.font = skin.getFont(DEFAULT);
		textFieldStyle.fontColor = new Color(1, 1, 0.5f, 1);
		textFieldStyle.cursor = skin.getDrawable(WHITE_TEXTURE);
		textFieldStyle.selection = skin.newDrawable(WHITE_TEXTURE, Color.WHITE);
		textFieldStyle.messageFont = skin.getFont(DEFAULT);
		textFieldStyle.messageFontColor = new Color(0.5f, 0.5f, 0.5f, 1);
		skin.add(DEFAULT, textFieldStyle);

		// Configure a LabelStyle and name it "default"
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont(DEFAULT);
		skin.add(DEFAULT, labelStyle);
		labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("font8");
		skin.add("font8", labelStyle);

		// Configure a SliderStyle and name it "default-horizontal"
		Drawable sliderBackgroundDrawable = skin.newDrawable(WHITE_TEXTURE, Color.DARK_GRAY);
		sliderBackgroundDrawable.setMinHeight(6);
		Drawable knobDrawable = skin.newDrawable(WHITE_TEXTURE);
		knobDrawable.setMinHeight(12);
		knobDrawable.setMinWidth(6);

		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = sliderBackgroundDrawable;
		sliderStyle.knob = knobDrawable;
		skin.add("default-horizontal", sliderStyle);

		// Configure a TextButtonStyle and name it "default"
		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.background = buttonBackground;
		selectBoxStyle.font = skin.getFont(DEFAULT);
		selectBoxStyle.scrollStyle = new ScrollPaneStyle();
		selectBoxStyle.scrollStyle.background = buttonBackground;
		selectBoxStyle.listStyle = new ListStyle();
		selectBoxStyle.listStyle.selection = buttonOver;
		selectBoxStyle.listStyle.font = skin.getFont(DEFAULT);
		skin.add(DEFAULT, selectBoxStyle);
	}

	/**
	 * Gibt immer die gleiche Instanz der Klasse {@link Assets} zurück (Singleton-Pattern)
	 *
	 * @return Instanz der Klasse {@link Assets}
	 */
	public static Assets getInstance()
	{
		if (instance == null)
		{
			instance = new Assets();
		}
		return instance;
	}

	/**
	 * Ruft die Methode {@link AssetManager#update()} auf.
	 *
	 * @return true, wenn alles geladen
	 */
	public boolean update()
	{
		return manager.update();
	}

	/**
	 * Ruft die Methode {@link AssetManager#getProgress()} auf.
	 *
	 * @return Fortschritt in Prozent
	 */
	public float getProgress()
	{
		return manager.getProgress();
	}

	/**
	 * Ruft die Methode {@link AssetManager#finishLoading()} auf.
	 *
	 */
	public void finishLoading()
	{
		manager.finishLoading();
	}

	/**
	 * Fügt eine Texture aus dem Texture-Ordner der loadingQueue hinzu. Bevor die Texture genutzt werden kann, muss {@link update} und/oder
	 * {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Texturename, ohne Pfad/Endung
	 */
	public void addTexture(String name)
	{
		manager.load("textures/" + name + ".png", Texture.class);
	}

	/**
	 * Fügt eine unbegrenzte Anzahl Texturen aus dem Texture-Ordner der loadingQueue hinzu. Bevor die Texturen genutzt werden können, muss {@link update}
	 * und/oder {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Texturename, ohne Pfad/Endung
	 */
	public void addTextures(String... names)
	{
		for (String s : names)
		{
			addTexture(s);
		}
	}

	/**
	 * Gibt die Texture mit dem übergebenen Namen zurück.
	 *
	 * @param name
	 * @return
	 */
	public Texture getTexture(String name)
	{
		Texture texture = manager.get("textures/" + name + ".png", Texture.class);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		return texture;
	}

	/**
	 * Fügt einen Partikeleffekt aus dem Particles-Ordner der loadingQueue hinzu. Bevor der Partikeleffekt genutzt werden kann, muss {@link update} und/oder
	 * {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Partikeleffektname, ohne Pfad/Endung
	 */
	public void addParticle(String name)
	{
		manager.load("particles/" + name + ".p", ParticleEffect.class);
	}

	/**
	 * Fügt eine unbegrenzte Anzahl Partikeleffekte aus dem Particles-Ordner der loadingQueue hinzu. Bevor die Partikeleffekte genutzt werden können, muss
	 * {@link update} und/oder {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Partikeleffektname, ohne Pfad/Endung
	 */
	public void addParticles(String... names)
	{
		for (String s : names)
		{
			addParticle(s);
		}
	}

	/**
	 * Gibt den Partikeleffekt mit dem übergebenen Namen zurück.
	 *
	 * @param name
	 * @return
	 */
	public ParticleEffect getParticle(String name)
	{
		ParticleEffect particleEffect = manager.get("particles/" + name + ".p", ParticleEffect.class);
		return particleEffect;
	}

	/**
	 * Fügt einen Sound aus dem sound-Ordner der loadingQueue hinzu. Bevor der Sound genutzt werden kann, muss {@link update} und/oder {@link finishLoading}
	 * aufgerufen werden.
	 *
	 * @param name
	 *            Soundname, ohne Pfad/Endung
	 */
	public void addSound(String name)
	{
		manager.load("sounds/" + name + ".wav", Sound.class);
	}

	/**
	 * Fügt eine unbegrenzte Anzahl Sounds aus dem sounds-Ordner der loadingQueue hinzu. Bevor die Sounds genutzt werden können, muss {@link update} und/oder
	 * {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Soundname, ohne Pfad/Endung
	 */
	public void addSounds(String... names)
	{
		for (String s : names)
		{
			addSound(s);
		}
	}

	/**
	 * Gibt den Sound mit dem übergebenen Namen zurück.
	 *
	 * @param name
	 * @return
	 */
	public Sound getSound(String name)
	{
		return manager.get("sounds/" + name + ".wav", Sound.class);
	}

	/**
	 * Fügt eine Musik aus dem music-Ordner der loadingQueue hinzu. Bevor die Musik genutzt werden kann, muss {@link update} und/oder {@link finishLoading}
	 * aufgerufen werden.
	 *
	 * @param name
	 *            Musikname, ohne Pfad/Endung
	 */
	public void addMusic(String name)
	{
		manager.load("music/" + name + ".ogg", Music.class);
	}

	/**
	 * Fügt eine unbegrenzte Anzahl Musik aus dem music-Ordner der loadingQueue hinzu. Bevor die Musik genutzt werden können, muss {@link update} und/oder
	 * {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Musikname, ohne Pfad/Endung
	 */
	public void addMusic(String... names)
	{
		for (String s : names)
		{
			addMusic(s);
		}
	}

	/**
	 * Gibt die Musik mit dem übergebenen Namen zurück.
	 *
	 * @param name
	 * @return
	 */
	public Music getMusic(String name)
	{
		return manager.get("music/" + name + ".ogg", Music.class);
	}

	/**
	 * Fügt eine Tilemap aus dem tilemap-Ordner der loadingQueue hinzu. Bevor die Tilemap genutzt werden kann, muss {@link update} und/oder
	 * {@link finishLoading} aufgerufen werden.
	 *
	 * @param name
	 *            Tilemapname, ohne Pfad/Endung
	 */
	public void addTilemap(String name)
	{
		manager.load("tileMaps/" + name + ".tmx", TiledMap.class);
	}

	/**
	 * Gibt die Tilemap mit dem übergebenen Namen zurück.
	 *
	 * @param name
	 * @return
	 */
	public TiledMap getTilemap(String name)
	{
		return manager.get("tileMaps/" + name + ".tmx", TiledMap.class);
	}

	/**
	 * Diese Methode lädt anhand des übergebenen Namens einen Shader und legt ihn im Cache ( {@link #shaderMap}) ab
	 *
	 * @param shaderType
	 *            Shadertyp
	 */
	private void loadShader(String shader)
	{
		ShaderProgram shaderProgram;
		if (shader != null)
		{
			String name = shader.toLowerCase();
			shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/" + name + "/vertexShader.glsl").readString(),
					Gdx.files.internal("shaders/" + name + "/fragShader.glsl").readString());
			if (!shaderProgram.isCompiled())
			{
				throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram.getLog());
			}
		}
		else
		{
			shaderProgram = SpriteBatch.createDefaultShader();
		}
		shaderMap.put(shader, shaderProgram);
	}

	/**
	 * Diese Methode gibt anhand des übergebenen Namens den entsprechenden Shader zurück
	 *
	 * @param shaderKey
	 *            Shadername
	 * @return der entsprechende Shader
	 */
	public ShaderProgram getShader(String shaderKey)
	{
		ShaderProgram shader = shaderMap.get(shaderKey);
		if (shader == null)
		{
			throw new IllegalArgumentException("Shader " + shaderKey + " nicht bekannt.");
		}
		return shader;
	}

	public Skin getSkin()
	{
		return skin;
	}

	public void addAssetsFromClass(Class<?> assetsClass)
	{
		try
		{
			for (Field field : assetsClass.getFields())
			{
				LoadAsset annotation = field.getAnnotation(LoadAsset.class);
				if (annotation != null)
				{
					String assetName = (String) field.get(null);
					switch (annotation.value())
					{
					case SOUND:
						addSound(assetName);
						break;
					case TEXTURE:
						addTexture(assetName);
						break;
					case PARTICLES:
						addParticle(assetName);
						break;
					case MUSIC:
						addMusic(assetName);
						break;
					case SHADER:
						loadShader(assetName);
						break;
					default:
						throw new IllegalArgumentException("AssetsType " + annotation.value() + " nicht bekannt!");
					}
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}
