package Crawler;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;
import java.util.*;

public class parseRobot {
	
	public void parseRobots(URL url) throws IOException{
		
		int currentIndex = 0;
		String robots="http://"+ url.getHost() + "/robots.txt";
		URL u1 = new URL(robots);
		
		//Take all contents of robots.txt and save as string and the parse 
		String robotTextFile = robotFile(u1);  //u1("http://" + u2 + "/robots.txt"));
		
		Matcher uA = Pattern.compile("(?i)User-Agent:\\s*(.*)")  //user Agent line
				.matcher(robotTextFile);
		Matcher dA = Pattern.compile("(?i)Disallow:\\s*(.*)")    //Dissallow line parsing
				.matcher(robotTextFile);
		Matcher blank = Pattern.compile("\n\\s*\n").matcher(robotTextFile);  //blankLine parsing 

		List<String> disAllows= new ArrayList<>();
		
		
		// Find each user-agent portion of file
		while (uA.find()) 
		{
			if (uA.group(1).indexOf('*') != -1) 
			{
				currentIndex = uA.end();
				blank.region(currentIndex, robotTextFile.length());
				int blankLineIndex = robotTextFile.length();
				if ( blank.find() )
					blankLineIndex = blank.start();
				dA.region(currentIndex, blankLineIndex);
				while (dA.find()) 
				{
					String disallow = dA.group(1).trim();
					if (disallow.length() > 0) 
					{
						if (disallow.endsWith("/"))
							disallow = disallow.substring(0, disallow.lastIndexOf('/'));
					}
					disAllows.add(disallow);
				}
			}
		}
		Start.mainMap.put(robots, disAllows);
		
	}//End of parseRobots
	
	
	public static String robotFile(URL u) throws IOException {
		StringBuffer str = new StringBuffer();
		BufferedReader x =null;
		try{
			x=new BufferedReader(new InputStreamReader(u.openConnection().getInputStream()));
		}
		catch(IllegalArgumentException exc){
			System.out.println("URL can not be parsed \n");
		}
		String line = "";
		while ((line=x.readLine())!=null) 
		{
			str.append(line + "\n");
		}
		x.close();
		return str.toString();
	}

}
