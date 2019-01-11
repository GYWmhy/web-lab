import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class indexWriter {
	public static void main(String[] args) throws IOException, ParseException {
    	
    	addFile();
		//SearchFiles();
    }
	
	public static void addFile() throws IOException {

	    final Path path = Paths.get("G:\\log\\");

	    Directory directory = FSDirectory.open(path);
	    Analyzer analyzer = new StandardAnalyzer();

	    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
	    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

	    IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
	    
	    File dir = new File("G:\\testoutput");
	    String s[] = dir.list();
	    
	    for (int i = 0; i < s.length; i++) {
		    BufferedReader f = new BufferedReader(new FileReader("G:\\extractoutput\\" + s[i]));
		    //System.out.println("doc: " + s[i]);
		    
		    String date = "", sent = "", ddl = "", topic = "", position = "";
	    	String temp = "";
		    
		    temp = f.readLine();
		    if(temp.matches("DATE")) {
		    	sent = f.readLine();
		    	ddl = f.readLine();
		    	date = f.readLine();
		    }
		    while(!temp.matches("TOPIC")) {
		    	temp = f.readLine();
		    }
		    if(temp.matches("TOPIC")) {
		    	temp = f.readLine();
		    	while(!temp.matches("POSITION")) {
		    		topic += temp + " ";
		    		temp = f.readLine();
		    	}
		    }
		    position = f.readLine();
		    
		    /*rank*/
		    
		    String score = "";
		    if(topic.contains("ACM") || topic.contains("IEEE"))	score = "4";
		    else if(topic.contains("AAAI") || topic.contains("Springer") || topic.contains("Elsevier"))	score = "3";
		    else if(topic.contains("Journal"))	score = "2";
		    else if(topic.contains("International") || topic.contains("Conference"))	score = "1";
		    else	score = "0";
		    
		    
		    Document document = new Document();
		    document.add(new TextField("filename", s[i], Field.Store.YES));
		    document.add(new TextField("SENT", sent, Field.Store.YES));
		    document.add(new TextField("DDL", ddl, Field.Store.YES));
		    document.add(new TextField("DATE", date, Field.Store.YES));
			document.add(new TextField("TOPIC", topic, Field.Store.YES));
			document.add(new TextField("POSITION", position, Field.Store.YES));
			document.add(new TextField("SCORE", score, Field.Store.YES));
		    indexWriter.addDocument(document);
		    f.close();
	    }
	    
	    indexWriter.close();

	}
	
	public static void SearchFiles() throws IOException, ParseException {

    	Scanner in = new Scanner(System.in);
    	String qString = in.nextLine();
    	//System.out.println(qString);

      //多条件
//      Query q = MultiFieldQueryParser.parse(new String[]{},new String[]{},new StandardAnalyzer());

      final Path path = Paths.get("G:\\log\\");
      Directory directory = FSDirectory.open(path);
      Analyzer analyzer = new StandardAnalyzer();

      IndexReader indexReader = DirectoryReader.open(directory);
      IndexSearcher indexSearcher = new IndexSearcher(indexReader);

      //单条件
      //QueryParser queryParser = new QueryParser("DATE",analyzer);
      QueryParser queryParser = new QueryParser("TOPIC",analyzer);
      //QueryParser queryParser = new QueryParser("POSITION",analyzer);
      Query query = queryParser.parse(qString);

      TopDocs topDocs = indexSearcher.search(query,10);

      long cnt = topDocs.totalHits;
      System.out.println("检索总条数："+cnt);
      ScoreDoc[] scoreDocs = topDocs.scoreDocs;
      for (ScoreDoc scoreDoc : scoreDocs) {
          Document document = indexSearcher.doc(scoreDoc.doc);
          //System.out.println("相关度："+scoreDoc.score);
          System.out.println(document.get("filename"));
          System.out.println(document.get("TOPIC"));
          System.out.println(document.get("SENT"));
          System.out.println(document.get("DDL"));
          System.out.println(document.get("DATE"));
          System.out.println(document.get("POSITION"));
          System.out.println(document.get("SCORE"));
      }
      
      in.close();
  }
	
}
