package com.gothpoodle.util;

public class HashUtil
{
	public static class SDBM
	{
		public static String hash(String input)
		{
			long h = 0;

			for (byte b: input.getBytes())
			{
				h = b + (h << 6) + (h << 16) - h;
			}

			//return Long.toString(h & 0xFFFFFFF, 16);
			return Long.toString(h, 16);
		}
	}

	public static class DJB2
	{
		public static String hash(String input)
		{
			long h = 5481;

			for (byte b: input.getBytes())
			{
				h = 33 * h ^ b;
				//h = ((h << 5) + h) ^ b;
			}

			return Long.toString(h, 16);
		}
	}

	public static class FNV1
	{
		public static String hash(String input)
		{
   	    	long h = 0xcbf29ce484222325L;

			for (byte b: input.getBytes())
			{
				h ^= b;
				h *= 0x100000001b3L;
			}

        	return Long.toString(h, 16);
		}
    }
}
