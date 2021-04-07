package com.gothpoodle.util.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config
{
	public ArrayList<Stanza> stanzas = new ArrayList<>();

	public Config()
	{
	}

	public Config(String path)
		throws ParseException, IOException
	{
		Parser.parse(this, Paths.get(path));
	}

	public Config(Path path)
		throws ParseException, IOException
	{
		Parser.parse(this, path);
	}

	public Config addStanza(Stanza stanza)
	{
		stanzas.add(stanza);
		return this;
	}

	public List<Stanza> getStanzas()
	{
		return stanzas;
	}

	public String toString()
	{
		return stanzas.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
}
