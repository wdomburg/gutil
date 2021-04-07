package com.gothpoodle.util.collections;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Cache<K,V>
	extends AbstractMap<K,V>
{
	private long maxAge;

	private LRUMap<K,Element<V>> lru;

	public Cache(int maxCount, long maxAge)
	{
		lru = new LRUMap<>(maxCount);
		this.maxAge = maxAge;
	}

	public V put(K key, V value)
	{
		lru.put(key,new Element<V>(value));
		return value;
	}

	public V get(Object key)
	{
		Element<V> elem = lru.get(key);

		if (elem == null)
		{
			return null;
		}

		long age = new Date().getTime() - elem.getTimestamp();

		if (age > maxAge)
		{
			remove(key);
			return null;
		}

		return elem.getValue();
	}

	public V remove(Object key)
	{
		Element<V> elem = lru.get(key);

		if (elem == null)
		{
			return null;
		} 	

		V value = elem.getValue();

		lru.remove(key);
		return value;
	}

	public int size()
	{
		return lru.size();
	}

	// TODO: Figure out how to do this with streams
	//return lru.entrySet().stream().map(Cache::convert).collect(Collectors.<Map.Entry<K,V>> toSet());
	public Set<Map.Entry<K,V>> entrySet()
	{
		Set<Map.Entry<K,V>> set = new LinkedHashSet<>();

		for(Map.Entry<K,Element<V>> entry: lru.entrySet())
		{
			set.add(extract(entry));
		}

		return set;
	}

	public Map.Entry<K,V> extract(Map.Entry<K,Element<V>> entry)
	{
		K key = entry.getKey();
		V value = entry.getValue().getValue();
		return new AbstractMap.SimpleEntry<>(key,value);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("{");

		Iterator<Entry<K,Element<V>>> i = lru.entrySet().iterator();

		while(i.hasNext())
		{
			Entry<K,Element<V>> entry = i.next();
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue().getValue());
			if (i.hasNext())
			{
				sb.append(", ");
			}
		}
		sb.append("}");

		return sb.toString();
	}

	private static class LRUMap<K,V>
		extends LinkedHashMap<K,V>
	{
		private int capacity;

		public LRUMap(int capacity)
		{
			super(capacity + 1, 0.1f, true);
			this.capacity = capacity;
		}	

		public boolean removeEldestEntry(Map.Entry<K,V> entry)
		{
			return size() > capacity;
		}
	}
		
	private static class Element<V>
	{
		V obj;
		long ts; 

		public Element(V obj)
		{
			this.obj = obj;
			this.ts = new Date().getTime();
		}

		public V getValue()
		{
			return obj;
		}

		public long getTimestamp()
		{
			return ts;
		}

		public String toString()
		{
			return String.format("'%s' [%d]", obj.toString(), ts);
		}
	}
}
