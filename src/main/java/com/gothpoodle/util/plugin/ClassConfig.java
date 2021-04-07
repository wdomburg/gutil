package com.gothpoodle.util.plugin;

import java.util.Properties;

public class ClassConfig
	extends Properties
{

	public Boolean booleanValue(String name)
	{
		return booleanValue(name, null);
	}

	public Boolean booleanValue(String name, Boolean defaultValue)
	{
		String strValue = getProperty(name);

		System.out.println("Trying to parse " + name + "=" + strValue);

		if (strValue == null)
		{
			return defaultValue;
		}

		switch(strValue)
		{
			case "true":
				return Boolean.TRUE;
			case "false":
				return Boolean.FALSE;
			default:
				return defaultValue;
		}
	}

	public Integer intValue(String name)
	{
		return intValue(name, null);
	}

	public Integer intValue(String name, Integer defaultValue)
		throws NumberFormatException
	{
		String strValue = getProperty(name);

		try
		{
			return Integer.valueOf(strValue);
		}
		catch (NumberFormatException E)
		{
			return defaultValue;
		}
	}

}
