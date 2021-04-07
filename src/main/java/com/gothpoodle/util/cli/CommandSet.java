package com.gothpoodle.util.cli;

import java.util.Arrays;
import java.util.HashMap;

// Holds a set of commands
public class CommandSet
	implements Command
{
	public HashMap<String,Command> commands;

	public CommandSet()
	{
		commands = new HashMap<>();
	}

	// Need to include idea of name & alias so help output isn't annoying
	public CommandSet add(Command command, String... names)
	{
		for (String name: names)
		{
			commands.put(name, command);
		}

		return this;
	}

	public void execute(String[] args)
	{
		String commandName = null;
		String[] commandArgs = {};


		if (args.length > 0) commandName = args[0];
		if (args.length > 1) commandArgs = Arrays.copyOfRange(args, 1, args.length);

		if (commandName == null)
		{
			help();
		}

		Command command = commands.get(commandName);

		if (command != null)
		{
			command.execute(commandArgs);
		}
		else
		{
			help();
		}
	}

	public void help()
	{
			System.out.println("Available commands:");
			for (String commandName: commands.keySet())
			{
				System.out.println(" " + commandName);
			}
	}
}
