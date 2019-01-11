# Web lab1: Search Engine Based on DBWorld  
This is the first programming assignment in the course *Web Information Processing and Applications* in 2018 fall semester.
  
## Descriptions
In this lab, I design a search engine based on DBWorld: https://research.cs.wisc.edu/dbworld/browse.html, I need to write a crawler in *java* to get websites and information, and then do the preprocessing in order to extract information conveniently. After these, I need to design the UI in *JSP* and *HTML5*.  
  
## Environment
* Language: java, JSP
* IDE: eclipse J2EE
* Others: jsoup-1.11.3, Lucene-7.1.0, Tomcat-7.0, StanfordCoreNLP  
  
## Overview
#### Crawler  
The crawler is written using Jsoup, according to the characteristics of DBWorld.  
* List: JsoupTest.java  
  
#### Extract Information  
In this part, I use StanfordCoreNLP tools to get the NER labels of words in order to extract the information of location and time. I use a pattern to extract topic information, which is really stupid.  
* List: preText.java(Bug sometimes occurs, but this step is not quite important), extractInfo.java  
  
#### Indexer  
Lucene is really convenient.  
* List: indexWriter.java  
  
#### Searcher  
Lucene is really convenient.  
* List: Searcher.java  
  
#### Interface  
This part is written in *JSP* and *HTML5*, I partly referenced to the work of Weizhen Qi.  
* List: main.jsp, test.jsp, History.jsp  
  
## Presentation
![Homepage](https://github.com/GYWmhy/web-lab/blob/master/img/main.png)
![Search](https://github.com/GYWmhy/web-lab/blob/master/img/test.png)
It's roughly ranked by the importance of conferences:  
![Rank](https://github.com/GYWmhy/web-lab/blob/master/img/test0.png)
This part I learned from Weizhen Qi:
![History](https://github.com/GYWmhy/web-lab/blob/master/img/history.png)
