<%@ page language="java" contentType="text/html; charset=UTF-8"
	 import="searcher.*, javax.servlet.http.Cookie, javax.servlet.http.HttpServletResponse, java.net.URLEncoder, java.net.URLDecoder"  
	 pageEncoding="UTF-8"%>

<!DOCTYPE html>


<%request.setCharacterEncoding("UTF-8");
String qWord = request.getParameter("qWord");%>
<html>
<div  align="center">
	<font style="font-family: Palatino Linotype;" color=white><h1>Search Results</h1></font>
</div>
<div align="center">
	<form action="test.jsp" method = "post" accept-charset=“UTF-8” >
		<input type = "text" name = "qWord" size="35" value=<%=qWord%> required>
		<input type = "submit" name = "submit" value = "Search">
		<br/>
		<p><input type = "radio" name= "type" value="Topic" checked><font color=white> Topic</font></p>
		<p><input type = "radio" name= "type" value="Location"><font color=white> Location</font></p>
		<p><input type = "radio" name= "type" value="Deadline"><font color=white> Deadline</font></p>
		<p><input type = "radio" name= "type" value="Date"><font color=white> Date</font></p>
		<!-- <p><input type = "radio" name= "type" value="title" checked><font color=white> title</font></p>
		<p><input type = "radio" name= "type" value="poetry"><font color=white> poetry</font></p> -->
	</form>
	<form action="History.jsp">
		<input type = "submit" name = "hs" value = "View History">
	</form>
</div>

<hr style="background-color:white; height: 3px;">

	
<%
	request. setCharacterEncoding("UTF-8");
	String option = request.getParameter("type");
	
	String result;
	int cnt = 0;
	//result = openFile.Search(qWord, option);
	result = Searcher.Search(qWord, option);
	int index = result.indexOf(" term(s).</h3>");
	index--;
	int weight = 0;
	while(result.charAt(index) != ' '){ 
		cnt += (result.charAt(index) - '0') * Math.pow(10,weight);
		weight++;
		index--;
	}

	Cookie qCookie = new Cookie(option+"OPT"+String.valueOf(cnt),qWord);

	qCookie.setMaxAge(60*60);
	response.addCookie(qCookie);
	
	//result = option + " "+ qWord;
    out.print(result);%>

</html>
