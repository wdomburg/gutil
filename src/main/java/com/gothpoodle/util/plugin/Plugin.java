package com.gothpoodle.util.plugin;

import java.util.HashMap;

public abstract class Plugin
{

	private static HashMap<String,ClassCache> plugins = new HashMap<>();

	public static void registerType(String string, Class<Plugin> cls)
	{
	}
}
