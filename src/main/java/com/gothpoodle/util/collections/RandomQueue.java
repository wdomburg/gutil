package com.gothpoodle.util.collections;

import java.lang.Integer;
import java.lang.UnsupportedOperationException;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

class RandomQueue<E>
	extends AbstractQueue<E> implements BlockingQueue<E>
{

		// Callable that produces an element of the required type
		Callable<E> c;
		// Default element to return if the callable fails
		E d;

		public RandomQueue(Callable<E> c, E d)
		{
			this.c = c;
			this.d = d;
		}

		public RandomQueue(Callable<E> c)
		{
			this.c = c;
			this.d = null;
		}

		public Iterator<E> iterator()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException();
		}

		public boolean add(E e)
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<? extends E> c)
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException();
		}

		public void put(E e)
			throws IllegalStateException
		{
			throw new IllegalStateException("Not supported for this collection.");
		}

		public boolean offer(E e)
			throws IllegalStateException
		{
			throw new IllegalStateException();
		}

		public boolean offer(E e, long timeout, TimeUnit unit)
			throws IllegalStateException
		{
			throw new IllegalStateException("Not supported for this collection.");
		}

		public int drainTo(Collection<? super E> c)
		{
			throw new IllegalStateException("Not implemented.");
		}

		public int drainTo(Collection<? super E> c, int maxElements)
		{
			throw new IllegalStateException("Not implemented.");
		}

		public E peek()
		{
			throw new IllegalStateException("Not implemented.");
		}

		public E element()
		{
			throw new IllegalStateException("Not implemented.");
		}

		public E take()
		{
			E e;
			try
			{
				e = c.call();
			}
			catch (Exception exp)
			{
				e = d;
			}

			return e;
		}

		public E poll()
		{
			return take();
		}

		public E poll(long timeout, TimeUnit unit)
		{
			return poll();
		}

		public E remove()
		{
			return poll();
		}

		public boolean contains(Object o)
		{
			return false;
		}

		public int size()
		{
			return Integer.MAX_VALUE;
		}

		public int remainingCapacity()
		{
			return 0;
		}

		public void clear()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException();
		}

}
