package com.rowanstringer;

/**
 * Program to count to a specified number using multiple threads
 */
public class Main
{
    private static final int c_expectedParamCount = 2;

    /**
     * Program entry point
     */
    public static void main(String[] args)
    {
        if(args.length == Main.c_expectedParamCount)
        {
            try
            {
                // Parse command line arguments
                int numThreads = Integer.parseInt(args[0]);
                int maxCount = Integer.parseInt(args[1]);

                // Start counting
                MultiThreadedCounter counter = new MultiThreadedCounter();
                if(counter.Initialise(numThreads))
                {
                    counter.Count(maxCount);
                }
            }
            catch (NumberFormatException e) // Unexpected arguments
            {
                printHelp();
            }
        }
        else
        {
            printHelp();
        }
    }

    /**
     * Print help text in case of incorrect parameters
     */
    private static void printHelp()
    {
        System.out.println("Two integer parameters are required to run this program (numThreads, maxCount): " +
                "e.g. java main.java 12 1000000 ");
    }
}
