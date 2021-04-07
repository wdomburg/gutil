package com.gothpoodle.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/** Utility methods for class instantiation */
public class ClassUtil
{

	public static class ClassCache<T>
	{
		private Class<T> cls;
		private HashMap<String,T> instances = new HashMap<>();

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
				instance = getInstance(name, cls, props);
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

	/**
	 * Creates a class instance with an empty argument list
	 *
	 * @param <T> Class or interface to return
	 * @param className Name of the specific class to instantiate
	 * @param classType Class or interface to cast object to
	 * @return T The object instance
	 * @throws InstantiationException for any error creating or casting object
	 */
	public static <T> T getInstance(String className, Class<T> classType)
		throws InstantiationException
	{
		if (className == null || className.equals(""))
			throw new InstantiationException("No class specified.");

		try
		{
			return classType.cast(Class.forName(className).newInstance());
		}
		catch (Exception e)
		{
			throw new InstantiationException("Could not instantiate class:" + e.getMessage());
		}
	}

	/**
	 * Creates a class instance passing a properties file as the argument
	 *
	 * @param <T> Class or interface to return
	 * @param className Name of the specific class to instantiate
	 * @param classType Class or interface to cast object to
	 * @param properties Properties necessary for object construction
	 * @return T The object instance
	 * @throws InstantiationException for any error creating or casting object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String className, Class<T> classType, Properties properties)
		throws InstantiationException
	{
		if (className == null || className.equals(""))
			throw new InstantiationException("No class specified.");

		try
		{
			Object instance = Class.forName(className).getConstructor(Properties.class).newInstance(properties);
			return classType.cast(instance);
		}
		catch (InvocationTargetException e)
		{
			throw new InstantiationException("Could not instantiate class: " + e.getCause().getMessage());
		}
		catch (Exception e)
		{
			throw new InstantiationException("Could not instantiate class: " + e.getMessage());
		}
	}

	/**
	 * Convienence function for getting a class and doing a runtime casting check
	 *
	 * @param <T> Class or interface to return
	 * @param className Name of the specific class to instantiate
	 * @param classType Class or interface to cast object to
	 * @throws ClassNotFoundException if specified class does not exist or is not of classType
	 * @throws ClassCastException if specified class does not match requested class or interface
	 * @return Class The requested class, properly cast
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getClass(String className, Class<T> classType)
		throws ClassNotFoundException, ClassCastException
	{
		Class cls = Class.forName(className);

		if (classType.isAssignableFrom(cls))
		{
			return (Class<? extends T>) Class.forName(className);
		}
		else
		{
			throw new ClassCastException("Specified class does not implement return class or interface.");
		}
	}
}
