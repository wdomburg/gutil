package com.gothpoodle.util;

import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.Instant;
import java.util.Random;

public class Tx120
{
	private static final char[] CHARS = "0123456789:@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	private static final Random random = new Random();

	public static byte[] generate()
	{
		return Tx120.generate(Instant.now());
	}

	public static byte[] generate(Instant instant)
	{
		byte[] randomBytes = new byte[8];

		random.nextBytes(randomBytes);

		return Tx120.generate(instant, randomBytes);
	}

	public static byte[] generate(Instant instant, byte[] randomBytes)
	{
		ByteBuffer id = ByteBuffer.allocate(15);

		long micros = (instant.getEpochSecond() * 1_000_000) + (instant.getNano() / 1_000);
		
		for (int i=48; i>=0; i-=8)
		{
			id.put((byte) ((micros >> i) & 0xff));
		}

		id.put(randomBytes);

		return id.array();
	}

	public static String encode(byte[] bytes)
	{
		ByteBuffer id = ByteBuffer.wrap(bytes);

		char[] chars = new char[20];

		for (int i=0; i<5; i++)
		{
			int block = (id.get() & 0xFF) << 16 | (id.get() & 0xFF) << 8 | (id.get() & 0xFF);

			chars[i*4]   = CHARS[(block >> 18) & 0x3F];
			chars[i*4+1] = CHARS[(block >> 12) & 0x3F];
			chars[i*4+2] = CHARS[(block >>  6) & 0x3F];
			chars[i*4+3] = CHARS[(block)       & 0x3F];
		}

		return new String(chars);
	}

	public static String generateString()
	{
		return Tx120.encode(Tx120.generate());
	}

	public static String generateString(Instant instant)
	{
		return Tx120.encode(Tx120.generate(instant));
	}

	public static String generateString(Instant instant, byte[] randomBytes)
	{
		return Tx120.encode(Tx120.generate(instant, randomBytes));
	}

}
