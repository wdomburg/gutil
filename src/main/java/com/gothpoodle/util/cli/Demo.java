package com.gothpoodle.util.cli;

public class Demo
{

	public static void hello(String[] args)
	{
		System.out.println("Hello.");
	}

	public static void goodbye(String[] args)
	{
		System.out.println("Goodbye.");
	}

	public static void main(String[] args)
	{

		CommandSet subcmds = new CommandSet();

		subcmds.add(Demo::hello, "hello", "hi");
		subcmds.add(Demo::goodbye, "goodbye", "bye");

		subcmds.execute(args);
	}

}
