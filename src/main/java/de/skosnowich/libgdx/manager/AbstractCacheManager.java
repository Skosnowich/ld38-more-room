package de.skosnowich.libgdx.manager;

import java.util.HashMap;
import java.util.Map;

public class AbstractCacheManager<T>
{

	protected Map<Class<? extends T>, T> cacheMap;

	public AbstractCacheManager()
	{
		cacheMap = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public <U extends T> U get(Class<U> type)
	{
		T instance = cacheMap.get(type);
		return (U) instance;
	}

	public <U extends T> void put(Class<U> clazz, U instance)
	{
		cacheMap.put(clazz, instance);
	}

}
