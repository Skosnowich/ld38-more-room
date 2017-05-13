package de.skosnowich.libgdx.gameobjects;

import java.util.*;

public class GameObjects
{
	private static GameObjects instance;

	private final Map<String, GameObject> gameObjectsByName;
	private final Map<String, Set<GameObject>> gameObjectSetsByName;

	private final Map<String, GameObject> gameObjectsByTag;
	private final Map<String, Set<GameObject>> gameObjectSetsByTag;

	public static void init()
	{
		instance = new GameObjects();
	}

	public static void addGameObject(GameObject gameObject)
	{
		instance.add(gameObject);
	}

	public static void addTagToGameObject(GameObject gameObject, String tag)
	{
		instance.addTag(gameObject, tag);
	}

	public static void removeGameObject(GameObject gameObject)
	{
		instance.remove(gameObject);
	}

	public static void removeTagFromGameObject(GameObject gameObject, String tag)
	{
		instance.removeTag(gameObject, tag);
	}

	public static <T extends GameObject> T getGameObjectByName(String name)
	{
		return instance.getSingleByName(name);
	}

	public static <T extends GameObject> Set<T> getGameObjectsByName(String name)
	{
		return instance.getSetByName(name);
	}

	public static <T extends GameObject> T getGameObjectByTag(String tag)
	{
		return instance.getSingleByTag(tag);
	}

	public static <T extends GameObject> Set<T> getGameObjectsByTag(String tag)
	{
		return instance.getSetByTag(tag);
	}

	public GameObjects()
	{
		gameObjectsByName = new HashMap<>();
		gameObjectSetsByName = new HashMap<>();

		gameObjectsByTag = new HashMap<>();
		gameObjectSetsByTag = new HashMap<>();
	}

	String removeId(String name)
	{
		name = name.substring(0, name.lastIndexOf("-"));
		return name;
	}

	private void add(GameObject gameObject)
	{
		String name = removeId(gameObject.getName());
		gameObjectsByName.put(name, gameObject);
		Set<GameObject> nameSet = gameObjectSetsByName.computeIfAbsent(name, n -> new HashSet<>());
		nameSet.add(gameObject);

		// if there are more than one GameObjects with the same name, delete the GameObjects with this name in the single-GameObject-per-name map
		if (nameSet.size() > 1)
		{
			gameObjectsByName.remove(name);
		}
	}

	private void remove(GameObject gameObject)
	{
		String name = removeId(gameObject.getName());
		gameObjectsByName.remove(name);
		Set<GameObject> setByName = gameObjectSetsByName.get(name);
		setByName.remove(gameObject);

		// if there is only one gameObject remaining with that name, add it to the single-list
		if (setByName.size() == 1)
		{
			GameObject remaining = (GameObject) setByName.toArray()[0];
			gameObjectsByName.put(removeId(remaining.getName()), remaining);
		}

		Set<String> tags = gameObject.getTags();
		for (String tag : tags)
		{
			gameObjectsByTag.remove(tag);
			Set<GameObject> setByTag = gameObjectSetsByTag.get(tag);
			setByTag.remove(gameObject);

			// if there is only one gameObject remaining with that tag, add it to the single-list
			if (setByTag.size() == 1)
			{
				GameObject remaining = (GameObject) setByTag.toArray()[0];
				gameObjectsByTag.put(tag, remaining);
			}
		}
	}

	private void removeTag(GameObject gameObject, String tag)
	{
		gameObjectsByTag.remove(tag);
		Set<GameObject> setByTag = gameObjectSetsByTag.get(tag);
		if (setByTag != null)
		{
			setByTag.remove(gameObject);

			// if there is only one gameObject remaining with that tag, add it to the single-list
			if (setByTag.size() == 1)
			{
				GameObject remaining = (GameObject) setByTag.toArray()[0];
				gameObjectsByTag.put(tag, remaining);
			}
		}
	}

	private void addTag(GameObject gameObject, String tag)
	{
		gameObjectsByTag.put(tag, gameObject);
		Set<GameObject> tagSet = gameObjectSetsByTag.computeIfAbsent(tag, n -> new HashSet<>());
		tagSet.add(gameObject);

		// if there are more than one GameObjects with the same tag, delete the GameObjects with this tag in the single-GameObject-per-tag map
		if (tagSet.size() > 1)
		{
			gameObjectsByTag.remove(tag);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends GameObject> T getSingleByTag(String tag)
	{
		return (T) gameObjectsByTag.get(tag);
	}

	@SuppressWarnings("unchecked")
	private <T extends GameObject> Set<T> getSetByTag(String tag)
	{
		Set<T> set = (Set<T>) gameObjectSetsByTag.get(tag);
		return set == null ? Collections.emptySet() : set;
	}

	@SuppressWarnings("unchecked")
	private <T extends GameObject> T getSingleByName(String name)
	{
		return (T) gameObjectsByName.get(name);
	}

	@SuppressWarnings("unchecked")
	private <T extends GameObject> Set<T> getSetByName(String name)
	{
		Set<T> set = (Set<T>) gameObjectSetsByName.get(name);
		return set == null ? Collections.emptySet() : set;
	}

}
