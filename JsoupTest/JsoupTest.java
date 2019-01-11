package JsoupTest;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.lang.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class JsoupTest {
    public static void main(String[] args) throws IOException{
    	Scanner in = new Scanner(System.in);
    	String url = "https://research.cs.wisc.edu/dbworld/browse.html";
    	
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        //System.out.println("title is: " + title);
        
        Elements tbodys = doc.getElementsByTag("tbody");
        int i = 0;
        
        Elements links = doc.getElementsByTag("a");
        for (Element link:links) {
        	String linkhref = link.attr("href");
        	String linktext = link.text();
        	String senttime = "";
        	String deadline = "";
        	
        	String tdtext = tbodys.first().toString();
        	
        	int tdind = tdtext.indexOf("-201");
        	int tdhd = tdind, tdrr = tdind + 5;
        	if(tdind == -1) {
        		senttime = "[sent time]0-0-2000 ";
        	}
        	else {
	        	for(int j = tdind;j > 0;j--) {
	        		if(tdtext.charAt(j) == '>') {
	        			tdhd = j + 1;
	        			break;
	        		}
	        	}
	        	senttime = "[sent time]" + tdtext.substring(tdhd, tdrr) + " ";
        	}
        	
	        if(linkhref.startsWith("http") && !linktext.matches("web page")) {
	        	//System.out.println("link: " + linkhref);
	        	Document subdoc = Jsoup.connect(linkhref).get();
	        	Elements body = subdoc.select("body");
	        	
	        	tdind = tdtext.indexOf("-201", tdrr);
	        	if(tdind == -1) {
	        		deadline = "[deadline]0-0-2000 ";
	        	}
	        	else {
		        	tdhd = tdind;
		        	tdrr = tdind + 5;
		        	for(int j = tdind;j > 0;j--) {
		        		if(tdtext.charAt(j) == '>') {
		        			tdhd = j + 1;
		        			break;
		        		}
		        	}
		        	deadline = "[deadline]" + tdtext.substring(tdhd, tdrr) + " ";
	        	}
	        	tbodys = tbodys.next();
	        	
	        	String bodytext = body.text().replaceAll("\n", "\r\n");
	        	bodytext += "\r\n" + senttime + "\r\n" + deadline;
	        	writeText(bodytext,linktext,i);
	        	i++;
	        	try {
	        		TimeUnit.SECONDS.sleep(1);
	        	} catch(InterruptedException ex) {
	        		Thread.currentThread().interrupt();
	        	}
	        }
	        if(i >= 200)		//only get last 200 pages
	        	break;
        }
        in.close();
    }
    
    public static void writeText(String s, String linktext, int i) 
    {
       String fileName = "G:\\data\\" + i + ".txt";
       File file = new File(fileName);
       if(!file.exists()&&!file.isFile())
       {
          try {
              file.createNewFile();
          } catch (IOException e) {
              e.printStackTrace();
          }
       }
       if(file.exists()&&file.isFile()){
           FileOutputStream fos = null;
           try {
               fos = new FileOutputStream(fileName);
           } catch (FileNotFoundException e2) {
               e2.printStackTrace();
           }
           try {
               fos.write(s.getBytes());
           } catch (IOException e1) {
               e1.printStackTrace();
           }
           try {
               fos.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       else{
           System.out.println(linktext+" can't write.");
       }
    }
}
