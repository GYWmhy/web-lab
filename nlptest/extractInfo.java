import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Properties;
import java.io.*;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class extractInfo{
    public static void main(String[] args) throws IOException {
    	
    	readDir();

    }
    
    static String text;
    
    public static void readDir() throws IOException {
    	File dir = new File("G:\\testoutput");
    	int i = 0;
    	int [] tag = {0};
    	if (dir.isDirectory()) {
	        //System.out.println("目录: " + "G:\\testoutput");
	        String s[] = dir.list();
	        
	        for (i = 0; i < s.length; i++) {
		        text = "";
		        
		        //System.out.println("doc: " + s[i]);
		    	
	        	BufferedReader f = new BufferedReader(new FileReader("G:\\testoutput\\" + s[i]));
	        	
	        	String str = "";
	        	String temp = "";
	        	while ((temp = f.readLine()) != null) {
	        		str += temp + "\r\n";
	        	}
	        	
	        	String [] sens = str.split("\\.");
	        	
	        	int maxyr = 2000;
	        	int maxmon = 0;
	        	int maxday = 0;
	        	year = month = day = 0;
	        	position = "";
	        	havepos = 0;
	        	for(String sen:sens) {
	        		String [] se = sen.split("\n");
	        		for(String ss:se) {
	        			tag = proSentence(ss);
	        			String date = hasDate(ss);
	        			if(tag[0] >= 2 && date.length() > 4) {
	        				getDate(date.replaceAll(",", ""));
	        			}
	        			else if(tag[0] >= 2 && date.length() > 0) {
	        				month = day = 0;
	        				char [] tmpdt = date.toCharArray();
	        				int yr, k;
	        				for(k = 0, yr = 0;k < 4;k++) {
	        					yr *= 10;
	        					yr += tmpdt[k] - '0';
	        				}
	        				year = yr;
	        			}
	        			if(year > maxyr) {
    						maxyr = year; maxmon = month; maxday = day;
    					}
    					else if(year == maxyr && month > maxmon) {
    						maxyr = year; maxmon = month; maxday = day;
    					}
    					else if(year == maxyr && month == maxmon && day > maxday) {
    						maxyr = year; maxmon = month; maxday = day;
    					}
	        		}
	        	}
	        	int [] sent = extractOtherDate(str,"sent time");
	        	int [] ddl = extractOtherDate(str,"deadline");
	        	
	        	text += "DATE\r\n" + sent[0] + "-" + sent[1] + "-" + sent[2] + "\r\n";
	        	text += ddl[0] + "-" + ddl[1] + "-" + ddl[2] + "\r\n";
	        	text += maxmon + "-" + maxday + "-" + maxyr + "\r\n";
	        	text += "TOPIC\r\n" + pattern(str) + "\r\n";
	        	text += "POSITION\r\n" + position + "\r\n";
	        	writeText(text,s[i]);
    			//System.out.println(s[i] + " finish");
	        	
	        	f.close();
	        }
	    } else {
	        System.out.println("Not a dir");
	    }
    }
    
    static String position = "";
    static int havepos = 0;
    
    public static int [] proSentence(String str) {
    	Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(str);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        List<String> words = new ArrayList<>();
        List<String> nerTags = new ArrayList<>();
        
        int flagpos = 0, flagdate = 0;
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                words.add(word);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                nerTags.add(ne);
                if(havepos == 0) {
                	if(ne.matches("CITY") || ne.matches("STATE_OR_PROVINCE") || ne.matches("LOCATION") || ne.matches("COUNTRY")) {
                		position += word + " ";
	                	flagpos++;
	                }
                }
                if(ne.matches("DATE"))	flagdate++;
            }
        }
        if(havepos == 0 && flagpos > 0) {
        	havepos = 1;
        }
        //System.out.println(words.toString());
        //System.out.println(nerTags.toString());
        //text += "\r\n" + words.toString() + "\r\n" + nerTags.toString();
        int [] ret = {flagdate,flagpos};
		return ret;
    }
    
    static String [] MONTH = {"January","February","March","April",
    						  "May","June","July","August",
    						  "September","October","November","December",
    						  "JANUARY","FEBRUARY","MARCH","APRIL",
    						  "MAY","JUNE","JULY","AUGUST",
    						  "SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER",
    						  "Jan.","Feb.","Mar.","Apr.",
    						  "May.","Jun.","Jul.","Aug.",
    						  "Sep.","Oct.","Nov.","Dec.",
    						  "Jan ","Feb ","Mar ","Apr ",
    						  "May ","Jun ","Jul ","Aug ",
    						  "Sep ","Oct ","Nov ","Dec "};
    static int month = 0;
    static int year = 0;
    static int day = 0;
    
    public static String hasDate(String str) {
    	int i = 0;
    	for(i = 0;i < 12;i++) {
    		if(str.contains(MONTH[i])) {
    			//System.out.println(i + "-1");
    			month = i + 1;
    			break;
    		}
    	}
    	if(i == 12) {
    		for(i = 12;i < 24;i++) {
    			//System.out.println(i + "-2");
	    		if(str.contains(MONTH[i])) {
	    			month = i - 11;
	    			break;
	    		}
	    	}
    	}
    	if(i == 24) {
    		for(i = 24;i < 36;i++) {
    			//System.out.println(i + "-3");
	    		if(str.contains(MONTH[i])) {
	    			month = i - 23;
	    			break;
	    		}
	    	}
    	}
    	if(i == 36) {
    		for(i = 36;i < 48;i++) {
    			//System.out.println(i + "-4");
	    		if(str.contains(MONTH[i])) {
	    			month = i - 35;
	    			break;
	    		}
	    	}
    	}
    	if(i == 48) {
    		int index = str.indexOf("201");
    		if(index >= 0) {
    			//System.out.println(str.substring(index,index+4));
    			return str.substring(index, index + 4);
    		}	
    		else
    			return "";
    	}
    	int index = str.indexOf(MONTH[i]);
    	
    	char [] temp = str.substring(index).toCharArray();
    	int cnt, j;
    	for(j = 0, cnt = 0;j < temp.length;j++) {
    		if(temp[j] >= '0' && temp[j] <= '9')
    			cnt++;
    	}
    	if(cnt <= 4) {
    		int flag = 0;
    		temp = str.toCharArray();
    		for(j = index;j >= 0;j--) {
    			if(temp[j] >= '0' && temp[j] <= '9') {
    				flag = 1;
    			}
    			else if(flag > 0)
    				break;
    		}
    		index = j + 1;
    	}
    	
    	return str.substring(index);
    }
    
    public static void getDate(String str) {
    	if(str.contains(MONTH[month - 1])) {
    		str.replaceAll(MONTH[month - 1], "");
    	}
    	else if(str.contains(MONTH[month + 11])) {
    		str.replaceAll(MONTH[month + 11], "");
    	}
    	else if(str.contains(MONTH[month + 23])) {
    		str.replaceAll(MONTH[month + 23], "");
    	}
    	else if(str.contains(MONTH[month + 35])) {
    		str.replaceAll(MONTH[month + 35], "");
    	}
    	int yearindex = str.indexOf("201");
    	if(yearindex < 0) {
    		month = 0;
    		year = 0;
    		day = 0;
    		return;
    	}
    	String yearstr = str.substring(yearindex, yearindex + 4);
    	char [] yearch = yearstr.toCharArray();
    	
    	year = 0;
    	for(int i = 0;i < 4;i++) {
    		year *= 10;
    		year += yearch[i] - '0';
    	}
    	str.replaceAll(yearstr, " ");
    	str.replaceAll("th", "");
    	char [] rest = str.toCharArray();
    	
    	day = 0;
    	for(char ch:rest) {
    		if(ch >= '0' && ch <= '9') {
    			day *= 10;
    			day += ch - '0';
    		}
    		else if(day > 0)
    			break;
    	}
    	if(day > 31)
    		day = 0;
    }
    
    static String [] OMONTH = {
    		"Jan","Feb","Mar","Apr",
			  "May","Jun","Jul","Aug",
			  "Sep","Oct","Nov","Dec"
    };
    
    public static int [] extractOtherDate(String text, String type) {
    	int [] ret= {0,0,0};
    	int index = text.indexOf("[" + type + "]");
    	index += type.length() + 2;
    	int i;
    	for(i = index;i < text.length();i++) {
    		if(text.charAt(i) == ' ')
    			break;
    	}
    	String tempstr = text.substring(index, i);
    	String [] divstr = tempstr.split("-");
    	int j, sum;
    	for(j = 0, sum = 0;j < divstr[0].length();j++) {
    		sum *= 10;
    		sum += divstr[0].charAt(j) - '0';
    	}
    	ret[1] = sum;
    	
    	for(j = 0;j < 12;j++) {
    		if(divstr[1].matches(OMONTH[j])){
    			ret[0] = j + 1;
    			break;
    		}
    	}
    	
    	for(j = 0, sum = 0;j < divstr[2].length();j++) {
    		sum *= 10;
    		sum += divstr[2].charAt(j) - '0';
    	}
    	ret[2] = sum;
    	
    	return ret;
    }
    
    public static String pattern(String text) {
    	//System.out.println(text);
    	String outputmsg = "";
    	
    	/*case 1*/
    	
    	String pattern = "(Call for papers|"
    				+ "Call For Papers|"
	        		+ "Call for Papers|"
	        		+ "CALL FOR PAPERS|"
	        		+ "Call for Applications|"
	        		+ "CALL FOR PARTICIPATION|"
	        		+ "Call for Contest Participation|"
	        		+ "CALL FOR WORKSHOP PROPOSALS|"
	        		+ "CALL FOR TUTORIAL PROPOSALS|"
	        		+ "Call for|"
	        		+ "AIMS|"
	        		+ "Issue on|"
	        		+ "Topics of Interest|"
	        		+ "TOPICS OF INTEREST|"
	        		+ "TOPICS|"
	        		+ "Topics|"
	        		+ "Keywords)"
	        		+ "([^a-zA-Z0-9]*)"
	        		+ "([a-zA-Z0-9\\s()&:'\"?/,.~–-]*)"
	        		+ "(\r\n)";
	    	
		Pattern r = Pattern.compile(pattern);
		Matcher patternMatcher = r.matcher(text);
			
		if(patternMatcher.find()){
			//System.out.println(patternMatcher.group(3));
			String [] msgs = patternMatcher.group(3).split("\r\n");
			outputmsg = msgs[0];
			for(int i = 1;i < msgs.length;i++) {
				String msg = msgs[i];
				if(msg.length() > 0) {
					int j;
					for(j = 0;j < msg.length();j++) {
						char ch = msg.charAt(j);
						String type = istype(ch);
						if(type == "isother")
							continue;
						if(type == "isbreak")
							break;
						if(type == "islower" || type == "isnum") {
							outputmsg += ", " + msg.substring(j);
							break;
						}
						if(type == "isupper") {
							j = -1;
							break;
						}
					}
					if(j < 0)
						break;
					
				}
			}
		}
		
		/*case 2*/
		
		if(outputmsg.length() == 0) {
			int index = text.indexOf("International Conferences");
			if(index < 0) {
				index = text.indexOf("International Conference");
			}
			if(index < 0) {
				index = text.indexOf("Workshop");
			}
			if(index < 0) {
				index = text.indexOf("Conference");
			}
			if(index < 0) {
				
			}
			else {
				int head = index, rear = index;
				for(;head > 0;head--) {
					if(text.charAt(head) == '(' || text.charAt(head) == '\n' || text.charAt(head) == '.') {
						break;
					}
				}
				for(;rear < text.length();rear++) {
					if(text.charAt(rear) == ')' || text.charAt(rear) == '\n' || text.charAt(rear) == '.') {
						break;
					}
				}
				outputmsg = text.substring(head, rear + 1);
			}
		}
		
		/*case 3*/
		
		if(outputmsg.length() == 0) {
			String [] msgs = text.split("\r\n");
			int i = 0;
			if(msgs[0].charAt(0) == '[')
				i++;
			for(;i < msgs.length;i++) {
				String msg = msgs[i];
				if(msg.length() > 0) {
					int j;
					for(j = 0;j < msg.length();j++) {
						char ch = msg.charAt(j);
						String type = istype(ch);
						if(type == "isother")
							continue;
						if(type == "isbreak")
							break;
						if(type == "islower" || type == "isnum") {
							if(outputmsg.length() == 0)	outputmsg = msg.substring(j);
							else
								outputmsg += ", " + msg.substring(j);
							break;
						}
						if(type == "isupper") {
							if(outputmsg.length() == 0)	outputmsg = msg.substring(j);
							else
								j = -1;
							break;
						}
					}
					if(j < 0)
						break;
				}
			}
		}
		if(outputmsg.length() > 150)
			outputmsg = outputmsg.substring(0, 150);
		//System.out.println(outputmsg);
		return outputmsg;
    }
    
    public static String istype(char ch) {
    	String ret = "";
    	if(ch >= 'a' && ch <= 'z') {
    		ret = "islower";
    	}
    	else if(ch >= 'A' && ch <= 'Z') {
    		ret = "isupper";
    	}
    	else if(ch >= '0' && ch <= '9') {
    		ret = "isnum";
    	}
    	else if(ch == '\r' || ch == '\n') {
    		ret = "isbreak";
    	}
    	else {
    		ret = "isother";
    	}
    	return ret;
    }
    
    public static void writeText(String s, String i) {
       String fileName = "G:\\extractoutput\\" + i;
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
