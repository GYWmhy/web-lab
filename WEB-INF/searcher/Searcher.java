package searcher;

import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

import org.apache.lucene.search.*;
import org.apache.lucene.document.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Searcher {
	
	public static void main(String[] args) throws IOException, ParseException{
    	
    	Scanner in = new Scanner(System.in);
    	String qWord = in.nextLine();
    	String option = in.nextLine();
    	String ans = Search(qWord, option);
    	System.out.println(ans);
    	in.close();
    }
	
	public static String Search(String qWord, String option) throws IOException, ParseException{

		String qString = qWord;
		StringBuffer sb = new StringBuffer("");

		final Path path = Paths.get("G:\\log\\");
		Directory directory = FSDirectory.open(path);
		Analyzer analyzer = new StandardAnalyzer();

	      IndexReader indexReader = DirectoryReader.open(directory);
	      IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	
	      
	      QueryParser queryParser;
	      if(option.matches("Topic")) {
	    	  queryParser = new QueryParser("TOPIC",analyzer);
	      }
	      else if(option.matches("Location")) {
	    	  queryParser = new QueryParser("POSITION",analyzer);
	      }
	      else if(option.matches("Deadline")) {
	    	  queryParser = new QueryParser("DDL",analyzer);
	      }
	      else if(option.matches("Date")) {
	    	  queryParser = new QueryParser("DATE",analyzer);
	      }
	      else {
	    	  queryParser = new QueryParser("TOPIC",analyzer);
	      }
	      
	      Query query;
			try {
				query = queryParser.parse(qString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error!";
			}
	      
	      TopDocs topDocs = indexSearcher.search(query,200);
	      long cnt = topDocs.totalHits;
	      
	      sb.append("<head>"
	      		+ "<title>Search for " + qString.toUpperCase() + "</title>"
	      		+ "<style type=\"text/css\">"
		      		+ ".TM {background:rgba(200, 200, 200, 0.3) none repeat scroll 0 0 !important;"
		      		+ "filter:Alpha(opacity=80);"
		      		+ "font-family: \"Palatino Linotype\";"
		      		+ "color: #f1f1f1;"
		      		+ "position: relative;"
		      		+ "padding-left: 10%;"
		      		+ "padding-right: 10%;"
		      		+ "border: none;}"
		      		+ "</style>"
		      		+ "<link rel=\"shortcut icon\" href=\"img/logo.jpg\">"
		      		+ "</head>"
		      		+ "<body background=\"img/bg0.jpg\"><table border = 1 cellpadding=\"10\" class=\"TM\">\r\n");
	      
		      sb.append("<div align=left style=\"padding-left:10%;\"><font color=white style=\"font-family: \"Trebuchet MS\", Sans-serif;\">"
		      		+ "<h3><b>\"" + qString + "\"</b> found " + cnt + " term(s).</h3>"
		      				+ "</font></div>\r\n");
	      if(cnt != 0) {
		      sb.append("<thead>\r\n" + 
						"<tr>\r\n" + 
						"<th>No.</th>\r\n" + 
						"<th>Topic</th>\r\n" + 
						"<th>Location</th>\r\n" + 
						"<th>Sent time</th>\r\n" + 
						"<th>Deadline</th>\r\n" + 
						"<th>Date</th>\r\n" + 
						"</tr>\r\n" + 
						"</thead>\r\n");
	      }
	      else {
	    	  
	      }

	      ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	      int [] tag = new int[(int)cnt];
	      int max = 0;
	      for(int k = 0;k < cnt;k++) {
	    	  Document doc = indexSearcher.doc(scoreDocs[k].doc);
	    	  tag[k] = doc.get("SCORE").charAt(0) - '0';
	      }
	      
	      int t;
	      for(int k = 0;k < cnt - 1;k++) {
	    	  t = k;
	    	  max = tag[t];
	    	  for(int j = k;j < cnt;j++) {
	    		  if(tag[j] > max) {
		    		  max = tag[j];
		    		  t = j;
		    	  }
	    	  }
	    	  if(k != t) {
	    		  ScoreDoc temp = scoreDocs[k];
	    		  scoreDocs[k] = scoreDocs[t];
	    		  scoreDocs[t] = temp;
	    		  int tmp = tag[k];
	    		  tag[k] = tag[t];
	    		  tag[t] = tmp;
	    	  }
	      }

	      
	      int i = 1;
	      for (ScoreDoc scoreDoc : scoreDocs) {
	          Document document = indexSearcher.doc(scoreDoc.doc);
	          String filename = document.get("filename");
	          String topic = document.get("TOPIC");
	          String senttime = changeDate(document.get("SENT"));
	          String deadline = changeDate(document.get("DDL"));
	          String date = changeDate(document.get("DATE"));
	          String location = document.get("POSITION");
	          
	          
	          sb.append("<tbody><tr valign=TOP>\r\n" + 
		          	"<td>" + i + "</td>\r\n" +  
	          		"<td>" + topic + "</td>\r\n" + 
	          		"<td>" + location + "</td>\r\n" + 
	          		"<td>" + senttime + "</td>\r\n" + 
	          		"<td>" + deadline + "</td>\r\n" + 
	          		"<td>" + date + "</td>\r\n" + 
	          		"</tr></tbody>\r\n");
	          i++;
	      }
	      
	      sb.append("</table></body>");
	      String ret = sb.toString().replaceAll(qString, "<font color=\"#6dff54\"><b><u>" + qString + "</u></b></font>");
	      if(!qWord.matches("IEEE") && !qWord.matches("ACM") && !qWord.matches("AAAI") && !qWord.matches("Springer")) {
		      ret = ret.replaceAll("IEEE", "<font color=\"#4ae5ff\">IEEE</font>");
		      ret = ret.replaceAll("ACM", "<font color=\"#4ae5ff\">ACM</font>");
		      ret = ret.replaceAll("AAAI", "<font color=\"#4ae5ff\">AAAI</font>");
		      ret = ret.replaceAll("Springer", "<font color=\"#4ae5ff\">Springer</font>");
	      }
	      return ret;
	}
	
	static String [] MONTH = {
    		"Jan","Feb","Mar","Apr",
			  "May","Jun","Jul","Aug",
			  "Sep","Oct","Nov","Dec"
    };
	
	public static String changeDate(String date) {
		if(date.matches("0-0-2000")) {
			return " ";
		}
		String [] divDate = date.split("-");
		if(divDate[0].matches("0")) {
			return divDate[2];
		}
		else if(divDate[1].matches("0")) {
			int sum = 0;
			for(int i = 0;i < divDate[0].length();i++) {
				sum *= 10;
				sum += divDate[0].charAt(i) - '0';
			}
			return MONTH[sum - 1] + "-" + divDate[2];
		}
		else {
			return date;
		}
	}
	
}
