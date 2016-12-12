package Crawler;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;


//This code takes arguments:
//0:seedURL
//1:Number of pages
//2:hop level
//3:Output Directory


public class Start { //extends checkRobots

	static String saveLocation = null;
	//static String saveLocation = "D:/Tanaya/Study material/Fall'16/IR/Project/Lucene Test FIles/CrawledData/";
	public static Queue< String > urls = new LinkedList< String >();
	static ConcurrentHashMap< String,Integer > mapChkSum = new ConcurrentHashMap< String,Integer >();
	static int maxThreadCount =50;
	static int minLevels = 0;
	static int maxLevels = 999;
	static int hopLevel=0;
	static int numPages=0;
	static int downloadedPages=0;
	public static ConcurrentHashMap<String, List<String>>  mainMap
	= new ConcurrentHashMap<String,List<String>>();
	
	public static HashMap<String, Integer> alreadyParsedURLs = new HashMap<String, Integer>();  
	
	public static void main(String[] args) {
		String sCurrentLine; 
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(args[0])); //"D:/Tanaya/Study material/Fall'16/IR/test.txt"))
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			//e.printStackTrace();
		}

		try {
			while ((sCurrentLine = br.readLine()) != null) {
				urls.offer(sCurrentLine);
			  System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		
		hopLevel=Integer.parseInt(args[1]);
		numPages=Integer.parseInt(args[2]);
		saveLocation =args[3];
		
		Integer threadCounter = 0;
		ExecutorService executor = Executors.newCachedThreadPool();

		while( true )
		{
			threadCounter++;
			executor.execute(new multiThreading( ));
			if( threadCounter == maxThreadCount )
			{
				break;
			}
		}
		
		executor.shutdown();
        System.out.println("Finished all threads");
}
}	