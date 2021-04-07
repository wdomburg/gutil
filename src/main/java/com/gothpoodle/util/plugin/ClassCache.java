package com.gothpoodle.util.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ClassCache<T>
{
	private Class<T> cls;
	private HashMap<String,T> instances = new LinkedHashMap<>();

	public ClassCache(Class<T> cls)
	{
		this.cls = cls;
	}

	public T add(String name)
	{
		T instance;
		try
		{
			instance = cls.newInstance();
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Could not instantiate class: " + e.getMessage());
		}

		instances.put(name, instance);

		return instance;
	}

	public T add(String name, Properties props)
	{
		T instance;
		try
		{
			instance = ClassUtil.getInstance(name, cls, props);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Could not instantiate class: " + e.getMessage());
		}

		instances.put(name, instance);

		return instance;
	}

	public T get(String name)
	{
		return instances.get(name);
	}

	public Collection<T> getAll()
	{
		return instances.values();
	}

}
