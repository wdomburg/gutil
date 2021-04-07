package com.gothpoodle.util.config;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser
{

	public static Config parse(Config config, Path path)
		throws ParseException, IOException
	{
		Scanner sc = new Scanner(path).useDelimiter("\n\n");

		while (sc.hasNext())
		{
			Stanza stanza = parseStanza(sc.next());
			config.addStanza(stanza);
		}

		return config;
	}

	public static Stanza parseStanza(String text)
		throws ParseException
	{
		Scanner sc = new Scanner(text);
		Stanza stanza = new Stanza();

		stanza.setDirective(sc.next());

		String argument;
		while ((argument = sc.findInLine("\\S+")) != null)
		{
			stanza.addArgument(argument);
		}

		while (sc.hasNext())
		{
			try 
			{
				String key = sc.next();
				String value = sc.nextLine();
				stanza.addProperty(key, value);
			}
			catch (NoSuchElementException e)
			{
				//throw new ParseException("Property without value: " + text, 0);
				System.out.println("OM NOM EXCEPTION NOM NOM");
			}
		}

		return stanza;
	}
}
