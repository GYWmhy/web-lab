import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Properties;
import java.io.*;


public class preText{
    public static void main(String[] args) throws IOException {
    	
    	readDir();

    }
    
    static String text;
    
    public static void readDir() throws IOException {
    	File dir = new File("G:\\data");
    	int i = 0;
    	if (dir.isDirectory()) {
	        System.out.println("目录: " + "G:\\data");
	        String s[] = dir.list();
	        for (i = 0; i < s.length; i++) {
		    //for (i = 0; i < 1; i++) {
		        text = "";
		        
		        //System.out.println("doc: " + s[i]);
		    	
	        	BufferedReader f = new BufferedReader(new FileReader("G:\\data\\" + s[i]));
	        	
	        	String str = "";
	        	String temp = "";
	        	while ((temp = f.readLine()) != null) {
	        		str += temp + "\r\n";
	        	}
	        	
	        	str = pretreat(str);
	        	text += str;
	        	
	        	writeText(text,s[i]);
    			System.out.println(s[i] + " finish");
	        	
	        	f.close();
	        }
	    } else {
	        System.out.println("Not a dir");
	    }
    }
    
    public static String pretreat(String text) {
    	int index = 0;
    	/*deal with urls*/
    	text = text.replace('?', '#');
    	text = text.replace('+', '%');
    	while((index = text.indexOf("http")) != -1) {
    		String temp = text.substring(index);
    		char [] tempch = temp.toCharArray();
    		
    		int rear = 0;
    		for(char ch:tempch) {
    			if(ch == '\r'||ch == ' '||ch == ')') {
    				break;
    			}
    			rear++;
    		}
    		temp = temp.substring(0, rear);
    		text = text.replaceFirst(temp, ".");
    	}
    	
    	/*deal with emails*/
    	while((index = text.indexOf("@")) != -1) {
    		char [] tempch = text.substring(0, index).toCharArray();
    		int head = index - 1;
    		for(;head >= 0;head--) {
    			if(tempch[head] == ' ' || tempch[head] == '(' || tempch[head] == '\n')
    				break;
    		}
    		if(head != 0) {
    			tempch = text.substring(index).toCharArray();
    			int rear = 0;
    			for(;rear < text.length() - index;rear++) {
    				if(tempch[rear] == '\r'||tempch[rear] == ' '||tempch[rear] == ')') {
        				break;
        			}
    			}
    			text = text.replaceFirst(text.substring(head, rear + index + 1), ".");
    		}
    	}

    	text = text.replace('#', '?');
    	text = text.replace('%', '+');
    	return text;
    }
    
    public static void writeText(String s, String i) {
       String fileName = "G:\\testoutput\\" + i;
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
           System.out.println(i + " can't write.");
       }
    }

}
