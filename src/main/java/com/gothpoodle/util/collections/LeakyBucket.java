package com.gothpoodle.util.collections;

import java.lang.InterruptedException;
import java.lang.UnsupportedOperationException;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Leaky bucket
public class LeakyBucket<E>
	extends AbstractQueue<E> implements BlockingQueue<E>
{
	final BlockingQueue<E> wrappedQueue;
	
	private final ReentrantLock lock;
	private final Condition available;

	TimerThread timerThread;
	int perMinute;

	public LeakyBucket(BlockingQueue<E> wrappedQueue, int perMinute)
	{
		this.wrappedQueue = wrappedQueue;
		this.perMinute = perMinute;

		lock = new ReentrantLock(false);
		available = lock.newCondition();

		start();
	}

	public void start()
	{
		timerThread = new TimerThread();
		timerThread.start();
	}

	public void stop()
	{
		timerThread.runThread = false;
	}

	public void setInterval(int perMinute)
	{
		this.perMinute = perMinute;
		timerThread.updateInterval();
	}

	public int getInterval()
	{
		return perMinute;
	}

	private class TimerThread extends Thread
	{
		long interval;

		public volatile boolean runThread = true;

		TimerThread()
		{
			updateInterval();
		}

		protected void updateInterval()
		{
			interval = TimeUnit.MINUTES.toNanos(1) / perMinute;
		}

		public void run()
		{
			while(runThread)
			{
				try
				{
					TimeUnit.NANOSECONDS.sleep(interval);
				}
				catch (InterruptedException e)
				{
					System.out.println("Got exception " + e.getMessage());
				}

				lock.lock();
				try
				{
					available.signal();
				}
				finally
				{
					lock.unlock();
				}
			}
		}
	}

	public E take()
		throws InterruptedException
	{
		lock.lockInterruptibly();
		try
		{
			available.await();
			return wrappedQueue.take();
		}
		finally
		{
			lock.unlock();
		}
	}

	public E poll()
	{
		try
		{
			lock.lockInterruptibly();
			available.await();
			return wrappedQueue.poll();
		}
		catch (InterruptedException e)
		{
			return null;
		}

		finally
		{
			lock.unlock();
		}
	}

	public E poll(long timeout, TimeUnit unit)
		throws InterruptedException
	{
		//FIXME: This needs to first wait for the "available", calculate the remaining time
		//And then call wrappedQueue.poll() with the remaining time.
		return wrappedQueue.poll(timeout, unit);
	}

	public E peek()
	{
		return wrappedQueue.peek();
	}

	public void put(E e)
		throws InterruptedException
	{
		wrappedQueue.put(e);
	}

	public boolean offer(E e)
	{
		return wrappedQueue.offer(e);
	}

	public boolean offer(E e, long timeout, TimeUnit unit)
		throws InterruptedException
	{
		return wrappedQueue.offer(e, timeout, unit);
	}

	public int size()
	{
		return wrappedQueue.size();
	}

	public int remainingCapacity()
	{
		return wrappedQueue.remainingCapacity();
	}

	public boolean isEmpty()
	{
		return wrappedQueue.isEmpty();
	}

	public boolean contains(Object o)
	{
		return wrappedQueue.contains(o);
	}

	public Object[] toArray()
	{
		return wrappedQueue.toArray();
	}

	public <T> T[] toArray(T[] a)
	{
		return wrappedQueue.toArray(a);
	}

	public String toString()
	{
		return wrappedQueue.toString();
	}

	public boolean remove(Object o)
	{
		return wrappedQueue.remove(o);
	}

	public void clear()
	{
		wrappedQueue.clear();
	}

	public boolean containsAll(Queue<?> coll)
	{
		return wrappedQueue.containsAll(coll);
	}

	public boolean removeAll(Queue<?> coll)
	{
		return wrappedQueue.removeAll(coll);
	}

	public int drainTo(Collection<? super E> c)
	{
		throw new IllegalStateException("Not implemented.");
	}

	public int drainTo(Collection<? super E> c, int maxElements)
	{
		throw new IllegalStateException("Not implemented.");
	}

	public LeakyBucketIterator iterator()
	{
		lock.lock();
		try
		{
			return new LeakyBucketIterator();
		}
		finally
		{
			lock.unlock();
		}
	}

	private class LeakyBucketIterator
		implements Iterator<E>
	{
		Iterator<E> wrappedIterator = wrappedQueue.iterator();

		public boolean hasNext()
		{
			return wrappedIterator.hasNext();
		}

		public E next()
		{
			lock.lock();
			try
			{
				available.awaitUninterruptibly();
				return wrappedIterator.next();
			}
			finally
			{
				lock.unlock();
			}
		}

		public void remove()
		{
			wrappedIterator.remove();
		}
	}

}
