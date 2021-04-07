package com.gothpoodle.util.config;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Stanza {

	String directive;
	ArrayList<String> arguments = new ArrayList<>();
	Properties properties = new Properties();

	/* Construct a blank config stanza */
	public Stanza()
	{
	}

	public Stanza setDirective(String directive)
	{
		this.directive = directive;
		return this;
	}

	public String getDirective()
	{
		return directive;
	}

	public Stanza addArgument(String argument)
	{
		arguments.add(argument);
		return this;
	}

	public ArrayList<String> getArguments()
	{
		return arguments;
	}

	public Stanza addProperty(String key, String value)
	{
		properties.setProperty(key,value);
		return this;
	}

	public Properties getProperties()
	{
		return properties;
	}

	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}

	public String getProperty(String key, String defaultValue)
	{
		return properties.getProperty(key, defaultValue);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(directive);
		sb.append(" ");
		sb.append(String.join(" ", arguments));
		sb.append("\n");

		for(Map.Entry<?,?> prop: properties.entrySet())
		{
			sb.append("\t");
			sb.append(prop.getKey().toString());
			sb.append(" ");
			sb.append(prop.getValue().toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}
