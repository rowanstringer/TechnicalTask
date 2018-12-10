package com.rowanstringer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MultiThreadedCounter: counts to a specified number using specified number of threads
 */
public class MultiThreadedCounter implements Runnable
{
    // Validation
    private final int c_minThreadCount = 1;
    private final int c_maxThreadCount = 255;

    // State
    private int m_count = 0;
    private int m_maxCount = 0;
    private boolean m_initialised = false;
    private boolean m_countInProgress = false;

    // Use non-greedy lock
    private ReentrantLock m_lock = new ReentrantLock(true);

    // Worker threads
    private List<Thread> m_threads;

    /**
     * Initialise a MultiThreadedCounter with a set number of allocated threads
     * @param numThreads - number of threads to count with
     */
    public boolean Initialise(int numThreads)
    {
        if(ValidateThreadCount(numThreads))
        {
            m_threads = new ArrayList<>(numThreads);

            for(int i = 0; i < numThreads; ++i)
            {
                m_threads.add(new Thread(this));
            }

            m_initialised = true;
        }

        return m_initialised;
    }

    /**
     * Validate whether a given thread count is acceptable
     * @param count - number of threads to be allocated
     */
    private boolean ValidateThreadCount(int count)
    {
        if(count < c_minThreadCount)
        {
            System.out.println(String.format("Thread count (%d) is smaller than the minimum allowed (%d), " +
                            "please choose a higher number.", count, c_minThreadCount));
            return false;
        }
        else if(count > c_maxThreadCount)
        {
            System.out.println(String.format("Thread count (%d) is greater than the maximum allowed (%d), " +
                            "please choose a smaller number.", count, c_maxThreadCount));
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Count from 0 to specified max value
     */
    public void Count(int maxCount)
    {
        if(m_initialised)
        {
                if(!m_countInProgress)
            {
                m_countInProgress = true;
                m_maxCount = maxCount;

                int numThreads = m_threads.size();
                for (int i = 0; i < numThreads; ++i)
                {
                    m_threads.get(i).start();
                }
            }
            else
            {
                System.out.println("Count is already in progress - ignoring request to count.");
            }
        }
        else
        {
            System.out.println("Please initialise counter before starting count!");
        }
    }

    /**
     * Implementation of Runnable interface
     */
    @Override
    public void run()
    {
        boolean continueCounting = true;
        while(continueCounting)
        {
            continueCounting = incrementCounter();
        }

        if(m_countInProgress)
        {
            m_countInProgress = false;
        }
    }

    /**
     * Prints and increments count by one in a thread-safe manner
     */
    private boolean incrementCounter()
    {
        m_lock.lock();

        try
        {
            if(m_count <= m_maxCount)
            {
                System.out.println(m_count);
                m_count++;
                return true;
            }
            else
            {
                return false;
            }
        }
        finally
        {
            m_lock.unlock();
        }
    }
}