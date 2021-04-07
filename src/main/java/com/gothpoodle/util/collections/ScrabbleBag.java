package com.gothpoodle.util.collections;

import java.util.concurrent.ThreadLocalRandom;

import java.lang.UnsupportedOperationException;

import java.lang.String;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ScrabbleBag<E>
	extends AbstractCollection<E>
{
	public Node<E> root;

	public class Node<E>
	{
		public int count;
		public E value;
		public Node<E> left;
		public Node<E> right;

		public Node(E value, int count)
		{
			this.value = value;
			this.count = count;
		}

		public E next()
		{
			if (count == 0)
			{
				return null;
			}

			int n = ThreadLocalRandom.current().nextInt(count);

			return next(n);
		}

		public E next(int n)
		{
			if (count < n)
			{
				System.err.println("Called next() out of bounds. Should not get here.");
				return null;
			}

			if (value != null)
			{
				return value;
			}
			else
			{
				if (n < left.count)
				{
					return left.next(n);
				}
				else
				{
					return right.next(n - left.count);
				}
			}
		}

		public E take()
		{
			if (count == 0)
			{
				return null;
			}

			int n = ThreadLocalRandom.current().nextInt(count);

			return take(n);
		}

		public E take(int n)
		{
			E rv;

			if (value != null)
			{
				if (count < n)
				{
					System.err.println("Called take() out of bounds. Should not get here.");
					return null;
				}

				count -= 1;

				rv = value;
			}
			else
			{
				Node<E> n0;
				Node<E> n1;

				// Traversing "left" or "right"
				if (n < left.count)
				{
					n0 = left;
					n1 = right;
				}
				else
				{
					n -= left.count;
					n0 = right;
					n1 = left;
				}

				// Grab the value
				rv = n0.take(n);

				// Merge the other node up if we emptied this one
				if (n0.count == 0)
				{
					if (n1.value == null)
					{
						n0 = n1.left;
						n1 = n1.right;
					}
					else
					{
						value = n1.value;
						left = null;
						right = null;
					}
				}

				count -= 1;
			}

			return rv;
		}


		public Node<E> add(Node<E> node)
		{
			if (value != null)
			{
				left = new Node<E>(value, count);
				right = node;
				value = null;
			}
			else
			{
				if (left.count < right.count)
				{
					left.add(node);
				}
				else
				{
					right.add(node);
				}
			}

			count += node.count;

			return this;
		}

		public void print()
		{
			print(0);
		}

		public void print(int depth)
		{
			if (value == null)
			{
				System.out.println(indent(depth) + "*: (" + count + ")");
				left.print(depth + 1);
				right.print(depth + 1);
			}
			else
			{
				System.out.println(indent(depth) + value.toString() +": (" + count + ")");
			}
		}

		public String indent(int depth)
		{
			return String.join("", Collections.nCopies(depth, "=> "));
		}

		public Object[] toArray()
		{
			Object[] ary = new Object[count];
			return toArray(ary, 0);
		}

		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] ary, int offset)
		{
			if (value == null)
			{
				left.toArray(ary, offset);
				right.toArray(ary, offset + left.count);
			}
			else
			{
				for (int i=offset;i<offset+count;i++)
				{
					ary[i] = (T) value;
				}
			}

			return ary;
		}
	}

	// Consuming iterator; consider making configurable?
	protected class NodeIterator
		implements Iterator<E>
	{
		public boolean hasNext()
		{
			return size() > 0;
		}

		public E next()
		{
			return take();
		}

		// Don't throw UnsupportedOperationException to allow for use
		// with executors.
		public void remove()
		{
		}
	}

	public Iterator<E> iterator()
		throws NoSuchElementException
	{
		return new NodeIterator();
	}

	public boolean add(E value)
	{
		add(value, 1);

		return true;
	}

	public ScrabbleBag add(E value, int count)
	{
		Node<E> node = new Node<E>(value, count);

		root = (root == null) ? node : root.add(node);

		return this;
	}

	public E next()
	{
		E rv = null;
		if (root != null)
		{
			rv = root.next();
		}
		return rv;
	}

	public E take()
	{
		E rv = null;
		if (root != null)
		{
			rv = root.take();

			if (root.count == 0)
			{
				root = null;
			}
		}
		return rv;
	}

	public int size()
	{
		if (root != null)
		{
			return root.count;
		}
		else
		{
			return 0;
		}
	}

	public int count()
	{
		return size();
	}

	public void print()
	{
		if (root != null)
		{
			root.print();
		}
	}

	public void clear()
	{
		root = null;
	}

	public boolean retainAll(Collection<?> c)
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public Object[] toArray()
	{
		return root.toArray();
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a)
	{
		if (a.length < size())
		{
			Class aClass = a.getClass().getComponentType();
			a = (T[]) java.lang.reflect.Array.newInstance(aClass, size());
		}
		
		return (T[]) root.toArray(a, 0);
	}

	public static void main(String[] args)
	{
		int n = 1;
		int c = 1;
		int r = 1;

		ScrabbleBag<Integer> sb = new ScrabbleBag<Integer>();

		if (args.length > 2)
		{
			r = Integer.parseInt(args[2]);
		}
		if (args.length > 1)
		{
			c = Integer.parseInt(args[1]);
		}
		if (args.length > 0)
		{
			n = Integer.parseInt(args[0]);
		}

		System.err.printf("Populating with %d * %d items.\n", n, c);
		for(int i=0;i<n;i++)
		{
			sb.add(i, c);
		}

		System.err.printf("Retrieving %d items.\n", r);
		for (int i=0;i<r;i++)
		{
			System.out.println(sb.take().toString());
		}
	}

}
