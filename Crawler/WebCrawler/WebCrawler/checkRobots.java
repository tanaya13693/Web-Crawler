package Crawler;

import java.io.IOException;
import java.net.URL;
import java.util.List;



public class checkRobots {
	
	static boolean checkCrawling(URL url) {
		String robots="http://"+ url.getHost() + "/robots.txt";
   	 	
   	 	if(!Start.mainMap.containsKey(robots)){   //Domain not present in mainMap
   	 		parseRobot p= new parseRobot();
   	 		try {
   	 			p.parseRobots(url);
   	 		} catch (IOException e) {
   	 			// TODO Auto-generated catch block
   	 			//e.printStackTrace();
   	 		}
   	 	}
   	 	
   	 List<String> disAllows=Start.mainMap.get(robots);
	//checking for thedisallowed contents in the main Map
   	for (String disAllow : disAllows) {
   	    if(disAllow.equals(url.getPath())){
   	    	return false;
   	    }
   	}
   	
   	return true;
		
	}	
	}



