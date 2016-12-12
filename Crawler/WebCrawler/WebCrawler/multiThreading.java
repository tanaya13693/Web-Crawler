package Crawler;

import java.io.IOException;

//Java code for thread creation by implementing
//the Runnable Interface
class multiThreading implements Runnable
{	
	public void run()
	{
		try
		{
			parse.crawling();
			
		}
		catch (InterruptedException e1)
		{
			System.out.println("Thread Interupted");
		}
		catch( IOException e2 )
		{
			//e2.printStackTrace();
		}
		catch( Exception e3 )
		{
			//e3.printStackTrace();
		}	
     }

}