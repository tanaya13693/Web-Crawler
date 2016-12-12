package Crawler;

import java.io.BufferedWriter;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Queue;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class parse
{	
	
	public static void  crawling( ) throws Exception 
	{
		checkRobots cC= new checkRobots();
		double size=0;
		int file_id=28370;
		String url = "";
		String checkSumString;
		
		while(!Start.urls.isEmpty()|| Start.downloadedPages<=Start.numPages){
			if(size> 655360)//taking size in kilobytes. If size >5GB break
				break;
			
			synchronized (Start.class){  //Keep polling from the queue synchronized
				url=Start.urls.poll(); //Else Pop (w/o exception)
			}
			
			//Fetching the content from the web
				try {
					//System.out.println(url.toString());
					URL url1= new URL(url.toString());
					if(cC.checkCrawling(url1)){ //Check is crawling is allowed
						Start.alreadyParsedURLs.put(url, 0);
						System.out.println(url.toString());
						//**Get check sum and store in checksum map
						checkSumString=getCheckSum(url.toString());
						Start.mapChkSum.put(checkSumString, 0);
						
						Document document = Jsoup.connect(url.toString())
								  .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
							      .referrer("http://www.google.com")
							      .timeout(1000*10) 
							      .get();
						
						String fileLocation=Start.saveLocation +file_id+".txt";
						file_id++;						
						FileWriter fileWriter = new FileWriter(fileLocation);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					    bufferedWriter.write(document.toString());
					    bufferedWriter.close();
					    File file =new File(fileLocation);
					    Start.downloadedPages++;
					    
					    synchronized (Start.class){
					    writeURLToFile(url.toString());
					    }
					    if(file.exists())   //Q:  big size??
							size=size+(file.length()/(1024));
					    Document parser=Jsoup.parse(file, "utf-8");
					    Elements links = parser.select("a[href]");  
					    
					    
					    for (Element link : links) 
					    { //Q: check
					    	String hostname = url1.getHost();
					    	if(hostname.toLowerCase().contains(".edu")&& !(link.attr("abs:href").isEmpty())){
					    		//checking whether we already crawled that url
					    		if(!(Start.alreadyParsedURLs.containsKey(link.attr("abs:href")))){
					    
					    			if(!Start.mapChkSum.contains(link))
					    			  			Start.urls.offer(link.attr("abs:href"));
					    			
					    		}
					    	}
					    }
					
					    //writer.close();				    
					}
					
				}catch (NullPointerException e) {
					//System.out.println("Here it is");
			        //e.printStackTrace();
			    } catch (HttpStatusException e) {
			        //e.printStackTrace();
			    } catch (IOException e) {
			        //e.printStackTrace();
			    }
		}
	}
	
	public boolean isFileDuplicate( String file1, String file2 ) throws Exception
    {	
    	String chkSum1 = getCheckSum( file1 );
    	String chkSum2 = getCheckSum( file2 );
    	
    	if( chkSum1.equals( chkSum2 ) )
    	{
    		System.out.println(" Files are equals ");
    		return true;
    	}
    	
    	return false;
    }
    
    public static String getCheckSum( String str ) throws Exception
    {
    	MessageDigest md =  MessageDigest.getInstance( "MD5" );
    	byte[] bytesOfStr = str.getBytes( "UTF-8" );
    	
    	byte[] mdBytes = md.digest( bytesOfStr );
    	
    	//convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdBytes.length; i++) 
        {
        	sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

    	return sb.toString();
    }
	
public static void writeURLToFile( String URLToWrite ){
	String filePath = Start.saveLocation +"//uniqueURLs.txt";
	FileWriter fw ;
	BufferedWriter bw;
    try {
		fw = new FileWriter( filePath , true );
		bw = new BufferedWriter( fw );
		bw.append( URLToWrite + "\n");
		bw.close();
		fw.close();

	} catch (IOException e) {
		e.printStackTrace();
	}	
}//End function writeURLToFile()
}