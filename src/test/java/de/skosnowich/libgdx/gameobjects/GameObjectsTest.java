package de.skosnowich.libgdx.gameobjects;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.skosnowich.libgdx.gameobjects.impl.TestGameObject;

public class GameObjectsTest
{
	private GameObject gameObject;

	@Before
	public void beforeTest() throws Exception
	{
		GameObjects.init();

		gameObject = new TestGameObject(0, 0);
	}

	@Test
	public void testAddGameObject() throws Exception
	{
		GameObjects.addGameObject(gameObject);

		GameObject actualGameObject = GameObjects.getGameObjectByName("test-gameobject");
		assertEquals(gameObject, actualGameObject);
	}

	@Test
	public void testAddGameObject_MultipleGameObjectsWithSameName() throws Exception
	{
		GameObject gameObject2 = new TestGameObject(0, 0);
		gameObject2.setName(gameObject.getName());

		GameObjects.addGameObject(gameObject);
		GameObjects.addGameObject(gameObject2);

		GameObject actualGameObject = GameObjects.getGameObjectByName("test-gameobject");
		assertNull(actualGameObject);
	}

	@Test
	public void testAddTagToGameObject() throws Exception
	{
		GameObjects.addTagToGameObject(gameObject, "test-tag");

		GameObject actualGameObject = GameObjects.getGameObjectByTag("test-tag");
		assertEquals(gameObject, actualGameObject);
	}

	@Test
	public void testAddTagToGameObject_MultipleGameObjectsWithSameTag() throws Exception
	{
		GameObject gameObject2 = new TestGameObject(0, 0);

		GameObjects.addTagToGameObject(gameObject, "test-tag");
		GameObjects.addTagToGameObject(gameObject2, "test-tag");

		GameObject actualGameObject = GameObjects.getGameObjectByTag("test-tag");
		assertNull(actualGameObject);
	}

	@Test
	public void testGetGameObjectByName() throws Exception
	{
		GameObject actualGameObject = GameObjects.getGameObjectByName("test-gameobject");

		assertEquals(gameObject, actualGameObject);
	}

	@Test
	public void testGetGameObjectByName_NoGameObjectWithThatName() throws Exception
	{
		GameObject actualGameObject = GameObjects.getGameObjectByName("non-existing");

		assertNull(actualGameObject);
	}

	@Test
	public void testGetGameObjectByName_NoGameObjectsAtAll() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		GameObject actualGameObject = GameObjects.getGameObjectByName("non-existing");

		assertNull(actualGameObject);
	}

	@Test
	public void testGetGameObjectsByName() throws Exception
	{
		GameObject gameObject2 = new TestGameObject(0, 0);
		gameObject2.setName(gameObject.getName());

		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByName("test-gameobject");

		assertEquals(2, actualGameObjects.size());
		assertThat(actualGameObjects, hasItems(new GameObject[] { gameObject, gameObject2 }));
	}

	@Test
	public void testGetGameObjectsByName_OnlyOneGameObject() throws Exception
	{
		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByName("test-gameobject");

		assertEquals(1, actualGameObjects.size());
		assertThat(actualGameObjects, hasItems(new GameObject[] { gameObject }));
	}

	@Test
	public void testGetGameObjectsByName_NoGameObjectsAtAll() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByName("non-existing");

		assertEquals(0, actualGameObjects.size());
	}

	@Test
	public void testGetGameObjectsByName_NoGameObjectsWithThatName() throws Exception
	{
		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByName("non-existing");

		assertEquals(0, actualGameObjects.size());
	}

	@Test
	public void testGetGameObjectByTag() throws Exception
	{
		gameObject.addTag("test-tag");

		GameObject actualGameObject = GameObjects.getGameObjectByTag("test-tag");

		assertEquals(gameObject, actualGameObject);
	}

	@Test
	public void testGetGameObjectByTag_NoGameObjectWithThatTag() throws Exception
	{
		GameObject actualGameObject = GameObjects.getGameObjectByTag("non-existing");

		assertNull(actualGameObject);
	}

	@Test
	public void testGetGameObjectByTag_NoGameObjectsAtAll() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		GameObject actualGameObject = GameObjects.getGameObjectByTag("non-existing");

		assertNull(actualGameObject);
	}

	@Test
	public void testGetGameObjectsByTag() throws Exception
	{
		gameObject.addTag("test-tag");

		GameObject gameObject2 = new TestGameObject(0, 0);
		gameObject2.addTag("test-tag");

		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByTag("test-tag");

		assertEquals(2, actualGameObjects.size());
		assertThat(actualGameObjects, hasItems(new GameObject[] { gameObject, gameObject2 }));
	}

	@Test
	public void testGetGameObjectsByTag_OnlyOneGameObject() throws Exception
	{
		gameObject.addTag("test-tag");

		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByTag("test-tag");

		assertEquals(1, actualGameObjects.size());
		assertThat(actualGameObjects, hasItems(new GameObject[] { gameObject }));
	}

	@Test
	public void testGetGameObjectsByTag_NoGameObjectsAtAll() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByTag("non-existing");

		assertEquals(0, actualGameObjects.size());
	}

	@Test
	public void testGetGameObjectsByTag_NoGameObjectsWithThatTag() throws Exception
	{
		Set<GameObject> actualGameObjects = GameObjects.getGameObjectsByTag("non-existing");

		assertEquals(0, actualGameObjects.size());
	}

	@Test
	public void testRemoveGameObject() throws Exception
	{
		GameObject gameObject2 = new TestGameObject(0, 0);

		gameObject.addTag("tag");
		gameObject2.addTag("tag");

		GameObjects.removeGameObject(gameObject);

		assertEquals(gameObject2, GameObjects.getGameObjectByName("test-gameobject"));
		assertEquals(gameObject2, GameObjects.getGameObjectByTag("tag"));
		assertEquals(1, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertEquals(1, GameObjects.getGameObjectsByTag("tag").size());
		assertTrue(GameObjects.getGameObjectsByName("test-gameobject").contains(gameObject2));
		assertTrue(GameObjects.getGameObjectsByTag("tag").contains(gameObject2));
	}

	@Test
	public void testRemoveGameObject_LastOne() throws Exception
	{
		gameObject.addTag("tag");

		GameObjects.removeGameObject(gameObject);

		assertNull(GameObjects.getGameObjectByName("test-gameobject"));
		assertNull(GameObjects.getGameObjectByTag("tag"));
		assertEquals(0, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertEquals(0, GameObjects.getGameObjectsByTag("tag").size());
	}

	@Test
	public void testRemoveGameObject_NonExistent() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		GameObjects.removeGameObject(gameObject);

		assertNull(GameObjects.getGameObjectByName("test-gameobject"));
		assertNull(GameObjects.getGameObjectByTag("test-gameobject"));
		assertEquals(0, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertEquals(0, GameObjects.getGameObjectsByTag("test-gameobject").size());
	}

	@Test
	public void testRemoveTagFromGameObject() throws Exception
	{
		GameObject gameObject2 = new TestGameObject(0, 0);

		gameObject.addTag("tag");
		gameObject2.addTag("tag");

		GameObjects.removeTagFromGameObject(gameObject, "tag");

		assertNull(GameObjects.getGameObjectByName("test-gameobject"));
		assertEquals(gameObject2, GameObjects.getGameObjectByTag("tag"));
		assertEquals(2, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertTrue(GameObjects.getGameObjectsByName("test-gameobject").contains(gameObject));
		assertTrue(GameObjects.getGameObjectsByName("test-gameobject").contains(gameObject2));
		assertEquals(1, GameObjects.getGameObjectsByTag("tag").size());
		assertTrue(GameObjects.getGameObjectsByTag("tag").contains(gameObject2));
	}

	@Test
	public void testRemoveTagFromGameObject_LastOne() throws Exception
	{
		gameObject.addTag("tag");

		GameObjects.removeTagFromGameObject(gameObject, "tag");

		assertEquals(gameObject, GameObjects.getGameObjectByName("test-gameobject"));
		assertNull(GameObjects.getGameObjectByTag("tag"));
		assertEquals(1, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertTrue(GameObjects.getGameObjectsByName("test-gameobject").contains(gameObject));
		assertEquals(0, GameObjects.getGameObjectsByTag("tag").size());
	}

	@Test
	public void testRemoveTagFromGameObject_NonExistent() throws Exception
	{
		GameObjects.removeGameObject(gameObject);

		GameObjects.removeTagFromGameObject(gameObject, "tag");

		assertNull(GameObjects.getGameObjectByName("test-gameobject"));
		assertNull(GameObjects.getGameObjectByTag("test-gameobject"));
		assertEquals(0, GameObjects.getGameObjectsByName("test-gameobject").size());
		assertEquals(0, GameObjects.getGameObjectsByTag("test-gameobject").size());
	}

	@Test
	public void testRemoveId()
	{
		String actualName = new GameObjects().removeId(gameObject.getName());

		String expectedName = "test-gameobject";
		assertEquals(expectedName, actualName);
	}

}
